package com.edroplet.qxx.saneteltabactivity.fragments.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/21.
 */

public class WelcomeFixDevice extends Fragment {
    public static WelcomeFixDevice newInstance() {
        Bundle args = new Bundle();
        WelcomeFixDevice fragment = new WelcomeFixDevice();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_fix_device, null);
        return view;
    }
}
