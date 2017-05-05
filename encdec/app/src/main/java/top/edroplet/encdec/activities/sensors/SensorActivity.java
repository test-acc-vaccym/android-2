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

/**
 * http://www.runoob.com/w3cnote/android-tutorial-sensor4.html
 */
public class SensorActivity extends Activity implements SensorEventListener, OnClickListener {
    

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
    proximity,          // 距离
    ambientTemperature; // 手机外部温度
	
    SensorManager sm;
	float []
	accelerometerValue,
	orientationValue,
    gyroscopeValue = new float[3],
	magneticValue,
	gravityValue,
	linearAccelerationValue,
	temperatureValue,
	pressureValue,
	lightValue,
	gameRotationVectorValue,
    proximityValue,
    ambinetTemperatureValue;
	
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
		gameRotationVector = sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        ambientTemperature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


        //  获得声音服务
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volValue = audioManager.getRingerMode();

        List<Sensor> ls = sm.getSensorList(Sensor.TYPE_ALL);

        tv_number.setText(String.valueOf(ls.size()));

        for (Sensor s : ls) {
            sensors.add(new SensorData(s.getType(), s.getName(), String.valueOf(s.getVersion()), s.getVendor()));
        }

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
            /**
             * 方向传感器的三个值
             * values[0]：方位角，手机绕着Z轴旋转的角度。
             *      0表示正北(North)，90表示正东(East)，180表示正南(South)，270表示正西(West)。
             *      假如values[0]的值刚好是这四个值的话，并且手机沿水平放置的话，那么当前手机的正前方就是这四个方向，可以利用这一点来写一个指南针！
             * values[1]：倾斜角，手机翘起来的程度，当手机绕着x轴倾斜时该值会发生变化。
             *      取值范围是[-180,180]之间。假如把手机放在桌面上，而桌面是完全水平的话，values1的则应该是0，当然很少桌子是绝对水平的。
             *      从手机顶部开始抬起，直到手机沿着x轴旋转180(此时屏幕乡下水平放在桌面上)。
             *      在这个旋转过程中，values[1]的值会从0到-180之间变化，
             *      即手机抬起时，values1的值会逐渐变小，知道等于-180；
             *      而加入从手机底部开始抬起，直到手机沿着x轴旋转180度，此时values[1]的值会从0到180之间变化。
             *      我们可以利用value[1]的这个特性结合value[2]来实现一个平地尺！
             * value[2]：滚动角，沿着Y轴的滚动角度，取值范围为：[-90,90].
             *      假设将手机屏幕朝上水平放在桌面上，这时如果桌面是平的，values2的值应为0。
             *      将手机从左侧逐渐抬起，values[2]的值将逐渐减小，知道垂直于手机放置，此时values[2]的值为-90，
             *      从右侧则是0-90；加入在垂直位置时继续向右或者向左滚动，values[2]的值将会继续在-90到90之间变化！
             */

