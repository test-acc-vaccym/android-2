package com.edroplet.sanetel.utils;

import android.support.v4.util.LruCache;

/**
 * Created by qxs on 2017/9/21.
 */

public class CustomLRU extends LruCache {
    public CustomLRU(int size){
        super(size);
    }
}
