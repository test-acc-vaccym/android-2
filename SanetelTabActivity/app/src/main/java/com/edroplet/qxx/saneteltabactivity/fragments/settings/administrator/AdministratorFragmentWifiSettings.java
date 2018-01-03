package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterStartsWith;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.utils.sscanf.Sscanf;
import com.edroplet.qxx.saneteltabactivity.view.BroadcastReceiverFragment;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.qxx.saneteltabactivity.utils.CustomSP.WifiSettingsNameKey;
import static com.edroplet.qxx.saneteltabactivity.utils.SystemServices.XWWT_PREFIX;

/**
 * Created by qxs on 2017/9/19.
 * Wifi设置
 */

public class AdministratorFragmentWifiSettings extends BroadcastReceiverFragment {
    public static final String WifiSettingsAction = "com.edroplet.sanetel.WifiSettingsAction";
    public static final String WifiSettingsData = "com.edroplet.sanetel.WifiSettingsData";

    @BindView(R.id.administrator_setting_wifi_name)
    CustomEditText etWifiName;

    private static final int[] icons = {R.drawable.antenna_exploded};

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    private Unbinder unbinder;
    Context context;
    String wifiName;

    public static AdministratorFragmentWifiSettings newInstance(
            boolean showFirst, String firstLine, boolean showSecond,
            String secondLine, boolean showThird, String thirdLineStart,
            int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentWifiSettings fragment = new AdministratorFragmentWifiSettings();
        args.putBoolean(PopDialog.SHOW_FIRST,showFirst);
        args.putString(PopDialog.FIRST, firstLine);
        args.putBoolean(PopDialog.SHOW_SECOND,showSecond);
        args.putString(PopDialog.SECOND, secondLine);
        args.putBoolean(PopDialog.SHOW_THIRD,showThird);
        args.putString(PopDialog.START, thirdLineStart);
        args.putInt(PopDialog.ICON, icon);
        args.putString(PopDialog.BUTTON_TEXT, buttonText);
        args.putString(PopDialog.END, thirdLineEnd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        String[] action = {WifiSettingsAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        Protocol.sendMessage(context, Protocol.cmdGetWifiName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_wifi_name, null);
        if (view == null){
            return null;
        }

        unbinder = ButterKnife.bind(this, view);

        context = getContext();

        etWifiName.setFilters(new InputFilter[]{new InputFilterStartsWith(XWWT_PREFIX)});

        String deviceName = CustomSP.getString(context,WifiSettingsNameKey, "");
        etWifiName.setText(deviceName);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifiSSID = etWifiName.getText().toString();
                // 确保WiFi名称以xwwt开头
                if (!wifiSSID.startsWith(XWWT_PREFIX))
                    wifiSSID = XWWT_PREFIX + wifiSSID;
                CustomSP.putString(context,WifiSettingsNameKey,wifiSSID);
                // send command
                // 5.6	WIFI名称 发送指令格式：$cmd,set wifi name*ff<CR><LF>
                Protocol.sendMessage(context,String.format(Protocol.cmdSetWifiName, wifiSSID));
                getActivity().finish();
            }
        });

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);

            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon >= 0) {
                popDialog.setDrawable(ContextCompat.getDrawable(context,icons[0]));
            }
        }
        return popDialog.show();
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(WifiSettingsData);
        Object[] objects = Sscanf.scan(rawData,Protocol.cmdGetWifiNameResult, wifiName);
        wifiName = (String) objects[0];
        if (wifiName != null && wifiName.length() > 0 && wifiName.startsWith(XWWT_PREFIX)){
            etWifiName.setText(wifiName);
            CustomSP.putString(context,WifiSettingsNameKey,wifiName);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
