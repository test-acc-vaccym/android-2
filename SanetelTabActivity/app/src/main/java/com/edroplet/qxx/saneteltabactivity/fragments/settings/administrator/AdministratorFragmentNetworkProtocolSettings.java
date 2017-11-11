package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

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

public class AdministratorFragmentNetworkProtocolSettings extends Fragment {
    private static final String NetworkProtocolKey = "networkProtocol";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;

    private CustomRadioGroupWithCustomRadioButton radioGroupWithCustomRadioButton;

    @BindId(R.id.administrator_setting_network_protocol_1)
    private CustomRadioButton radioButtonNetworkProtocol1;
    private CustomRadioButton radioButtonNetworkProtocol2;
    private CustomRadioButton radioButtonNetworkProtocol3;
    private CustomRadioButton radioButtonNetworkProtocol4;
    private CustomRadioButton radioButtonNetworkProtocol5;
    private CustomRadioButton radioButtonNetworkProtocol6;
    private CustomRadioButton radioButtonNetworkProtocol7;

    private CustomRadioButton radioButton;
    private String selected;

    public static AdministratorFragmentNetworkProtocolSettings newInstance(boolean showFirst, String firstLine, boolean showSecond,
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_network_protocol, null);
        if (view == null){
            return null;
        }
        ViewInject.inject(getActivity(), getContext());

        thirdButton = view.findViewById(R.id.pop_dialog_third_button);
        radioButtonNetworkProtocol1 = view.findViewById(R.id.administrator_setting_network_protocol_1);
        radioButtonNetworkProtocol2 = view.findViewById(R.id.administrator_setting_network_protocol_2);
        radioButtonNetworkProtocol3 = view.findViewById(R.id.administrator_setting_network_protocol_3);
        radioButtonNetworkProtocol4 = view.findViewById(R.id.administrator_setting_network_protocol_4);
        radioButtonNetworkProtocol5 = view.findViewById(R.id.administrator_setting_network_protocol_5);
        radioButtonNetworkProtocol6 = view.findViewById(R.id.administrator_setting_network_protocol_6);
        radioButtonNetworkProtocol7 = view.findViewById(R.id.administrator_setting_network_protocol_7);

        String type = CustomSP.getString(getContext(),NetworkProtocolKey,getString(R.string.administrator_setting_network_protocol_1));
        if (type.equals(getString(R.string.administrator_setting_network_protocol_1))){
            radioButtonNetworkProtocol1.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_network_protocol_2))){
            radioButtonNetworkProtocol2.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_network_protocol_3))){
            radioButtonNetworkProtocol3.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_network_protocol_4))){
            radioButtonNetworkProtocol4.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_network_protocol_5))){
            radioButtonNetworkProtocol5.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_network_protocol_6))){
            radioButtonNetworkProtocol6.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_network_protocol_7))){
            radioButtonNetworkProtocol7.setChecked(true);
        }

        radioGroupWithCustomRadioButton = view.findViewById(R.id.administrator_setting_network_protocol_radio_group);
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = (CustomRadioButton)view.findViewById(radioGroupWithCustomRadioButton.getCheckedRadioButtonId());
                selected = radioButton.getText().toString();
                CustomSP.putString(getContext(), NetworkProtocolKey, selected);
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
