package com.edroplet.qxx.saneteltabactivity.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qxs on 2017/10/22.
 */

public class ConvertUtil {
    //把String转化为float
    public static float convertToFloat(String number, float defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    //把String转化为float
    public static float convertToFloat(Object number, float defaultValue) {
        return convertToFloat(String.valueOf(number), defaultValue);
    }

    //把String转化为double
    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    //把String转化为int
    public static int convertToInt(String number, int defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    //把String转化为boolean
    public static boolean convertToBool(String number, boolean defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**  * 定义分割常量 （#在集合中的含义是每个元素的分割，|主要用于map类型的集合用于key与value中的分割）  */
    private static final String SEP1 = "#";
    private static final String SEP2 = "|";

    /**  * List转换String  *   * @param list  *            :需要转换的List  * @return String转换后的字符串  */
    /*
    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    sb.append(SEP1);
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    sb.append(SEP1);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP1);
                }
            }
        }
        // return "L" + EspUtils.EncodeBase64(sb.toString());
        return sb.toString();
    }
*   /
    /**  * Map转换String  *   * @param map  *            :需要转换的Map  * @return String转换后的字符串  */
    /*
    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {    continue;   }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + ListToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1      + MapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP1 + value.toString());
                sb.append(SEP2);
            }
        }
        // return "M" + EspUtils.EncodeBase64(sb.toString());
        return sb.toString();
    }
    */

    /**  * String转换Map  *   * @param mapText  *            :需要转换的字符串  * @param KeySeparator  *            :字符串中的分隔符每一个key与value中的分割  * @param ElementSeparator  *            :字符串中每个元素的分割  * @return Map<?,?>  */
    /*
    public static Map<String, Object> StringToMap(String mapText) {
        if (mapText == null || mapText.equals("")) {
            return null;
        }
        mapText = mapText.substring(1);
        mapText = EspUtils.DecodeBase64(mapText);
        Map<String, Object> map = new HashMap<String, Object>();
        String[] text = mapText.split("\\" + SEP2);
        for (String str : text) {
            String[] keyText = str.split(SEP1);
            if (keyText.length < 1) {
                continue;
            }
            String key = keyText[0];
            String value = keyText[1];
            if (value.charAt(0) == 'M') {
                Map<?, ?> map1 = StringToMap(value);
                map.put(key, map1);
            } else if (value.charAt(0) == 'L') {
                List<?> list = StringToList(value);
                map.put(key, list);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
    */
    /**  * String转换List  *   * @param listText  *            :需要转换的文本  * @return List<?>  */
    /*
    public static List<Object> StringToList(String listText) {
        if (listText == null || listText.equals("")) {   return null;  }
        listText = listText.substring(1);
        listText = EspUtils.DecodeBase64(listText);
        List<Object> list = new ArrayList<Object>();
        String[] text = listText.split(SEP1);
        for (String str : text) {
            if (str.charAt(0) == 'M') {
                Map<?, ?> map = StringToMap(str);
                list.add(map);
            } else if (str.charAt(0) == 'L') {
                List<?> lists = StringToList(str);    list.add(lists);
            } else {
                list.add(str);
            }
        }
        return list;
    }
    */

    public static ArrayList<String> string2List(String str, String sep){
        if (str.startsWith("["))
            str = str.substring(1);
        if (str.endsWith("]"))
            str = str.substring(0,str.length()-1);
        String[] res = str.split(sep);
        ArrayList<String> result = new ArrayList<>();
        for (String s: res){
            result.add(s);
        }
        return result;
    }
}
