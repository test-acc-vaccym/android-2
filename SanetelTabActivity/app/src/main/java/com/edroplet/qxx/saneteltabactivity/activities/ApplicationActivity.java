package com.edroplet.qxx.saneteltabactivity.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.MainViewPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.fragments.MainFragmentBase;
import com.edroplet.qxx.saneteltabactivity.utils.BottomNavigationViewHelper;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;

public class ApplicationActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main_navigation_monitor:
                    Toast.makeText(getBaseContext(),"选择了 监视", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.main_navigation_application:
                    Toast.makeText(getBaseContext(),"选择了 应用", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.main_navigation_status:
                    Toast.makeText(getBaseContext(),"选择了 状态", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.main_navigation_collect:
                    Toast.makeText(getBaseContext(),"选择了 采集", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.main_navigation_settings:
                    Toast.makeText(getBaseContext(),"选择了 设置", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_application);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_content_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.main_application_operate);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = (ViewPager) findViewById(R.id.main_viewpager);

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
    }

    private void setupViewPager(ViewPager viewPager) {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(MainFragmentBase.newInstance(getString(R.string.main_bottom_nav_monitor)));
        adapter.addFragment(MainFragmentBase.newInstance(getString(R.string.main_bottom_nav_application)));
        adapter.addFragment(MainFragmentBase.newInstance(getString(R.string.main_bottom_nav_status)));
        adapter.addFragment(MainFragmentBase.newInstance(getString(R.string.main_bottom_nav_collect)));
        adapter.addFragment(MainFragmentBase.newInstance(getString(R.string.main_bottom_nav_settings)));
        viewPager.setAdapter(adapter);
    }

}
