package com.edroplet.sanetel.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by qxs on 2017/10/23.
 * 过滤float
 */

public class InputFilterFloat implements InputFilter {
    public static final float beaconMin = 10750f;
    public static final float beaconMax = 40000f;
    public static final float carrierMin = 10750f;
    public static final float carrierMax = 40000f;
    public static final float thresholdMin = 0;
    public static final float thresholdMax = 10.0f;
    public static final float longitudeMin = -180.0f;
    public static final float longitudeMax = 180.0f;
    public static final float dvbMin = -180.0f;
    public static final float dvbMax = 180.0f;

    private double min, max;
    private int validBitNumber = 3;
    private static int minLength = 0;

    public InputFilterFloat(double min, double max) {
        this.min = min;
        this.max = max;
        minLength = getIntegerStringLength(min);
    }

    private int getIntegerStringLength(Double dv){
        String s = String.valueOf(dv.intValue());
        return s.length();
    }
    public InputFilterFloat(String min, String max) {
        this.min = Double.parseDouble(min);
        this.max = Double.parseDouble(max);
        minLength = min.length();
    }


    public InputFilterFloat(double min, double max, int validBits) {
        this.min = min;
        this.max = max;
        validBitNumber = validBits;
        minLength = getIntegerStringLength(min);
    }

    public InputFilterFloat(String min, String max, int validBits) {
        this.min = Double.parseDouble(min);
        this.max = Double.parseDouble(max);
        validBitNumber = validBits;
        minLength = min.length();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String doubleString = dest.toString() + source.toString();

            // 如果为负数，首位为"-"
            if (doubleString.equals("-")){
                return null;
            }
            // 判断有效数
            String[] numbers = doubleString.split("\\.");
            // 格式判断
            if (numbers.length > 2){
                return "";
            }
            // 位数判断
            if (!dest.toString().isEmpty()  && numbers.length == 2 && numbers[1].length()> validBitNumber){
                // 只有返回空才不填入，否则会填入
                return "";
            }else if (dest.toString().isEmpty() && numbers.length == 2 && numbers[1].length()> validBitNumber){
                return numbers[0]+"."+numbers[1].substring(0,validBitNumber);
            }
            if (doubleString.length() < minLength) return null;
            double input = Double.parseDouble(doubleString);
            if (isInRange(min, max, input))  return null;
        } catch (NumberFormatException nfe) { return ""; }
        return "";
    }

    private boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
