package com.edroplet.qxx.saneteltabactivity.fragments.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.guide.FollowMeActivity;
import com.edroplet.qxx.saneteltabactivity.activities.guide.GuideEntryActivity;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialoger;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainFragmentGuide extends Fragment {
    public static String device = "P120-3-";
    public static MainFragmentGuide newInstance(String info) {
        Bundle args = new Bundle();
        MainFragmentGuide fragment = new MainFragmentGuide();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_guide, null);

        Context context = getContext();
        final Activity activity = getActivity();
        String ssid = SystemServices.getConnectWifiSsid(context);
        Toast.makeText(context,ssid, Toast.LENGTH_SHORT).show();
        if (!ssid.contains(device)){
            RandomDialoger.onConfirm(context, "没有连接设备\n请连接设备" + device, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SystemServices.startWifiManager(activity);
                    RandomDialoger.dialogBuilder.dismiss();
                }
            });
        }

        view.findViewById(R.id.main_guide_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GuideEntryActivity.class));
                // getActivity().finish();
            }
        });
        return view;
    }
}
