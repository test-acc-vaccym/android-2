package com.edroplet.sanetel.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.IpUtils;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.SystemServices;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.IPEdit;
import com.edroplet.sanetel.view.custom.CustomButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 5.7	网络参数指令
 * IP 设置
 */

public class AdministratorFragmentIPSettings extends BroadcastReceiverFragment {
    public static final String IPSettingsAction = "com.edroplet.sanetel.IPSettingsAction";
    public static final String IPSettingsData = "com.edroplet.sanetel.IPSettingsData";

    private static final int[] icons = {R.drawable.antenna_exploded};

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.administrator_setting_ip_address)
    IPEdit ipAddress;
    @BindView(R.id.administrator_setting_ip_mask)
    IPEdit ipMask;

    Unbinder unbinder;
    String ip;
    String mask;
    Context context;

    public static AdministratorFragmentIPSettings newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                              String secondLine, boolean showThird, String thirdLineStart,
                                                              int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentIPSettings fragment = new AdministratorFragmentIPSettings();
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
        String[] action = {IPSettingsAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        Protocol.sendMessage(context, Protocol.cmdGetIP);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_ip_settings, null);
        if (view == null){
            return null;
        }

        context = getContext();
        unbinder =  ButterKnife.bind(this, view);
        SystemServices.networkInfo networkInfo = SystemServices.getIPAddress(context);
        String ipWIfi = networkInfo.getGateway();
        String address = CustomSP.getString(context,CustomSP.KeyIPSettingsAddress, ipWIfi);
        ipAddress.setText(address);

        String maskWIfi = networkInfo.getIp();
        String mask = CustomSP.getString(context,CustomSP.KeyIPSettingsMask, maskWIfi);
        ipMask.setText(mask);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipAddress.getText();
                String mask = ipMask.getText();
                String gateWay = IpUtils.getLowAddr(ip,mask);
                CustomSP.putString(context,CustomSP.KeyIPSettingsAddress, ip);
                CustomSP.putString(context,CustomSP.KeyIPSettingsMask, mask);
                // send command
                // cmd,set ip,网络IP,子网掩码,网关*ff<CR><LF>
                Protocol.sendMessage(context, String.format(Protocol.cmdSetIP,ip, mask,gateWay));
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
        String rawData = intent.getStringExtra(IPSettingsData);
        Object[] o = Sscanf.scan(rawData, Protocol.cmdGetIPResult,ip, mask);
        ip = (String) o[0];
        mask = (String) o[1];
        if (ip != null && ip.length() > 0) {
            ipAddress.setText(ip);
            CustomSP.putString(context,CustomSP.KeyIPSettingsAddress, ip);
        }
        if (mask != null && mask.length() > 0) {
            ipMask.setText(mask);
            CustomSP.putString(context, CustomSP.KeyIPSettingsMask, mask);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
