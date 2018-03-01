package com.edroplet.sanetel.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Trace;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created on 2018/2/28.
 * 6.0 以上系统要求权限运行时申请
 * @author qxs
 */

public class RequestPermission {
    public static final int REQUEST_PERMISSION = 0;
    public static boolean getPermission(Activity activity, @NonNull String permission){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_PERMISSION);
            }
        }
        return true;
    }
}
