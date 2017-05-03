package top.edroplet.encdec.activities.sensors;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import top.edroplet.encdec.R;
import top.edroplet.encdec.service.WalkingService;
import top.edroplet.encdec.utils.data.StepCounterSQLiteHelper;
import top.edroplet.encdec.view.WalkingView;

public class StepCounterActivity extends Activity implements View.OnClickListener {

    //数据库名称
    public static final String DB_NAME = "step.db";
    public static final String WALKING_SERVICE = "top.edroplet.encdec.service.WalkingService";
    public static final String WALKING_ACTIVITY = "top.edroplet.encdec.activities.WalkingActivity";
    WalkingView wv;                      //WalkingView 对象引用
    StepCounterSQLiteHelper schelper;   // 声明数据库辅助类
    SQLiteDatabase db;                  // 数据库对象
    Button btnToBackstage,              // 转入后台按钮
    btnStopService,                     // 停止服务按钮
    btnDeleteData;                      // 删除数据按钮

    StepUpdateReceiver receiver;
    //定义一个继承自BroadcastReceiver 的内部类 StepUpdateReceiver 来接受传感器的信息

    public class StepUpdateReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();//获得Bundle
            int steps = bundle.getInt("step");//读取步数
            wv.stepsToday = steps;
            wv.isMoving = true;
            wv.postInvalidate(); //刷新WalkingView
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        wv = (WalkingView)  findViewById(R.id.walkingView);
        btnToBackstage = (Button) findViewById(R.id.stepCounterButtonDispose);
        btnStopService = (Button) findViewById(R.id.stepCounterButtonStop);
        btnDeleteData = (Button) findViewById(R.id.stepCounterButtonDeleteData);

        btnToBackstage.setOnClickListener(this);
        btnDeleteData.setOnClickListener(this);
        btnStopService.setOnClickListener(this);

        // 注册Receiver
        receiver = new StepUpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WALKING_ACTIVITY);
        registerReceiver(receiver, filter);
        //启动注册了传感器监听的Service

        Intent i = new Intent(this,StepCounterActivity.class);
        startService(i);
        schelper = new StepCounterSQLiteHelper(this,DB_NAME,null,1);
        requireData(); //向Service 请求今日走过步数
    }

    @Override
    public void onClick(View view) {
        if(view == btnStopService){
            //停止后台服务
            Intent intent = new Intent();
            intent.setAction(WALKING_SERVICE);
            intent.putExtra("cmd",  WalkingService.CMD_STOP);
            sendBroadcast(intent);

        } else if(view == btnToBackstage){
            finish();//转到后台
        } else if(view == btnDeleteData){
            //查看历史数据
            SQLiteDatabase db = openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE, null);

            db.delete(StepCounterSQLiteHelper.TABLE_NAME, null,null);
            db.close();
            wv.stepsInWeek = wv.getSQLData("7");
            wv.postInvalidate();

        }
    }

    //方法：向Service 请求今日走过的步数
    public void requireData(){
        Intent intent = new Intent(); //创建Intent
        intent.setAction(WALKING_SERVICE);
        intent.putExtra("cmd", WalkingService.CMD_UPDATAE);
        sendBroadcast(intent); //发出消息广播
    }

}
