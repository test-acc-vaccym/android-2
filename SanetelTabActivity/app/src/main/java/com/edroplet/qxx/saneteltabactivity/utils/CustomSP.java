package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by qxs on 2017/9/21.
 */

public class CustomSP {
    private static final String spFileName="sanetel";
    public  static boolean getBoolean(Context context, String key, boolean defaultVal){
        //用SharedPreferences保存是否第一次进入App的参数
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,defaultVal);
    }
    public  static String getString(Context context, String key, @Nullable String defaultVal){
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        return sharedPreferences.getString(key,defaultVal);
    }

    public static float getFloat(Context context, String key, float defaultVal){
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        return sharedPreferences.getFloat(key,defaultVal);
    }

    public  static int getInt(Context context, String key, int defaultVal){
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        return sharedPreferences.getInt(key,defaultVal);
    }

    public  static void putBoolean(Context context, String key, boolean val){
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,val);
        editor.apply();
    }

}
