package com.edroplet.qxx.saneteltabactivity.fragments.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsActivity;
import com.edroplet.qxx.saneteltabactivity.activities.guide.FollowMeActivity;
import com.edroplet.qxx.saneteltabactivity.activities.guide.GuideEntryActivity;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainFragmentGuide extends Fragment implements View.OnClickListener{
    public static String device = "P120-3";
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
        /*
        view.findViewById(R.id.main_guide_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GuideEntryActivity.class));
            }
        });
        */
        setToolbar(view);
        view.findViewById(R.id.guide_main_button_explode).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_location).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_destination).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_search_mode).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_search).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_lock).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_saving).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), FollowMeActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.guide_main_button_explode:
                bundle.putInt(FollowMeActivity.POSITION, 0 );
                break;
            case R.id.guide_main_button_location:
                bundle.putInt(FollowMeActivity.POSITION, 1 );
                break;
            case R.id.guide_main_button_destination:
                bundle.putInt(FollowMeActivity.POSITION, 2 );
                break;
            case R.id.guide_main_button_search_mode:
                bundle.putInt(FollowMeActivity.POSITION, 3 );
                break;
            case R.id.guide_main_button_search:
                bundle.putInt(FollowMeActivity.POSITION, 4 );
                break;
            case R.id.guide_main_button_lock:
                bundle.putInt(FollowMeActivity.POSITION, 5 );
                break;
            case R.id.guide_main_button_saving:
                bundle.putInt(FollowMeActivity.POSITION, 6);
                break;
            default:
                bundle.putInt(FollowMeActivity.POSITION, 0 );
                break;
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public void setToolbar(View view){
        Toolbar toolbar = view.findViewById(R.id.guide_tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.guide_enter_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FunctionsActivity.class));
            }
        });
    }

}
