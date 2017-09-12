package com.edroplet.qxx.saneteltabactivity;

import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.adapters.SectionsPagerAdapter;
import com.edroplet.qxx.saneteltabactivity.utils.hashMapUtils;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SanetelTabActivity extends AppCompatActivity {
    public static Toolbar toolbar;
    private static LinkedHashMap<String, String> map = new LinkedHashMap<>();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String snackHelpMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanetel_tab);
        setupToolbar();
        initView();
//        setupFab();

    }

    public SanetelTabActivity setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.content_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setHomeAsUpIndicator(R.drawable.ic_back_arrow);

        // 隐藏ActionBar的标题
        // ab.setTitle(null);
        ab.setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.title_background,null));
        // 一个布局内的所有控件可以获取到焦点
        for (int i = 0; i< toolbar.getChildCount(); i++){
            View v = toolbar.getChildAt(i);
            v.setFocusable(true);
        }
        // 使用drawable资源但不为其设置theme主题
        // ab.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.status_background,null));
        //关键下面两句话，设置了回退按钮，及点击事件的效果
        ab.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.hideOverflowMenu();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        return this;
    }


    public  SanetelTabActivity initView(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        map.put("功放厂家","更换参数，点击▲ 设置永久生效。");
        map.put("功放本振","更换参数，点击▲ 设置永久生效。");
        map.put("邻星干扰","更换参数，点击▲ 设置永久生效。");
        map.put("发射开关","更换参数，点击▲ 设置永久生效。");
        mSectionsPagerAdapter.setTab(map);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (tabLayout.getChildCount()>4)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        else
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Map.Entry<String , String > entry = hashMapUtils.getElemntFromLinkHashMap(map,tab.getPosition());
                // 这儿使用getSupportedActionBar没有用
                if (entry != null && toolbar != null)
                    toolbar.setTitle(entry.getKey());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        toolbar.setTitle(hashMapUtils.getElemntFromLinkHashMap(map,0).getKey());

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
                    sbFold.onConfirm("确认暂停吗？", new View.OnClickListener() {
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

        return this;
    }

    public SanetelTabActivity setupFab(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snackHelpMessage == null || snackHelpMessage.isEmpty()) {
                    snackHelpMessage = "Replace with your own action";
                }
                Snackbar.make(view, snackHelpMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return this;
    }

    public SanetelTabActivity setHelpMessage(String message){
        this.snackHelpMessage = message;
        return this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sanetel_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            // TODO 显示帮助文档
            Toast.makeText(getBaseContext(),"No Help", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
