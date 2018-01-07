package com.edroplet.sanetel.beans.status;

import android.content.Context;

import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;

/**
 * Created by qxs on 2018/1/4.
 * 运行状态-故障-来自监视信息的故障状态
 */

public class FaultCondition {
    private char ReserveCommunication;  // 预留通信 D0  1 断开 0 正在通信
    private static final String KeyReserveCommunication = "KeyReserveCommunication";

    private char HandsetCommunication;  // 手持机  D1  1 断开 0 正在通信
    private static final String KeyHandsetCommunication = "KeyHandsetCommunication";
    private char GNSSCommunication;     // GNSS   D2  1 断开 0 正在通信
    private static final String KeyGNSSCommunication = "KeyGNSSCommunication";
    private char BeaconCommunication;   // 信标机 D3  1 断开 0 正在通信
    private static final String KeyBeaconCommunication = "KeyBeaconommunication";
    private char DipCommunication;      // 倾角   D4  1 断开 0 正在通信
    private static final String KeyDipCommunication = "KeyDipCommunication";
    private char WifiCommunication;     //    wifi    D5  1 断开 0 正在通信
    private static final String KeyWifiCommunication = "KeyWifiCommunication";
    private char AmplifierCommunication; //    功放   D6  1 断开 0 正在通信
    private static final String KeyAmplifierCommunication = "KeyAmplifierCommunication";
    private char Reserve2Communication;   //    预留通信   D7  1 断开 0 正在通信
    private static final String KeyReserve2Communication = "KeyReserve2Communication";
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

    public void setWifiCommunication(Context ctx, int wifiCommunication) {
        CustomSP.putInt(ctx, KeyWifiCommunication, wifiCommunication);
    }

    public char getWifiCommunication(Context ctx) {
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

    public char
    getHandsetCommunication(Context context) {
        char defaultVal = 0;
        HandsetCommunication = CustomSP.getChar(context, KeyHandsetCommunication, defaultVal);
        return HandsetCommunication;
    }

    public char
    getBeaconCommunication(Context context) {
        char defaultVal = 0;
        BeaconCommunication = CustomSP.getChar(context, KeyBeaconCommunication, defaultVal);
        return BeaconCommunication;
    }

    public char
    getDipCommunication(Context context) {
        char defaultVal = 0;
        DipCommunication = CustomSP.getChar(context, KeyDipCommunication, defaultVal);
        return DipCommunication;
    }

    public char
    getAzimuthLockHolzerStatus(Context context) {
        char defaultVal = 0;
        AzimuthLockHolzerStatus = CustomSP.getChar(context, KeyAzimuthLockHolzerStatus, defaultVal);
        return AzimuthLockHolzerStatus;
    }

    public char
    getGNSSCommunication(Context context) {
        char defaultVal = 0;
        GNSSCommunication = CustomSP.getChar(context, KeyGNSSCommunication, defaultVal);
        return GNSSCommunication;
    }

    public char
    getPitchHolzerStatus(Context context) {
        char defaultVal = 0;
        PitchHolzerStatus = CustomSP.getChar(context, KeyPitchHolzerStatus, defaultVal);
        return PitchHolzerStatus;
    }

    public char
    getPitchLockHolzerStatus(Context context) {
        char defaultVal = 0;
        PitchLockHolzerStatus = CustomSP.getChar(context, KeyPitchLockHolzerStatus, defaultVal);
        return PitchLockHolzerStatus;
    }

    public char
    getPitchMotorStatus(Context context) {
        char defaultVal = 0;
        PitchMotorStatus = CustomSP.getChar(context, KeyPitchMotorStatus, defaultVal);
        return PitchMotorStatus;
    }

    public char
    getPolMotorStatus(Context context) {
        char defaultVal = 0;
        PolMotorStatus = CustomSP.getChar(context, KeyPolMotorStatus, defaultVal);
        return PolMotorStatus;
    }

    public char
    getPolPotentiometerStatus(Context context) {
        char defaultVal = 0;
        PolPotentiometerStatus = CustomSP.getChar(context, KeyPolPotentiometerStatus, defaultVal);
        return PolPotentiometerStatus;
    }

    public void setWifiCommunication(Context context, char wifiCommunication) {
        CustomSP.putChar(context, KeyWifiCommunication, wifiCommunication);
        WifiCommunication = wifiCommunication;
    }

    public void setAmplifierCommunication(Context context, char amplifierCommunication) {
        CustomSP.putChar(context, KeyAmplifierCommunication, amplifierCommunication);
        AmplifierCommunication = amplifierCommunication;
    }

    public char
    getReserveCommunication(Context context) {
        char defaultVal = 0;
        ReserveCommunication = CustomSP.getChar(context, KeyReserveCommunication, defaultVal);
        return ReserveCommunication;
    }

    public char
    getReserve2Communication(Context context) {
        char defaultVal = 0;
        Reserve2Communication = CustomSP.getChar(context, KeyReserve2Communication, defaultVal);
        return Reserve2Communication;
    }
}
