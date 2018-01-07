package com.edroplet.sanetel.beans.status;

import android.content.Context;

import com.edroplet.sanetel.beans.LockerInfo;
import com.edroplet.sanetel.beans.SavingInfo;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;

/**
 * Created by qxs on 2018/1/4.
 * 运行状态-故障-来自监视信息的flag
 */

public class RunningInfo {
    private char energyInfo; //D0:  0:不节能        1：节能
    private char pitchLockerInfo; //D1：0:俯仰未锁紧1：俯仰锁紧
    private char azimuthLockerInfo; //D2:  0:方位未锁紧 1：方位锁紧
    private char pitchElectricLowLimit; //D3:  0:  1：俯仰电气低限位
    private char pitchElectricHighLimit; //D4:  0:  1：俯仰电气高限位
    private char pitchSoftLowLimit; //D5:  0:  1：俯仰软件低限位
    private char pitchSoftHighLimit; //D6:  0:  1：俯仰软件高限位
    private char azimuthSoftLowLimit; //D7:  0:  1：方位软件低限位
    private char azimuthSoftHighLimit; //D8:  0:  1：方位软件高限位
    private char polarSoftLowLimit; //D9:  0:  1：极化软件低限位
    private char polarSoftHighLimit; //D10:  0:  1：极化软件高限位
    // ……
    //    D31

    private static final String KeyEnergyInfo = "KeyEnergyInfo";
    private static final String KeyPitchLockerInfo = "KeyPitchLockerInfo";
    private static final String KeyAzimuthLockerInfo = "KeyAzimuthLockerInfo";
    private static final String KeyPitchElectricLowLimit = "KeyPitchElectricLowLimit";
    private static final String KeyPitchElectricHighLimit = "KeyPitchElectricHighLimit";
    private static final String KeyPitchSoftLowLimit = "KeyPitchSoftLowLimit";
    private static final String KeyPitchSoftHighLimit = "KeyPitchSoftHighLimit";
    private static final String KeyAzimuthSoftLowLimit = "KeyAzimuthSoftLowLimit";
    private static final String KeyAzimuthSoftHighLimit = "KeyAzimuthSoftHighLimit";
    private static final String KeyPolarSoftLowLimit = "KeyPolarSoftLowLimit";
    private static final String KeyPolarSoftHighLimit = "KeyPolarSoftHighLimit";

    public static RunningInfo parseRunningInfo(Context context, String flag){
        RunningInfo runningInfo = new RunningInfo();
        String binaryString = Integer.toBinaryString(ConvertUtil.convertToInt(flag,0));
        runningInfo.energyInfo = binaryString.charAt(0);
        CustomSP.putInt(context, KeyEnergyInfo, runningInfo.energyInfo);
        SavingInfo.setSavingState(context, runningInfo.energyInfo);

        runningInfo.pitchLockerInfo = binaryString.charAt(1);
        CustomSP.putInt(context, KeyPitchLockerInfo, runningInfo.pitchLockerInfo);
        runningInfo.azimuthLockerInfo = binaryString.charAt(2);
        CustomSP.putInt(context, KeyAzimuthLockerInfo, runningInfo.azimuthLockerInfo);
        if (runningInfo.azimuthLockerInfo == LockerInfo.LOCKER_STATE_LOCKED || runningInfo.pitchLockerInfo == LockerInfo.LOCKER_STATE_LOCKED){
            LockerInfo.setLockerState(context,LockerInfo.LOCKER_STATE_LOCKED);
        }else{
            LockerInfo.setLockerState(context,LockerInfo.LOCKER_STATE_UNLOCK);
        }

        runningInfo.pitchElectricLowLimit = binaryString.charAt(3);
        CustomSP.putInt(context, KeyPitchElectricLowLimit, runningInfo.pitchElectricLowLimit);
        runningInfo.pitchElectricHighLimit = binaryString.charAt(4);
        CustomSP.putInt(context, KeyPitchElectricHighLimit, runningInfo.pitchElectricHighLimit);
        runningInfo.pitchSoftLowLimit = binaryString.charAt(5);
        CustomSP.putInt(context, KeyPitchSoftLowLimit, runningInfo.pitchSoftLowLimit);
        runningInfo.pitchSoftHighLimit = binaryString.charAt(6);
        CustomSP.putInt(context, KeyPitchSoftHighLimit, runningInfo.pitchSoftHighLimit);
        runningInfo.azimuthSoftLowLimit = binaryString.charAt(7);
        CustomSP.putInt(context, KeyAzimuthSoftLowLimit, runningInfo.azimuthSoftLowLimit);
        runningInfo.azimuthSoftHighLimit = binaryString.charAt(8);
        CustomSP.putInt(context, KeyAzimuthSoftHighLimit, runningInfo.azimuthSoftHighLimit);
        runningInfo.polarSoftLowLimit = binaryString.charAt(9);
        CustomSP.putInt(context, KeyPolarSoftLowLimit, runningInfo.polarSoftLowLimit);
        runningInfo.polarSoftHighLimit = binaryString.charAt(10);
        CustomSP.putInt(context, KeyPolarSoftHighLimit, runningInfo.polarSoftHighLimit);
        return runningInfo;
    }

