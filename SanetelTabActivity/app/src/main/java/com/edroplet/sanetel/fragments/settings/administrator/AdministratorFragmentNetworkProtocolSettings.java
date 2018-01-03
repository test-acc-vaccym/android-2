package com.edroplet.sanetel.fragments.settings.administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
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
import com.edroplet.sanetel.view.custom.CustomRadioGroupWithCustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 网络协议选择
 */

public class AdministratorFragmentNetworkProtocolSettings extends BroadcastReceiverFragment {
    public static final String NetworkProtocolSettingsAction = "com.edroplet.sanetel.NetworkProtocolSettingsAction";
    public static final String NetworkProtocolSettingsData = "com.edroplet.sanetel.NetworkProtocolSettingsData";
    private static final String NetworkProtocolKey = "networkProtocol";
    private  final int[] icons = {R.drawable.antenna_exploded };

    String port = "0", networkProtocol = "40";
    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.administrator_setting_network_protocol_radio_group)
    CustomRadioGroupWithCustomRadioButton networkProtocolGroup;

    SparseIntArray mapNetworkProtocol = new SparseIntArray(7);
    int [] networkProtocolIds = {R.id.administrator_setting_network_protocol_1,R.id.administrator_setting_network_protocol_2,
            R.id.administrator_setting_network_protocol_3,R.id.administrator_setting_network_protocol_4,
            R.id.administrator_setting_network_protocol_5,R.id.administrator_setting_network_protocol_6,
            R.id.administrator_setting_network_protocol_7};

    public static AdministratorFragmentNetworkProtocolSettings newInstance(
            boolean showFirst, String firstLine, boolean showSecond,
            String secondLine, boolean showThird, String thirdLineStart,
            int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentNetworkProtocolSettings fragment = new AdministratorFragmentNetworkProtocolSettings();
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

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String[] action = {NetworkProtocolSettingsAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        Protocol.sendMessage(getContext(),Protocol.cmdGetNetUserid);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_network_protocol, null);
        if (view == null){
            return null;
        }
        unbinder = ButterKnife.bind(this, view);
        int i = 0;
        for (int id: networkProtocolIds){
            mapNetworkProtocol.put(i++, id);
        }
        int type = CustomSP.getInt(getContext(),NetworkProtocolKey,0);
        networkProtocolGroup.check(mapNetworkProtocol.get(type));

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mapNetworkProtocol.indexOfKey(networkProtocolGroup.getCheckedRadioButtonId()) ;
                CustomSP.putInt(getContext(), NetworkProtocolKey, pos);
                // send command
                // cmd,set net userid,端口号,协议
                Protocol.sendMessage(getContext(),String.format(Protocol.cmdSetNetUserid, port, String.valueOf(40+pos)));
                getActivity().finish();
            }
        });

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);

            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon >= 0) {
                popDialog.setDrawable(ContextCompat.getDrawable(getContext(),icons[0]));
            }
        }
        popDialog.show();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(NetworkProtocolSettingsData);
        Object[] o = Sscanf.scan(rawData,Protocol.cmdGetNetUseridResult,port,networkProtocol);
        port = (String)o[0];
        if (port.length() == 0){
            port = "0";
        }
        networkProtocol = (String)o[1];
        int pos = Integer.parseInt(networkProtocol) - 40;
        if (pos < 0){
            pos = 0;
        }
        CustomSP.putInt(getContext(), NetworkProtocolKey, pos);
        networkProtocolGroup.check(mapNetworkProtocol.get(pos));
    }
}
