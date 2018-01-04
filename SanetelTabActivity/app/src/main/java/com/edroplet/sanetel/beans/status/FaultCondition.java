package com.edroplet.sanetel.beans.status;

import com.edroplet.sanetel.utils.ConvertUtil;

/**
 * Created by qxs on 2018/1/4.
 */

public class FaultCondition {
    public char ReserveCommunication;  // 预留通信 D0  1 断开 0 正在通信
    public char HandsetCommunication;  // 手持机  D1  1 断开 0 正在通信
    public char GNSSCommunication;     // GNSS   D2  1 断开 0 正在通信
    public char BeaconCommunication;   // 信标机 D3  1 断开 0 正在通信
    public char DipCommunication;      // 倾角   D4  1 断开 0 正在通信
    public char WifiCommunication;     //    wifi    D5  1 断开 0 正在通信
    public char AmplifierCommunication; //    功放   D6  1 断开 0 正在通信
    public char Reserve2Communication;   //    预留通信   D7  1 断开 0 正在通信
    public char AzimuthMotorStatus;     //    方位电机和编码器：D8 1 故障 0 正常
    public char AzimuthHolzerStatus;    //    方位霍尔开关   D9  1故障  0正常
    public char AzimuthLockHolzerStatus;  //    方位锁紧霍尔开关 D10 1故障 0 正常
    public char PitchMotorStatus;      //    俯仰电机   D11  1故障 0 正常
    public char PitchHolzerStatus;      //    俯仰霍尔开关D12  1故障 0正常
    public char PitchLockHolzerStatus;  //    俯仰锁紧霍尔开关D13  1故障 0正常
    public char PolMotorStatus;        //    极化电机D14  1故障 0正常
    public char PolPotentiometerStatus;  //    极化电位计D15  1故障 0正常
    //    D16~ D31:保留

    public static FaultCondition parseFaultCondition(String faultConditionString){
        FaultCondition faultCondition = new FaultCondition();
        String binaryString = Integer.toBinaryString(ConvertUtil.convertToInt(faultConditionString,0));
        faultCondition.ReserveCommunication = binaryString.charAt(0);
        faultCondition.HandsetCommunication = binaryString.charAt(1);
        faultCondition.GNSSCommunication = binaryString.charAt(2);
        faultCondition.BeaconCommunication = binaryString.charAt(3);
        faultCondition.DipCommunication = binaryString.charAt(4);
        faultCondition.WifiCommunication = binaryString.charAt(5);
        faultCondition.AmplifierCommunication = binaryString.charAt(6);
        faultCondition.Reserve2Communication = binaryString.charAt(7);
        faultCondition.AzimuthMotorStatus = binaryString.charAt(8);
        faultCondition.AzimuthHolzerStatus = binaryString.charAt(9);
        faultCondition.AzimuthLockHolzerStatus = binaryString.charAt(10);
        faultCondition.PitchMotorStatus = binaryString.charAt(11);
        faultCondition.PitchHolzerStatus = binaryString.charAt(12);
        faultCondition.PitchLockHolzerStatus = binaryString.charAt(13);
        faultCondition.PolMotorStatus = binaryString.charAt(14);
        faultCondition.PolPotentiometerStatus = binaryString.charAt(15);
        return faultCondition;
    }
}
