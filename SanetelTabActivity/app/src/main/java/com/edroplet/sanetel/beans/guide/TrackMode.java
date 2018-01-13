package com.edroplet.sanetel.beans.guide;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;

import com.edroplet.sanetel.beans.Protocol;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by qxs on 2017/11/20.
 */

public class TrackMode implements Serializable {
    private static final int TrackModeBeacon = 0;
    private static final int TrackModeDvb = 1;
    /*定义部分*/
    @IntDef({TrackModeBeacon, TrackModeDvb})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SearchingMode {}

    private Context context;

    TrackMode(Context ctx){
        context = ctx;
    }

    private @SearchingMode int mSearchingMode;

    public static TrackMode newInstance(Context ctx) {
        Bundle args = new Bundle();
        TrackMode fragment = new TrackMode(ctx);
        return fragment;
    }

    public void setTrackMode(@SearchingMode int trackMode) {
        Protocol.sendMessage(context,String.format(Protocol.cmdSetTrackMode,trackMode));
    }
}
