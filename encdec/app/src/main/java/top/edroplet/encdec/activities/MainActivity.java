package top.edroplet.encdec.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import top.edroplet.encdec.R;
import top.edroplet.encdec.activities.animation.AnimatorActivity;
import top.edroplet.encdec.activities.io.EncodingTransferActivity;
import top.edroplet.encdec.activities.io.FindReplaceActivity;
import top.edroplet.encdec.activities.sensors.SensorStudy;
import top.edroplet.encdec.utils.SendMessageUtil;

public class MainActivity extends Activity implements View.OnClickListener {
    //    private static final String TAG="MainUI";
    /**
     * 发送与接收的广播
     **/
    private static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Button btnTransfer, btnFindReplace, btnAnim, btnSensor, sendSMS;

        btnTransfer = (Button) findViewById(R.id.main_btn_transfer);
        btnFindReplace = (Button) findViewById(R.id.main_btn_find_replace);
        btnAnim = (Button) findViewById(R.id.main_btn_anim);
        btnSensor = (Button) findViewById(R.id.main_btn_sensor);
        sendSMS = (Button) findViewById(R.id.mainButtonSmsSend);

        btnAnim.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
        btnFindReplace.setOnClickListener(this);
        btnSensor.setOnClickListener(this);
        sendSMS.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.main_btn_transfer:
                intent = new Intent(this, EncodingTransferActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_find_replace:
                intent = new Intent(this, FindReplaceActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_anim:
                intent = new Intent(this, AnimatorActivity.class);
                startActivity(intent);
                break;
			case R.id.main_btn_sensor:
                intent = new Intent(this, SensorStudy.class);
                startActivity(intent);
                break;
            case R.id.mainButtonSmsSend:
                SmsManager smsManager = SmsManager.getDefault();
                EditText etSmsNumber, etSmsContent;
                etSmsNumber = (EditText) findViewById(R.id.mainEditTextSmsNumber);
                etSmsContent = (EditText) findViewById(R.id.mainEditTextSmsContent);
                String smsNumber = etSmsNumber.getText().toString();
                String smsContent = etSmsContent.getText().toString();
                if (smsNumber.isEmpty()) {
                    Toast.makeText(this, "号码为空", Toast.LENGTH_LONG).show();
                    break;
                }
                if (smsContent.isEmpty()) {
                    Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
                    break;
                }
                //smsManager.sendTextMessage(smsNumber,null,smsContent,null,null);
                SendMessageUtil smu = new SendMessageUtil();
                smu.sendMessage(this, smsNumber, smsContent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
