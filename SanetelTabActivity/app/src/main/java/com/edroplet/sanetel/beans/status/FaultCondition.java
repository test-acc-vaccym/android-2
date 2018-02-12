package com.edroplet.sanetel.beans.status;

import android.content.Context;

import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;

/**
 * Created by qxs on 2018/1/4.
 * 运行状态-故障-来自监视信息的故障状态
 */

public class FaultCondition {
    private char AzimuthMotorStatus;     //    方位电机和编码器：D8 1 故障 0 正常
    private static final String KeyAzimuthMotorStatus = "KeyAzimuthMotorStatus";
    private char AzimuthHolzerStatus;    //    方位霍尔开关   D9  1故障  0正常
    private static final String KeyAzimuthHolzerStatus = "KeyAzimuthHolzerStatus";
    private char AzimuthLockHolzerStatus;  //    方位锁紧霍尔开关 D10 1故障 0 正常
    private static final String KeyAzimuthLockHolzerStatus = "KeyAzimuthLockHolzerStatus";
    private char PitchMotorStatus;      //    俯仰电机   D11  1故障 0 正常
    private static final String KeyPitchMotorStatus = "KeyPitchMotorStatus";
    private char PitchHolzerStatus;      //    俯仰霍尔开关D12  1故障 0正常
    private static final String KeyPitchHolzerStatus = "KeyPitchHolzerStatus";
    private char PitchLockHolzerStatus;  //    俯仰锁紧霍尔开关D13  1故障 0正常
    private static final String KeyPitchLockHolzerStatus = "KeyPitchLockHolzerStatus";
    private char PolMotorStatus;        //    极化电机D14  1故障 0正常
    private static final String KeyPolMotorStatus = "KeyPolMotorStatus";
    private char PolPotentiometerStatus;  //    极化电位计D15  1故障 0正常
    private static final String KeyPolPotentiometerStatus = "KeyPolPotentiometerStatus";
    //    D16~ D31:保留

    public static FaultCondition parseFaultCondition(Context context, String faultConditionString){
        FaultCondition faultCondition = new FaultCondition();
        String binaryString = Integer.toBinaryString(ConvertUtil.convertToInt(faultConditionString,0));
        faultCondition.AzimuthMotorStatus = binaryString.charAt(0);
        setAzimuthMotorStatus(context,faultCondition.AzimuthMotorStatus);
        faultCondition.AzimuthHolzerStatus = binaryString.charAt(1);
        setAzimuthHolzerStatus(context,faultCondition.AzimuthHolzerStatus);
        faultCondition.AzimuthLockHolzerStatus = binaryString.charAt(2);
        setAzimuthLockHolzerStatus(context,faultCondition.AzimuthLockHolzerStatus);
        faultCondition.PitchMotorStatus = binaryString.charAt(3);
        setPitchMotorStatus(context,faultCondition.PitchMotorStatus);
        faultCondition.PitchHolzerStatus = binaryString.charAt(4);
        setPitchHolzerStatus(context,faultCondition.PitchHolzerStatus);
        faultCondition.PitchLockHolzerStatus = binaryString.charAt(5);
        setPitchLockHolzerStatus(context,faultCondition.PitchLockHolzerStatus);
        faultCondition.PolMotorStatus = binaryString.charAt(6);
        setPolMotorStatus(context,faultCondition.PolMotorStatus);
        faultCondition.PolPotentiometerStatus = binaryString.charAt(7);
        setPolPotentiometerStatus(context,faultCondition.PolPotentiometerStatus);

        return faultCondition;
    }

    public static char
    getAzimuthHolzerStatus(Context context) {
        return CustomSP.getChar(context, KeyAzimuthHolzerStatus, 0);
    }

    public static char getAzimuthMotorStatus(Context context){
        return (char)CustomSP.getInt(context,KeyAzimuthMotorStatus,0);
    }

    public static char
    getAzimuthLockHolzerStatus(Context context) {
        return CustomSP.getChar(context, KeyAzimuthLockHolzerStatus, 0);
    }

    public static char
    getPitchHolzerStatus(Context context) {
        return CustomSP.getChar(context, KeyPitchHolzerStatus, 0);
    }

    public static char
    getPitchLockHolzerStatus(Context context) {
        return CustomSP.getChar(context, KeyPitchLockHolzerStatus, 0);
    }

    public static char
    getPitchMotorStatus(Context context) {
        return CustomSP.getChar(context, KeyPitchMotorStatus, 0);
    }

    public static char
    getPolMotorStatus(Context context) {
        return CustomSP.getChar(context, KeyPolMotorStatus, 0);
    }

    public static char
    getPolPotentiometerStatus(Context context) {
        return CustomSP.getChar(context, KeyPolPotentiometerStatus, 0);
    }

    private static void setAzimuthHolzerStatus(Context context, char azimuthHolzerStatus) {
        CustomSP.putChar(context, KeyAzimuthHolzerStatus, azimuthHolzerStatus);
    }

    private static void setAzimuthLockHolzerStatus(Context context, char azimuthLockHolzerStatus) {
        CustomSP.putChar(context, KeyAzimuthLockHolzerStatus, azimuthLockHolzerStatus);
    }

    private static void setAzimuthMotorStatus(Context context, char azimuthMotorStatus) {
        CustomSP.putChar(context, KeyAzimuthMotorStatus, azimuthMotorStatus);
    }

    private static void setPitchHolzerStatus(Context context, char pitchHolzerStatus) {
        CustomSP.putChar(context, KeyPitchHolzerStatus, pitchHolzerStatus);
    }

    private static void setPitchLockHolzerStatus(Context context, char pitchLockHolzerStatus) {
        CustomSP.putChar(context, KeyPitchLockHolzerStatus, pitchLockHolzerStatus);
    }

    private static void setPitchMotorStatus(Context context, char pitchMotorStatus) {
        CustomSP.putChar(context, KeyPitchMotorStatus, pitchMotorStatus);
    }

    private static void setPolMotorStatus(Context context, char polMotorStatus) {
        CustomSP.putChar(context, KeyPolMotorStatus, polMotorStatus);
    }

    private static void setPolPotentiometerStatus(Context context, char polPotentiometerStatus) {
        CustomSP.putChar(context, KeyPolPotentiometerStatus, polPotentiometerStatus);
    }

}
