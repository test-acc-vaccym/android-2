package com.edroplet.qxx.saneteltabactivity.beans.monitor;

import android.content.Context;
import android.content.Intent;

import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.services.CommunicateWithDeviceService;
import com.edroplet.qxx.saneteltabactivity.services.communicate.CommunicateDataReceiver;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;

import java.io.Serializable;

/**
 * Created by qxs on 2017/11/20.
 */

public class DeviceInfo implements Serializable {
    private static final String KEY_productId = "monitor_info_productId";
    private static final String KEY_hardVer = "monitor_info_hardVer";
    private static final String KEY_softVer = "monitor_info_softVer";
    private static final String KEY_softReleaseTime = "monitor_info_softReleaseTime";
    private String productId;
    private String hardVer; // 主控板版本
    private String softVer; //软件版本
    private String softReleaseTime; // 发布时间

    private Context context;
    DeviceInfo(Context context){
        this.context = context;
    }

    public String getHardVer() {
        hardVer = CustomSP.getString(context, KEY_hardVer,"P120-3-V1.0");
        return hardVer;
    }

    public String getProductId() {
        productId = CustomSP.getString(context, KEY_productId,"XWWT-P120_201708001");
        return productId;
    }

    public String getSoftReleaseTime() {
        softReleaseTime = CustomSP.getString(context, KEY_softReleaseTime,"2017-08-09");
        return softReleaseTime;
    }

    public String getSoftVer() {
        softVer = CustomSP.getString(context, KEY_softVer,"P120-3-V1.00");
        return softVer;
    }

    public void setHardVer(String hardVer) {
        CustomSP.putString(context, KEY_hardVer,hardVer);
        this.hardVer = hardVer;
    }

    public void setProductId(String productId) {
        CustomSP.putString(context, KEY_productId, productId);
        this.productId = productId;
    }

    public void setSoftReleaseTime(String softReleaseTime) {
        CustomSP.putString(context, KEY_softReleaseTime, softReleaseTime);
        this.softReleaseTime = softReleaseTime;
    }

    public void setSoftVer(String softVer) {
        CustomSP.putString(context, KEY_softVer, softVer);
        this.softVer = softVer;
    }

    public static void getDeviceInfo(Context context){
        Intent intent = new Intent(CommunicateDataReceiver.ACTION_RECEIVE_DATA);
        intent.putExtra(CommunicateWithDeviceService.EXTRA_PARAM_SEND_CMD, Protocol.cmdGeteEquipmentInfo);
        context.sendBroadcast(intent);
    }
}
