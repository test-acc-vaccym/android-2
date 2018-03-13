package com.edroplet.sanetel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.edroplet.sanetel.services.LogService;
import com.forlong401.log.transaction.log.manager.LogManager;


/**
 * Created by will on 2016/10/29.
 * 基本
 * https://github.com/licong/log
 */

public class DropletBaseApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
        LogManager.getManager(getApplicationContext()).registerCrashHandler();
        Intent stateService =  new Intent(this, LogService.class);
        startService( stateService );
        // init();
    }
    public static Context getGlobalContext(){
        return mContext;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogManager.getManager(getApplicationContext()).unregisterCrashHandler();
    }
}
