package io.pcyan.drawersample;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;


/**
 * Created by qxs on 2017/8/30.
 */

public class LvMenuItem {
        public LvMenuItem(int icon, String name, int background) {
            this.icon = icon;
            this.name = name;
            this.background = background;
            if (icon == NO_ICON && TextUtils.isEmpty(name)){
                type = TYPE_SEPARATOR;
            } else if (icon == NO_ICON){
                type = TYPE_NO_ICON;
            } else{
                type = TYPE_NORMAL;
            }

            if (type != TYPE_SEPARATOR && TextUtils.isEmpty(name)){
                throw new IllegalArgumentException("you need set a name for a non-SEPARATOR item");
            }

            Log.e("LvMenuItem", "LvMenuItem: ");


        }

        public LvMenuItem(String name){
            this(NO_ICON, name, Color.TRANSPARENT);
        }

        public LvMenuItem(){
            this(null);
        }

        private static final int NO_ICON = 0;
        public static final int TYPE_NORMAL = 0;
        public static final int TYPE_NO_ICON = 1;
        public static final int TYPE_SEPARATOR = 2;
        public int type;
        public String name;
        public int icon;
        public int background;
}

