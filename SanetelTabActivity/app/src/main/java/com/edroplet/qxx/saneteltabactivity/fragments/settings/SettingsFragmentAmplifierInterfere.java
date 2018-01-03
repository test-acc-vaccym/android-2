package com.edroplet.qxx.saneteltabactivity.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 *
 */

public class SettingsFragmentAmplifierInterfere extends Fragment {
    public static SettingsFragmentAmplifierInterfere newInstance(String info) {
        Bundle args = new Bundle();
        SettingsFragmentAmplifierInterfere fragment = new SettingsFragmentAmplifierInterfere();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }


    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;
    @BindView(R.id.settings_amplifier_interfere_use)
    CustomRadioButton interfereUse;
    @BindView(R.id.settings_amplifier_interfere_disuse)
    CustomRadioButton interfereDisuse;

    public static final String KEY_USE_INTERFERE="KEY_USE_INTERFERE";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings_amplifier_interfere, null);
        ButterKnife.bind(this, view);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interfereUse.isChecked()){
                    // 保存设置
                    CustomSP.putBoolean(getContext(),KEY_USE_INTERFERE,true);
                    // TODO: 2017/11/12 发送命令使用零星干扰
                }else {
                    // 保存设置
                    CustomSP.putBoolean(getContext(),KEY_USE_INTERFERE,false);
                    // TODO: 2017/11/12 发送命令不使用零星干扰
                }
            }
        });

        boolean isUse = CustomSP.getBoolean(getContext(),KEY_USE_INTERFERE, true);
        if (isUse){
            interfereUse.setChecked(true);
        }else {
            interfereDisuse.setChecked(true);
        }

        // 设置自定义框内容
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        Context context = getContext();
        popDialog.setContext(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOW_SECOND, true);
        bundle.putBoolean(PopDialog.SHOW_THIRD, true);
        bundle.putString(PopDialog.SECOND, context.getString(R.string.settings_amplifier_interfere_second));
        bundle.putString(PopDialog.START, context.getString(R.string.settings_amplifier_manufacture_message_third_start));
        bundle.putString(PopDialog.END, context.getString(R.string.follow_me_forever));
        popDialog.setBundle(bundle);
        popDialog.setButtonText(context, context.getString(R.string.setting_button_text));
        popDialog.show();

        return view;
    }
}
