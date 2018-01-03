package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

/**
 * Created by qxs on 2017/9/19.
 */

public class AdministratorFragmentRecoveryFactory extends Fragment {

    private  final int[] icons = {R.drawable.antenna_exploded };

    private CustomButton thirdButton;

    public static AdministratorFragmentRecoveryFactory newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                   String secondLine, boolean showThird, String thirdLineStart,
                                                                   int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentRecoveryFactory fragment = new AdministratorFragmentRecoveryFactory();
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
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_recovery_factory, null);
        if (view == null){
            return null;
        }
        ViewInject.inject(getActivity(), getContext());
        thirdButton = view.findViewById(R.id.pop_dialog_third_button);
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // send command
                // 5.1	恢复出厂 重新初始化Flash信息
                // 指令格式：$cmd,reset flash info*ff<CR><LF>
                Protocol.sendMessage(getContext(),String.format(Protocol.cmdResetFlashInfo));
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
}
