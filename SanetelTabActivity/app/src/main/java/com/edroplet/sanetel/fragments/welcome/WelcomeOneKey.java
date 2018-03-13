package com.edroplet.sanetel.fragments.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;

/**
 * Created by qxs on 2017/9/21.
 * 一键通功能
 */

public class WelcomeOneKey extends Fragment {
    public static WelcomeOneKey newInstance() {
        Bundle args = new Bundle();
        WelcomeOneKey fragment = new WelcomeOneKey();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_one_key, null);
        return view;
    }
}
