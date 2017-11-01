package com.edroplet.qxx.saneteltabactivity.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;

/**
 * Created by qxs on 2017/9/19.
 */

public class SettingsFragmentAmplifiereEmit extends Fragment {
    public static SettingsFragmentAmplifiereEmit newInstance(String info) {
        Bundle args = new Bundle();
        SettingsFragmentAmplifiereEmit fragment = new SettingsFragmentAmplifiereEmit();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings_fragment_amplifier_emit, null);

        // 设置自定义框内容
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        Context context = getContext();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOWTHIRD, true);
        bundle.putString(PopDialog.START, context.getString(R.string.settings_amplifier_manufacture_message_third_start));
        bundle.putString(PopDialog.END, context.getString(R.string.follow_me_forever));
        popDialog.setBundle(bundle);
        popDialog.setButtonText(context, context.getString(R.string.setting_button_text));
        popDialog.show();

        return view;
    }
}
