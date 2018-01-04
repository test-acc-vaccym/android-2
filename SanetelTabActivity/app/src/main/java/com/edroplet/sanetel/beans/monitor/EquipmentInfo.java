package com.edroplet.sanetel.beans.monitor;

import android.content.Context;
import android.content.Intent;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.services.CommunicateWithDeviceService;
import com.edroplet.sanetel.services.communicate.CommunicateDataReceiver;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.sscanf.Sscanf;

import java.io.Serializable;

/**
 * Created by qxs on 2017/11/20.
 * 设备信息
 */

public class EquipmentInfo implements Serializable {
    public static final String EquipmentInfoAction = "com.edroplet.sanetel.EquipmentInfoAction";
    public static final String EquipmentInfoData = "com.edroplet.sanetel.EquipmentInfoData";

    private static final String KEY_productId = "monitor_info_productId";
    private static final String KEY_hardVer = "monitor_info_hardVer";
    private static final String KEY_softVer = "monitor_info_softVer";
    private static final String KEY_softReleaseTime = "monitor_info_softReleaseTime";
    private String productId; // 设备名称
    private String hardVer; // 主控板版本
    private String softVer; //软件版本
    private String softReleaseTime; // 发布时间

    private Context context;
    EquipmentInfo(Context context){
        this.context = context;
    }

    public String getHardVer() {
        hardVer = CustomSP.getString(context, KEY_hardVer,context.getString(R.string.main_status_version_hardware_version));
        return hardVer;
    }

    public String getProductId() {
        productId = CustomSP.getString(context, KEY_productId,context.getString(R.string.main_me_error_report_serial_number_hint));
        return productId;
    }

    public String getSoftReleaseTime() {
        softReleaseTime = CustomSP.getString(context, KEY_softReleaseTime,"2017-08-09");
        return softReleaseTime;
    }

    public String getSoftVer() {
        softReleaseTime = CustomSP.getString(context, KEY_softReleaseTime,context.getString(R.string.main_status_version_software_version));
        return softVer;
    }

    public static EquipmentInfo processEquipmentInfo(Context context, String data){
        EquipmentInfo equipmentInfo = new EquipmentInfo(context);
        Object[] objects = Sscanf.scan(data,Protocol.cmdGetEquipmentInfoResult,equipmentInfo.productId, equipmentInfo.hardVer,equipmentInfo.softVer, equipmentInfo.softReleaseTime);
        equipmentInfo.productId = (String) objects[0];
        CustomSP.putString(context, KEY_productId, equipmentInfo.productId);
        equipmentInfo.hardVer = (String) objects[1];
        CustomSP.putString(context, KEY_hardVer,equipmentInfo.hardVer);
        equipmentInfo.softVer = (String)objects[2];
        CustomSP.putString(context, KEY_softVer, equipmentInfo.softVer);
        equipmentInfo.softReleaseTime = (String)objects[3];
        CustomSP.putString(context, KEY_softReleaseTime, equipmentInfo.softReleaseTime);
        return equipmentInfo;
    }

}
