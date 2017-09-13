package com.sanetel.control;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sanetel.control.activities.LoginActivity;
import com.sanetel.control.adapter.WelcomePageAdapter;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivitiy extends AppCompatActivity  implements OnPageChangeListener {


    private ViewPager vp;
    private WelcomePageAdapter vpAdapter;
    private List<View> views;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;
    Boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去掉标题栏全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activitiy);


        // 初始化页面
        initViews();

        // 初始化底部小点
        initDots();

    }


    private void initViews() {

        SharedPreferences pref = getSharedPreferences("first",Activity.MODE_PRIVATE);
        isFirst = pref.getBoolean("status",true);

        if(!isFirst){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout guideFour = (RelativeLayout) inflater.inflate(R.layout.whats_news_gallery4, null);
        guideFour.findViewById(R.id.toMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivitiy.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.whats_news_gallery1, null));
        views.add(inflater.inflate(R.layout.whats_news_gallery2, null));
        views.add(inflater.inflate(R.layout.whats_news_gallery3, null));
        views.add(guideFour);
        // 初始化Adapter
        vpAdapter = new WelcomePageAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.whatsnew_viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);


    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurrentDot(arg0);
    }


}
