package com.edroplet.sanetel.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 出厂标定
 */

public class AdministratorFragmentFactoryIncriminate extends BroadcastReceiverFragment {
    private static final String FactoryIncriminateAzimuthKey = "factoryIncriminateAzimuth";
    private static final String FactoryIncriminatePitchKey = "factoryIncriminatePitch";
    private static final String FactoryIncriminatePolarizationKey = "factoryIncriminatePolarization";
    public static final String FactoryIncriminateAction = "com.edroplet.sanetel.FactoryIncriminateAction";
    public static final String FactoryIncriminateData = "com.edroplet.sanetel.FactoryIncriminateData";
    private static final int[] icons = {R.drawable.antenna_exploded};

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.administrator_antenna_incriminate_tv_azimuth)
    CustomEditText factoryIncriminateAzimuth;
    @BindView(R.id.administrator_antenna_incriminate_tv_pitch)
    CustomEditText factoryIncriminatePitch;
    @BindView(R.id.administrator_antenna_incriminate_polarization)
    CustomEditText factoryIncriminatePolarization;

    Unbinder unbinder;

    String azimuth="", pitch="", reserve="", polarization="", pitchOffset="";
    Context context;

    public static AdministratorFragmentFactoryIncriminate newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                      String secondLine, boolean showThird, String thirdLineStart,
                                                                      int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentFactoryIncriminate fragment = new AdministratorFragmentFactoryIncriminate();
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
        String[] action = {FactoryIncriminateAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        Protocol.sendMessage(context, Protocol.cmdGetFactoryCalibAnt);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_factory_incriminate, null);
        if (view == null){
            return null;
        }
        context = getContext();
        unbinder = ButterKnife.bind(this, view);

        factoryIncriminateAzimuth.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.azimuthMin,InputFilterFloat.azimuthMax,InputFilterFloat.angleValidBit)});
        factoryIncriminatePitch.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.pitchMin,InputFilterFloat.pitchMax,InputFilterFloat.angleValidBit)});
        factoryIncriminatePolarization.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.polarizationMin,InputFilterFloat.polarizationMax,InputFilterFloat.angleValidBit)});

        String Azimuth = CustomSP.getString(context,FactoryIncriminateAzimuthKey, "");
        factoryIncriminateAzimuth.setText(Azimuth);
        String Pitch = CustomSP.getString(context,FactoryIncriminatePitchKey, "");
        factoryIncriminatePitch.setText(Pitch);
        String Polarization = CustomSP.getString(context,FactoryIncriminatePolarizationKey, "");
        factoryIncriminatePolarization.setText(Polarization);

        thirdButton = view.findViewById(R.id.pop_dialog_third_button);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                azimuth = factoryIncriminateAzimuth.getText().toString();
                CustomSP.putString(context,FactoryIncriminateAzimuthKey,azimuth);
                pitch = factoryIncriminatePitch.getText().toString();
                CustomSP.putString(context,FactoryIncriminatePitchKey,pitch);
                polarization = factoryIncriminatePolarization.getText().toString();
                CustomSP.putString(context,FactoryIncriminatePolarizationKey,polarization);
                // send command
                // 方位,俯仰，备用，极化
                Protocol.sendMessage(context, String.format(Protocol.cmdSetFactoryCalibAnt,azimuth, pitch, reserve, polarization));
                getActivity().finish();
            }
        });

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);

            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon >= 0) {
                popDialog.setDrawable(ContextCompat.getDrawable(context,icons[0]));
            }
        }
        return popDialog.show();
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(FactoryIncriminateData);
        Object[] o = Sscanf.scan(rawData,Protocol.cmdGetFactoryCalibAntResult,azimuth,pitch,reserve,polarization, pitchOffset);
        azimuth = (String) o[0];
        pitch  = (String) o[1];
        reserve = (String) o[2];
        polarization = (String) o[3];
        pitchOffset = (String) o[4];

        if (azimuth != null && azimuth.length() > 0) {
            factoryIncriminateAzimuth.setText(azimuth);
            CustomSP.putString(context, FactoryIncriminateAzimuthKey, azimuth);
        }
        if (azimuth != null && azimuth.length() > 0) {
            factoryIncriminatePitch.setText(pitch);
            CustomSP.putString(context, FactoryIncriminatePitchKey, pitch);
        }
        if (azimuth != null && azimuth.length() > 0) {
            factoryIncriminatePolarization.setText(polarization);
            CustomSP.putString(context, FactoryIncriminatePolarizationKey, polarization);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
