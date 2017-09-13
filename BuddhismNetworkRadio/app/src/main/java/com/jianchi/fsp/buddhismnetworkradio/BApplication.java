package com.jianchi.fsp.buddhismnetworkradio;

import android.app.Application;

/**
 * Created by fsp on 16-7-13.
 * 保存状态，以便在屏幕旋转后使用
 */
public class BApplication extends Application {
    /**
     * 屏幕方向，初始化为纵向
     */
    boolean screenPORTRAIT = true;

    /**
     * 是否为仅声音
     */
    boolean isOnlySound = false;

    /**
     * 记录是否正在播放，以便在恢复时使用
     */
    boolean isPlayingResume = false;

    /**
     * 数据中心
     */
    DataCenter data;

    //载入错误次数
    int errTimes = 0;

    /**
     * 网络数据API
     */
    WebApi api;

}
