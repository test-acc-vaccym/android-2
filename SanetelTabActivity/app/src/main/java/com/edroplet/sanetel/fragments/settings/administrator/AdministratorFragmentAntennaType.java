package com.edroplet.sanetel.fragments.settings.administrator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomRadioGroupWithCustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 天线类型， 不需要发送获取和设置命令
 */

public class AdministratorFragmentAntennaType extends Fragment {
    private static final String AntennaType = "antennaType";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.id_administrator_settings_antenna_type_radio_group)
    CustomRadioGroupWithCustomRadioButton antennaTypeGroup;

    static SparseIntArray mapAntennaType = new SparseIntArray(5);

    Unbinder unbinder;

    void initAntennaTypeArray(){
        mapAntennaType.put(0, R.id.administrator_setting_antenna_type_two_two);
        mapAntennaType.put(1, R.id.administrator_setting_antenna_type_two_one);
        mapAntennaType.put(2, R.id.administrator_setting_antenna_type_two_zero);
        mapAntennaType.put(3, R.id.administrator_setting_antenna_type_three_one);
        mapAntennaType.put(4, R.id.administrator_setting_antenna_type_three_zero);
    }

    public static AdministratorFragmentAntennaType newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                               String secondLine, boolean showThird, String thirdLineStart,
                                                               int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentAntennaType fragment = new AdministratorFragmentAntennaType();
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
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_antenna_type, null);
        if (view == null){
            return null;
        }
        initAntennaTypeArray();

        unbinder = ButterKnife.bind(this,view);

        int type = CustomSP.getInt(getContext(),AntennaType,1);
        antennaTypeGroup.check(mapAntennaType.get(type));

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomSP.putInt(getContext(), AntennaType, mapAntennaType.indexOfValue(antennaTypeGroup.getCheckedRadioButtonId()));
                // no need send command
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
}
