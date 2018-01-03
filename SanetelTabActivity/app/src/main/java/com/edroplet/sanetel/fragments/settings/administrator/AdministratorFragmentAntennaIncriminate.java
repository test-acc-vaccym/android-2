package com.edroplet.sanetel.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.edroplet.sanetel.view.ViewInject;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 天线标定
 */

public class AdministratorFragmentAntennaIncriminate extends BroadcastReceiverFragment {
    private static final String AntennaIncriminateAzimuthKey = "antennaIncriminateAzimuth";
    private static final String AntennaIncriminatePitchKey = "antennaIncriminatePitch";
    private static final String AntennaIncriminatePolarizationKey = "antennaIncriminatePolarization";
    public static final String AntennaIncriminateAction = "com.edroplet.sanetel.AntennaIncriminateAction";
    public static final String AntennaIncriminateData = "com.edroplet.sanetel.AntennaIncriminateData";
    private static final int[] icons = {R.drawable.antenna_exploded};

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.administrator_antenna_incriminate_tv_azimuth)
    CustomEditText antennaIncriminateAzimuth;
    @BindView(R.id.administrator_antenna_incriminate_tv_pitch)
    CustomEditText antennaIncriminatePitch;
    @BindView(R.id.administrator_antenna_incriminate_polarization)
    CustomEditText antennaIncriminatePolarization;

    Unbinder unbinder;

    String azimuth="", pitch="", reserve="", polarization="", pitchOffset="";
    Context context;

    public static AdministratorFragmentAntennaIncriminate newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                      String secondLine, boolean showThird, String thirdLineStart,
                                                                      int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentAntennaIncriminate fragment = new AdministratorFragmentAntennaIncriminate();
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
        String[] action = {AntennaIncriminateAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        Protocol.sendMessage(context, Protocol.cmdGetCalibAnt);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_incriminate, null);
        if (view == null){
            return null;
        }
        context = getContext();
        unbinder = ButterKnife.bind(this, view);

        antennaIncriminateAzimuth.setFilters(new InputFilter[]{new InputFilterFloat(0.0, 360.0)});
        antennaIncriminatePitch.setFilters(new InputFilter[]{new InputFilterFloat(-10.0, 90.0)});
        antennaIncriminatePolarization.setFilters(new InputFilter[]{new InputFilterFloat(0.0, 360.0)});

        String Azimuth = CustomSP.getString(context,AntennaIncriminateAzimuthKey, "");
        antennaIncriminateAzimuth.setText(Azimuth);
        String Pitch = CustomSP.getString(context,AntennaIncriminatePitchKey, "");
        antennaIncriminatePitch.setText(Pitch);
        String Polarization = CustomSP.getString(context,AntennaIncriminatePolarizationKey, "");
        antennaIncriminatePolarization.setText(Polarization);

        thirdButton = view.findViewById(R.id.pop_dialog_third_button);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                azimuth = antennaIncriminateAzimuth.getText().toString();
                CustomSP.putString(context,AntennaIncriminateAzimuthKey,azimuth);
                pitch = antennaIncriminatePitch.getText().toString();
                CustomSP.putString(context,AntennaIncriminatePitchKey,pitch);
                polarization = antennaIncriminatePolarization.getText().toString();
                CustomSP.putString(context,AntennaIncriminatePolarizationKey,polarization);
                // send command
                // 方位,俯仰，备用，极化
                Protocol.sendMessage(context, String.format(Protocol.cmdSetCalibAnt,azimuth, pitch, reserve, polarization));
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
        String rawData = intent.getStringExtra(AntennaIncriminateData);
        Object[] o = Sscanf.scan(rawData,Protocol.cmdGetCalibAntResult,azimuth,pitch,reserve,polarization, pitchOffset);
        azimuth = (String) o[0];
        pitch  = (String) o[1];
        reserve = (String) o[2];
        polarization = (String) o[3];
        pitchOffset = (String) o[4];

        if (azimuth != null && azimuth.length() > 0) {
            antennaIncriminateAzimuth.setText(azimuth);
            CustomSP.putString(context, AntennaIncriminateAzimuthKey, azimuth);
        }
        if (azimuth != null && azimuth.length() > 0) {
            antennaIncriminatePitch.setText(pitch);
            CustomSP.putString(context, AntennaIncriminatePitchKey, pitch);
        }
        if (azimuth != null && azimuth.length() > 0) {
            antennaIncriminatePolarization.setText(polarization);
            CustomSP.putString(context, AntennaIncriminatePolarizationKey, polarization);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
