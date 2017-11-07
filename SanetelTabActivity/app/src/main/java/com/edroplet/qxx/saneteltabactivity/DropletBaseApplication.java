package com.edroplet.qxx.saneteltabactivity;

import android.app.Application;
import android.content.Context;

import com.edroplet.qxx.saneteltabactivity.adapters.download.DownloadAdapter;
import com.edroplet.qxx.saneteltabactivity.services.down.DLDownloadListener;
import com.edroplet.qxx.saneteltabactivity.services.down.DownloadInit;
import com.tamic.rx.fastdown.client.DLClientFactory;
import com.tamic.rx.fastdown.client.Type;
import com.tamic.rx.fastdown.core.Download;

/**
 * Created by will on 2016/10/29.
 */

public class DropletBaseApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
        init();
    }
    public static Context getGlobalContext(){
        return mContext;
    }

    public static DownloadAdapter downloadAdapter;
    private void init() {

        DownloadInit.init(getBaseContext());

        new Download.ConfigBuilder<>()
                .addMaxCount(5)
                .downloadListener(new DLDownloadListener(this.getBaseContext()))
                .baseClient(DLClientFactory.createClient(Type.NORMAL, getBaseContext()))
                .newbuild(this, downloadAdapter);
       /* RxDownloadManager manager = RxDownloadManager.getInstance();
        manager.init(getBaseContext(), null);
        manager.setContext(getBaseContext());
        manager.setListener(new DLDownloadListener(getBaseContext()));


        DLNormalCallback normalCallback = new DLNormalCallback();
        if (manager.getClient() != null) {
            manager.getClient().setCallback(normalCallback);
        }*/






    }
}
