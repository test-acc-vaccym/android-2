package com.edroplet.sanetel.beans;

import android.content.Context;

import com.edroplet.sanetel.utils.CustomSP;

/**
 * Created on 2018/1/16.
 *
 * @author qxs
 */

public class TimerName {
    public static final String TimerNameHead = "com.edroplet.timer";
    public static final String MonitorInfo = TimerNameHead + "MonitorInfo";

    public static boolean isTimerRun(Context context, String name){
        if (name.startsWith(TimerNameHead))
        return CustomSP.getBoolean(context, name, false);
        return CustomSP.getBoolean(context, TimerNameHead+name, false);
    }

    public static void setTimer(Context context, String name){
        if (name.startsWith(TimerNameHead))
            CustomSP.putBoolean(context, name, true);
        else
            CustomSP.putBoolean(context, TimerNameHead+name, true);
    }

    public static void cancelTimer(Context context, String name){
        if (name.startsWith(TimerNameHead))
            CustomSP.putBoolean(context, name, false);
        else
            CustomSP.putBoolean(context, TimerNameHead+name, false);
    }

}
