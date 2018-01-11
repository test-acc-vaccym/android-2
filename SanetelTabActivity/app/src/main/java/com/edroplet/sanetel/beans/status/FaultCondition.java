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
        setReserveCommunication(context,faultCondition.ReserveCommunication);
        faultCondition.HandsetCommunication = binaryString.charAt(1);
        setHandsetCommunication(context,faultCondition.HandsetCommunication);
        faultCondition.GNSSCommunication = binaryString.charAt(2);
        setGNSSCommunication(context,faultCondition.GNSSCommunication);
        faultCondition.BeaconCommunication = binaryString.charAt(3);
        setBeaconCommunication(context,faultCondition.BeaconCommunication);
        faultCondition.DipCommunication = binaryString.charAt(4);
        setDipCommunication(context,faultCondition.DipCommunication);
        faultCondition.WifiCommunication = binaryString.charAt(5);
        setWifiCommunication(context,faultCondition.WifiCommunication);
        faultCondition.AmplifierCommunication = binaryString.charAt(6);
        setAmplifierCommunication(context,faultCondition.AmplifierCommunication);
        faultCondition.Reserve2Communication = binaryString.charAt(7);
        setReserve2Communication(context,faultCondition.Reserve2Communication);
        faultCondition.AzimuthMotorStatus = binaryString.charAt(8);
        setAzimuthMotorStatus(context,faultCondition.AzimuthMotorStatus);
        faultCondition.AzimuthHolzerStatus = binaryString.charAt(9);
        setAzimuthHolzerStatus(context,faultCondition.AzimuthHolzerStatus);
        faultCondition.AzimuthLockHolzerStatus = binaryString.charAt(10);
        setAzimuthLockHolzerStatus(context,faultCondition.AzimuthLockHolzerStatus);
        faultCondition.PitchMotorStatus = binaryString.charAt(11);
        setPitchMotorStatus(context,faultCondition.PitchMotorStatus);
        faultCondition.PitchHolzerStatus = binaryString.charAt(12);
        setPitchHolzerStatus(context,faultCondition.PitchHolzerStatus);
        faultCondition.PitchLockHolzerStatus = binaryString.charAt(13);
        setPitchLockHolzerStatus(context,faultCondition.PitchLockHolzerStatus);
        faultCondition.PolMotorStatus = binaryString.charAt(14);
        setPolMotorStatus(context,faultCondition.PolMotorStatus);
        faultCondition.PolPotentiometerStatus = binaryString.charAt(15);
        setPolPotentiometerStatus(context,faultCondition.PolPotentiometerStatus);

        return faultCondition;
    }

    public static char getWifiCommunication(Context ctx) {
        return (char)CustomSP.getInt(ctx, KeyWifiCommunication,1);
    }

    public static char
    getAmplifierCommunication(Context context) {
        return CustomSP.getChar(context, KeyAmplifierCommunication, 0);
    }

    public static char
    getAzimuthHolzerStatus(Context context) {
        return CustomSP.getChar(context, KeyAzimuthHolzerStatus, 0);
    }

    public static char getAzimuthMotorStatus(Context context){
        return (char)CustomSP.getInt(context,KeyAzimuthMotorStatus,0);
    }

    public static char
    getHandsetCommunication(Context context) {
        return CustomSP.getChar(context, KeyHandsetCommunication, 0);
    }

    public static char
    getBeaconCommunication(Context context) {
        return CustomSP.getChar(context, KeyBeaconCommunication, 0);
    }

    private static void setReserveCommunication(Context context, char reserveCommunication) {
        CustomSP.putChar(context, KeyReserveCommunication, reserveCommunication);
    }

    public static char
    getDipCommunication(Context context) {
        return CustomSP.getChar(context, KeyDipCommunication, 0);
    }

    public static char
    getAzimuthLockHolzerStatus(Context context) {
        return CustomSP.getChar(context, KeyAzimuthLockHolzerStatus, 0);
    }

    public static char
    getGNSSCommunication(Context context) {
        return CustomSP.getChar(context, KeyGNSSCommunication, 0);
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

    private static void setWifiCommunication(Context context, char wifiCommunication) {
        CustomSP.putChar(context, KeyWifiCommunication, wifiCommunication);
    }

    private static void setAmplifierCommunication(Context context, char amplifierCommunication) {
        CustomSP.putChar(context, KeyAmplifierCommunication, amplifierCommunication);
    }

    public static char
    getReserveCommunication(Context context) {
        return CustomSP.getChar(context, KeyReserveCommunication, 0);
    }

    public static char
    getReserve2Communication(Context context) {
        return CustomSP.getChar(context, KeyReserve2Communication, 0);
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

    private static void setBeaconCommunication(Context context, char beaconCommunication) {
        CustomSP.putChar(context, KeyBeaconCommunication, beaconCommunication);
    }

    private static void setDipCommunication(Context context, char dipCommunication) {
        CustomSP.putChar(context, KeyDipCommunication, dipCommunication);
    }

    private static void setGNSSCommunication(Context context, char GNSSCommunication) {
        CustomSP.putChar(context, KeyGNSSCommunication, GNSSCommunication);
    }

    private static void setHandsetCommunication(Context context, char handsetCommunication) {
        CustomSP.putChar(context, KeyHandsetCommunication, handsetCommunication);
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

    private static void setReserve2Communication(Context context, char reserve2Communication) {
        CustomSP.putChar(context, KeyReserve2Communication, reserve2Communication);
    }
}
