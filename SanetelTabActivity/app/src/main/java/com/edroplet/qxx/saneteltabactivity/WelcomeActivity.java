package com.edroplet.qxx.saneteltabactivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.edroplet.qxx.saneteltabactivity.activities.GuideEntryActivity;
import com.edroplet.qxx.saneteltabactivity.activities.ManualActivity;
import com.edroplet.qxx.saneteltabactivity.adapters.WelcomePageAdapter;
import com.edroplet.qxx.saneteltabactivity.utils.CustomLRU;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends AppCompatActivity  implements OnPageChangeListener {

    private static final int SLEEP_time=5000;//欢迎界面的显示时间
    private static final int GO_home=100;
    private static final int GO_Viewpager=101;
    private boolean isFirstIn=false;  //记录是否是第一次进入APP

    private ViewPager vp;
    private WelcomePageAdapter vpAdapter;
    private List<View> views;
    private ViewPager viewPager;
    private Button btnstarthome;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GO_home://进入主界面
                    Intent intenthome=new Intent(WelcomeActivity.this,ManualActivity.class);
                    startActivity(intenthome);
                    WelcomeActivity.this.finish();
                    break;
                case GO_Viewpager://进入导航页面
                    Intent intentviewpager=new Intent(WelcomeActivity.this,GuideEntryActivity.class);
                    startActivity(intentviewpager);
                    WelcomeActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //用SharedPreferences保存是否第一次进入App的参数
        isFirstIn = CustomSP.getBoolean(this, "isFirstIn", true);
        if (isFirstIn){
            handler.sendEmptyMessageDelayed(GO_Viewpager,SLEEP_time);
            CustomSP.putBoolean(this, "isFirstIn", false);
        }else {
            handler.sendEmptyMessageDelayed(GO_home,SLEEP_time);
        }
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

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout guideFour = (RelativeLayout) inflater.inflate(R.layout.welcome_gallery4, null);
        guideFour.findViewById(R.id.toMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,SanetelTabActivity.class);
                startActivity(intent);
                finish();
            }
        });
        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.welcome_gallery1, null));
        views.add(inflater.inflate(R.layout.welcome_gallery2, null));
        views.add(inflater.inflate(R.layout.welcome_gallery3, null));
        views.add(guideFour);
        // 初始化Adapter
        vpAdapter = new WelcomePageAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.welcome_viewpager);
        vp.setAdapter(vpAdapter);

        btnstarthome = (Button) views.get(3).findViewById(R.id.toMain);
        btnstarthome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到主界面并杀死导航页面
                Intent intent=new Intent(WelcomeActivity.this,ManualActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });

        // 监听ViewPager
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
