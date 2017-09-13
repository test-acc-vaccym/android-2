package com.jianchi.fsp.buddhismnetworkradio;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jianchi.fsp.buddhismnetworkradio.api.Channel;
import com.jianchi.fsp.buddhismnetworkradio.api.Server;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.RunnableFuture;

import it.sephiroth.android.library.widget.HListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //region 变量区
    /**
     * 视频播放器
     */
    VideoView videoView;

    /**
     * 仅声音选择按扭
     */
    CheckBox cb_onlySound;

    /**
     * DrawerLayout容器
     */
    private DrawerLayout mDrawer_layout;
    /**
     * 右侧抽屉
     */
    private RelativeLayout mMenu_layout_right;

    /**
     * 播放按扭
     */
    private ImageButton bt_play;

    /**
     * 全屏按扭
     */
    private ImageButton bt_fullScreen;

    /**
     * 播放器外框
     */
    private FrameLayout player_frame;

    /**
     * 下方经文讲义而已
     */
    private LinearLayout note_linear;

    /**
     * 等待对话框
     */
    ProgressDialog proDialog;

    /**
     * 加载视频动画
     */
    ProgressBar proBar;
    int proBarThreadId = 0;

    /**
     * 管理播放器周边按扭的类
     */
    VideoMenuManager menuManager;

    /**
     * 自定义APP类
     */
    BApplication app;

    ListView lv_servers;
    ListView lv_programs;
    HListView lv_programsType;

    boolean actionIsChangeScreen = false;
    boolean actionIsBack = false;

    TextView tv_note_title;
    TextView tv_note;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取自定义APP，APP内存在着数据，若为旋转屏幕，此处记录以前的内容
        app = (BApplication)getApplication();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //判断是否连接到网络
        if(!isNetworkConnected()){
            networkFailClose();
        }else {

            //左右侧抽屉菜单初始化
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            mDrawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

            tv_note = (TextView) findViewById(R.id.tv_note);
            tv_note_title = (TextView) findViewById(R.id.tv_note_title);

            proBar = (ProgressBar) findViewById(R.id.progressBar);

            //初始化videoView，设置video大小，以及错误处理，以及角屏事件
            initVideoView();

            /**
             * 调整字体大小
             */
            ImageButton bt_increase_font_size = (ImageButton)findViewById(R.id.bt_increase_font_size);
            bt_increase_font_size.setOnClickListener(bt_increase_font_sizeOnClickListener);

            ImageButton bt_reduce_font_size = (ImageButton)findViewById(R.id.bt_reduce_font_size);
            bt_reduce_font_size.setOnClickListener(bt_reduce_font_sizeOnClickListener);

            //初始化数据
            initData();

            //TODO 这里要检测是恢复还是新开，叵为恢复，需要还原状态
        }
    }

    CompoundButton.OnCheckedChangeListener cb_onlySoundOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            app.isOnlySound = b;
            resetVideoView(true);
        }
    };

    View.OnClickListener bt_playOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resetVideoView(!videoView.isPlaying());
        }
    };


    View.OnClickListener bt_fullScreenOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            app.screenPORTRAIT=!app.screenPORTRAIT;
            actionIsChangeScreen = true;
            if(app.screenPORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            if(videoView.isPlaying()){
                menuManager.dShow();
                menuManager.delayHide();
            }
        }
    };

    //放大字体，最大48号字
    View.OnClickListener bt_increase_font_sizeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            float sp = tv_note.getTextSize();
            sp=px2sp(sp)+1;
            if(sp>48) return;
            tv_note.setTextSize(sp);
        }
    };

    //缩小字体，最小6号字
    View.OnClickListener bt_reduce_font_sizeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //sp
            float sp = tv_note.getTextSize();
            sp=px2sp(sp)-1;
            if(sp<6) return;
            tv_note.setTextSize(sp);
        }
    };

    float px2sp(float pxValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return pxValue / fontScale;
    }
    /*
    退出流程设计
    若为打电话或按HOME键
        执行 onStop 事件后不执行 onDestory
        在返回时 不执行 onCreate ，而是执行 onSavedInstanceState。onRestart()开始-onStart()-onResume()

    若为back键
        finish前台的activity，即activity的状态为onDestory为止
        再次启动该activity则从onCreate开始，不会调用onSavedInstanceState方法
     */

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(app.isPlayingResume)
            resetVideoView(true);
    }


    /**
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    protected void onPause() {
        if(videoView.isPlaying()) {
            app.isPlayingResume =true;
            resetVideoView(false);
        } else {
            app.isPlayingResume =false;
        }
        super.onPause();
    }

    /**
     * 配制发生变化，这里处理屏幕旋转的事件
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //根据app内的设置，来设置屏幕方向，并判断是否全屏
        if(app.screenPORTRAIT) {
            screenPortrait();
        } else {
            screenLandscape();
        }
    }

    /**
     * 竖屏时布局设置
     */
    void screenPortrait(){

        AppBarLayout toolbar_bar = (AppBarLayout) findViewById(R.id.toolbar_bar);
        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        toolbar_bar.setVisibility(View.VISIBLE);

        note_linear.setVisibility(View.VISIBLE);

        bt_fullScreen.setImageResource(R.mipmap.ic_full_screen);

        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //竖屏
        int h = width * 9 / 16;

        RelativeLayout contentPanel = (RelativeLayout)findViewById(R.id.contentPanel);
        CoordinatorLayout.LayoutParams cl = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        actionBarHeight+=dip2px(this, 48);
        cl.setMargins(0,actionBarHeight,0,0);
        contentPanel.setLayoutParams(cl);

        RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(width, h);
        l.setMargins(0,0,0,0);
        player_frame.setLayoutParams(l);
        videoView.setLayoutParams(new FrameLayout.LayoutParams(width, h));
    }

    /**
     * 横屏时布局设置
     */
    void screenLandscape(){

        AppBarLayout toolbar_bar = (AppBarLayout) findViewById(R.id.toolbar_bar);
        //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        toolbar_bar.setVisibility(View.INVISIBLE);

        note_linear.setVisibility(View.INVISIBLE);

        bt_fullScreen.setImageResource(R.mipmap.ic_shrink_screen);

        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //横屏全屏
        RelativeLayout contentPanel = (RelativeLayout)findViewById(R.id.contentPanel);
        CoordinatorLayout.LayoutParams cl = new CoordinatorLayout.LayoutParams(width, height);
        cl.setMargins(0,0,0,0);
        contentPanel.setLayoutParams(cl);

        RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(width, height);
        l.setMargins(0,0,0,0);
        player_frame.setLayoutParams(l);
        videoView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
    }

    void resetVideoMenu(){
        if(videoView==null){
            if(!menuManager.menuVisible){
                menuManager.alwaysShow();
            } else {

            }
        }
    }


    /**
     * 网络连接失败后关闭程序
     */
    void networkFailClose(){
        Toast.makeText(this, R.string.wljwl, Toast.LENGTH_LONG).show();//提示信息
        MyLog.v("onCreate", getString(R.string.wljwl));
        //提示过信息5秒后关闭程序
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //没有连接到网络，关系程序
                finish();
            }
        }).start();
    }

    /**
     * 刷新显示信息
     */
    void referer(){


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

/*                if(app.data.getChannels().selectedChannelTitle.equals("淨空老法師直播")){
                    //设置显示仅声音按扭
                    tv_note_title.setText(R.string.jwjy);
                    tv_note.setText(app.data.getNote());
                } else {
                    //获取节目列表
                    tv_note_title.setText(R.string.jmlb);
                }*/

                if(app.data.getChannels().getSelectedChannel().audioUrl.isEmpty()){
                    cb_onlySound.setVisibility(View.INVISIBLE);
                } else {
                    cb_onlySound.setVisibility(View.VISIBLE);
                }

                app.data.getPrograms().programs.clear();
                ((ProgramListAdapter) lv_programs.getAdapter()).notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lv_programs);
            }
        });

        if(app.data.getChannels().selectedChannelTitle.equals("淨空老法師直播")){
            app.api.GetNote(new IStringEvent() {
                @Override
                public void getMsg(String msg) {
                    if(msg!=null) {
                        app.data.setNote(msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setNote();
                            }
                        });
                    }
                }
            });
        }

        String type = app.data.getChannels().selectedChannelTitle;

        app.api.GetProgramsList(new IProgramsListEvent() {
            @Override
            public void getItems(ProgramsList programs) {
                if(programs!=null) {
                    app.data.setPrograms(programs);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //初始化数据
                            if (!app.data.getChannels().selectedChannelTitle.equals("淨空老法師直播")) {
                                setNote();
                            }

                            ((ProgramListAdapter) lv_programs.getAdapter()).notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(lv_programs);
                        }
                    });
                }
            }
        }, type);
    }

    /**
     * 初始化数据
     */
    void initData(){
        if(app.api==null) {
            //首次运行，API未初始化
            // 打开等待初始化的对话框
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    proDialog = ProgressDialog.show(MainActivity.this, getString(R.string.zrsj), getString(R.string.sjjzz));
                }
            });

            app.api = new WebApi(getApplicationContext());
            app.data = new DataCenter();
            app.isOnlySound = cb_onlySound.isChecked();

            //获取新闻
            app.api.GetNewsList(new INewsListEvent() {
                @Override
                public void getItems(List<String> programs) {
                    app.data.setNews(programs);
                }
            });
            //获取经文讲义
            app.api.GetNote(new IStringEvent() {
                @Override
                public void getMsg(String msg) {
                    app.data.setNote(msg);
                    if (msg == null) {
                        MyLog.v("GetNote", "GetNote fail。");
                    }
                }
            });
            //获取节目列表
            app.api.GetProgramsList(new IProgramsListEvent() {
                @Override
                public void getItems(ProgramsList programs) {
                    if(programs!=null) {
                        app.data.setPrograms(programs);
                    }
                }
            }, "淨空老法師直播");

            //获取服务器地址
            app.api.GetServersList(new IServersListEvent() {
                @Override
                public void getServers(ServersList servers) {
                    app.data.setServers(servers);
                }
            });

            app.api.GetChannelList(new IChannelListEvent(){

                @Override
                public void getItems(ChannelList channelList) {
                    app.data.setChannels(channelList);
                }
            });
        }

        //开启等待初始化动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!app.data.allBack()) {//等等数据下载完毕
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (app.data.allSetValue()) {//检测数据是否下载正确
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //初始化左右抽屉菜单列表
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            View headerView = navigationView.getHeaderView(0);
                            lv_programs = (ListView) headerView.findViewById(R.id.lv_programs);
                            lv_programs.setAdapter(new ProgramListAdapter(MainActivity.this, app.data));

                            ListView lv_news = (ListView) headerView.findViewById(R.id.lv_news);
                            lv_news.setAdapter(new NewsListAdapter(MainActivity.this, app.data));

                            NavigationView navigationViewR = (NavigationView) findViewById(R.id.right_nav_view);
                            View headerViewR = navigationViewR.getHeaderView(0);
                            lv_programsType = (HListView) findViewById(R.id.lv_programsType);
                            ChannelListAdapter programsTypeListAdapter = new ChannelListAdapter(MainActivity.this, app.data);
                            lv_programsType.setAdapter(programsTypeListAdapter);
                            lv_servers = (ListView) headerViewR.findViewById(R.id.lv_servers);
                            lv_servers.setAdapter(new ServerListAdapter(MainActivity.this, app.data));

                            setListViewHeightBasedOnChildren(lv_programs);
                            setListViewHeightBasedOnChildren(lv_news);
                            //setListViewHeightBasedOnChildren(lv_programsType);
                            setListViewHeightBasedOnChildren(lv_servers);

                            setNote();

                            lv_programs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    mDrawer_layout.closeDrawer(GravityCompat.START);
                                }
                            });
                            lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    mDrawer_layout.closeDrawer(GravityCompat.START);
                                }
                            });

                            /**
                             * 线路选择事件
                             */
                            lv_servers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Server si = (Server) view.getTag();

                                    if (!si.title.equals(app.data.getServers().getSelectedServer().title)) {
                                        app.data.getServers().setSelectedServer(si);
                                        ((ServerListAdapter)lv_servers.getAdapter()).notifyDataSetChanged();
                                        mDrawer_layout.closeDrawer(GravityCompat.END);
                                        resetVideoView(true);
                                    }
                                }
                            });

                            lv_programsType.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int i, long l) {
                                    Channel programType = (Channel) view.getTag();
                                    if (!programType.title.equals(app.data.getChannels().selectedChannelTitle)) {

                                        app.data.getChannels().selectedChannelTitle = programType.title;

                                        //刷新数据，更改节目列表以及
                                        referer();

                                        //改变颜色
                                        ((ChannelListAdapter)lv_programsType.getAdapter()).notifyDataSetChanged();
                                        mDrawer_layout.closeDrawer(GravityCompat.END);
                                        resetVideoView(true);

                                        //TextView tvTitle = (TextView)findViewById(R.id.tv_title);
                                        //tvTitle.setText(app.data.getChannels().selectedChannelTitle);
                                    }
                                }
                            });

                            //TextView tvTitle = (TextView)findViewById(R.id.tv_title);
                            //tvTitle.setText(app.data.getChannels().selectedChannelTitle);

                            MyLog.i("initData", "initData sucess。");

                            //结束等待
                            if(proDialog!=null)proDialog.dismiss();
                        }
                    });
                }else {
                    //数据下载不完整，关闭程序
                    if(proDialog!=null)proDialog.dismiss();
                    MyLog.v("initData", "initData 失败。");
                    Toast.makeText(MainActivity.this, R.string.sjhcsb, Toast.LENGTH_LONG).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(8000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finish();
                        }
                    }).start();
                }
            }

        }).start();
    }

    void setNote(){
        String note = app.data.getNote();
        note = TW2CN.getInstance(getApplicationContext()).toLocalString(note);
        if(app.data.getChannels().selectedChannelTitle.equals("淨空老法師直播") && note!=null && !note.isEmpty()){
            //设置显示仅声音按扭
            tv_note_title.setText(R.string.jwjy);
            tv_note.setText(note);
        } else {
            //获取节目列表
            tv_note_title.setText(R.string.jmlb);
            StringBuilder sb = new StringBuilder();
            for (String s : app.data.getPrograms().programs) {
                sb.append(s).append("\r\n");
            }
            tv_note.setText(sb.toString());
        }
    }

    /**
     * 核心函数，用来初始化视频播放器。主要功能有
     * 1、在全屏时进行特别设置
     * 2、处理错误数据
     * 3、处理点击
     */
    void initVideoView(){

        //初始化三个关键变量
        player_frame = (FrameLayout) findViewById(R.id.player_frame);
        videoView = (VideoView) findViewById(R.id.videoView);
        note_linear = (LinearLayout)findViewById(R.id.note_linear);
        bt_fullScreen = (ImageButton)findViewById(R.id.bt_fullScreen);

        screenPortrait();

        videoView.setVisibility(View.VISIBLE);
        videoView.setBackgroundResource(R.drawable.zcgt);

        videoView.setOnPreparedListener(videoViewOnPreparedListener);

        videoView.setOnErrorListener(videoViewOnErrorListener);

        //LinearLayout videoView_top = (LinearLayout)findViewById(R.id.videoView_top);
        RelativeLayout videoView_bottom = (RelativeLayout)findViewById(R.id.videoView_bottom);

        menuManager=new VideoMenuManager(MainActivity.this, videoView_bottom);//, videoView_top
        videoView.setOnTouchListener(videoViewOnTouchListener);


        bt_fullScreen.setOnClickListener(bt_fullScreenOnClickListener);

        //按放按扭
        bt_play = (ImageButton) findViewById(R.id.bt_play);
        bt_play.setOnClickListener(bt_playOnClickListener);

        //仅声音按扭
        cb_onlySound = (CheckBox) findViewById(R.id.cb_onlySound);
        cb_onlySound.setOnCheckedChangeListener(cb_onlySoundOnCheckedChangeListener);
    }

    MediaPlayer.OnPreparedListener videoViewOnPreparedListener=new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            //缓冲结束
            if(proBar.getVisibility()==View.VISIBLE) proBar.setVisibility(View.INVISIBLE);
            if(!app.isOnlySound)videoView.setBackgroundResource(0);
            app.errTimes=0;
            VideoViewBuffering vb = new VideoViewBuffering();
            vb.start();
        }
    };

    MediaPlayer.OnErrorListener videoViewOnErrorListener=new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
            //region 错误信息翻译说明
                    /*
                    错误常数

MEDIA_ERROR_IO
文件不存在或错误，或网络不可访问错误
值: -1004 (0xfffffc14)

MEDIA_ERROR_MALFORMED
流不符合有关标准或文件的编码规范
值: -1007 (0xfffffc11)

MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK
视频流及其容器不适用于连续播放视频的指标（例如：MOOV原子）不在文件的开始.
值: 200 (0x000000c8)

MEDIA_ERROR_SERVER_DIED
媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
值: 100 (0x00000064)

MEDIA_ERROR_TIMED_OUT
一些操作使用了过长的时间，也就是超时了，通常是超过了3-5秒
值: -110 (0xffffff92)

MEDIA_ERROR_UNKNOWN
未知错误
值: 1 (0x00000001)

MEDIA_ERROR_UNSUPPORTED
比特流符合相关编码标准或文件的规格，但媒体框架不支持此功能
值: -1010 (0xfffffc0e)


what 	int: the type of error that has occurred:
    MEDIA_ERROR_UNKNOWN
    MEDIA_ERROR_SERVER_DIED
extra 	int: an extra code, specific to the error. Typically implementation dependent.
    MEDIA_ERROR_IO
    MEDIA_ERROR_MALFORMED
    MEDIA_ERROR_UNSUPPORTED
    MEDIA_ERROR_TIMED_OUT
    MEDIA_ERROR_SYSTEM (-2147483648) - low-level system error.

* */
            //endregion

            MyLog.e("MediaPlayer onError", "int what "+what+", int extra"+extra);


            //根据不同的错误进行信息提示
            if(what==MediaPlayer.MEDIA_ERROR_SERVER_DIED){
                //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
                Toast.makeText(MainActivity.this, R.string.wlfwcw,
                        Toast.LENGTH_LONG).show();
            }else if(what==MediaPlayer.MEDIA_ERROR_UNKNOWN){
                if(extra==MediaPlayer.MEDIA_ERROR_IO){
                    //文件不存在或错误，或网络不可访问错误
                    Toast.makeText(MainActivity.this,R.string.wlljcw,
                            Toast.LENGTH_LONG).show();
                } else if(extra==MediaPlayer.MEDIA_ERROR_TIMED_OUT){
                    //超时
                    Toast.makeText(MainActivity.this,R.string.wlcs,
                            Toast.LENGTH_LONG).show();
                }
            }

            if(app.errTimes>3){
                app.errTimes=0;
                //发生错误，关闭播放的视频
                resetVideoView(false);
            }else {
                resetVideoView(true);
            }

            return false;
        }
    };

    View.OnTouchListener videoViewOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //TODO 动画显示隐藏信息
            if(videoView.isPlaying()){
                if(menuManager.menuVisible){
                    menuManager.hideMenu();
                } else {
                    menuManager.displayMenu(true);
                }
            }
            return false;
        }
    };

    private void resetVideoView(boolean toPlay){

        if(proBar.getVisibility()==View.VISIBLE) proBar.setVisibility(View.INVISIBLE);

        if(!isNetworkConnected()){
            networkFailClose();
        }else {
            if (toPlay) {

                try {
                    videoView.stopPlayback();
                }catch (Exception e){}

                videoView.setVisibility(View.VISIBLE);
                videoView.setBackgroundResource(R.drawable.zcgt);

                proBar.setVisibility(View.VISIBLE);
                proBarThreadId++;
                //启动线程还控制进度控件的显示，当开始播放后，缓冲进度控件消失
                new ProBarThread(proBarThreadId).start();

                if(menuManager.menuVisible)
                    menuManager.delayHide();
                else
                    menuManager.displayMenu(true);

                if (app.isOnlySound) {
                    videoView.setVideoURI(app.data.getSoundUriAuto());
                } else {
                    videoView.setVideoURI(app.data.getVideoUriAuto());
                }
                videoView.start();
                bt_play.setImageResource(R.mipmap.ic_stop);
            } else {

                try {
                    videoView.stopPlayback();
                }catch (Exception e){}

                if(menuManager.menuVisible)
                    menuManager.alwaysShow();
                else
                    menuManager.displayMenu(false);

                videoView.setVisibility(View.VISIBLE);
                videoView.setBackgroundResource(R.drawable.zcgt);
                bt_play.setImageResource(R.mipmap.ic_play);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer_layout.isDrawerOpen(GravityCompat.START)) {
            mDrawer_layout.closeDrawer(GravityCompat.START);
        } else if (mDrawer_layout.isDrawerOpen(GravityCompat.END)) {
            mDrawer_layout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
            actionIsBack = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_select_server) {
            //显示右侧栏
            if (mDrawer_layout.isDrawerOpen(GravityCompat.START)) {
                mDrawer_layout.closeDrawer(GravityCompat.START);
            }
            mDrawer_layout.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 计算弄表高度
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 检测网络是否可用
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    /**
     * 判断是不是竖屏，这个不用了，现在使用用户设置
     * @return
     */
    public boolean isPortrait() {
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //监视视频播放过种中是否存在暂停缓冲的线程，如果视频暂停缓冲，则等待50秒，到时还未播放则报错
    class VideoViewBuffering extends Thread {
        int old_duration = -1;
        int isBuffering = 0;//0 无proBar 1 要求设置显示proBar 2 已设置显示proBar
        int bufferingC = 0;
        public void run() {
            old_duration = -1;
            isBuffering = 0;
            bufferingC = 0;

            //视频没有开始播放则一直等待
            while (!videoView.isPlaying()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //等待50秒，若50秒没播放则报错
            //视频播放中，则开始检测状态
            while (videoView.isPlaying()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //未播放或已经停止则返回
                if(videoView==null || !videoView.isPlaying()){
                    break;
                }

                //获取当前进度
                int duration = videoView.getCurrentPosition();

                //如果新的进度和老的进度相等，则说明视频暂停了，处于缓冲状态，否则又开始播放了
                if (old_duration == duration && videoView.isPlaying()) {
                    //第一次进入缓冲暂停，则显示等等进度
                    if(isBuffering<1){
                        MyLog.i("VideoViewBuffering", "开始缓冲");
                        isBuffering = 1;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                proBar.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    //如果超过10秒仍未播放则重启播放
                    bufferingC++;
                    if(bufferingC>100) {
                        MyLog.i("VideoViewBuffering", "缓冲超过50秒，重新开始播放");
                        //超过50秒后则重新启动
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resetVideoView(true);
                            }
                        });
                        return;
                    }
                } else if(isBuffering>=1){
                    //又开始播放了
                    MyLog.i("VideoViewBuffering", "缓冲完成，继续播放");
                    bufferingC=0;
                    isBuffering=0;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            proBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                old_duration = duration;
                //MyLog.i("VideoViewBuffering", String.valueOf(duration));
            }

            MyLog.i("VideoViewBuffering", "跳出循环");
            if(isBuffering>=1 && !videoView.isPlaying()){
                MyLog.i("VideoViewBuffering", "停止播放，结束缓冲");
                bufferingC=0;
                isBuffering=0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        proBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    //点开始播放后等等缓冲结束，仅在播放前缓冲时运行。
    class ProBarThread extends Thread
    {
        private int tid;
        public ProBarThread(int tid)
        {
            this.tid = tid;
        }

        //等待50秒，若50秒没播放则报错
        public void run() {
            int i = 0;
            while (!videoView.isPlaying()) {

                if(proBarThreadId!=tid)
                    return;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                if (i > 500)
                    break;
            }

            //开始了其它视频加载
            if(proBarThreadId!=tid)
                return;

            //视频加载失败
            if (!videoView.isPlaying()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (proBar.getVisibility() == View.VISIBLE) {
                            resetVideoView(false);
                            Toast.makeText(MainActivity.this, R.string.spjzsb,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }
}
