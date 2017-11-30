package com.edroplet.qxx.saneteltabactivity.beans;

import android.content.Context;

import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;

/**
 * Created by qxs on 2017/11/5.
 * 节能数据
 */

public class SavingInfo {
    public static final String KEY_ENERGY_STATE = "KEY_ENERGY_STATE";
    public static final int SAVING_STATE_OPEN=1;
    public static final int SAVING_STATE_CLOSE=0;


    public static int getSavingState(Context context){
        return CustomSP.getInt(context, KEY_ENERGY_STATE,SAVING_STATE_OPEN);
    }
    public static void setSavingState(Context context, int status){
       CustomSP.putInt(context, KEY_ENERGY_STATE, status);
    }
}
