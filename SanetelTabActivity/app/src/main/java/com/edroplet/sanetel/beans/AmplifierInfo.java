package com.edroplet.sanetel.beans;

import android.content.Context;
import android.content.Intent;

import com.edroplet.sanetel.services.CommunicateWithDeviceService;
import com.edroplet.sanetel.services.communicate.CommunicateDataReceiver;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.sscanf.Sscanf;

import java.io.Serializable;

/**
 * Created by qxs on 2017/9/15.
 * 功放信息
 */

public class AmplifierInfo implements Serializable {
    private String amplifierFactory; // 功放厂家
    private String amplifierOscillator; // 功放本振
    private String amplifierOutput; // 输出功率
    private String amplifierTemperature; // 温度
    private String amplifierState; // 状态
    /**
        Bit0;		//0温控
        Bit1;		//1锁相环
        Bit2;		//2功放数据有效状态
        Bit3;		//3开关状态
     */
    private String amplifierProtectStatus; // 邻星保护状态
    private String amplifierAttenuation; // 功放衰减

    static Context context;
    public static AmplifierInfo getInstance(Context ctx) {
        context = ctx;
        Intent intent = new Intent(CommunicateDataReceiver.ACTION_RECEIVE_DATA);
        intent.putExtra(CommunicateWithDeviceService.EXTRA_PARAM_SEND_CMD, Protocol.cmdGetBucInfo);
        context.sendBroadcast(intent);
        return null;
    }

    public static AmplifierInfo parseAmplifierInfo(String src){

        AmplifierInfo amplifierInfo = new AmplifierInfo();
        Object o[] =  Sscanf.scan(src, Protocol.cmdGetBucInfoResult,
                amplifierInfo.amplifierFactory, amplifierInfo.amplifierOscillator,amplifierInfo.amplifierOutput,amplifierInfo.amplifierTemperature,
                amplifierInfo.amplifierState,amplifierInfo.amplifierProtectStatus,amplifierInfo.amplifierAttenuation);
        amplifierInfo.amplifierFactory = String.valueOf(o[0]);
        amplifierInfo.amplifierOscillator = String.valueOf(o[1]);
        amplifierInfo.amplifierOutput = String.valueOf(o[2]);
        amplifierInfo.amplifierTemperature = String.valueOf(o[3]);
        amplifierInfo.amplifierState = String.valueOf(o[4]);
        amplifierInfo.amplifierProtectStatus = String.valueOf(o[5]);
        amplifierInfo.amplifierAttenuation = String.valueOf(o[6]);
        return amplifierInfo;
    }

    public String getAmplifierAttenuation() {
        return amplifierAttenuation;
    }

    public String getAmplifierFactory() {
        return amplifierFactory;
    }

    public String getAmplifierOscillator() {
        return amplifierOscillator;
    }

    public static void setContext(Context context) {
        AmplifierInfo.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public String getAmplifierOutput() {
        return amplifierOutput;
    }

    public String getAmplifierPLLStatus() {
        // String s=Integer.toBinaryString(ConvertUtil.convertToInt(amplifierState,0)); // 二进制字符串
        //取第 1 位
        // return String.valueOf(s.charAt(s.length() - 1 - 1));
        return String.valueOf(ConvertUtil.getBitValue(amplifierState,1,0));
    }

    public String getAmplifierProtectStatus() {
        // 0：关1：开
        return amplifierProtectStatus;
    }

    public String getAmplifierState() {
        //取第 2 位
        return String.valueOf(ConvertUtil.getBitValue(amplifierState,2,0));
    }

    public String getAmplifierTemperature() {
        return amplifierTemperature;
    }

    public String getAmplifierTemperatureState() {
        //取第 0 位
        return String.valueOf(ConvertUtil.getBitValue(amplifierState,0,0));
    }
}
