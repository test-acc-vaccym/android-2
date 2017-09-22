package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentExplode extends Fragment {
    public static GuideFragmentExplode newInstance(String info) {
        Bundle args = new Bundle();
        GuideFragmentExplode fragment = new GuideFragmentExplode();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_me_explode, null);
        return view;
    }
}
