package top.edroplet.encdec.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.Calendar;

import top.edroplet.encdec.R;
import top.edroplet.encdec.activities.sensors.StepCounterActivity;
import top.edroplet.encdec.utils.data.StepCounterSQLiteHelper;
import top.edroplet.encdec.view.WalkingView;

/**
 * Created by xw on 2017/5/2.
 */

public class WalkingService extends Service {
    public WalkingView wv;
    public SensorManager mySensorManager;
    public  Sensor accelerometer;
    public WalkingListener wl;
    public int steps = 0;
    public boolean isActivityOn = false; //Activity 是否运行
    public boolean isServiceOn = false;

    NotificationManager nm;//声明NotificationManager
    public long timeInterval = 24 * 60 * 60 * 1000;
    //Handler 延迟发
    //送消息的时延
    public final static int CMD_STOP = 0;
    public final static int CMD_UPDATAE = 1;
    CommandReceiver receiver; //声明BroadcastReceiver

    Handler myHandler = new Handler() {//定时上传数据
        public void handleMessage(Message msg) {
            uploadData();
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 计算了从Service 被启动时刻到这一天的结束之间的时间间隔，
         * 并将该时间间隔作为Handler对象发送消息的延迟，
         * 这样在本天过完之后， 会及时地向数据库中插入数据。
         */
        wl = new WalkingListener(this); //创建监听器类
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mySensorManager.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER);
        //初始化传感器
        mySensorManager.registerListener(wl, accelerometer, SensorManager.SENSOR_DELAY_UI);

        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Calendar c = Calendar.getInstance();
        long militime = c.getTimeInMillis();
        //将Calendar 设置为第二天0 时
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long nextDay = c.getTimeInMillis();
        timeInterval = nextDay - militime;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        // 调用了showNotification 方法，该方法将会在手机的状态栏(显示信号强度、电池等状态的区域)
        // 显示本程序的Notification， 展开状态栏并点击Notification 后会启动WalkingActivity
        isServiceOn = true;
        showNotification();//添加Notification
        receiver = new CommandReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(StepCounterActivity.WALKING_SERVICE);
        registerReceiver(receiver, filter1);

        //设定Message 并延迟到本日结束发送

        if(isServiceOn){
            Message msg = myHandler.obtainMessage();
            myHandler.sendMessageDelayed(msg, timeInterval);
        }
    }

