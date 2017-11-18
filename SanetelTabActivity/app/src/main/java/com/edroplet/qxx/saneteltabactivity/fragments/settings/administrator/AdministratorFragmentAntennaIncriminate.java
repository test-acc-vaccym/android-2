package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.InputFilterFloat;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;

import butterknife.BindView;

/**
 * Created by qxs on 2017/9/19.
 */

public class AdministratorFragmentAntennaIncriminate extends Fragment {
    private static final String AntennaIncriminateAzimuthKey = "antennaIncriminateAzimuth";
    private static final String AntennaIncriminatePitchKey = "antennaIncriminatePitch";
    private static final String AntennaIncriminatePolarizationKey = "antennaIncriminatePolarization";
    private static final int[] icons = {R.drawable.antenna_exploded};

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    private CustomEditText antennaIncriminateAzimuth;
    private CustomEditText antennaIncriminatePitch;
    private CustomEditText antennaIncriminatePolarization;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_incriminate, null);
        if (view == null){
            return null;
        }

        final Context context = getContext();

        ViewInject.inject(getActivity(), getActivity());
        antennaIncriminateAzimuth = view.findViewById(R.id.administrator_antenna_incriminate_tv_azimuth);
        antennaIncriminatePitch = view.findViewById(R.id.administrator_antenna_incriminate_tv_pitch);
        antennaIncriminatePolarization = view.findViewById(R.id.administrator_antenna_incriminate_polarization);

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
                CustomSP.putString(context,AntennaIncriminateAzimuthKey,antennaIncriminateAzimuth.getText().toString());
                CustomSP.putString(context,AntennaIncriminatePitchKey,antennaIncriminatePitch.getText().toString());
                CustomSP.putString(context,AntennaIncriminatePolarizationKey,antennaIncriminatePolarization.getText().toString());
                // todo send command
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
//            if (bundle.getString(PopDialog.BUTTON_TEXT).length() > 0){
//                popDialog.setButtonText(context, context.getString(R.string.setting_button_text));
//            }
        }
        return popDialog.show();
    }
}
