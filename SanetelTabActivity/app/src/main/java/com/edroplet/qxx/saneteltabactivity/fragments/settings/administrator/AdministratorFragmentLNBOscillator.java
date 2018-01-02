package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
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

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.utils.sscanf.Sscanf;
import com.edroplet.qxx.saneteltabactivity.view.BroadcastReceiverFragment;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator.AdministratorFragmentBandSelect.BandTypeKey;

/**
 * Created by qxs on 2017/9/19.
 * LNB本振
 */

public class AdministratorFragmentLNBOscillator extends BroadcastReceiverFragment {
    private static final String LNBFrequency = "lnbFrequency";
    private static final String LNBFrequencyResourcePos = "LNBFrequencyResourcePos";
    private static final String LNB_OSC_ACTION = "com.edroplet.sanetel.LNB_OSC_ACTION";
    private static final String LNB_OSC_DATA = "com.edroplet.sanetel.LNB_OSC_DATA";

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

    Unbinder unbinder;

    Context context;
    static SparseIntArray mapKaPosId = new SparseIntArray();
    static SparseIntArray mapKuPosId = new SparseIntArray();

    static final int kuPosition = 0;
    static final int [] kaVals = {17400, 19250};
    static final int [] kaValIds={R.id.id_administrator_settings_lnb_ka_value_1,
            R.id.id_administrator_settings_lnb_ka_value_2};

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
        String []action = {LNB_OSC_ACTION};
        setAction(action);
        super.onCreate(savedInstanceState);

        Protocol.sendMessage(context,Protocol.cmdGetLnbLf);
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData =intent.getStringExtra(LNB_OSC_DATA);
        String lnbLf = "" ;

        Object[] o = Sscanf.scan(rawData, Protocol.cmdGetLnbLfResult,lnbLf);
        lnbLf = (String)o[0];
        int lnb = Integer.parseInt(lnbLf);
        int bandType = CustomSP.getInt(context, BandTypeKey , 0 );
        if (bandType != kuPosition) {
            int pos = Arrays.asList(kaVals).indexOf(lnb);
            if (pos == -1){
                pos = 0;
            }
            oscillatorKaSelect.check(mapKaPosId.get(pos));
        }else{
            int pos = Arrays.asList(kuVals).indexOf(lnb);
            if (pos == -1){
                pos = kuVals.length;
            }
            // 自定义
            if (pos == kuVals.length){
                tvCustomVal.setText(String.valueOf(lnb));
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
        int i = 0;
        for (int id : kaValIds){
            mapKaPosId.put(i++, id);
        }
        i = 0;
        for (int id : kuValIds){
            mapKuPosId.put(i++, id);
        }

        context = getContext();

        final int band = CustomSP.getInt(context, BandTypeKey, kuPosition);
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
                ((CustomEditText) view.findViewById(R.id.oscillator_custom_ku_val)).setText("");
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
                if (band == kuPosition) {
                    int id = oscillatorKuSelect.getCheckedRadioButtonId();
                    int pos = mapKuPosId.indexOfValue(id);
                    CustomSP.putInt(context,LNBFrequencyResourcePos,pos);
                    if (id == R.id.id_administrator_settings_lnb_ku_value_7){
                        val = ((CustomEditText) view.findViewById(R.id.oscillator_custom_ku_val)).getText().toString();
                        CustomSP.putString(context, LNBFrequency, val);
                    }else {
                        val = String.valueOf(kuVals[pos]) ;
                    }
                }else{
                    int id = oscillatorKaSelect.getCheckedRadioButtonId();
                    int pos = mapKuPosId.indexOfValue(id);
                    CustomSP.putInt(context,LNBFrequencyResourcePos,pos);
                    val = String.valueOf(kuVals[pos]) ;
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
        unbinder.unbind();
    }
}
