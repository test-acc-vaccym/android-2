package com.edroplet.qxx.saneteltabactivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.text.format.Time;

import com.edroplet.qxx.saneteltabactivity.activities.main.MainActivity;
import com.edroplet.qxx.saneteltabactivity.fragments.welcome.WelcomeCheckDevice;
import com.edroplet.qxx.saneteltabactivity.fragments.welcome.WelcomeFixDevice;
import com.edroplet.qxx.saneteltabactivity.fragments.welcome.WelcomeInstallWire;
import com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntro;

import java.util.Date;
import java.util.Random;

/**
 * Created by qxs on 2017/9/21.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        // addSlide(AppIntroFragment.newInstance("", "", R.mipmap.welcome, Color.TRANSPARENT));
        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        // 检查设备
        addSlide(WelcomeCheckDevice.newInstance());
        // 固定设备
        addSlide(WelcomeFixDevice.newInstance());
        // 安装线缆
        addSlide(WelcomeInstallWire.newInstance());


        // OPTIONAL METHODS
        // Override bar/separator color.
        // setBarColor(Color.parseColor("#3F51B5"));
        // setSeparatorColor(Color.parseColor("#2196F3"));
        setBarColor(ContextCompat.getColor(getBaseContext(), R.color.bottom_tab_nav_background));
        setSeparatorColor(ContextCompat.getColor(getBaseContext(), R.color.favorite_contacts_separator_color));

        // Hide Skip/Done button.
        showSkipButton(true);
        setSkipText(getText(R.string.guide_done));
        setColorSkipButton(ContextCompat.getColor(this,R.color.button_text));
        setProgressButtonEnabled(true);
        // setProgressIndicator();
        setDoneText(getText(R.string.guide_done));
        // setDoneTextTypeface("msyhbd.ttc");
        setColorDoneText(ContextCompat.getColor(getBaseContext(),R.color.button_text));
        setBackButtonVisibilityWithDone(true);
        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);

        setFlowAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        // 直接跳转到引导主页
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
        Random random = new Random(System.currentTimeMillis());
        // comes with some pager animations
        /*
        switch(random.nextInt(5)) {
            case 0:
                setSlideOverAnimation();
                break;
            case 1:
                setDepthAnimation();
                break;
            case 2:
                setFlowAnimation();
                break;
            case 3:
                setZoomAnimation();
                break;
            case 4:
                setFadeAnimation();
                break;
        }
        */
    }
}
