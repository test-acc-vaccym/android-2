package com.edroplet.sanetel.beans.status;

import android.content.Context;

import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;

/**
 * Created by qxs on 2018/1/4.
 * 运行状态-故障-来自监视信息的故障状态
 */

public class FaultCondition {
    public char ReserveCommunication;  // 预留通信 D0  1 断开 0 正在通信
    public char HandsetCommunication;  // 手持机  D1  1 断开 0 正在通信
    public static final String KeyHandsetCommunication = "KeyHandsetCommunication";
    public char GNSSCommunication;     // GNSS   D2  1 断开 0 正在通信
    public static final String KeyGNSSCommunication = "KeyGNSSCommunication";
    public char BeaconCommunication;   // 信标机 D3  1 断开 0 正在通信
    public static final String KeyBeaconCommunication = "KeyBeaconommunication";
    public char DipCommunication;      // 倾角   D4  1 断开 0 正在通信
    public static final String KeyDipCommunication = "KeyDipCommunication";
    public char WifiCommunication;     //    wifi    D5  1 断开 0 正在通信
    public static final String KeyWifiCommunication = "KeyWifiCommunication";
    public char AmplifierCommunication; //    功放   D6  1 断开 0 正在通信
    public static final String KeyAmplifierCommunication = "KeyAmplifierCommunication";
    public char Reserve2Communication;   //    预留通信   D7  1 断开 0 正在通信
    public char AzimuthMotorStatus;     //    方位电机和编码器：D8 1 故障 0 正常
    public static final String KeyAzimuthMotorStatus = "KeyAzimuthMotorStatus";
    public char AzimuthHolzerStatus;    //    方位霍尔开关   D9  1故障  0正常
    public static final String KeyAzimuthHolzerStatus = "KeyAzimuthHolzerStatus";
    public char AzimuthLockHolzerStatus;  //    方位锁紧霍尔开关 D10 1故障 0 正常
    public static final String KeyAzimuthLockHolzerStatus = "KeyAzimuthLockHolzerStatus";
    public char PitchMotorStatus;      //    俯仰电机   D11  1故障 0 正常
    public static final String KeyPitchMotorStatus = "KeyPitchMotorStatus";
    public char PitchHolzerStatus;      //    俯仰霍尔开关D12  1故障 0正常
    public static final String KeyPitchHolzerStatus = "KeyPitchHolzerStatus";
    public char PitchLockHolzerStatus;  //    俯仰锁紧霍尔开关D13  1故障 0正常
    public static final String KeyPitchLockHolzerStatus = "KeyPitchLockHolzerStatus";
    public char PolMotorStatus;        //    极化电机D14  1故障 0正常
    public static final String KeyPolMotorStatus = "KeyPolMotorStatus";
    public char PolPotentiometerStatus;  //    极化电位计D15  1故障 0正常
    public static final String KeyPolPotentiometerStatus = "KeyPolPotentiometerStatus";
    //    D16~ D31:保留

    public static FaultCondition parseFaultCondition(Context context, String faultConditionString){
        FaultCondition faultCondition = new FaultCondition();
        String binaryString = Integer.toBinaryString(ConvertUtil.convertToInt(faultConditionString,0));
        faultCondition.ReserveCommunication = binaryString.charAt(0);
        faultCondition.HandsetCommunication = binaryString.charAt(1);
        CustomSP.putInt(context,KeyHandsetCommunication,faultCondition.HandsetCommunication);
        faultCondition.GNSSCommunication = binaryString.charAt(2);
        CustomSP.putInt(context,KeyGNSSCommunication,faultCondition.GNSSCommunication);
        faultCondition.BeaconCommunication = binaryString.charAt(3);
        CustomSP.putInt(context,KeyBeaconCommunication,faultCondition.BeaconCommunication);
        faultCondition.DipCommunication = binaryString.charAt(4);
        CustomSP.putInt(context,KeyDipCommunication,faultCondition.DipCommunication);
        faultCondition.WifiCommunication = binaryString.charAt(5);
        CustomSP.putInt(context,KeyWifiCommunication,faultCondition.WifiCommunication);
        faultCondition.AmplifierCommunication = binaryString.charAt(6);
        CustomSP.putInt(context,KeyAmplifierCommunication,faultCondition.AmplifierCommunication);
        faultCondition.Reserve2Communication = binaryString.charAt(7);
        faultCondition.AzimuthMotorStatus = binaryString.charAt(8);
        CustomSP.putInt(context,KeyAzimuthMotorStatus,faultCondition.AzimuthMotorStatus);
        faultCondition.AzimuthHolzerStatus = binaryString.charAt(9);
        CustomSP.putInt(context,KeyAzimuthHolzerStatus,faultCondition.AzimuthHolzerStatus);
        faultCondition.AzimuthLockHolzerStatus = binaryString.charAt(10);
        CustomSP.putInt(context,KeyAzimuthLockHolzerStatus,faultCondition.AzimuthLockHolzerStatus);
        faultCondition.PitchMotorStatus = binaryString.charAt(11);
        CustomSP.putInt(context,KeyPitchMotorStatus,faultCondition.PitchMotorStatus);
        faultCondition.PitchHolzerStatus = binaryString.charAt(12);
        CustomSP.putInt(context,KeyPitchHolzerStatus,faultCondition.PitchHolzerStatus);
        faultCondition.PitchLockHolzerStatus = binaryString.charAt(13);
        CustomSP.putInt(context,KeyPitchLockHolzerStatus,faultCondition.PitchLockHolzerStatus);
        faultCondition.PolMotorStatus = binaryString.charAt(14);
        CustomSP.putInt(context,KeyPolMotorStatus,faultCondition.PolMotorStatus);
        faultCondition.PolPotentiometerStatus = binaryString.charAt(15);
        CustomSP.putInt(context,KeyPolPotentiometerStatus,faultCondition.PolPotentiometerStatus);

        return faultCondition;
    }

    public static void setWifiCommunication(Context ctx, int wifiCommunication) {
        CustomSP.putInt(ctx, KeyWifiCommunication, wifiCommunication);
    }

    public static char getWifiCommunication(Context ctx) {
        return (char)CustomSP.getInt(ctx, KeyWifiCommunication,1);
    }

    public char getAmplifierCommunication(Context context){
        return (char)CustomSP.getInt(context,KeyAmplifierCommunication,0);
    }

    public char getAzimuthHolzerStatus(Context context) {
        return (char)CustomSP.getInt(context,KeyAzimuthHolzerStatus,0);
    }

    public char getAzimuthMotorStatus(Context context){
        return (char)CustomSP.getInt(context,KeyAzimuthMotorStatus,0);
    }
}
