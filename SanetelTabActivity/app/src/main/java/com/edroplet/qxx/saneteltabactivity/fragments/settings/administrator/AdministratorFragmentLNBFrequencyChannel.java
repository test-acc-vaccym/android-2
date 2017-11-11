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

public class AdministratorFragmentLNBFrequencyChannel extends Fragment {
    private static final String LNBFrequency = "lnbFrequency";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;

    @BindId(R.id.administrator_settings_lnb_frequency_channel_radio_group)
    private CustomRadioGroupWithCustomRadioButton radioGroupWithCustomRadioButton;

    @BindId(R.id.administrator_setting_lnb_frequency_channel_1)
    private CustomRadioButton radioButtonFrequency1;

    @BindId(R.id.administrator_setting_lnb_frequency_channel_2)
    private CustomRadioButton radioButtonFrequency2;

    @BindId(R.id.administrator_setting_lnb_frequency_channel_3)
    private CustomRadioButton radioButtonFrequency3;

    @BindId(R.id.administrator_setting_lnb_frequency_channel_4)
    private CustomRadioButton radioButtonFrequency4;

    private CustomRadioButton radioButton;
    private String selected;

    public static AdministratorFragmentLNBFrequencyChannel newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                       String secondLine, boolean showThird, String thirdLineStart,
                                                                       int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentLNBFrequencyChannel fragment = new AdministratorFragmentLNBFrequencyChannel();
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
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_lnb_frequency_channel, null);
        if (view == null){
            return null;
        }
        ViewInject.inject(getActivity(), getContext());

        thirdButton = view.findViewById(R.id.pop_dialog_third_button);
        radioButtonFrequency1 = view.findViewById(R.id.administrator_setting_lnb_frequency_channel_1);
        radioButtonFrequency2 = view.findViewById(R.id.administrator_setting_lnb_frequency_channel_2);
        radioButtonFrequency3 = view.findViewById(R.id.administrator_setting_lnb_frequency_channel_3);
        radioButtonFrequency4 = view.findViewById(R.id.administrator_setting_lnb_frequency_channel_4);

        String type = CustomSP.getString(getContext(),LNBFrequency,getString(R.string.administrator_setting_lnb_frequency_channel_1));
        if (type.equals(getString(R.string.administrator_setting_lnb_frequency_channel_1))){
            radioButtonFrequency1.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_lnb_frequency_channel_2))){
            radioButtonFrequency2.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_lnb_frequency_channel_3))){
            radioButtonFrequency3.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_lnb_frequency_channel_4))){
            radioButtonFrequency4.setChecked(true);
        }

        radioGroupWithCustomRadioButton = view.findViewById(R.id.administrator_settings_lnb_frequency_channel_radio_group);
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = (CustomRadioButton)view.findViewById(radioGroupWithCustomRadioButton.getCheckedRadioButtonId());
                selected = radioButton.getText().toString();
                CustomSP.putString(getContext(), LNBFrequency, selected);
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
