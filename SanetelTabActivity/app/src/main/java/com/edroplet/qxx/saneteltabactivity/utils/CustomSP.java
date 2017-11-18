package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by qxs on 2017/9/21.
 */

public class CustomSP {
    private static final String spFileName="sanetel";
    public static final String globalLanguage = "global.language";
    public static final String globalFont = "global.font";
    public static  final String firstReadSatellites = "firstReadSatellites";
    public static  final String firstReadCities = "firstReadCities";
    public static  final String showWelcome = "showWelcome";
    public static  final String searchingMode = "searchingMode";
    public static final String WifiSettingsNameKey = "deviceName";

    public static final String KEY_SEARCHING_MODE = "KEY_SEARCHING_MODE";

    // IP设置相关
    public static final String KeyIPSettingsAddress = "ipAddress";
    public static final String KeyIPSettingsPort = "ipPort";
    public static final String KeyIPSettingsMask = "ipMask";
    public static final int DefaultPort = 8080;
    public static final String DefaultIP = "127.0.0.1";

    public  static boolean getBoolean(final Context context, String key, boolean defaultVal){
        if (null == context)
            return defaultVal;
        //用SharedPreferences保存是否第一次进入App的参数
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        if (null == sharedPreferences) return defaultVal;
        return sharedPreferences.getBoolean(key,defaultVal);
    }
    public  static String getString(final Context context, String key, @Nullable String defaultVal){
        if (null == context)
            return defaultVal;
        if (spFileName == null || spFileName.isEmpty()) return defaultVal;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        if (null == sharedPreferences) return defaultVal;
        return sharedPreferences.getString(key,defaultVal);
    }

    public static float getFloat(final Context context, String key, float defaultVal){
        if (null == context)
            return defaultVal;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        if (null == sharedPreferences) return defaultVal;
        return sharedPreferences.getFloat(key,defaultVal);
    }

    public  static int getInt(final Context context, String key, int defaultVal){
        if (null == context)
            return defaultVal;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        if (null == sharedPreferences) return defaultVal;
        return sharedPreferences.getInt(key,defaultVal);
    }

    public  static void putBoolean(final Context context, String key, boolean val){
        if (null == context)
            return;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,val);
        editor.apply();
    }

    public  static void putInt(final Context context, String key, int val){
        if (null == context)
            return;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,val);
        editor.apply();
    }

    public  static void putFloat(final Context context, String key, float val){
        if (null == context)
            return;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,val);
        editor.apply();
    }

    public  static void putString(final Context context, String key, String val){
        if (null == context)
            return;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,val);
        editor.apply();
    }

    public  static void putStringSet(final Context context, String key, Set<String> val){
        if (null == context)
            return;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key,val);
        editor.apply();
    }
    public static void clear(final Context context){
        if (null == context)
            return;
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
