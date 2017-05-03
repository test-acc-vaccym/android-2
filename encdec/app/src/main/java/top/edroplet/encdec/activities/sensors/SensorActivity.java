package top.edroplet.encdec.activities.sensors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class SensorActivity extends Activity implements SensorEventListener, OnClickListener {
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
    gyroscopeValue = new float[3],
	magneticValue,
	gravityValue,
	linearAccelerationValue,
	temperatureValue,
	pressureValue,
	lightValue,
	gameRotionVectorValue,
    proximityValue;
	
    private float count, lastPoint,
            detector;
    private boolean firstStepFlag = true;

    AudioManager audioManager;
    int volValue = 0;

    Button btnMovements; // 运动
    Button btnCompass; // 罗盘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_study);

        tv_number = (TextView) findViewById(R.id.sensor_studyTextView_number);

        btnMovements = (Button) findViewById(R.id.sensor_studyButtonMovements);
        btnMovements.setOnClickListener(this);

        btnCompass = (Button) findViewById(R.id.sensor_studyButtonCompass);
        btnCompass.setOnClickListener(this);

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
		proximity = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        //  获得声音服务
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volValue = audioManager.getRingerMode();

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

    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;

    @Override
    public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
       	case Sensor.TYPE_STEP_COUNTER:
			if (false){
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
			}else{
				count = event.values[0];
			}
            break;
            case Sensor.TYPE_STEP_DETECTOR:
            if (event.values[0] == 1.0) {
                detector++;
            }
			break;
        case Sensor.TYPE_ORIENTATION:
            oritentionValue = event.values;
            //  在这里规定翻转角度小于-120度时静音，values[2]表示翻转角度，也可以设置其他角度
            if (oritentionValue[2] < -120)
            {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
            break;
        case  Sensor.TYPE_GRAVITY:
            /**
             * 加速度传感器的类型常量是Sensor.TYPE_GRAVITY。重力传感器与加速度传感器使用同一套坐标系。
             * values数组中三个元素分别表示了X、Y、Z轴的重力大小。Android SDK定义了一些常量，
             * 用于表示星系中行星、卫星和太阳表面的重力。下面就来温习一下天文知识，
             * 将来如果在地球以外用Android手机，也许会用得上。
             * public static final float GRAVITY_SUN= 275.0f;
             public static final float GRAVITY_MERCURY= 3.70f;
             public static final float GRAVITY_VENUS= 8.87f;
             public static final float GRAVITY_EARTH= 9.80665f;
             public static final float GRAVITY_MOON= 1.6f;
             public static final float GRAVITY_MARS= 3.71f;
             public static final float GRAVITY_JUPITER= 23.12f;
             public static final float GRAVITY_SATURN= 8.96f;
             public static final float GRAVITY_URANUS= 8.69f;
             public static final float GRAVITY_NEPTUNE= 11.0f;
             public static final float GRAVITY_PLUTO= 0.6f;
             public static final float GRAVITY_DEATH_STAR_I= 0.000000353036145f;
             public static final float GRAVITY_THE_ISLAND= 4.815162342f;
             */
			 gravityValue = event.values;
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
            pressureValue = event.values;
            break;
		case Sensor.TYPE_LIGHT:
            /**
             * 光线传感器的类型常量是Sensor.TYPE_LIGHT。values数组只有第一个元素（values[0]）有意义。
             * 表示光线的强度。最大的值是120000.0f。Android SDK将光线强度分为不同的等级，每一个等级的最大值由一个常量表示，
             * 这些常量都定义在SensorManager类中，代码如下：
             * public static final float LIGHT_SUNLIGHT_MAX =120000.0f;
             * public static final float LIGHT_SUNLIGHT=110000.0f;
             * public static final float LIGHT_SHADE=20000.0f;
             * public static final float LIGHT_OVERCAST= 10000.0f;
             * public static final float LIGHT_SUNRISE= 400.0f;
             * public static final float LIGHT_CLOUDY= 100.0f;
             * public static final float LIGHT_FULLMOON= 0.25f;
             * public static final float LIGHT_NO_MOON= 0.001f;
             */
            lightValue = event.values;
            break;
		case Sensor.TYPE_GYROSCOPE:
            /**
             * alues[0]：延X轴旋转的角速度。
             * values[1]：延Y轴旋转的角速度。
             * values[2]：延Z轴旋转的角速度。
             * 当手机逆时针旋转时，角速度为正值，顺时针旋转时，角速度为负值。陀螺仪传感器经常被用来计算手机已转动的角度，代码如下：
             */
            if (timestamp != 0)
            {
                //  event.timesamp表示当前的时间，单位是纳秒（1百万分之一毫秒）
                final float dT = (event.timestamp - timestamp) * NS2S;
                gyroscopeValue[0] += event.values[0] * dT;
                gyroscopeValue[1] += event.values[1] * dT;
                gyroscopeValue[2] += event.values[2] * dT;
            }
            timestamp = event.timestamp;
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
        if (volValue > 0) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
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
						msg +=  " 距离传感器" + String.valueOf(proximityValue);
                        break;
					case Sensor.TYPE_GRAVITY:
						msg += " 重力传感器，" + String.valueOf(gravityValue);
						break;
                    default:
                        msg +=  " 未知传感器";
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.sensor_studyButtonMovements:
                intent = new Intent(this, StepCounterActivity.class);
                startActivity(intent);
                break;
            case R.id.sensor_studyButtonCompass:
                intent = new Intent(this, CompassActivity.class);
                startActivity(intent);
                break;
        }
    }
}

