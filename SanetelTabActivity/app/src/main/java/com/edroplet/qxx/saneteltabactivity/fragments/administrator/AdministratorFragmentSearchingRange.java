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

public class AdministratorFragmentSearchingRange extends Fragment {
    private static final String SearchingRangeKey = "searchingRange";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindId(R.id.pop_dialog_third_button)
    private CustomButton thirdButton;

    @BindId(R.id.administrator_setting_searching_range_radio_group)
    private CustomRadioGroupWithCustomRadioButton radioGroupWithCustomRadioButton;

    @BindId(R.id.administrator_setting_searching_range_1)
    private CustomRadioButton radioButtonRange1;

    @BindId(R.id.administrator_setting_searching_range_2)
    private CustomRadioButton radioButtonRange2;

    @BindId(R.id.administrator_setting_searching_range_3)
    private CustomRadioButton radioButtonRange3;

    private CustomRadioButton radioButton;
    private String selected;

    public static AdministratorFragmentSearchingRange newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                  String secondLine, boolean showThird, String thirdLineStart,
                                                                  int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentSearchingRange fragment = new AdministratorFragmentSearchingRange();
        args.putBoolean(PopDialog.SHOWFIRST,showFirst);
        args.putString(PopDialog.FIRST, firstLine);
        args.putBoolean(PopDialog.SHOWSECOND,showSecond);
        args.putString(PopDialog.SECOND, secondLine);
        args.putBoolean(PopDialog.SHOWTHIRD,showThird);
        args.putString(PopDialog.START, thirdLineStart);
        args.putInt("icon", icon);
        args.putString(PopDialog.BUTTONTEXT, buttonText);
        args.putString(PopDialog.END, thirdLineEnd);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_searching_range, null);
        if (view == null){
            return null;
        }
        ViewInject.inject(getActivity(), getContext());

        thirdButton = view.findViewById(R.id.pop_dialog_third_button);
        radioButtonRange1 = view.findViewById(R.id.administrator_setting_searching_range_1);
        radioButtonRange2 = view.findViewById(R.id.administrator_setting_searching_range_2);
        radioButtonRange3 = view.findViewById(R.id.administrator_setting_searching_range_3);

        String type = CustomSP.getString(getContext(),SearchingRangeKey,getString(R.string.administrator_setting_searching_range_1));
        if (type.equals(getString(R.string.administrator_setting_searching_range_1))){
            radioButtonRange1.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_searching_range_2))){
            radioButtonRange2.setChecked(true);
        }else if (type.equals(getString(R.string.administrator_setting_searching_range_3))){
            radioButtonRange3.setChecked(true);
        }

        radioGroupWithCustomRadioButton = view.findViewById(R.id.administrator_setting_searching_range_radio_group);
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = (CustomRadioButton)view.findViewById(radioGroupWithCustomRadioButton.getCheckedRadioButtonId());
                selected = radioButton.getText().toString();
                CustomSP.putString(getContext(), SearchingRangeKey, selected);
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

            int icon = bundle.getInt("icon", -1);
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