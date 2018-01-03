package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.utils.sscanf.Sscanf;
import com.edroplet.qxx.saneteltabactivity.view.BroadcastReceiverFragment;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 功放监控
 * ●无监控
 ◎RS232监控
 ◎RS422监控
 ◎RS485监控
 */


public class AdministratorFragmentAmplifierMonitor extends BroadcastReceiverFragment {
    public static final String KEY_AMPLIFIER_MONITOR = "KEY_AMPLIFIER_MONITOR";
    public static final String AmplifierMonitorAction = "com.edroplet.sanetel.AmplifierMonitorAction";
    public static final String AmplifierMonitorData = "com.edroplet.sanetel.AmplifierMonitorData";
    private  final int[] icons = {R.drawable.antenna_exploded };

    public static SparseIntArray mapAmplifierMonitorPosId = new SparseIntArray(4);

    public static void initSparseIntArray(){
        mapAmplifierMonitorPosId.put(0, R.id.id_administrator_amplifier_monitor_none);
        mapAmplifierMonitorPosId.put(1, R.id.id_administrator_amplifier_monitor_rs232);
        mapAmplifierMonitorPosId.put(2, R.id.id_administrator_amplifier_monitor_rs422);
        mapAmplifierMonitorPosId.put(3, R.id.id_administrator_amplifier_monitor_rs485);
    }
    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.id_administrator_settings_amplifier_monitor_radio_group)
    RadioGroup amplifierMonitorRadioGroup;

    Unbinder unbinder;

    public static AdministratorFragmentAmplifierMonitor newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                    String secondLine, boolean showThird, String thirdLineStart,
                                                                    int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentAmplifierMonitor fragment = new AdministratorFragmentAmplifierMonitor();
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

    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String []action = {AmplifierMonitorAction};
        setAction(action);
        super.onCreate(savedInstanceState);

        context = getContext();
        Protocol.sendMessage(context, Protocol.cmdGetBucInfoSwitch);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_amplifier_monitor, null);
        if (view == null){
            return null;
        }
        context = getContext();

        unbinder = ButterKnife.bind(this, view);

        initSparseIntArray();

        int selected;
        selected = CustomSP.getInt(context, KEY_AMPLIFIER_MONITOR,0);
        amplifierMonitorRadioGroup.check(mapAmplifierMonitorPosId.get(selected));

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = mapAmplifierMonitorPosId.indexOfValue( amplifierMonitorRadioGroup.getCheckedRadioButtonId());
                CustomSP.putInt(getContext(), KEY_AMPLIFIER_MONITOR, index);
                // send command
                // 系统通信规范 4.14.7
                Protocol.sendMessage(context, String.format(Protocol.cmdSetBucInfoSwitch, index));
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
        return popDialog.show();
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(AmplifierMonitorData);
        String bucInfoSwitch = "0";
        Object[] o= Sscanf.scan(rawData, Protocol.cmdGetBucInfoSwitchResult, bucInfoSwitch);
        bucInfoSwitch = (String) o[0];
        int pos = Integer.parseInt(bucInfoSwitch);
        amplifierMonitorRadioGroup.check(mapAmplifierMonitorPosId.get(pos));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
