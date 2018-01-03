package com.edroplet.sanetel.beans;

import android.content.Context;

import com.edroplet.sanetel.utils.CustomSP;

/**
 * Created by qxs on 2017/11/5.
 */

public class LockerInfo {
    private static final String KEY_LOCKER_STATE="KEY_LOCKER_STATE";
    public static final int LOCKER_STATE_UNLOCK=0;
    public static final int LOCKER_STATE_LOCKED=1;

    public static int getLockerState(Context context) {
        return CustomSP.getInt(context, KEY_LOCKER_STATE, LOCKER_STATE_UNLOCK);
    }

    public static void setLockerState(Context context, int lockerState) {
        CustomSP.putInt(context, KEY_LOCKER_STATE, lockerState);
    }
}
