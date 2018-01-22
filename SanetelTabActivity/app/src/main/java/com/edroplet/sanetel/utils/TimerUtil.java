package com.edroplet.sanetel.utils;

import com.edroplet.sanetel.beans.EdropletTimer;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created on 2018/1/21.
 *
 * @author qxs
 */

public class TimerUtil{
    public static final String SysState = "systemState";
    private EdropletTimer edropletTimer;
    private static Map<String,EdropletTimer> mapTimer = new HashMap<>();

    public TimerUtil(String name){
        this(name, false);
    }

    public TimerUtil(String name, boolean daemon){
        edropletTimer = new EdropletTimer();
        edropletTimer.timerName = name;
        edropletTimer.isRunning = false;
        edropletTimer.isDaemon = daemon;
        edropletTimer.period = 0;

        if (!mapTimer.containsKey(name)) {
            edropletTimer.timer = new Timer(name, daemon);
            mapTimer.put(name, edropletTimer);
        }else {
            edropletTimer.timer = getTimer(name);
        }
    }

    public static EdropletTimer getEdropletTimer(String name) {
        EdropletTimer et = mapTimer.get(name);
        return et;
    }

    public Timer getTimer(){
        return edropletTimer.timer;
    }

    public static Timer getTimer(String name){
        EdropletTimer et = mapTimer.get(name);
        if (null != et){
            return et.timer;
        }
        return new TimerUtil(name).getTimer();
    }

    public static long getPeriod(String name){
        EdropletTimer et = mapTimer.get(name);
        if (null != et){
            return et.period;
        }
        return 0;
    }

    public static boolean getDaemon(String name){
        EdropletTimer et = mapTimer.get(name);
        if (null != et) {
            return et.isDaemon;
        }
        return false;
    }
    /*本想记录运行状态的，但是想了下，timer肯定要运行的，否则不声明*/
    public static void schedule(String name, TimerTask task, long delay, long period) {
        EdropletTimer et = mapTimer.get(name);

        if (null != et) {
            et.period = period;
            et.isRunning = true;
            mapTimer.put(name,et);
            Timer t = et.timer;
            if (null != t) {
                t.schedule(task, delay, period);
            }
        }
    }


    public static void close(String name){
        EdropletTimer et = mapTimer.get(name);
        if (et != null) {
            Timer t = et.timer;
            if (t != null) {
                t.purge();
                t.cancel();
                mapTimer.remove(name);
            }
        }
    }

    public static boolean isRun(String name){
        if (mapTimer.containsKey(name)) return mapTimer.get(name).isRunning;
        return false;
    }
}
