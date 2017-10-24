package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.WaveBand;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

public class LowNoiseBlockOscillatorActivity extends AppCompatActivity {
    @BindId(R.id.lnb_toolbar)
    private Toolbar lnbToolbar;

    @BindId(R.id.layout_lnb_ku)
    private LinearLayout linearLayoutKu;

    @BindId(R.id.layout_lnb_ka)
    private LinearLayout linearLayoutKa;

    @BindId(R.id.low_noise_block_oscillator_ku_radio_group)
    private CustomRadioGroupWithCustomRadioButton oscillatorKuSelect;

    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;

    @BindId(R.id.low_noise_block_oscillator_ka_radio_group)
    private RadioButton oscillatorKaSelect;

    @BindId(R.id.settings_lnb_ku_value_1)
    private RadioButton settings_lnb_ku_value_1;
    @BindId(R.id.settings_lnb_ku_value_2)
    private RadioButton settings_lnb_ku_value_2;
    @BindId(R.id.settings_lnb_ku_value_3)
    private RadioButton settings_lnb_ku_value_3;
    @BindId(R.id.settings_lnb_ku_value_4)
    private RadioButton settings_lnb_ku_value_4;
    @BindId(R.id.settings_lnb_ku_value_5)
    private RadioButton settings_lnb_ku_value_5;
    @BindId(R.id.settings_lnb_ku_value_6)
    private RadioButton settings_lnb_ku_value_6;
    @BindId(R.id.settings_lnb_ku_value_7)
    private RadioButton settings_lnb_ku_value_7;


    @BindId(R.id.settings_lnb_ka_value_1)
    private RadioButton settings_lnb_ka_value_1;
    @BindId(R.id.settings_lnb_ka_value_2)
    private RadioButton settings_lnb_ka_value_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_noise_block_oscillator);

        ViewInject.inject(this, this);
        lnbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String band = CustomSP.getString(this, WaveBand.Key, WaveBand.KU);
        // 根据不同的波段显示不同的layout
        if (band.equals(WaveBand.KU)){
            linearLayoutKu.setVisibility(View.VISIBLE);
            linearLayoutKa.setVisibility(View.GONE);
        }else {
            linearLayoutKa.setVisibility(View.VISIBLE);
            linearLayoutKu.setVisibility(View.GONE);
        }

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/10/23 设置命令
            }
        });
        /*
        不需要 这些代码，在控件中已经处理了
        settings_lnb_ku_value_1.setOnCheckedChangeListener(generalOnCheckedChangeListener);
        settings_lnb_ku_value_2.setOnCheckedChangeListener(generalOnCheckedChangeListener);
        settings_lnb_ku_value_3.setOnCheckedChangeListener(generalOnCheckedChangeListener);
        settings_lnb_ku_value_4.setOnCheckedChangeListener(generalOnCheckedChangeListener);
        settings_lnb_ku_value_5.setOnCheckedChangeListener(generalOnCheckedChangeListener);
        settings_lnb_ku_value_6.setOnCheckedChangeListener(generalOnCheckedChangeListener);
        settings_lnb_ku_value_7.setOnCheckedChangeListener(generalOnCheckedChangeListener);

        settings_lnb_ka_value_1.setOnCheckedChangeListener(generalOnCheckedChangeListener);
        settings_lnb_ka_value_2.setOnCheckedChangeListener(generalOnCheckedChangeListener);
        */
        PopDialog popDialog = new PopDialog(this);

        popDialog.setView(findViewById(R.id.low_noise_block_oscillator_pop));
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOWSECOND, true);
        bundle.putString(PopDialog.SECOND,getString(R.string.settings_lnb_message_first_line));
        bundle.putBoolean(PopDialog.SHOWTHIRD, true);
        bundle.putString(PopDialog.START,getString(R.string.follow_me_message_click));
        bundle.putString(PopDialog.END,getString(R.string.follow_me_forever));
        popDialog.setBundle(bundle);
        popDialog.setSetFirstColor(true);
        popDialog.setButtonText(this,getString(R.string.setting_button_text));
        popDialog.show();
    }


    public CompoundButton.OnCheckedChangeListener generalOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            boolean isCustom = false;
            try {
                ViewParent vp = compoundButton.getParent();
                if (vp instanceof RelativeLayout){
                    // 自定义项
                    vp = vp.getParent();
                    isCustom = true;
                }
                CustomRadioGroupWithCustomRadioButton edGroup = (CustomRadioGroupWithCustomRadioButton) vp;
                int childCount = edGroup.getChildCount();

                for (int i = 0; i < childCount; i++){
                    if (edGroup.getChildAt(i) instanceof RelativeLayout){
                        if (!isCustom){
                            settings_lnb_ku_value_7.setChecked(false);
                        } else {
                            settings_lnb_ku_value_7.setChecked(true);
                        }
                    }else if (edGroup.getChildAt(i).getId() != compoundButton.getId()){
                        CustomRadioButton rdButton =  (CustomRadioButton)edGroup.getChildAt(i);
                        if (b && rdButton.isChecked()){
                            rdButton.setChecked(false);
                        }
                    }
                }
            } catch (ClassCastException e){
                e.printStackTrace();
            }
        }
    };
}
