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
    private com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton btnSpeed;
    private View.OnClickListener speedClick;
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
        final View view = inflater.inflate(R.layout.fragment_functions_application, null);
        view.findViewById(R.id.main_control_speed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListener(0);
            }
        });

        view.findViewById(R.id.main_control_manual_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListener(1);
            }
        });

        view.findViewById(R.id.main_control_manual_calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickListener(2);
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