        case Sensor.TYPE_ORIENTATION:
            orientationValue = event.values;
            //  在这里规定翻转角度小于-120度时静音，values[2]表示翻转角度，也可以设置其他角度
            if (orientationValue[2] < -120)
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
            /**
             * 加速度传感器(Accelerometer sensor),单位：加速度(m/s^2),方向传感器获取到的加速度是：手机运动的加速度与重力加速度(9.81m/s^2)的合加速度
             */
		case Sensor.TYPE_ACCELEROMETER:
			accelerometerValue = event.values;
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			magneticValue = event.values;
			break;
		case Sensor.TYPE_LINEAR_ACCELERATION:
			linearAccelerationValue = event.values;
			break;
            /**
             * 温度传感器（Temperature sensor）
             * 数值单位：℃，摄氏度
             * 传感器获取：Sensor.TYPE_TEMPERATURE(手机内部)/TYPE_AMBIENT_TEMPERATURE(手机外部)
             */
		case Sensor.TYPE_TEMPERATURE:
			temperatureValue = event.values;
			break;
        case Sensor.TYPE_AMBIENT_TEMPERATURE:
            ambinetTemperatureValue = event.values;
            break;
            /**
             * 气压传感器(Pressure sensor)用于测量大气压力，常用于测量海拔高度
             * 数值单位：hPa，百帕
             */
		case Sensor.TYPE_PRESSURE:
            pressureValue = event.values;
            break;
		case Sensor.TYPE_LIGHT:
            /**
             * 光线传感器(Light sensor),数值单位：lux，1流明每平方米面积，就是1勒克斯(lux)，最大值是：120000.0f
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
             * 陀螺仪传感器(Gyroscope sensor)陀螺仪又叫角速度传感器，一般用来检测手机姿态的，
             * 好像手机中的陀螺仪传感器一般都是三轴的！体感游戏用得最多，手机拍照防抖，GPS惯性导航，
             * 还有为APP添加一些动作感应(比如轻轻晃动手机关闭来电铃声)
             * 角速度(弧度/秒)radians/second
             * alues[0]：延X轴旋转的角速度。
             * values[1]：延Y轴旋转的角速度。
             * values[2]：延Z轴旋转的角速度。
             * 当手机逆时针旋转时，角速度为正值，顺时针旋转时，角速度为负值。陀螺仪传感器经常被用来计算手机已转动的角度，
             *
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
            gameRotationVectorValue = event.values;
            break;
            /**
             * 距离传感器(Proximity sensor)
             * 用于感应手机与人体的距离，用得最多的就是手机通话时候，脸部贴近屏幕时，屏幕会熄灭，
             * 当脸部离开屏幕一段距离后，屏幕又会亮起，这样可以避免通过过程脸部误碰挂断按钮，从而导致通话中断
             * 数值单位：cm，厘米
             * ①关于距离传感器可能有两种，一种是能直接给出距离的，而另一种则是给出靠近或者远离！
             * 就是只返回两个值，0.0或者最大值！我们可以通过对比解析度和最大值是否相等进行判断！
             * 假如相等说明是后者，假如不等说明是前者！
             * ②调用sensor.getResolution()方法获得解析度，调用getMaximumRange()获得最大值！
             */
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
        sm.registerListener(this,ambientTemperature,SensorManager.SENSOR_DELAY_NORMAL);
		
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
                    StringBuilder msg = new StringBuilder("传感器参数\n\t设备名称：");
                    msg.append(data.getName());
                    msg.append("\n\t设备版本：");
                    msg.append(data.getVersion());
                    msg.append("\n\t供应商：");
                    msg.append(data.getVendor());
                    msg.append("\n传感器有效数值\n");
					switch(type){
                    case Sensor.TYPE_STEP_COUNTER:
                        msg.append("\t计步器\t总步数：");
                        msg.append(count);
						break;
                    case Sensor.TYPE_STEP_DETECTOR:
                        msg.append("\t步进器\t本次步数：");
                        msg.append(detector);
						break;
					case Sensor.TYPE_ACCELEROMETER:
                        msg.append("加速度\tx方向:");
                        msg.append(accelerometerValue[0]);
                        msg.append("\n\tY方向:");
                        msg.append(accelerometerValue[1]);
                        msg.append("\n\tZ方向:");
                        msg.append(accelerometerValue[2]);
                        break;
                    case Sensor.TYPE_GYROSCOPE:
                        msg.append("陀螺仪传感器\t");
                        msg.append(gyroscopeValue);
                        break;
                    case Sensor.TYPE_LIGHT:
                        msg.append("环境光线传感器\t亮度");
                        msg.append(lightValue[0]);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        msg.append("电磁场传感器\tx方向:");
                        msg.append(magneticValue[0]);
                        msg.append("\n\tY方向:");
                        msg.append(magneticValue[1]);
                        msg.append("\n\tZ方向:");
                        msg.append(magneticValue[2]);
                        break;

                    case Sensor.TYPE_PRESSURE:
                        msg.append("压力传感器pressure\tX:");
                        msg.append(pressureValue[0]);
                        msg.append("\n\tY方向:");
                        msg.append(pressureValue[1]);
                        msg.append("\n\tZ方向:");
                        msg.append(pressureValue[2]);
                        break;
                    case Sensor.TYPE_PROXIMITY:
                        msg.append(" 距离传感器");
                        msg.append(proximityValue[0]);
                        break;
					case Sensor.TYPE_GRAVITY:
                        msg.append(" 重力传感器\tX:");
                        msg.append(gravityValue[0]);
                        msg.append("\n\tY方向:");
                        msg.append(gravityValue[1]);
                        msg.append("\n\tZ方向:");
                        msg.append(gravityValue[2]);
						break;
                    case Sensor.TYPE_TEMPERATURE:
                        msg.append("手机温度\t");
                        msg.append(temperatureValue[0]);
                        // msg.append(temperatureValue[1]);
                        // msg.append(temperatureValue[2]);
                        break;
                    case Sensor.TYPE_AMBIENT_TEMPERATURE:
                        msg.append("外部温度\t");
                        msg.append(ambinetTemperatureValue[0]);
                        break;
                    case Sensor.TYPE_ORIENTATION:
                        msg.append("方向x:");
                        msg.append(orientationValue[0]);
                        msg.append("\t方向y:");
                        msg.append(orientationValue[1]);
                        msg.append("\t方向z:");
                        msg.append(orientationValue[2]);
                        break;
                    default:
                        msg.append("未知传感器");
                        break;
                    }
                    msg.append("\n现在的手机放置状态是\n" );
                    msg.append(SensorsUtils.calculateOrientation(accelerometerValue, magneticValue));
                    Toast.makeText(p1.getContext(), msg.toString(), Toast.LENGTH_SHORT).show();
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

