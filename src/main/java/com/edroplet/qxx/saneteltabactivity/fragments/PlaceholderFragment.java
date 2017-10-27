package com.edroplet.qxx.saneteltabactivity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/12.
 * A placeholder fragment containing a simple view.
 */

public class PlaceholderFragment  extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_MSG = "section_msg";
    private static final String ARG_SECTION_TITLE = "section_toolbar_title";
    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(String sectionMessage, String title) {
        PlaceholderFragment fragment = new PlaceholderFragment();

        Bundle args = new Bundle();
        args.putString(ARG_SECTION_MSG, sectionMessage);
        args.putString(ARG_SECTION_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment_power_ampiler, container, false);
        com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView textView = (com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView) rootView.findViewById(R.id.section_label);
        // getString(R.string.section_format
        textView.setText(getArguments().getString(ARG_SECTION_MSG));
        // textView.setText(msg);
        return rootView;
    }
}