    public char
    getAzimuthLockerInfo(Context context) {
        char defaultVal = 0;
        azimuthLockerInfo = CustomSP.getChar(context, KeyAzimuthLockerInfo, defaultVal);
        return azimuthLockerInfo;
    }

    public char
    getAzimuthSoftHighLimit(Context context) {
        char defaultVal = 0;
        azimuthSoftHighLimit = CustomSP.getChar(context, KeyAzimuthSoftHighLimit, defaultVal);
        return azimuthSoftHighLimit;
    }

    public char
    getAzimuthSoftLowLimit(Context context) {
        char defaultVal = 0;
        azimuthSoftLowLimit = CustomSP.getChar(context, KeyAzimuthSoftLowLimit, defaultVal);
        return azimuthSoftLowLimit;
    }

    public char
    getEnergyInfo(Context context) {
        char defaultVal = 0;
        energyInfo = CustomSP.getChar(context, KeyEnergyInfo, defaultVal);
        return energyInfo;
    }

    public char
    getPitchElectricHighLimit(Context context) {
        char defaultVal = 0;
        pitchElectricHighLimit = CustomSP.getChar(context, KeyPitchElectricHighLimit, defaultVal);
        return pitchElectricHighLimit;
    }

    public char
    getPitchElectricLowLimit(Context context) {
        char defaultVal = 0;
        pitchElectricLowLimit = CustomSP.getChar(context, KeyPitchElectricLowLimit, defaultVal);
        return pitchElectricLowLimit;
    }

    public char
    getPitchLockerInfo(Context context) {
        char defaultVal = 0;
        pitchLockerInfo = CustomSP.getChar(context, KeyPitchLockerInfo, defaultVal);
        return pitchLockerInfo;
    }

    public char
    getPitchSoftHighLimit(Context context) {
        char defaultVal = 0;
        pitchSoftHighLimit = CustomSP.getChar(context, KeyPitchSoftHighLimit, defaultVal);
        return pitchSoftHighLimit;
    }

    public char
    getPitchSoftLowLimit(Context context) {
        char defaultVal = 0;
        pitchSoftLowLimit = CustomSP.getChar(context, KeyPitchSoftLowLimit, defaultVal);
        return pitchSoftLowLimit;
    }

    public char
    getPolarSoftHighLimit(Context context) {
        char defaultVal = 0;
        polarSoftHighLimit = CustomSP.getChar(context, KeyPolarSoftHighLimit, defaultVal);
        return polarSoftHighLimit;
    }

    public char
    getPolarSoftLowLimit(Context context) {
        char defaultVal = 0;
        polarSoftLowLimit = CustomSP.getChar(context, KeyPolarSoftLowLimit, defaultVal);
        return polarSoftLowLimit;
    }

}
