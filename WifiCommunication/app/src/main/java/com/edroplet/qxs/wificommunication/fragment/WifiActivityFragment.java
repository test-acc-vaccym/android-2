package com.edroplet.qxs.wificommunication.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxs.wificommunication.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class WifiActivityFragment extends Fragment {

    public WifiActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }
}
