package com.edroplet.sanetel.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by qxs on 2018/1/6.
 * 含有定时器的fragment
 */

public class TimerFragment extends Fragment {
    private int Schedule = 1000;

    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    public TimerFragment setSchedule(int schedule) {
        Schedule = schedule;
        return this;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            doTimer();
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer.schedule(timerTask,0,Schedule);
    }

    public void doTimer(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }
}
