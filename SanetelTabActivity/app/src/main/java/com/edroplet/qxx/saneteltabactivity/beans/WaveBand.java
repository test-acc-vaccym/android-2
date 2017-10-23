package com.edroplet.qxx.saneteltabactivity.beans;

/**
 * Created by qxs on 2017/10/23.
 */

public class WaveBand {

    // 波段设置字段
    public static final String Key = "waveBand";
    public static final String KA = "ka";
    public static final String KU = "ku";

    public static boolean contains(String band){
        if (band != null) {
            if (band.equals(KA) || band.equals(KU)) {
                return true;
            }
        }
        return false;
    }
}
