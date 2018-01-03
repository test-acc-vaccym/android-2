package com.edroplet.sanetel.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by qxs on 2017/10/21.
 */

public class DateTime {
    public static String getCurrentDateTime(){
        SimpleDateFormat formatter = new SimpleDateFormat    ("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }
}
