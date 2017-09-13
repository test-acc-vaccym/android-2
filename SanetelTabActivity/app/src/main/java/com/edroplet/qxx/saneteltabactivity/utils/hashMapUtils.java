package com.edroplet.qxx.saneteltabactivity.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by qxs on 2017/9/12.
 */

public class hashMapUtils {
    public static Map.Entry<String, String> getElemntFromLinkHashMap(LinkedHashMap<String,String> hashMap, int position){
        if (hashMap.isEmpty())
            return null;
        if (position < hashMap.size()){
            Iterator it = hashMap.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry<String, String> enter=(Map.Entry<String, String>) it.next();
                if (i == position) {
                    return enter;
                }
                i++;
            }
        }
        return null;
    }
}
