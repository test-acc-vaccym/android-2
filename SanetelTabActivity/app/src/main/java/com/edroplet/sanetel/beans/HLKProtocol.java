package com.edroplet.sanetel.beans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.edroplet.sanetel.services.network.UdpSendReceive;

import static com.edroplet.sanetel.services.CommunicateWithDeviceService.EXTRA_PARAM_SEND_CMD;
import static com.edroplet.sanetel.services.communicate.CommunicateDataReceiver.ACTION_RECEIVE_DATA;

/**
 * Created on 2018/3/11.
 *
 * @author qxs
 */

public class HLKProtocol {
    public static final String Host = "192.168.5.111";
    public static final int Port = 988;

    public static final String LocalHost = "0.0.0.0";
    public static final int LocalPort = 2000;

    public static final String listenIPKey = "com.edroplet.sanetel.service.listenIPKey";
    public static final String listenPortKey = "com.edroplet.sanetel.service.listenPortKey";
    public static final String destIPKey = "com.edroplet.sanetel.service.destIPKey";
    public static final String destPortKey = "com.edroplet.sanetel.service.destPortKey";
    public static final String messagesKey = "com.edroplet.sanetel.service.messagesKey";
    public static final String expectedKey = "com.edroplet.sanetel.service.expectedKey";

    public static final String UdpEnable = "hlkATat+UdpAtEn=1\r\n";
    public static final String UdpEnableResponse = "at+RUdpAtEn=1\r\n";
    public static final String UdpRLANIp = "hlkATat+LANIp=%s\r\n";
    public static final String UdpRLANIpResponse = "at+RLANIp=%s\r\n";
    public static final String UdpLANIpMask = "hlkATat+LANIpMask=%s\r\n";
    public static final String UdpLANIpMaskResponse = "at+RLANIpMask=%s\r\n";
    public static final String UdpSave = "hlkATat+Save=1\r\n";
    public static final String UdpSaveResponse = "at+RSave=1\r\n";
    public static final String UdpApply = "hlkATat+Apply=1\r\n";
    public static final String UdpApplyResponse = "at+RApply=1\r\n";

    public static final String UdpAPSsid = "hlkATat+APSsid=%s\r\n";
    public static final String UdpAPSsidResponse = "at+RAPSsid=%s\r\n";
    public static final String UdpNPackLen1 = "hlkATat+NPackLen1=350\r\n";
    public static final String UdpNPackLen1Response = "at+RNPackLen1=350\r\n";

    public static final String UdpGetAPSsid = "hlkATat+APSsid=?\r\n";
    public static final String UdpGetAPSsidResponse = "at+RAPSsid=%s\r\n";
    public static final String UdpGetLANIp = "hlkATat+LANIp=?\r\n";
    public static final String UdpGetLANIpResponse = "at+RLANIp=%s\r\n";
    public static final String UdpGetLANIpMask = "hlkATat+LANIpMask=?\r\n";
    public static final String UdpGetLANIpMaskResponse = "at+RLANIpMask=%s\r\n";


    public static void sendUdpMessage(Context context, String listenIP, int listenPort, String  destIP, int destPort, String[] sendMessage, String expected[]){
        Intent intent = new Intent(context, UdpSendReceive.class);
        Bundle bundle = new Bundle();
        bundle.putString(listenIPKey, listenIP);
        bundle.putInt(listenPortKey, listenPort);
        bundle.putString(destIPKey, destIP);
        bundle.putInt(destPortKey, destPort);
        bundle.putStringArray(messagesKey, sendMessage);
        bundle.putStringArray(expectedKey, expected);
        intent.replaceExtras(bundle);
        context.startService(intent);
    }

}
