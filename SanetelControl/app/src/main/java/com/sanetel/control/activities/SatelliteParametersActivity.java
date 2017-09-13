package com.sanetel.control.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.sanetel.control.R;
import com.sanetel.control.adapter.BottomNavigationViewPagerAdapter;
import com.sanetel.control.fragments.BaseFragment;
import com.sanetel.control.fragments.SatelliteParametersLocationSettings;

import com.sanetel.control.activities.CommunicationSettingsActivity;
import com.sanetel.control.activities.MonitorInfoActivity;
import com.sanetel.control.view.BottomNavigationViewHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by bruce on 2016/11/1.
 * SatelliteParametersActivity 主界面
 */

public class SatelliteParametersActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private ImageButton ib;
    private String[] datas;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        datas = getResources().getStringArray(R.array.right_nav_items);

        ib = (ImageButton) findViewById(R.id.mainImageButtonMenu);
        ib.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_satellite:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_location_setting:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_oscillator:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.item_threshold:
                                viewPager.setCurrentItem(3);
                            case R.id.item_more:
                                viewPager.setCurrentItem(4);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                int correctPosition = position % bottomNavigationView.getMenu().size();
                menuItem = bottomNavigationView.getMenu().getItem(correctPosition);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        setupViewPager(viewPager);

        try {
            // 导航action bar
            ActionBar actionBar = getActionBar();
            // 是否显示应用程序图标，默认为true
            actionBar.setDisplayShowHomeEnabled(true);
            // 是否显示应用程序标题，默认为true
            actionBar.setDisplayShowTitleEnabled(true);
           /*
             * 是否将应用程序图标转变成可点击的按钮，默认为false。
             *
             * 如果设置了DisplayHomeAsUpEnabled为true，
             *
             * 则该设置自动为 true。
             */
            actionBar.setHomeButtonEnabled(true);
            /*
             * 在应用程序图标的左边显示一个向左的箭头，
             * 并且将HomeButtonEnabled设为true。
             * 默认为false。
             */
            actionBar.setDisplayHomeAsUpEnabled(true);

            forceShowOverflowMenu();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void setupViewPager(ViewPager viewPager) {
        BottomNavigationViewPagerAdapter adapter = new BottomNavigationViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(BaseFragment.newInstance(getString(R.string.satellite)));
        adapter.addFragment(SatelliteParametersLocationSettings.newInstance(getString(R.string.threshold), getString(R.string.reference)));
        adapter.addFragment(BaseFragment.newInstance(getString(R.string.location_setting)));
        adapter.addFragment(BaseFragment.newInstance(getString(R.string.oscillator)));
        adapter.addFragment(BaseFragment.newInstance(getString(R.string.more)));
        viewPager.setAdapter(adapter);
    }

    public void onClick(View v) {
        // TODO: 2016/5/17 构建一个popupwindow的布局
        View popupView = getLayoutInflater().inflate(R.layout.popupwindow_custom, null);

        // TODO: 2016/5/17 为了演示效果，简单的设置了一些数据，实际中大家自己设置数据即可，相信大家都会。
        ListView lsvMore = (ListView) popupView.findViewById(R.id.lsvMore);
        lsvMore.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas));

        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        PopupWindow window = new PopupWindow(popupView, 400, 600);
        // TODO: 2016/5/17 设置动画
        window.setAnimationStyle(R.style.popup_window_anim);
        // TODO: 2016/5/17 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // TODO: 2016/5/17 设置可以获取焦点
        window.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        window.update();
        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
        window.showAsDropDown(ib, 0, 20);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
              MenuInflater inflater = getMenuInflater();
              inflater.inflate(R.menu.menu_right_navigation, menu);
              return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
             * 将actionBar的HomeButtonEnabled设为ture，
             *
             * 将会执行此case
             */
            case R.id.item_camera:
                Toast.makeText(this, "拍照", Toast.LENGTH_LONG).show();
//                finish();
                Intent intent = new Intent(this, MonitorInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                Toast.makeText(this, "添加", Toast.LENGTH_LONG).show();
                Intent intentActionbar = new Intent(this, CommunicationSettingsActivity.class);
                startActivity(intentActionbar);
                break;
            // 其他省略...
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        setOverflowIconVisible(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 显示OverflowMenu的Icon
     *
     * @param featureId
     * @param menu
     */
    private void setOverflowIconVisible(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.d("OverflowIconVisible", e.getMessage());
                }
            }
        }
    }

    /**
       * 如果设备有物理菜单按键，需要将其屏蔽才能显示OverflowMenu
     */
    private void forceShowOverflowMenu() {
            try {
                 ViewConfiguration config = ViewConfiguration.get(this);
                 Field menuKeyField = ViewConfiguration.class
                         .getDeclaredField("sHasPermanentMenuKey");
                 if (menuKeyField != null) {
                         menuKeyField.setAccessible(true);
                         menuKeyField.setBoolean(config, false);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
      }

}
