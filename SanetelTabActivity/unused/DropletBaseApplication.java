package com.edroplet.sanetel;

import android.app.Application;
import android.content.Context;

import com.edroplet.sanetel.adapters.download.DownloadAdapter;
import com.edroplet.sanetel.services.down.DLDownloadListener;
import com.edroplet.sanetel.services.down.DownloadInit;
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
        // init();
    }
    public static Context getGlobalContext(){
        return mContext;
    }

}
