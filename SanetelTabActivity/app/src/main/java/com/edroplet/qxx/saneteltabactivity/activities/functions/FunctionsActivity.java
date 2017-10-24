package com.edroplet.qxx.saneteltabactivity.activities.functions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.control.OperateBarControl;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;
import com.edroplet.qxx.saneteltabactivity.fragments.functions.FunctionsFragmentCollect;
import com.edroplet.qxx.saneteltabactivity.fragments.functions.FunctionsFragmentManual;
import com.edroplet.qxx.saneteltabactivity.fragments.functions.FunctionsFragmentMonitor;
import com.edroplet.qxx.saneteltabactivity.fragments.functions.FunctionsFragmentSettings;
import com.edroplet.qxx.saneteltabactivity.fragments.functions.FunctionsFragmentStatus;
import com.edroplet.qxx.saneteltabactivity.utils.BottomNavigationViewHelper;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomFAB;

public class FunctionsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private CustomFAB cfab;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main_navigation_monitor:
                    // Toast.makeText(getBaseContext(),"选择了 监视", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.main_navigation_application:
                    // Toast.makeText(getBaseContext(),"选择了 应用", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.main_navigation_status:
                    // Toast.makeText(getBaseContext(),"选择了 状态", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(2);
                    return true;

                case R.id.main_navigation_collect:
                    // Toast.makeText(getBaseContext(),"选择了 采集", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(3);
                    return true;

                case R.id.main_navigation_settings:
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
        StatusBarControl.setupToolbar(this, R.id.main_content_toolbar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);

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
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
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
        OperateBarControl.setupOperatorBar(this);
        /*
        final StatusButton sbExploded = (StatusButton)  findViewById(R.id.button_operate_explode);
        final StatusButton sbFold = (StatusButton) findViewById(R.id.button_operate_fold);

        if (sbExploded != null)
            sbExploded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbExploded.onConfirm("你确定要展开吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO 处理确定事件
                            sbExploded.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                            if (sbFold!=null) {
                                sbFold.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                            }
                            sbExploded.getDialogBuilder().dismiss();
                        }
                    });
                    return;
                }
            });
        if (sbFold!=null){
            sbFold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbFold.onConfirm("确认收藏吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 设置为不可点击状态
                            sbFold.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                            if (sbExploded!=null) {
                                sbExploded.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                            }
                            sbFold.getDialogBuilder().dismiss();
                        }
                    });
                }
            });
        }
        final StatusButton sbPause = (StatusButton) findViewById(R.id.button_operate_pause);
        if (sbPause != null)
            sbPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbPause.onConfirm("暂停？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sbPause.getDialogBuilder().dismiss();
                        }
                    });
                }
            });

        final StatusButton sbReset = (StatusButton) findViewById(R.id.button_operate_reset);
        if (sbReset != null)
            sbReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbReset.onConfirm("复位吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sbReset.getDialogBuilder().dismiss();
                        }
                    });
                }
            });

        final StatusButton sbSearch = (StatusButton) findViewById(R.id.button_operate_search);
        if (sbSearch != null)
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbSearch.onConfirm("开始寻星？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sbSearch.getDialogBuilder().dismiss();
                        }
                    });
                }
            });
        */
        cfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FunctionsActivity.this, MonitorHelpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(FunctionsFragmentMonitor.newInstance(null));
        adapter.addFragment(FunctionsFragmentManual.newInstance(getString(R.string.main_application_operate)));
        adapter.addFragment(FunctionsFragmentStatus.newInstance(null));
        adapter.addFragment(FunctionsFragmentCollect.newInstance(null));
        adapter.addFragment(FunctionsFragmentSettings.newInstance(getString(R.string.main_bottom_nav_settings)));
        viewPager.setAdapter(adapter);
    }

}
