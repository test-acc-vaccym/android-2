package top.edroplet.encdec.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import top.edroplet.encdec.R;
import top.edroplet.encdec.activities.animation.AnimatorActivity;
import top.edroplet.encdec.activities.io.EncodingTransferActivity;
import top.edroplet.encdec.activities.io.FindReplaceActivity;
import top.edroplet.encdec.activities.sensors.SensorActivity;
import top.edroplet.encdec.activities.system.GpsActivity;
import top.edroplet.encdec.activities.system.KeyboardActivity;
import top.edroplet.encdec.activities.system.SmsActivity;

public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Button btnTransfer,
                btnFindReplace,
                btnAnim,
                btnSensor,
                btnGps,
                btnKeyboard;

        btnTransfer = (Button) findViewById(R.id.main_btn_transfer);
        btnFindReplace = (Button) findViewById(R.id.main_btn_find_replace);
        btnAnim = (Button) findViewById(R.id.main_btn_anim);
        btnSensor = (Button) findViewById(R.id.main_btn_sensor);
        btnGps = (Button) findViewById(R.id.mainButtonGps);
        btnKeyboard =(Button) findViewById(R.id.mainButtonKeyboard);

        btnAnim.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
        btnFindReplace.setOnClickListener(this);
        btnSensor.setOnClickListener(this);
        btnGps.setOnClickListener(this);
        btnKeyboard.setOnClickListener(this);

        findViewById(R.id.mainButtonSms).setOnClickListener(this);
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
                intent = new Intent(this, SensorActivity.class);
                startActivity(intent);
                break;
            case R.id.mainButtonSms:
                intent = new Intent(this, SmsActivity.class);
                startActivity(intent);
                break;
            case R.id.mainButtonGps:
                intent = new Intent(this, GpsActivity.class);
                startActivity(intent);
                break;

            case R.id.mainButtonKeyboard:
                intent = new Intent(this, KeyboardActivity.class);
                startActivity(intent);
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
