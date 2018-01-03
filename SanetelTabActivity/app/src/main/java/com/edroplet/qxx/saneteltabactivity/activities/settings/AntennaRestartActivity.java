package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

public class AntennaRestartActivity extends AppCompatActivity {

    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antenna_restart);
        context = this;
        ViewInject.inject(this, this);
        // StatusBarControl.setupToolbar(this, R.id.antenna_restart_toolbar);
        // 设置标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.antenna_restart_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 设置对话框
        PopDialog popDialog = new PopDialog(this);

        popDialog.setView(findViewById(R.id.main_settings_antenna_restart_pop));
        popDialog.setContext(this);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOW_THIRD, true);
        bundle.putString(PopDialog.START,getString(R.string.follow_me_message_click));
        bundle.putString(PopDialog.END,getString(R.string.settings_antenna_restart_third_end));
        popDialog.setBundle(bundle);
        popDialog.setSetFirstColor(true);
        popDialog.setButtonText(this,getString(R.string.settings_antenna_restart_button));
        popDialog.show();

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 发送重启命令
                Protocol.sendMessage(context, Protocol.cmdResetSystem);
                finish();
            }
        });

    }
}
