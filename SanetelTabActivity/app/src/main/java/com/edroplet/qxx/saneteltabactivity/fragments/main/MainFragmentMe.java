package com.edroplet.qxx.saneteltabactivity.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.main.MainMeLanguageActivity;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainFragmentMe extends Fragment implements View.OnClickListener{
    public static MainFragmentMe newInstance(String info) {
        Bundle args = new Bundle();
        MainFragmentMe fragment = new MainFragmentMe();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_me, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_me_toolbar);
        toolbar.setTitle(R.string.main_bottom_nav_me);
        CustomButton language = (CustomButton) view.findViewById(R.id.main_bottom_nav_me_language);
        language.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.main_bottom_nav_me_language:
                intent = new Intent(getActivity(), MainMeLanguageActivity.class);
                break;
            case R.id.main_bottom_nav_me_version:
            break;
            case R.id.main_bottom_nav_me_error_report:
            break;
            case R.id.main_bottom_nav_me_advices:
            break;
            case R.id.main_bottom_nav_me_switch_device:
            break;
            default:
            case R.id.main_bottom_nav_me_about:
                break;
        }
        if (intent != null) {
            startActivity(intent);
            // getActivity().finish();
        }
    }
}
