package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.ManualActivity;

/**
 * Created by qxs on 2017/9/14.
 */

public class FunctionsFragmentManual extends Fragment {

    public static FunctionsFragmentManual newInstance(String info) {
        Bundle args = new Bundle();
        FunctionsFragmentManual fragment = new FunctionsFragmentManual();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_functions_control, null);
        view.findViewById(R.id.main_control_manual_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListener(ManualActivity.stepIndex);
            }
        });

        view.findViewById(R.id.main_control_speed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListener(ManualActivity.speedIndex);
            }
        });

        view.findViewById(R.id.main_control_manual_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListener(ManualActivity.locationIndex);
            }
        });

        view.findViewById(R.id.main_control_manual_calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListener(ManualActivity.calculateIndex);
            }
        });

        return view;
    }
    private void setOnClickListener(int position){
        Intent intent = new Intent(getActivity(), ManualActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ManualActivity.POSITION, position);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
