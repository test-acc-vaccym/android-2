package com.edroplet.qxx.saneteltabactivity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentExplode;

public class TestFragmentActivity extends FragmentActivity {

    // 声明控件对象
    public FragmentTabHost mTabHost;
    // 布局填充器
    private LayoutInflater inflater;
    // 存放文本的数组
    private int tabHostTextArray[] = { R.string.tabhost_index,
            R.string.tabhost_category, R.string.tabhost_car,
            R.string.tabhost_my_jd, R.string.tabhost_more };
    // 存放图标的数组
    private int tabHostIconArray[] = { R.drawable.ic_wifi_open_0,
            R.drawable.ic_wifi_open_1, R.drawable.ic_wifi_open_2,
            R.drawable.ic_wifi_open_3, R.drawable.ic_wifi_open_4 };
    // 声明片段对应的数组
    private Class fragments[] = { GuideFragmentExplode.class, GuideFragmentExplode.class,
            GuideFragmentExplode.class, GuideFragmentExplode.class, GuideFragmentExplode.class };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        // 初始化布局填充器对象
        inflater = LayoutInflater.from(this);
        // 查找tabHost对象
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        // 启动tabHost
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 遍历片段
        for (int i = 0; i < fragments.length; i++) {
            // 创建一个TabSpec
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(
                    getResources().getString(tabHostTextArray[i]))
                    .setIndicator(getTabItemView(i));

            // 加入到TabHost中
            mTabHost.addTab(tabSpec, fragments[i], null);
        }

    }

    public View getTabItemView(int i) {
        View view = inflater.inflate(R.layout.tab_nav_item, null);
        ImageView tab_nav_img = (ImageView) view.findViewById(R.id.tab_nav_img);
        TextView tab_nav_text = (TextView) view.findViewById(R.id.tab_nav_text);
        // 设置图片及文本
        tab_nav_img.setImageResource(tabHostIconArray[i]);
        // Toast.makeText(this, tabHostIconArray[i], 1).show();
        tab_nav_text.setText(getResources().getString(tabHostTextArray[i]));

        return view;
    }

    /**
     * fragment
     */
    private void initview() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        GuideFragmentExplode moreFragment = GuideFragmentExplode.newInstance(false, null, true,"天线状态为收藏！", true, "请点击", 1, "展开");
        GuideFragmentExplode myJDIndexFragment = GuideFragmentExplode.newInstance(false, null, true,"展开中……！", false, "请点击", 1, "展开");

        // ft.add(R.id.realtabcontent, accountManagerFragment);
        // ft.add(R.id.realtabcontent, myJDFragment);
        GuideFragmentExplode settingFragment = GuideFragmentExplode.newInstance(true, "天线状态已为展开！", true,"天线面辅瓣放置在软包中," +
                "每个天线辅面配有图标；用户通过主瓣上标注的安装说明，可以轻而易举的完成天线面安装。", true, "安装完成之后，", -1, "可以点击下一步。");
        /*
         * ft.add(R.id.realtabcontent, accountManagerFragment);
         * ft.add(R.id.realtabcontent, myJDFragment);
         * ft.add(R.id.realtabcontent,myJDIndexFragment);
         * ft.add(R.id.realtabcontent,)
         */
        ft.commit();
    }

    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("userloginflag", 0);
        if (id == 1 ) {
            mTabHost.setCurrentTab(3);
        }
        super.onResume();
    }

}
