package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.WaveBand;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 */

public class AdministratorFragmentLNBOscillator extends Fragment {
    private static final String LNBFrequency = "lnbFrequency";
    private static final String LNBFrequencyResourcePos = "LNBFrequencyResourcePos";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindView(R.id.layout_lnb_ku)
    LinearLayout linearLayoutKu;

    @BindView(R.id.layout_lnb_ka)
    LinearLayout linearLayoutKa;

    @BindView(R.id.low_noise_block_oscillator_ku_radio_group)
    CustomRadioGroupWithCustomRadioButton oscillatorKuSelect;

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.low_noise_block_oscillator_ka_radio_group)
    CustomRadioGroupWithCustomRadioButton oscillatorKaSelect;

    @BindView(R.id.id_administrator_settings_lnb_ku_value_1)
    RadioButton administrator_settings_lnb_ku_value_1;
    @BindView(R.id.id_administrator_settings_lnb_ku_value_2)
    RadioButton administrator_settings_lnb_ku_value_2;
    @BindView(R.id.id_administrator_settings_lnb_ku_value_3)
    RadioButton administrator_settings_lnb_ku_value_3;
    @BindView(R.id.id_administrator_settings_lnb_ku_value_4)
    RadioButton administrator_settings_lnb_ku_value_4;
    @BindView(R.id.id_administrator_settings_lnb_ku_value_5)
    RadioButton administrator_settings_lnb_ku_value_5;
    @BindView(R.id.id_administrator_settings_lnb_ku_value_6)
    RadioButton administrator_settings_lnb_ku_value_6;
    @BindView(R.id.id_administrator_settings_lnb_ku_value_7)
    RadioButton administrator_settings_lnb_ku_value_7;


    @BindView(R.id.id_administrator_settings_lnb_ka_value_1)
    RadioButton administrator_settings_lnb_ka_value_1;
    @BindView(R.id.id_administrator_settings_lnb_ka_value_2)
    RadioButton administrator_settings_lnb_ka_value_2;

    Context context;

    static SparseIntArray mapKaPosId = new SparseIntArray();
    static SparseIntArray mapKuPosId = new SparseIntArray();

    public static AdministratorFragmentLNBOscillator newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                 String secondLine, boolean showThird, String thirdLineStart,
                                                                 int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentLNBOscillator fragment = new AdministratorFragmentLNBOscillator();
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
        final View view = inflater.inflate(R.layout.activity_low_noise_block_oscillator, null);
        if (view == null){
            return null;
        }

        ButterKnife.bind(this, view);
        mapKaPosId.put(0,R.id.id_administrator_settings_lnb_ka_value_1);
        mapKaPosId.put(1,R.id.id_administrator_settings_lnb_ka_value_2);

        mapKuPosId.put(0, R.id.id_administrator_settings_lnb_ku_value_1);
        mapKuPosId.put(1, R.id.id_administrator_settings_lnb_ku_value_2);
        mapKuPosId.put(2, R.id.id_administrator_settings_lnb_ku_value_3);
        mapKuPosId.put(3, R.id.id_administrator_settings_lnb_ku_value_4);
        mapKuPosId.put(4, R.id.id_administrator_settings_lnb_ku_value_5);
        mapKuPosId.put(5, R.id.id_administrator_settings_lnb_ku_value_6);
        mapKuPosId.put(6, R.id.id_administrator_settings_lnb_ku_value_7);

        context = getContext();

        final String band = CustomSP.getString(context, WaveBand.Key, WaveBand.KU);
        // 根据不同的波段显示不同的layout
        if (band.equals(WaveBand.KU)){
            // 设置可见性
            linearLayoutKu.setVisibility(View.VISIBLE);
            linearLayoutKa.setVisibility(View.GONE);

            // 设置选择的值
            int idPos = CustomSP.getInt(getContext(),LNBFrequencyResourcePos, 4);
            int id = mapKuPosId.get(idPos);
            // ((RadioButton) view.findViewById(id)).setChecked(true);
            oscillatorKuSelect.check(id);
            if (id == R.id.id_administrator_settings_lnb_ku_value_7){
                ((CustomEditText) view.findViewById(R.id.top_custom_val)).setText("");
            }
        }else {
            // 设置可见性
            linearLayoutKa.setVisibility(View.VISIBLE);
            linearLayoutKu.setVisibility(View.GONE);
            // 设置选择的值
            int id = mapKaPosId.get( CustomSP.getInt(context,LNBFrequencyResourcePos, 0));
            // ((RadioButton) view.findViewById(id)).setChecked(true);
            oscillatorKaSelect.check(id);

        }

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val;
                if (band.equals(WaveBand.KU)) {
                    int id = oscillatorKuSelect.getCheckedRadioButtonId();
                    CustomSP.putInt(context,LNBFrequencyResourcePos,mapKuPosId.indexOfValue(id));
                    if (id == R.id.id_administrator_settings_lnb_ku_value_7){
                        val = ((CustomEditText) view.findViewById(R.id.top_custom_val)).getText().toString();
                        CustomSP.putString(context, LNBFrequency, val);
                    }
                }else{
                    int id = oscillatorKaSelect.getCheckedRadioButtonId();
                    CustomSP.putInt(context,LNBFrequencyResourcePos,mapKaPosId.indexOfValue(id));
                }
                // TODO: 2017/10/23 设置命令

                // 退出
                getActivity().finish();
            }
        });

        PopDialog popDialog = new PopDialog(context);

        popDialog.setView(view.findViewById(R.id.low_noise_block_oscillator_pop));
        popDialog.setContext(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOW_SECOND, true);
        bundle.putString(PopDialog.SECOND,getString(R.string.settings_lnb_message_first_line));
        bundle.putBoolean(PopDialog.SHOW_THIRD, true);
        bundle.putString(PopDialog.START,getString(R.string.follow_me_message_click));
        bundle.putString(PopDialog.END,getString(R.string.follow_me_forever));
        popDialog.setBundle(bundle);
        popDialog.setSetFirstColor(true);
        popDialog.setButtonText(context,getString(R.string.setting_button_text));
        popDialog.show();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
