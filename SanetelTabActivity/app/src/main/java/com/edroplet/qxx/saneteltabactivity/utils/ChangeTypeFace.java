package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.Locale;

import static android.R.attr.typeface;

/**
 * Created by qxs on 2017/9/21.
 */

public class ChangeTypeFace {
    private static Typeface typeface;
    public static Typeface getSimHei(Context context){
        if (typeface == null)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/msyhbd.ttc");
        return typeface;
    }

}
