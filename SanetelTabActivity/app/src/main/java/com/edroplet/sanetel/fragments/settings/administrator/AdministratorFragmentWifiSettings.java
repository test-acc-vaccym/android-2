package com.edroplet.sanetel.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.HLKProtocol;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.InputFilterStartsWith;
import com.edroplet.sanetel.utils.NetworkUtils;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.sanetel.utils.CustomSP.WifiSettingsNameKey;
import static com.edroplet.sanetel.services.network.SystemServices.XWWT_PREFIX;

/**
 * Created by qxs on 2017/9/19.
 * Wifi设置
 */

public class AdministratorFragmentWifiSettings extends Fragment {
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
    String ipWIfi;

    public static AdministratorFragmentWifiSettings newInstance(
            boolean showFirst, String firstLine, boolean showSecond,
            String secondLine, boolean showThird, String thirdLineStart,
            int icon, String buttonText, String thirdLineEnd,
            boolean showForth, String forthLine) {
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
        args.putBoolean(PopDialog.SHOW_FORTH,showForth);
        args.putString(PopDialog.FORTH, forthLine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get wifi ssid;
//        String commandSets[] = {HLKProtocol.UdpEnable, String.format(HLKProtocol.UdpAPSsid, wifiSSID),HLKProtocol.UdpNPackLen1, HLKProtocol.UdpSave, HLKProtocol.UdpApply};
//        String expectedSets[] = {HLKProtocol.UdpEnableResponse,String.format(HLKProtocol.UdpAPSsidResponse, wifiSSID),HLKProtocol.UdpNPackLen1Response, HLKProtocol.UdpSaveResponse, HLKProtocol.UdpApplyResponse};
//
//        HLKProtocol.sendUdpMessage(context, HLKProtocol.LocalHost,HLKProtocol.LocalPort, ipWIfi,HLKProtocol.Port,commandSets, expectedSets);
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
        NetworkUtils.networkInfo networkInfo = NetworkUtils.getIPAddress(context);
        ipWIfi = networkInfo.getGateway();
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
                // Protocol.sendMessage(context,String.format(Protocol.cmdSetWifiName, wifiSSID));
                String commandSets[] = {HLKProtocol.UdpEnable, String.format(HLKProtocol.UdpAPSsid, wifiSSID),HLKProtocol.UdpNPackLen1, HLKProtocol.UdpSave, HLKProtocol.UdpApply};
                String expectedSets[] = {HLKProtocol.UdpEnableResponse,String.format(HLKProtocol.UdpAPSsidResponse, wifiSSID),HLKProtocol.UdpNPackLen1Response, HLKProtocol.UdpSaveResponse, HLKProtocol.UdpApplyResponse};

                HLKProtocol.sendUdpMessage(context, HLKProtocol.LocalHost,HLKProtocol.LocalPort, ipWIfi,HLKProtocol.Port,commandSets, expectedSets);

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
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
