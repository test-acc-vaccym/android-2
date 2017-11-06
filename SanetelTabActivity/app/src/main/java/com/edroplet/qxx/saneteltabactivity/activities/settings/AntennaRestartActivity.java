package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

public class AntennaRestartActivity extends AppCompatActivity {

    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antenna_restart);
        ViewInject.inject(this, this);
        StatusBarControl.setupToolbar(this, R.id.antenna_restart_toolbar);

        PopDialog popDialog = new PopDialog(this);

        popDialog.setView(findViewById(R.id.main_settings_antenna_restart_pop));
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
                // TODO: 2017/10/23  发送重启命令
            }
        });

    }
}
