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
    public static final float thresholdMax = 10f;
    public static final float longitudeMin = -180f;
    public static final float longitudeMax = 180f;
    public static final float dvbMin = 6000f;
    public static final float dvbMax = 30000f;
    public static final float latitudeMin = -90f;
    public static final float latitudeMax = 90f;

    public static final int   angleValidBit = 3;
    public static final float azimuthMin = 0f;
    public static final float azimuthMax = 360f;
    public static final float pitchMin = -10f;
    public static final float pitchMax = 90f;
    public static final float polarizationMin = -90f;
    public static final float polarizationMax = -90f;

    private double min, max;
    private int validBitNumber = 3;
    private int minLength = 0;
    private boolean isNegative = false;


    public InputFilterFloat(double min, double max) {
        this.min = min;
        this.max = max;
        minLength = getIntegerStringLength(min);
    }

    private int getIntegerStringLength(String s){
        int index = s.indexOf('.');
        int len = s.length();
        if (index < 0){
            if (s.startsWith("-")) return len - 1;
            return len;
        }
        s = s.substring(0,index);
        if (s.startsWith("-")){
            s = s.substring(1);
            isNegative = true;
        }
        len = s.length();
        return len;
    }

    private int getIntegerStringLength(Double dv){
        String s = String.valueOf(dv);
        return getIntegerStringLength(s);
    }

    public InputFilterFloat(double min, double max, int validBits) {
        this.min = min;
        this.max = max;
        validBitNumber = validBits;
        minLength = getIntegerStringLength(min);
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String doubleString = dest.toString() + source.toString();

            // 如果为负数，首位为"-"
            if (doubleString.equals("-") && isNegative){
                return null;
            }
            // 判断有效数
            String[] numbers = doubleString.split("\\.");
            String integerString = numbers[0];
            // 格式判断
            if (numbers.length > 2){
                return "";
            }
            // 位数判断
            if (!dest.toString().isEmpty()  && numbers.length == 2 && numbers[1].length()> validBitNumber){
                // 只有返回空才不填入，否则会填入
                return "";
            }else if (dest.toString().isEmpty() && numbers.length == 2 && numbers[1].length()> validBitNumber){
                return integerString + "." + numbers[1].substring(0,validBitNumber);
            }

            if ((integerString.startsWith("-") && integerString.length() < minLength + 1) || (!integerString.startsWith("-") && integerString.length() < minLength)) return null;
            double input = Double.parseDouble(doubleString);
            if (isInRange(min, max, input))  return null;
            return "";
        } catch (NumberFormatException nfe) {
            return "";
        }
    }

    private boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
