package com.edroplet.sanetel.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 * 发射手动
 * 命令 4.14.6	发射手动
 * UI 10.2.2.	发射开关
 */

public class SettingsFragmentAmplifierEmit extends Fragment {
    public static SettingsFragmentAmplifierEmit newInstance(String info) {
        Bundle args = new Bundle();
        SettingsFragmentAmplifierEmit fragment = new SettingsFragmentAmplifierEmit();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.settings_amplifier_emit_close)
    RadioButton emitClose;
    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;
    @BindView(R.id.settings_amplifier_emit_radio_group)
    RadioGroup emitSelectGroup;

    @BindView(R.id.main_monitor_amplifier_info_title)
    CustomTextView tvTitle;

    public static final String KEY_emit_state="KEY_emit_state";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings_amplifier_emit, null);
        ButterKnife.bind(this, view);

        final Context context = getContext();

        tvTitle.setVisibility(View.GONE);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emitClose.isChecked()){
                    CustomSP.putInt(context,KEY_emit_state,1);
                    // 2017/11/12 发送命令关闭
                    Protocol.sendMessage(context, Protocol.cmdSetBucOff);
                }else {
                    CustomSP.putInt(context,KEY_emit_state,0);
                    // 2017/11/12 发送命令打开
                    Protocol.sendMessage(context, Protocol.cmdSetBucOn);
                }
            }
        });
        int checkedPos = CustomSP.getInt(context,KEY_emit_state, 0);
        if (checkedPos == 0){
            emitSelectGroup.check(R.id.settings_amplifier_emit_open);
        }else {
            emitSelectGroup.check(R.id.settings_amplifier_emit_close);
        }

        // 设置自定义框内容
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOW_SECOND, true);
        bundle.putBoolean(PopDialog.SHOW_THIRD, true);
        bundle.putString(PopDialog.SECOND, context.getString(R.string.settings_amplifier_emit_pop_second));
        bundle.putString(PopDialog.START, context.getString(R.string.settings_amplifier_manufacture_message_third_start));
        bundle.putString(PopDialog.END, context.getString(R.string.follow_me_forever));
        popDialog.setBundle(bundle);
        popDialog.setButtonText(context, context.getString(R.string.setting_button_text));
        popDialog.show();

        return view;
    }
}
