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

    public static final String searchingModeBeacon = "beacon";
    public static final String searchingModeDVB = "dvb";

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

    public  static void putInt(Context context, String key, int val){
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,val);
        editor.apply();
    }

    public  static void putFloat(Context context, String key, float val){
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,val);
        editor.apply();
    }

    public  static void putString(Context context, String key, String val){
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,val);
        editor.apply();
    }

    public  static void putStringSet(Context context, String key, Set<String> val){
        SharedPreferences sharedPreferences= context.getSharedPreferences(spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key,val);
        editor.apply();
    }

}
