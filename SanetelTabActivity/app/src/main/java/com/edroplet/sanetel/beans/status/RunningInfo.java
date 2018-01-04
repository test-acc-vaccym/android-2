package com.edroplet.sanetel.beans.status;

import com.edroplet.sanetel.utils.ConvertUtil;

/**
 * Created by qxs on 2018/1/4.
 * 运行状态-故障-来自监视信息的flag
 */

public class RunningInfo {
    public char energyInfo; //D0:  0:不节能        1：节能
    public char pitchLockerInfo; //D1：0:俯仰未锁紧1：俯仰锁紧
    public char azimuthLockerInfo; //D2:  0:方位未锁紧 1：方位锁紧
    public char pitchElectricLowLimit; //D3:  0:  1：俯仰电气低限位
    public char pitchElectricHighLimit; //D4:  0:  1：俯仰电气高限位
    public char pitchSoftLowLimit; //D5:  0:  1：俯仰软件低限位
    public char pitchSoftHighLimit; //D6:  0:  1：俯仰软件高限位
    public char azimuthSoftLowLimit; //D7:  0:  1：方位软件低限位
    public char azimuthSoftHighLimit; //D8:  0:  1：方位软件高限位
    public char polarSoftLowLimit; //D9:  0:  1：极化软件低限位
    public char polarSoftHighLimit; //D10:  0:  1：极化软件高限位
    // ……
    //    D31

    public static RunningInfo parseRunningInfo(String flag){
        RunningInfo runningInfo = new RunningInfo();
        String binaryString = Integer.toBinaryString(ConvertUtil.convertToInt(flag,0));
        runningInfo.energyInfo = binaryString.charAt(0);
        runningInfo.pitchLockerInfo = binaryString.charAt(1);
        runningInfo.azimuthLockerInfo = binaryString.charAt(2);
        runningInfo.pitchElectricLowLimit = binaryString.charAt(3);
        runningInfo.pitchElectricHighLimit = binaryString.charAt(4);
        runningInfo.pitchSoftLowLimit = binaryString.charAt(5);
        runningInfo.pitchSoftHighLimit = binaryString.charAt(6);
        runningInfo.azimuthSoftLowLimit = binaryString.charAt(7);
        runningInfo.azimuthSoftHighLimit = binaryString.charAt(8);
        runningInfo.polarSoftLowLimit = binaryString.charAt(9);
        runningInfo.polarSoftHighLimit = binaryString.charAt(10);
        return runningInfo;
    }

}
