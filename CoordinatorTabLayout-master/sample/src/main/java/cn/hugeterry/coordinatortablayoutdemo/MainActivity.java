package cn.hugeterry.coordinatortablayoutdemo;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.hugeterry.coordinatortablayout.BaseActivity;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 17/1/28 17:32
 */
public class MainActivity extends BaseActivity{
    private CoordinatorTabLayout mCoordinatorTabLayout;
    private int[] mImageArray, mColorArray;
    private ArrayList<Fragment> mFragments;
    private final int[] mTitles = {R.string.tab1, R.string.tab2, R.string.tab3, R.string.tab4,R.string.tab5,R.string.tab6};;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments();
        initViewPager();
        mImageArray = new int[]{
                R.mipmap.bg_android,
                R.mipmap.bg_ios,
                R.mipmap.bg_js,
                R.mipmap.bg_other,
                R.mipmap.bg_android,
                R.mipmap.bg_ios};
        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark};

        mCoordinatorTabLayout = (CoordinatorTabLayout) findViewById(R.id.coordinatortablayout);
        mCoordinatorTabLayout.setTransulcentStatusBar(this)
                .setTitle("示例")
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager)
                .setTabsView(mTitles,mImageArray);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "this is main Activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        for (int title : mTitles) {
            mFragments.add(MainFragment.getInstance(getString(title)));
        }
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        String [] titles=new String[mTitles.length];
        int i = 0;
        for (int title : mTitles){
            titles[i++] = getString(title);
        }
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, titles));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        switch (item.getItemId()) {
            case R.id.action_about:
                IntentUtils.openUrl(this, "https://github.com/hugeterry/CoordinatorTabLayout");
                break;
            case R.id.action_about_me:
                IntentUtils.openUrl(this, "http://hugeterry.cn/about");
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id  = item.getItemId();
        switch (id) {
            case R.id.action_about:
                break;
            case R.id.action_about_me:
                break;
            default:
                //实现相应菜单项的功能
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }

        return super.onOptionsItemSelected(item);
    }

}
