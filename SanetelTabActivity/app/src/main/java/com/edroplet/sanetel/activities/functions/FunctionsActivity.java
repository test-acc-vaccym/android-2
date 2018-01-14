package com.edroplet.sanetel.activities.functions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.MainViewPagerAdapter;
import com.edroplet.sanetel.control.OperateBarControl;
import com.edroplet.sanetel.control.StatusBarControl;
import com.edroplet.sanetel.fragments.functions.FunctionsFragmentCollect;
import com.edroplet.sanetel.fragments.functions.FunctionsFragmentManual;
import com.edroplet.sanetel.fragments.functions.FunctionsFragmentMonitor;
import com.edroplet.sanetel.fragments.functions.FunctionsFragmentSettings;
import com.edroplet.sanetel.fragments.functions.FunctionsFragmentStatus;
import com.edroplet.sanetel.utils.BottomNavigationViewHelper;
import com.edroplet.sanetel.view.custom.CustomFAB;
import com.edroplet.sanetel.view.custom.WeChatRadioGroup;

public class FunctionsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private WeChatRadioGroup bottomNavigationView;
    private CustomFAB cfab;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.functions_navigation_monitor:
                    // Toast.makeText(getBaseContext(),"选择了 监视", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.functions_navigation_application:
                    // Toast.makeText(getBaseContext(),"选择了 应用", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.functions_navigation_status:
                    // Toast.makeText(getBaseContext(),"选择了 状态", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(2);
                    return true;

                case R.id.functions_navigation_collect:
                    // Toast.makeText(getBaseContext(),"选择了 采集", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(3);
                    return true;

                case R.id.functions_navigation_settings:
                    // Toast.makeText(getBaseContext(),"选择了 设置", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(4);
                    return true;
                case android.R.id.home:
                    finish();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions);
        cfab = (CustomFAB) findViewById(R.id.activity_functions_fab);
        StatusBarControl.setupToolbar(this, R.id.functions_content_toolbar);

        bottomNavigationView = (WeChatRadioGroup) findViewById(R.id.functions_navigation);
        // BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        // bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = (ViewPager) findViewById(R.id.functions_viewpager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0){
                    cfab.setVisibility(View.VISIBLE);
                }else {
                    cfab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                /*
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
                */
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        setupViewPager(viewPager);

        cfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FunctionsActivity.this, MonitorHelpActivity.class);
                startActivity(intent);
            }
        });
        bottomNavigationView.setViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OperateBarControl.setupOperatorBar(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(FunctionsFragmentMonitor.newInstance(null));
        adapter.addFragment(FunctionsFragmentManual.newInstance(getString(R.string.main_control_operate)));
        adapter.addFragment(FunctionsFragmentStatus.newInstance(null));
        adapter.addFragment(FunctionsFragmentCollect.newInstance(null));
        adapter.addFragment(FunctionsFragmentSettings.newInstance(getString(R.string.main_bottom_nav_settings)));
        viewPager.setAdapter(adapter);
    }

}
