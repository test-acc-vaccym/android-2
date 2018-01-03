package com.edroplet.sanetel.fragments.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 *
 */

public class SettingsFragmentAmplifierInterfere extends BroadcastReceiverFragment {
    public static final String AmplifierInterfereAction = "com.edroplet.sanetel.AmplifierInterfereAction";
    public static final String AmplifierInterfereData = "com.edroplet.sanetel.AmplifierInterfereData";

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

    public static final String KEY_USE_INTERFERE="KEY_USE_INTERFERE";
    Context context;
    Unbinder unbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String[] action = {AmplifierInterfereAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        Protocol.sendMessage(context,Protocol.cmdGetProtectState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings_amplifier_interfere, null);
        unbinder = ButterKnife.bind(this, view);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interfereUse.isChecked()){
                    // 保存设置
                    CustomSP.putBoolean(getContext(),KEY_USE_INTERFERE,true);
                    // TODO: 2017/11/12 发送命令使用邻星干扰
                    Protocol.sendMessage(context, String.format(Protocol.cmdSetProtectState,"1"));
                }else {
                    // 保存设置
                    CustomSP.putBoolean(getContext(),KEY_USE_INTERFERE,false);
                    // TODO: 2017/11/12 发送命令不使用邻星干扰
                    Protocol.sendMessage(context, String.format(Protocol.cmdSetProtectState,"0"));
                }
            }
        });

        boolean isUse = CustomSP.getBoolean(getContext(),KEY_USE_INTERFERE, true);
        if (isUse){
            interfereUse.setChecked(true);
        }else {
            interfereUse.setChecked(false);
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

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(AmplifierInterfereData);
        String use = "0";
        Object[]objects = Sscanf.scan(rawData,Protocol.cmdGetProtectStateResult,use);
        use = (String) objects[0];
        if (use.equals("0")){
            interfereUse.setChecked(false);
            CustomSP.putBoolean(getContext(),KEY_USE_INTERFERE, false);
        }else{
            interfereUse.setChecked(true);
            CustomSP.putBoolean(getContext(),KEY_USE_INTERFERE, true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
