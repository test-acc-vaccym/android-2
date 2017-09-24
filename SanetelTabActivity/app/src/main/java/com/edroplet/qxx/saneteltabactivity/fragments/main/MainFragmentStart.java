package com.edroplet.qxx.saneteltabactivity.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsActivity;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainFragmentStart extends Fragment {
    public static MainFragmentStart newInstance(String info) {
        Bundle args = new Bundle();
        MainFragmentStart fragment = new MainFragmentStart();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_start, null);
        view.findViewById(R.id.main_start_launch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FunctionsActivity.class));
                // getActivity().finish();
            }
        });
        return view;
    }
}
