package com.edroplet.sanetel.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomRadioGroupWithCustomRadioButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentBandSelect.BandTypeDefault;
import static com.edroplet.sanetel.fragments.settings.administrator.AdministratorFragmentBandSelect.BandTypeKey;

/**
 * Created by qxs on 2017/9/19.
 * LNB本振
 */

public class AdministratorFragmentLNBOscillator extends BroadcastReceiverFragment {
    private static final String LNBFrequency = "lnbFrequency";
    private static final String LNBFrequencyResourcePos = "LNBFrequencyResourcePos";
    public static final String LNBOscAction= "com.edroplet.sanetel.LNBOscAction";
    public static final String LNBOscData = "com.edroplet.sanetel.LNBOscData";

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
    @BindView(R.id.oscillator_custom_ku_val)
    CustomEditText tvCustomVal;

    @BindView(R.id.oscillator_custom_ka_val)
    CustomEditText tvKaCustomVal;

    Unbinder unbinder;

    Context context;
    static SparseIntArray mapKaPosId = new SparseIntArray();
    static SparseIntArray mapKuPosId = new SparseIntArray();

    static final int kuPosition = 0;
    static final int [] kaVals = {17400, 19250};
    static final int [] kaValIds={R.id.id_administrator_settings_lnb_ka_value_1,
            R.id.id_administrator_settings_lnb_ka_value_2,
            R.id.id_administrator_settings_lnb_ka_value_3};

    static final int [] kuVals = {5150, 9750, 10000, 10750, 11300, 10600};
    static final int [] kuValIds= {R.id.id_administrator_settings_lnb_ku_value_1,
            R.id.id_administrator_settings_lnb_ku_value_2,
            R.id.id_administrator_settings_lnb_ku_value_3,R.id.id_administrator_settings_lnb_ku_value_4,
            R.id.id_administrator_settings_lnb_ku_value_5,R.id.id_administrator_settings_lnb_ku_value_6,
            R.id.id_administrator_settings_lnb_ku_value_7};

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        context = getContext();
        String []action = {LNBOscAction};
        setAction(action);
        super.onCreate(savedInstanceState);

        Protocol.sendMessage(context,Protocol.cmdGetLnbLf);
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData =intent.getStringExtra(LNBOscData);
        String lnbLf = "" ;

        Object[] o = Sscanf.scan(rawData, Protocol.cmdGetLnbLfResult,lnbLf);
        lnbLf = (String)o[0];
        int lnb = Integer.parseInt(lnbLf);
        int bandType = CustomSP.getInt(context, BandTypeKey , BandTypeDefault );
        if (bandType != kuPosition) {
            int pos = Arrays.asList(kaVals).indexOf(lnb);
            if (pos == -1){
                pos = kaVals.length;
            }
            oscillatorKaSelect.check(mapKaPosId.get(pos));
            CustomSP.putInt(context,LNBFrequencyResourcePos,pos);
            // 自定义
            if (pos == kaVals.length){
                String val = String.valueOf(lnb);
                tvKaCustomVal.setText(val);
                CustomSP.putString(context, LNBFrequency, val);
            }
        }else{
            int pos = Arrays.asList(kuVals).indexOf(lnb);
            if (pos == -1){
                pos = kuVals.length;
            }
            CustomSP.putInt(context,LNBFrequencyResourcePos,pos);
            // 自定义
            if (pos == kuVals.length){
                String val = String.valueOf(lnb);
                tvCustomVal.setText(val);
                CustomSP.putString(context, LNBFrequency, val);
            }
            oscillatorKuSelect.check(mapKuPosId.get(pos));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_low_noise_block_oscillator, null);
        if (view == null){
            return null;
        }

        unbinder = ButterKnife.bind(this, view);

        // 监听焦点获取到后，选择该选项
        tvCustomVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oscillatorKuSelect.check(R.id.id_administrator_settings_lnb_ku_value_7);
            }
        });

        // 监听焦点获取到后，选择该选项
        tvKaCustomVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oscillatorKaSelect.check(R.id.id_administrator_settings_lnb_ka_value_3);
            }
        });

        tvCustomVal.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.lnbMin,InputFilterFloat.lnbMax,InputFilterFloat.angleValidBit)});
        tvKaCustomVal.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.lnbMin,InputFilterFloat.lnbMax,InputFilterFloat.angleValidBit)});
        int i = 0;
        for (int id : kaValIds){
            mapKaPosId.put(i++, id);
        }
        i = 0;
        for (int id : kuValIds){
            mapKuPosId.put(i++, id);
        }

        context = getContext();

        final int band = CustomSP.getInt(context, BandTypeKey, BandTypeDefault);

        String val = CustomSP.getString(context, LNBFrequency, "");
        // 根据不同的波段显示不同的layout
        if (band == kuPosition){
            // 设置可见性
            linearLayoutKu.setVisibility(View.VISIBLE);
            linearLayoutKa.setVisibility(View.GONE);

            // 设置选择的值
            int idPos = CustomSP.getInt(getContext(),LNBFrequencyResourcePos, 4);
            int id = mapKuPosId.get(idPos);
            // ((RadioButton) view.findViewById(id)).setChecked(true);
            oscillatorKuSelect.check(id);
            if (id == R.id.id_administrator_settings_lnb_ku_value_7){
                tvCustomVal.setText(val);
            }
        }else {
            // 设置可见性
            linearLayoutKa.setVisibility(View.VISIBLE);
            linearLayoutKu.setVisibility(View.GONE);
            // 设置选择的值
            int id = mapKaPosId.get( CustomSP.getInt(context,LNBFrequencyResourcePos, 0));
            // ((RadioButton) view.findViewById(id)).setChecked(true);
            oscillatorKaSelect.check(id);
            if (id == R.id.id_administrator_settings_lnb_ka_value_3){
                tvKaCustomVal.setText(val);
            }
        }

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val;
                if (band == kuPosition) {
                    int id = oscillatorKuSelect.getCheckedRadioButtonId();
                    int pos = mapKuPosId.indexOfValue(id);
                    if (pos < 0){
                        pos = kuVals.length;
                    }
                    CustomSP.putInt(context,LNBFrequencyResourcePos,pos);
                    if (id == R.id.id_administrator_settings_lnb_ku_value_7){
                        val = tvCustomVal.getText().toString();
                        CustomSP.putString(context, LNBFrequency, val);
                    }else {
                        val = String.valueOf(kuVals[pos]) ;
                    }
                } else{
                    int id = oscillatorKaSelect.getCheckedRadioButtonId();
                    int pos = mapKuPosId.indexOfValue(id);
                    if (pos < 0){
                        pos = kaVals.length;
                    }
                    CustomSP.putInt(context,LNBFrequencyResourcePos,pos);

                    if (id == R.id.id_administrator_settings_lnb_ka_value_3){
                        val = tvKaCustomVal.getText().toString();
                        CustomSP.putString(context, LNBFrequency, val);
                    }else {
                        val = String.valueOf(kaVals[pos]) ;
                    }
                }
                // 设置命令
                Protocol.sendMessage(context,String.format(Protocol.cmdSetLnbLf, val));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
