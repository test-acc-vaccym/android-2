package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
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
import com.edroplet.qxx.saneteltabactivity.view.IPEdit;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

/**
 * Created by qxs on 2017/9/19.
 */

public class AdministratorFragmentIPSettings extends Fragment {
    private static final String IPSettingsAddressKey = "ipAddress";
    private static final String IPSettingsMaskKey = "ipMask";

    private static final int[] icons = {R.drawable.antenna_exploded};

    private CustomButton thirdButton;

    private IPEdit ipAddress;
    private IPEdit ipMask;

    public static AdministratorFragmentIPSettings newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                              String secondLine, boolean showThird, String thirdLineStart,
                                                              int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentIPSettings fragment = new AdministratorFragmentIPSettings();
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
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_ip_settings, null);
        if (view == null){
            return null;
        }

        final Context context = getContext();

        ViewInject.inject(getActivity(), getActivity());
        ipAddress = view.findViewById(R.id.administrator_setting_ip_address);
        String address = CustomSP.getString(context,IPSettingsAddressKey, "");
        ipAddress.setText(address);

        ipMask = view.findViewById(R.id.administrator_setting_ip_mask);
        String mask = CustomSP.getString(context,IPSettingsMaskKey, "");
        ipMask.setText(mask);

        thirdButton = view.findViewById(R.id.pop_dialog_third_button);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomSP.putString(context,IPSettingsAddressKey,ipAddress.getText());
                CustomSP.putString(context,IPSettingsMaskKey,ipMask.getText());
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
        }
        return popDialog.show();
    }
}
