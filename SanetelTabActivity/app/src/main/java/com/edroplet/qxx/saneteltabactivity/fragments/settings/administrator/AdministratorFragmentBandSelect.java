package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.utils.sscanf.Sscanf;
import com.edroplet.qxx.saneteltabactivity.view.BroadcastReceiverFragment;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 频段选择
 */

public class AdministratorFragmentBandSelect extends BroadcastReceiverFragment {
    public static final String BandTypeKey = "bandType";
    public static final String BandTypeAction = "com.edroplet.sanetel.BandTypeAction";
    public static final String BandTypeData = "com.edroplet.sanetel.BandTypeData";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.id_administrator_settings_band_select_radio_group)
    CustomRadioGroupWithCustomRadioButton bandSelectGroup;

    int []bandTypeIds = {R.id.administrator_setting_band_ku,R.id.administrator_setting_band_ka};
    SparseIntArray bandTypes=new SparseIntArray(2);
    Unbinder unbinder;

    public static AdministratorFragmentBandSelect newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                              String secondLine, boolean showThird, String thirdLineStart,
                                                              int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentBandSelect fragment = new AdministratorFragmentBandSelect();
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
        String [] action = {BandTypeAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        // 发送命令
        Protocol.sendMessage(getContext(),Protocol.cmdGetBand);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_band_select, null);
        if (view == null){
            return null;
        }
        unbinder = ButterKnife.bind(this, view);
        int i = 0;
        for (int id: bandTypeIds){
            bandTypes.put(i++,id);
        }

        int type = CustomSP.getInt(getContext(),BandTypeKey,1);
        bandSelectGroup.check(bandTypes.get(type));

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = bandTypes.indexOfKey(bandSelectGroup.getCheckedRadioButtonId());
                CustomSP.putInt(getContext(), BandTypeKey, pos);
                //send command
                Protocol.sendMessage(getContext(), String.format(Protocol.cmdSetBand, pos));
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
        String rawData = intent.getStringExtra(BandTypeData);
        int band = 0;
        Object[] o = Sscanf.scan(rawData,Protocol.cmdGetBandResult,band);
        band = (Integer) o[0];
        // 设置选项
        bandSelectGroup.check(bandTypes.get(band));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
