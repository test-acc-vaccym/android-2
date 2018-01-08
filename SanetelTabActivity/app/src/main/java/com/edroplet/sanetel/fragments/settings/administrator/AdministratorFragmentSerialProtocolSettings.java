package com.edroplet.sanetel.fragments.settings.administrator;

import android.content.Intent;
import android.os.Binder;
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
 * 串口协议选择
 * 5.9	串口协议选择
 */

public class AdministratorFragmentSerialProtocolSettings extends BroadcastReceiverFragment {
    public static final String SerialProtocolAction = "com.edroplet.sanetel.SerialProtocolAction";
    public static final String SerialProtocolData = "com.edroplet.sanetel.SerialProtocolData";

    private static final String SerialProtocolKey = "serialProtocol";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;
    @BindView(R.id.administrator_setting_serial_protocol_radio_group)
    CustomRadioGroupWithCustomRadioButton serialProtocolGroup;

    SparseIntArray mapSerialProtocol = new SparseIntArray(7);
    Unbinder unbinder;

    public static AdministratorFragmentSerialProtocolSettings newInstance(
            boolean showFirst, String firstLine, boolean showSecond,
            String secondLine, boolean showThird, String thirdLineStart,
            int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentSerialProtocolSettings fragment = new AdministratorFragmentSerialProtocolSettings();
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
        String[] action = {SerialProtocolAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        Protocol.sendMessage(getContext(),Protocol.cmdGetComUserid);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_serial_protocol, null);
        if (view == null){
            return null;
        }
        unbinder = ButterKnife.bind(this,view);

        mapSerialProtocol.put(0,R.id.administrator_setting_serial_protocol_1);
        mapSerialProtocol.put(1,R.id.administrator_setting_serial_protocol_2);
        mapSerialProtocol.put(2,R.id.administrator_setting_serial_protocol_3);
        mapSerialProtocol.put(3,R.id.administrator_setting_serial_protocol_4);
        mapSerialProtocol.put(4,R.id.administrator_setting_serial_protocol_5);
        mapSerialProtocol.put(5,R.id.administrator_setting_serial_protocol_6);
        mapSerialProtocol.put(6,R.id.administrator_setting_serial_protocol_7);

        int type = CustomSP.getInt(getContext(),SerialProtocolKey,0);
        if (type >= mapSerialProtocol.size()){
            type = mapSerialProtocol.size() - 1;
        }
        serialProtocolGroup.check(mapSerialProtocol.get(type));

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mapSerialProtocol.indexOfValue(serialProtocolGroup.getCheckedRadioButtonId());

                // send command
                // 5.9.2	设置 发送指令格式：$cmd,set com userid,模式*ff<CR><LF>
                String val = String.valueOf(pos);
                if (pos == -1){
                    pos = 0;
                    val = "0";
                } if (pos == mapSerialProtocol.size() - 1){
                    val = "9";
                }
                CustomSP.putInt(getContext(), SerialProtocolKey, pos);

                Protocol.sendMessage(getContext(),String.format(Protocol.cmdSetComUserid,val));
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
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(SerialProtocolData);
        String type = "0";
        Object[] objects = Sscanf.scan(rawData,Protocol.cmdGetComUseridResult,type);
        type = (String) objects[0];

        int pos = Integer.parseInt(type);
        if (pos < 0){
            pos = 0;
        }else if (pos >= mapSerialProtocol.size()){
            pos = mapSerialProtocol.size() - 1;
        }
        CustomSP.putInt(getContext(), SerialProtocolKey, pos);
        serialProtocolGroup.check(mapSerialProtocol.get(pos));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
