package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeErrorReportActivity extends AppCompatActivity implements View.OnClickListener{

//    public static MainMeErrorReportActivity newInstance(String info) {
//        Bundle args = new Bundle();
//        MainMeErrorReportActivity fragment = new MainMeErrorReportActivity();
//        args.putString("info", info);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_me_error_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_me_error_report_toolbar);
        toolbar.setTitle(R.string.main_me_error_report_title);
        findViewById(R.id.main_me_error_report_return).setOnClickListener(this);
        findViewById(R.id.main_me_error_report_save).setOnClickListener(this);
        findViewById(R.id.main_me_error_report_commit).setOnClickListener(this);
        findViewById(R.id.main_me_error_report_photo).setOnClickListener(this);
        findViewById(R.id.main_me_error_report_attach).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.main_me_error_report_return:
                finish();
                break;
            case R.id.main_me_error_report_save:
                // TODO 保存到本地缓存
                break;
            case R.id.main_me_error_report_commit:
                // todo 提交
                break;
            case R.id.main_me_error_report_photo:
                // intent = new Intent(getContext(), Context.AUDIO_SERVICE);
                break;
            case R.id.main_me_error_report_attach:
                // intent = new Intent(getContext(), Context.AUDIO_SERVICE);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