    @Override
    public void onDestroy() {
        mySensorManager.unregisterListener(wl);
        wl = null;
        mySensorManager = null;
        nm.cancel(0);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //方法：向数据库中插入今日走过的步数
    public void uploadData(){
        /**
         * 使用Handler 来定时发送消息， 收到消息后会调用uploadData 方法将今日走过的步数插入到数据库， 该方法的代码如下
         */
        StepCounterSQLiteHelper mh = new StepCounterSQLiteHelper(this,StepCounterActivity.DB_NAME,null,1);

        SQLiteDatabase db = mh.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(StepCounterSQLiteHelper.STEP, this.steps);
        db.insert(StepCounterSQLiteHelper.TABLE_NAME, StepCounterSQLiteHelper.ID, values);
        Cursor c = db.query(StepCounterSQLiteHelper.TABLE_NAME,null, null, null, null, null, null);

        c.close();
        db.close(); //关闭数据库
        mh.close();

        if(isServiceOn){
            //设置24 小时后再发同样的消息
            Message msg = myHandler.obtainMessage();
            myHandler.sendMessageDelayed(msg,24*60*60*1000);
        }
    }

    //方法：显示Notification
    private void showNotification() {
        Intent intent = new Intent(this,StepCounterActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification myNotification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            if (pi == null) {
                pi = PendingIntent.getBroadcast(this, 0, new Intent(""), 0);
            }
            Notification.Builder builder = new Notification.Builder(this);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.icon)
                    .setTicker("计步器")
                    .setContentTitle("计步器运行中")
                    .setContentText("点击查看")
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentIntent(pi);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                myNotification = builder.build();
            }
        }
        // Notification通知setLatestEventInfo方法失效
        // doDelete(this, mNM);
        // myNotification.icon = R.drawable.icon;
        // myNotification.defaults = Notification.DEFAULT_ALL;
        // myNotification.setLatestEventInfo(this, "计步器运行中", "点击查看", pi);
        // myNotification.notify();
        try {
            nm.notify(0, myNotification);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CommandReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int cmd = intent.getIntExtra("cmd", -1);
            switch(cmd){
                case WalkingService.CMD_STOP://停止服务
                    stopSelf();
                    break;
                case WalkingService.CMD_UPDATAE: //传数据
                    isActivityOn = true;
                    Intent i = new Intent();
                    i.setAction("top.edroplet.encdec.activities.WalkingActivity");
                    i.putExtra("step", steps);
                    sendBroadcast(i);
                    break;
            }
        }
    }

    /**
     * WalkingListener 类的代码中， 主要是对SensorListener 接口中的onSensorChanged 方法进行了重写，
     * 在该方法中将读取到的传感器采样值传给analyseData 方法进行分析。在analyseData 方法中，
     * 调用了calculateAngle 方法来计算固定的时间间隔间手机加速度向量方向的夹角。
     */
    public class WalkingListener implements SensorEventListener {
        WalkingService father;             // WalkingService 引用
        float [] preCoordinate;
        double currentTime=0,lastTime=0;   //记录时间
        float WALKING_THRESHOLD = 20;

        public WalkingListener(WalkingService father){
            this.father = father;
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType() == SensorManager.SENSOR_ACCELEROMETER){
                //调用方法分析数据
                analyseData(sensorEvent.values);
            }
        }
        //方法：分析参数进行计算
        public void analyseData(float[] values){

            //获取当前时间
            currentTime=System.currentTimeMillis();

            //每隔200MS 取加速度力和前一个进行比较
            if(currentTime - lastTime >200){
                if(preCoordinate == null){
                    //还未存过数据
                    preCoordinate = new float[3];
                    for(int i=0;i<3;i++){
                        preCoordinate = values;
                    }
                } else { //记录了原始坐标的话，就进行比较
                    int angle=calculateAngle(values,preCoordinate);
                    /**
                     * 如果calculateAngle 方法返回的加速度向量角度变化超过了程序中设定的阈值，
                     * 应用程序将WalkingService 中已走步数计数器加1，
                     * 并调用updateData 方法将更新的步数传递给WalkingActivity 显示到界面
                     */
                    if(angle >= WALKING_THRESHOLD){
                        father.steps++; //步数增加
                        updateData(); //更新步数
                    }

                    for(int i=0;i<3;i++){
                        preCoordinate=values;
                    }}
                lastTime = currentTime;//重新计时
            }
        }

        //方法：计算加速度矢量角度的方法
        public int calculateAngle(float[] newPoints,float[] oldPoints){
            int angle=0;
            float vectorProduct=0; //向量积
            float newMold=0; //新向量的模
            float oldMold=0; //旧向量的模

            for(int i=0;i<3;i++){
                vectorProduct += newPoints[i] * oldPoints[i];
                newMold += newPoints[i] * newPoints[i];
                oldMold += oldPoints[i] * oldPoints[i];
            }

            newMold = (float)Math.sqrt(newMold);
            oldMold = (float)Math.sqrt(oldMold);
            //计算夹角的余弦
            float cosineAngle=(float)(vectorProduct/(newMold*oldMold));

            //通过余弦值求角度
            float fangle = (float)Math.toDegrees(Math.acos(cosineAngle));
            angle = (int)fangle;

            //返回向量的夹
            return angle;
        }

        //方法：向Activity 更新步数
        public void updateData(){
            Intent intent = new Intent(); //创建Intent 对象
            intent.setAction("top.droplet.encdec.activities.WalkingActivity");
            intent.putExtra("step", father.steps);//添加步数
            father.sendBroadcast(intent); //发出广播
        }
    }
}

