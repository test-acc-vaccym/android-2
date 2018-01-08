package com.edroplet.sanetel.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.Log;
import android.widget.FrameLayout;

import com.edroplet.sanetel.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by qxs on 2017/10/19.
 */

public class GalleryOnTime {
    private FrameLayout frameLayout;
    private @IdRes int[] images;
    private static int i;
    private Context context;
    private final int schedule;

    // 定时器
    private Timer timer = new Timer();

    public GalleryOnTime(Context context){
        this.context = context;
        schedule = context.getResources().getInteger(R.integer.detail_schedule_timer);
    }

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public void setImages(@IdRes int[] images) {
        this.images = images;
    }

    public Timer getTimer() {
        return timer;
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Log.d("GalleryOnTime Handle", i + "");
            frameLayout.setBackgroundResource(images[i%3]);
            frameLayout.invalidate();

            super.handleMessage(msg);
        }
    };

    public void setImageView() {
        if (null == timer){
            timer = new Timer();
        }
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                i++;
                Message message = new Message();
                message.what = i;
                handler.sendMessage(message);
                try {
                    Thread.sleep(schedule);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, schedule);
    }
}
