package com.edroplet.qxx.saneteltabactivity.fragments.administrator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

/**
 * Created by qxs on 2017/9/19.
 */

public class AdministratorFragmentSerialProtocolSettings extends Fragment {
    private static final String SerialProtocolKey = "serialProtocol";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;

    private CustomRadioGroupWithCustomRadioButton radioGroupWithCustomRadioButton;

    @BindId(R.id.administrator_setting_serial_protocol_1)
    private CustomRadioButton radioButtonSerialProtocol1;
    private CustomRadioButton radioButtonSerialProtocol2;
    private CustomRadioButton radioButtonSerialProtocol3;
    private CustomRadioButton radioButtonSerialProtocol4;
    private CustomRadioButton radioButtonSerialProtocol5;
    private CustomRadioButton radioButtonSerialProtocol6;
    private CustomRadioButton radioButtonSerialProtocol7;

    private CustomRadioButton radioButton;
    private String selected;

    public static AdministratorFragmentSerialProtocolSettings newInstance(boolean showFirst, String firstLine, boolean showSecond,
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_serial_protocol, null);
        if (view == null){
            return null;
        }
        ViewInject.inject(getActivity(), getContext());

        thirdButton = view.findViewById(R.id.pop_dialog_third_button);
        radioButtonSerialProtocol1 = view.findViewById(R.id.administrator_setting_serial_protocol_1);
        radioButtonSerialProtocol2 = view.findViewById(R.id.administrator_setting_serial_protocol_2);
        radioButtonSerialProtocol3 = view.findViewById(R.id.administrator_setting_serial_protocol_3);
        radioButtonSerialProtocol4 = view.findViewById(R.id.administrator_setting_serial_protocol_4);
        radioButtonSerialProtocol5 = view.findViewById(R.id.administrator_setting_serial_protocol_5);
        radioButtonSerialProtocol6 = view.findViewById(R.id.administrator_setting_serial_protocol_6);
        radioButtonSerialProtocol7 = view.findViewById(R.id.administrator_setting_serial_protocol_7);

        String type = CustomSP.getString(getContext(),SerialProtocolKey,getString(R.string.administrator_setting_serial_protocol_1));
        if (type.equals(getString(R.string.administrator_setting_serial_protocol_1))){
            radioButtonSerialProtocol1.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_serial_protocol_2))){
            radioButtonSerialProtocol2.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_serial_protocol_3))){
            radioButtonSerialProtocol3.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_serial_protocol_4))){
            radioButtonSerialProtocol4.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_serial_protocol_5))){
            radioButtonSerialProtocol5.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_serial_protocol_6))){
            radioButtonSerialProtocol6.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_serial_protocol_7))){
            radioButtonSerialProtocol7.setChecked(true);
        }

        radioGroupWithCustomRadioButton = view.findViewById(R.id.administrator_setting_serial_protocol_radio_group);
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = (CustomRadioButton)view.findViewById(radioGroupWithCustomRadioButton.getCheckedRadioButtonId());
                selected = radioButton.getText().toString();
                CustomSP.putString(getContext(), SerialProtocolKey, selected);
                // todo send command
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
}
