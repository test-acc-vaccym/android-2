package com.edroplet.sanetel;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.edroplet.sanetel.utils.ChangeTypeFace;
import com.edroplet.sanetel.view.CircleProgressbar;


/**
 * Created by qxs on 2017/8/15.
 * flash 页面， 展示图片
 */

public class SplashActivity extends Activity {
    private com.edroplet.sanetel.view.custom.CustomTextView tv_splash_version;
    private CircleProgressbar mCircleProgressbar;
    private boolean isClick = false;

    private static final String TAG ="sanetel_SplashActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        tv_splash_version = (com.edroplet.sanetel.view.custom.CustomTextView) findViewById(R.id.tv_splash_version);
        //设置版本号
        String input = getResources().getString(R.string.welcome_version);
        String output = String.format(input, String.format("%6s", getVersion()));
        tv_splash_version.setText(output);
        tv_splash_version.setTypeface(ChangeTypeFace.getSimHei(this));
        // 进度条
        /**
        mCircleProgressbar = (CircleProgressbar) findViewById(R.id.tv_red_skip);
        mCircleProgressbar.setOutLineColor(Color.TRANSPARENT);
        mCircleProgressbar.setInCircleColor(Color.parseColor("#505559"));
        mCircleProgressbar.setProgressColor(Color.parseColor("#1BB079"));
        mCircleProgressbar.setProgressLineWidth(5);
        mCircleProgressbar.setProgressType(CircleProgressbar.ProgressType.COUNT);
        mCircleProgressbar.setTimeMillis(getResources().getInteger(R.integer.splash_time)); // 设置2秒倒计时
        mCircleProgressbar.reStart();

        mCircleProgressbar.setCountdownProgressListener(1,progressListener);

        mCircleProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isClick = true;
//                Intent intent = new Intent(SplashActivity.this,WelcomeActivity.class);
                Intent intent = new Intent(SplashActivity.this,IntroActivity.class);
                startActivity(intent);
                finish();

            }
        });
        */

        //延迟2S跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);


    }


    /**
     * 返回应用程序的版本号
     *
     * @return
     */
    private String getVersion(){
         //用来管理手机的APK(包管理器)
         PackageManager pm = getPackageManager();
        try {
            //得到指定APK的功能清单文件
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
     }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isClick = true;

    }


    private CircleProgressbar.OnCountdownProgressListener progressListener = new CircleProgressbar.OnCountdownProgressListener() {
        @Override
        public void onProgress(int what, int progress)
        {

            if(what==1 && progress==100 && !isClick)
            {
                // Intent intent = new Intent(SplashActivity.this,WelcomeActivity.class);
                Intent intent = new Intent(SplashActivity.this,IntroActivity.class);
                startActivity(intent);
                finish();
                Log.e(TAG, "onProgress: =="+progress );
            }

        }
    };


}
