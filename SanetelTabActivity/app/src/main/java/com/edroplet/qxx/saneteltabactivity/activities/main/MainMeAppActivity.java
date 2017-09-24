package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeAppActivity extends AppCompatActivity implements View.OnClickListener{
//
//    public static MainMeAdviceActivity newInstance(String info) {
//        Bundle args = new Bundle();
//        MainMeAdviceActivity fragment = new MainMeAdviceActivity();
//        args.putString("info", info);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_me_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_me_app_toolbar);
        toolbar.setTitle(R.string.main_me_app_title);
        findViewById(R.id.main_me_app_update).setOnClickListener(this);
        findViewById(R.id.main_me_app_recovery).setOnClickListener(this);
        findViewById(R.id.main_me_app_browse).setOnClickListener(this);
        findViewById(R.id.main_me_app_download).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.main_me_advice_return:
                finish();
                break;
            case R.id.main_me_advice_save:
                // TODO 保存到本地缓存
                break;
            case R.id.main_me_advice_commit:
                // todo 提交
                break;
            case R.id.main_me_advice_photo:
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
