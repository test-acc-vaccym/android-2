package com.edroplet.sanetel.beans.status;

import android.content.Context;

import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;

/**
 * Created by qxs on 2018/1/4.
 * 运行状态-通信状态-来自监视信息的通信状态
 */

public class CommunicationCondition {
    private char HandsetCommunication;  // 手持机  D0D1  1 断开 0 正在通信
    private static final String KeyHandsetCommunication = "KeyHandsetCommunication";
    private char GNSSCommunication;     // GNSS D2D3    1 断开 0 正在通信
    private static final String KeyGNSSCommunication = "KeyGNSSCommunication";
    private char BeaconCommunication;   // 信标机 D4D5   1 断开 0 正在通信
    private static final String KeyBeaconCommunication = "KeyBeaconommunication";
    private char DipCommunication;      // 倾角  D6D7   1 断开 0 正在通信
    private static final String KeyDipCommunication = "KeyDipCommunication";
    private char WifiCommunication;     //    wifi   D8D9   1 断开 0 正在通信
    private static final String KeyWifiCommunication = "KeyWifiCommunication";
    private char AmplifierCommunication; //    功放   D10D11  1 断开 0 正在通信
    private static final String KeyAmplifierCommunication = "KeyAmplifierCommunication";
    //    D12~ D31:保留


    public static int bytesToShort(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8));
        return value;
    }


    public static char getValue(String binaryString, int srcPos, int destPos){
        char dst = 0;
        for (int i = srcPos; i<= destPos; i++) {
            dst += (char)( binaryString.charAt(i) - (char) 48);
        }
        return  dst;
    }

    public static CommunicationCondition parseCommunicationCondition(Context context, String communicateConditionString){
        CommunicationCondition communicateCondition = new CommunicationCondition();
        String binaryString = Integer.toBinaryString(ConvertUtil.convertToInt(communicateConditionString,0));

        communicateCondition.HandsetCommunication = getValue(binaryString, 0, 1);
        setHandsetCommunication(context,communicateCondition.HandsetCommunication);
        communicateCondition.GNSSCommunication = getValue(binaryString, 2, 3);
        setGNSSCommunication(context,communicateCondition.GNSSCommunication);
        communicateCondition.BeaconCommunication = getValue(binaryString, 4, 5);
        setBeaconCommunication(context,communicateCondition.BeaconCommunication);
        communicateCondition.DipCommunication = getValue(binaryString, 6, 7);
        setDipCommunication(context,communicateCondition.DipCommunication);
        communicateCondition.WifiCommunication = getValue(binaryString, 8, 9);
        setWifiCommunication(context,communicateCondition.WifiCommunication);
        communicateCondition.AmplifierCommunication = getValue(binaryString, 10, 11);
        setAmplifierCommunication(context,communicateCondition.AmplifierCommunication);
        
        return communicateCondition;
    }

    public static char getWifiCommunication(Context ctx) {
        return (char)CustomSP.getInt(ctx, KeyWifiCommunication,1);
    }

    public static char
    getAmplifierCommunication(Context context) {
        return CustomSP.getChar(context, KeyAmplifierCommunication, 0);
    }

    public static char
    getHandsetCommunication(Context context) {
        return CustomSP.getChar(context, KeyHandsetCommunication, 0);
    }

    public static char
    getBeaconCommunication(Context context) {
        return CustomSP.getChar(context, KeyBeaconCommunication, 0);
    }

    public static char
    getDipCommunication(Context context) {
        return CustomSP.getChar(context, KeyDipCommunication, 0);
    }

    public static char
    getGNSSCommunication(Context context) {
        return CustomSP.getChar(context, KeyGNSSCommunication, 0);
    }

    private static void setWifiCommunication(Context context, char wifiCommunication) {
        CustomSP.putChar(context, KeyWifiCommunication, wifiCommunication);
    }

    private static void setAmplifierCommunication(Context context, char amplifierCommunication) {
        CustomSP.putChar(context, KeyAmplifierCommunication, amplifierCommunication);
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

}
