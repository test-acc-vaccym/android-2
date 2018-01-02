package com.edroplet.qxx.saneteltabactivity.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.Arrays;

/**
 * Created by qxs on 2018/1/2.
 * 广播注册和反注册
 */

public class BroadcastReceiverFragment extends Fragment {
    private String[] action;
    private AllInfoReceiver allInfoReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册广播
        IntentFilter intentFilter1 = new IntentFilter();
        for (String a:action) {
            intentFilter1.addAction(a);
        }
        allInfoReceiver = new AllInfoReceiver();
        getActivity().registerReceiver(allInfoReceiver, intentFilter1);
    }

    class AllInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionName = intent.getAction();
            if (Arrays.asList(action).contains(actionName)) {
                processData(intent);
            }
        }
    }

    public void setAction(String[] action) {
        this.action = action;
    }

    public void processData(Intent intent){
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(allInfoReceiver);
    }
}
