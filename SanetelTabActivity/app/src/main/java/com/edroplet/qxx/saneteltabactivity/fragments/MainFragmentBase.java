package com.edroplet.qxx.saneteltabactivity.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/14.
 */

public class MainFragmentBase extends Fragment {
    public static MainFragmentBase newInstance(String info) {
        Bundle args = new Bundle();
        MainFragmentBase fragment = new MainFragmentBase();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_base, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.message);
        tvInfo.setText(getArguments().getString("info"));
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
