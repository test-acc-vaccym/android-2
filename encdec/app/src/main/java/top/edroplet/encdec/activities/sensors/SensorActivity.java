package top.edroplet.encdec.activities.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import top.edroplet.encdec.R;
import top.edroplet.encdec.utils.data.SensorData;
import top.edroplet.encdec.utils.data.SensorsUtils;

public class SensorActivity extends Activity implements SensorEventListener {
    SensorsUtils sensorsUtils = new SensorsUtils();

    TextView tv_number;
    ListView lv;
    ArrayList<SensorData> sensors = new ArrayList<>();
    LayoutInflater inflater;
    Sensor stepCount,  	// 步数
	stepDetector, 		// 走步探测
	accelerometer, 		// 加速度
	orientation, 		// 方向
	gyroscope, 			// 陀螺仪
	magnetic, 			// 磁场
	gravity,			// 重力
    linearAcceleration, // 线性加速度传感器
	temperature,		// 温度
	pressure,			// 压力
	light,				// 光感
	gameRotationVector, // 旋转
    proximity;          // 距离
	
    SensorManager sm;
	float []
	accelerometerValue,
	oritentionValue,
    gyroscopeValue,
	magneticValue,
	linearAccelerationValue,
	temperatureValue,
	pressureValue,
	lightValue,
	gameRotionVectorValue,
    proximityValue;
    private float count, lastPoint,
            detector;
    private boolean firstStepFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_study);

        tv_number = (TextView) findViewById(R.id.sensor_studyTextView_number);

        lv = (ListView) findViewById(R.id.sensor_studyListView_sensorList);


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
        stepCount = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetector = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		orientation = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		gyroscope = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		magnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		gravity = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        linearAcceleration = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		light = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
		temperature = sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
		pressure = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
		
		
        // sm.registerListener(this, stepCount, SensorManager.SENSOR_DELAY_FASTEST);
        // sm.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_FASTEST);
        List<Sensor> ls = sm.getSensorList(Sensor.TYPE_ALL);

        tv_number.setText(String.valueOf(ls.size()));

        // StringBuffer sb = new StringBuffer();
        for (Sensor s : ls) {
            sensors.add(new SensorData(s.getType(), s.getName(), String.valueOf(s.getVersion()), s.getVendor()));
            // sb.append(s.getName()+":"+ String.valueOf(s.getType())+";");
        }
        // Log.e("sensor", data.toString());
        Collections.sort(sensors, new SensorComparator());
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lv.setAdapter(new SensorListAdapter());
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (id == -1) {
                    // 点击headerview 或者 foot view
                    return;
                }
                int realPosition = (int) id;
                SensorData item = (SensorData) parent.getItemAtPosition(realPosition);
                Toast.makeText(null, item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        // tv_nameType.setText(sb.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor s, int accuracy) {
        // TODO: Implement this method
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
       	case Sensor.TYPE_STEP_COUNTER:
            if (firstStepFlag) {
                lastPoint = event.values[1];
                firstStepFlag = false;
            }
            //  当两个values[1]值之差的绝对值大于8时认为走了一步
            if (Math.abs(event.values[1] - lastPoint) > 8) {
                //  保存最后一步时的values[1]的峰值
                lastPoint = event.values[1];
                //  将当前计数加1
                ++count;
                // setStepCount(event.values[0]);
            }
            break;
            case Sensor.TYPE_STEP_DETECTOR:
            if (event.values[0] == 1.0) {
                detector++;
            }
			break;
        case Sensor.TYPE_ORIENTATION:
            oritentionValue = event.values;
        	break;
		case Sensor.TYPE_ACCELEROMETER:
			accelerometerValue = event.values;
				break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			magneticValue = event.values;
			break;
		case Sensor.TYPE_LINEAR_ACCELERATION:
			linearAccelerationValue = event.values;
			break;
		case Sensor.TYPE_TEMPERATURE:
			temperatureValue = event.values;
			break;
		case Sensor.TYPE_PRESSURE:
            temperatureValue = event.values;
            break;
		case Sensor.TYPE_LIGHT:
            oritentionValue = event.values;
            break;
		case Sensor.TYPE_GYROSCOPE:
            oritentionValue = event.values;
            break;
		case Sensor.TYPE_GAME_ROTATION_VECTOR:
            gameRotionVectorValue = event.values;
            break;
        case Sensor.TYPE_PROXIMITY:
            proximityValue = event.values;
            break;
		}
    }

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();

        sm.registerListener(this, stepCount, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(this, gravity,SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(this, light,SensorManager.SENSOR_DELAY_UI);
        sm.registerListener(this, proximity,SensorManager.SENSOR_DELAY_UI);

		sm.registerListener(this, linearAcceleration,SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, temperature,SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, pressure,SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, orientation,SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, gyroscope,SensorManager.SENSOR_DELAY_NORMAL);
		sm.registerListener(this, magnetic,SensorManager.SENSOR_DELAY_NORMAL);
		
		sm.registerListener(this, gameRotationVector, SensorManager.SENSOR_DELAY_GAME);
		
	}
	

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		super.onStop();
		sm.unregisterListener(this);
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}

    private void setStepCount(float c) {
        this.count = c;
    }

    class SensorComparator implements Comparator {
        @Override
        public int compare(Object p1, Object p2) {
            SensorData sd1 = (SensorData) p1;
            SensorData sd2 = (SensorData) p2;

            if (sd1.getType() < sd2.getType()) {
                return -1;
            }
            return 1;
        }
    }

    public class SensorListAdapter extends BaseAdapter implements OnItemClickListener {
        @Override
        public long getItemId(int p1) {
            return p1;
        }

        @Override
        public Object getItem(int p1) {
            return sensors.get(p1);
        }

        @Override
        public int getCount() {
            return sensors.size();
        }


        @Override
        public View getView(int p1, View p2, ViewGroup p3) {
            final SensorData data = sensors.get(p1);
            if (p2 == null) {
                p2 = inflater.inflate(R.layout.sensor_list_item, null);
            }

            TextView tv_name = (TextView) p2.findViewById(R.id.sensor_list_itemTextView_name);
            final String name = data.getName();
            final int type = data.getType();
            TextView tv_type = (TextView) p2.findViewById(R.id.sensor_list_itemTextView_type);
            // int type = ((intKey)data.entrySet().toArray()[p1]).ikey;
            tv_type.setText(String.valueOf(data.getType()));
            tv_name.setText(data.getName());
            p2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    // Log.e("NAME", na);
                    String msg = "\n" + "  设备名称：" + data.getName() + "\n" + "  设备版本：" + data.getVersion() + "\n" + "  供应商："
                            + data.getVendor() + "\n";
					switch(type){
                    case Sensor.TYPE_STEP_COUNTER:
                        msg += "总步数：" + String.valueOf(count);
						break;
                    case Sensor.TYPE_STEP_DETECTOR:
                        msg += "本次步数：" + String.valueOf(detector);
						break;
					case Sensor.TYPE_ACCELEROMETER:
                        msg += String.valueOf("加速度传感器, x方向:" + accelerometerValue[0] + "\n\tY方向:" + accelerometerValue[1] + "\n\tZ方向:" + accelerometerValue[2]);
                        break;
                    case Sensor.TYPE_GYROSCOPE:
                        msg +=  " 陀螺仪传感器:" + String.valueOf(gyroscopeValue);
                        break;
                    case Sensor.TYPE_LIGHT:
                        msg += " 环境光线传感器, 亮度" + String.valueOf(lightValue[0]);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        msg += String.valueOf(" 电磁场传感器,x方向:" + magneticValue[0] + "\n\tY方向:" + magneticValue[1] + "\n\tZ方向:" + magneticValue[2]);
                        break;

                    case Sensor.TYPE_PRESSURE:
                        msg +=  " 压力传感器pressure" + String.valueOf(pressureValue);
                        break;
                    case Sensor.TYPE_PROXIMITY:
                        msg +=  " 距离传感器" + String.valueOf(lightValue);
                        break;

                    default:
                        msg +=  " 未知传感器" + String.valueOf(lightValue);
                        break;
                    }
                    msg += "\n现在的手机放置状态是\n" + sensorsUtils.calculateOrientation(accelerometerValue, magneticValue);
                    Toast.makeText(p1.getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
            return p2;
        }

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            if (id == -1) {
                // 点击header view 或者 foot view
                return;
            }
            int realPosition = (int) id;
            SensorData item = (SensorData) parent.getItemAtPosition(realPosition);
            Toast.makeText(parent.getContext(), item.getName(), Toast.LENGTH_SHORT).show();
        }

    }
}

