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
        if (typeface == null) {
            int font = CustomSP.getInt(context, CustomSP.globalFont, 1);
            if (font == 0) {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/msyhbd.ttc");
            } else {
                typeface = Typeface.DEFAULT;
            }
        }
        return typeface;
    }
    public static void changeFont(Context context, int font){
        if (font == 0){
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/msyhbd.ttc");
        }else   {
            typeface = Typeface.DEFAULT;
        }
    }
}
