package com.edroplet.sanetel.beans.monitor;

import android.content.Context;
import android.content.Intent;

import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.status.FaultCondition;
import com.edroplet.sanetel.beans.status.RunningInfo;
import com.edroplet.sanetel.services.CommunicateWithDeviceService;
import com.edroplet.sanetel.services.communicate.CommunicateDataReceiver;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.sscanf.Sscanf;

import java.io.Serializable;

/**
 * Created by qxs on 2017/11/20.
 * 监视信息，发送广播，然后接收广播， 这里只定义数据内容，不接收数据，在UI中接收数据，因为刷新UI跟UI相关。
 */

public class MonitorInfo implements Serializable {
    public static final String MonitorInfoAction="com.edroplet.broadcast.MonitorInfoAction";

    public static final String MonitorInfoData="com.edroplet.broadcast.MonitorInfoData";


    public static final String KEY_MONITOR_LAST_ANTENNA_PRE_AZ = "KEY_MONITOR_LAST_ANTENNA_PRE_AZ";
    public static final String KEY_MONITOR_LAST_ANTENNA_PRE_EL = "KEY_MONITOR_LAST_ANTENNA_PRE_EL";
    public static final String KEY_MONITOR_LAST_ANTENNA_PRE_RV = "KEY_MONITOR_LAST_ANTENNA_PRE_RV";
    public static final String KEY_MONITOR_LAST_ANTENNA_PRE_POL = "KEY_MONITOR_LAST_ANTENNA_PRE_POL";
    public static final String KEY_MONITOR_LAST_ANTENNA_AZ = "KEY_MONITOR_LAST_ANTENNA_AZ";
    public static final String KEY_MONITOR_LAST_ANTENNA_EL = "KEY_MONITOR_LAST_ANTENNA_EL";
    public static final String KEY_MONITOR_LAST_ANTENNA_RV = "KEY_MONITOR_LAST_ANTENNA_RV";
    public static final String KEY_MONITOR_LAST_ANTENNA_POL = "KEY_MONITOR_LAST_ANTENNA_POL";

    public static final String KEY_MONITOR_LAST_TRACE_STATE = "KEY_MONITOR_LAST_TRACE_STATE";
    public static final String KEY_MONITOR_LAST_GNSS_STATE = "KEY_MONITOR_LAST_GNSS_STATE";

    public static final String KEY_MONITOR_LAST_LONGITUDE = "KEY_MONITOR_LAST_LONGITUDE";
    public static final String KEY_MONITOR_LAST_LATITUDE = "KEY_MONITOR_LAST_LATITUDE";
    public static final String KEY_MONITOR_LAST_HEIGHT = "KEY_MONITOR_LAST_HEIGHT";

    public static final String KEY_MONITOR_LAST_SATELLITE_TRACE_MODE = "KEY_MONITOR_LAST_SATELLITE_TRACE_MODE";
    public static final String KEY_MONITOR_LAST_SATELLITE_AGC = "KEY_MONITOR_LAST_AGC";
    public static final String KEY_MONITOR_LAST_SATELLITE_LONGITUDE = "KEY_MONITOR_LAST_SATELLITE_LONGITUDE";
    public static final String KEY_MONITOR_LAST_SATELLITE_POLARIZATION_MODE = "KEY_MONITOR_LAST_SATELLITE_POLARIZATION_MODE";
    public static final String KEY_MONITOR_LAST_SATELLITE_THRESHOLD = "KEY_MONITOR_LAST_SATELLITE_THRESHOLD";
    public static final String KEY_MONITOR_LAST_SATELLITE_BEACON = "KEY_MONITOR_LAST_SATELLITE_BEACON";
    public static final String KEY_MONITOR_LAST_SATELLITE_CARRIER = "KEY_MONITOR_LAST_SATELLITE_CARRIER";
    public static final String KEY_MONITOR_LAST_SATELLITE_DVB = "KEY_MONITOR_LAST_SATELLITE_DVB";
    public static final String KEY_MONITOR_LAST_FAULT_CONDITION = "KEY_MONITOR_LAST_FAULT_CONDITION";
    public static final String KEY_MONITOR_LAST_FLAG = "KEY_MONITOR_LAST_FLAG";


    private float prepareAZ; // 预置方位角  “AZ”是方位角的英文azimuth缩写;
    private float prepareEL; // 俯仰角 “EL”是俯仰角的英文elevation缩写;
    private float prepareRV; // “RV”是备用的英文reserve缩写
    private float preparePOL; // “POL”是极化角的英文polarization缩写
    private float AZ;
    private float EL;
    private float RV;
    private float POL;

    private int traceState; // 寻星状态 0—收藏；1—展开中；2—已展开；3—初始；4--寻星；5—手动；6—锁定；7—收藏中；8—失锁9---异常；10--停止；11—复位中；12—已复位
    private float longitude; // -180~180
    private float latitude; // -90 ~ 90
    private float height; // 本地高度
    private int gnssState;  // GNSS状态

    private int traceMode; // 寻星模式  0-信标机；1-DVB；
    private float satelliteLongitude; // 卫星经度
    private int plMode; // 极化方式 0-水平极化；1-垂直极化；2-左旋圆极化； 3-右旋圆极化；
    private float threshold; // 寻星门限
    private float agc; // AGC
    private float beacon; // 信标频率
    private float carrier; // 载波
    private float dvb; // 符号率

    private int faultCondition; // 故障状态
    private int flag; // 标志

    public static void getMonitorInfoFromServer(Context context){
        Intent intent = new Intent(CommunicateDataReceiver.ACTION_RECEIVE_DATA);
        intent.putExtra(CommunicateWithDeviceService.EXTRA_PARAM_SEND_CMD, Protocol.cmdGetSystemState);
        context.sendBroadcast(intent);
    }

    public static MonitorInfo parseMonitorInfo(Context context, String src){
        MonitorInfo monitorInfo = new MonitorInfo();
        Object o[] =  Sscanf.scan(src, Protocol.cmdGetSystemStateResult,
                monitorInfo.prepareAZ, monitorInfo.prepareEL,monitorInfo.prepareRV,monitorInfo.preparePOL,
                monitorInfo.AZ,monitorInfo.EL,monitorInfo.RV,monitorInfo.POL,
                monitorInfo.traceState,monitorInfo.latitude, monitorInfo.longitude, monitorInfo.height,
                monitorInfo.gnssState,monitorInfo.traceMode, monitorInfo.satelliteLongitude,monitorInfo.plMode,
                monitorInfo.threshold, monitorInfo.agc, monitorInfo.beacon,monitorInfo.carrier,
                monitorInfo.dvb, monitorInfo.faultCondition, monitorInfo.flag);

        monitorInfo.prepareAZ = (Float) o[0];
        CustomSP.putString(context,KEY_MONITOR_LAST_ANTENNA_PRE_AZ, String.valueOf(monitorInfo.prepareAZ));
        monitorInfo.prepareEL = (Float) o[1];
        CustomSP.putString(context,KEY_MONITOR_LAST_ANTENNA_PRE_EL, String.valueOf(monitorInfo.prepareEL));
        monitorInfo.prepareRV = (Float) o[2];
        CustomSP.putString(context,KEY_MONITOR_LAST_ANTENNA_PRE_RV, String.valueOf(monitorInfo.prepareRV));
        monitorInfo.preparePOL = (Float) o[3];
        CustomSP.putString(context,KEY_MONITOR_LAST_ANTENNA_PRE_POL, String.valueOf(monitorInfo.preparePOL));

        monitorInfo.AZ = (Float) o[4];
        CustomSP.putString(context,KEY_MONITOR_LAST_ANTENNA_AZ, String.valueOf(monitorInfo.AZ));
        monitorInfo.EL = (Float) o[5];
        CustomSP.putString(context,KEY_MONITOR_LAST_ANTENNA_EL, String.valueOf(monitorInfo.EL));
        monitorInfo.RV = (Float) o[6];
        CustomSP.putString(context,KEY_MONITOR_LAST_ANTENNA_RV, String.valueOf(monitorInfo.RV));
        monitorInfo.POL = (Float) o[7];
        CustomSP.putString(context,KEY_MONITOR_LAST_ANTENNA_POL, String.valueOf(monitorInfo.POL));

        monitorInfo.traceState = (Integer) o[8];
        CustomSP.putInt(context,KEY_MONITOR_LAST_TRACE_STATE, monitorInfo.traceState);
        monitorInfo.latitude = (Float) o[9];
        CustomSP.putString(context,KEY_MONITOR_LAST_LATITUDE, String.valueOf(monitorInfo.latitude));
        monitorInfo.longitude = (Float) o[10];
        CustomSP.putString(context,KEY_MONITOR_LAST_LONGITUDE, String.valueOf(monitorInfo.longitude));
        monitorInfo.height = (Float) o[11];
        CustomSP.putString(context,KEY_MONITOR_LAST_HEIGHT, String.valueOf(monitorInfo.height));

        monitorInfo.gnssState = (Integer) o[12];
        CustomSP.putInt(context,KEY_MONITOR_LAST_GNSS_STATE, monitorInfo.gnssState);
        monitorInfo.traceMode = (Integer) o[13];
        CustomSP.putInt(context,KEY_MONITOR_LAST_SATELLITE_TRACE_MODE, monitorInfo.traceMode);
        monitorInfo.satelliteLongitude = (Float) o[14];
        CustomSP.putString(context,KEY_MONITOR_LAST_SATELLITE_LONGITUDE, String.valueOf(monitorInfo.satelliteLongitude));
        monitorInfo.plMode = (Integer) o[15];
        CustomSP.putInt(context,KEY_MONITOR_LAST_SATELLITE_POLARIZATION_MODE, monitorInfo.plMode);

        monitorInfo.threshold = (Float) o[16];
        CustomSP.putString(context,KEY_MONITOR_LAST_SATELLITE_THRESHOLD, String.valueOf(monitorInfo.threshold));
        monitorInfo.agc = (Float) o[17];
        CustomSP.putString(context,KEY_MONITOR_LAST_SATELLITE_AGC, String.valueOf(monitorInfo.agc));
        monitorInfo.beacon = (Float) o[18];
        CustomSP.putString(context,KEY_MONITOR_LAST_SATELLITE_BEACON, String.valueOf(monitorInfo.beacon));
        monitorInfo.carrier = (Float) o[19];
        CustomSP.putString(context,KEY_MONITOR_LAST_SATELLITE_CARRIER, String.valueOf(monitorInfo.carrier));
        monitorInfo.dvb = (Float) o[20];
        CustomSP.putString(context,KEY_MONITOR_LAST_SATELLITE_DVB, String.valueOf(monitorInfo.dvb));

        monitorInfo.faultCondition = (Integer) o[21];
        FaultCondition.parseFaultCondition(context, String.valueOf(monitorInfo.faultCondition));
        monitorInfo.flag = (Integer) o[22];
        RunningInfo.parseRunningInfo(String.valueOf(monitorInfo.flag));

        return monitorInfo;
    }

    public float getAgc() {
        return agc;
    }

    public float getAZ() {
        return AZ;
    }

    public float getBeacon() {
        return beacon;
    }

    public float getCarrier() {
        return carrier;
    }

    public float getDvb() {
        return dvb;
    }

    public float getEL() {
        return EL;
    }

    public float getHeight() {
        return height;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getPOL() {
        return POL;
    }

    public float getPrepareAZ() {
        return prepareAZ;
    }

    public float getPrepareEL() {
        return prepareEL;
    }

    public float getPreparePOL() {
        return preparePOL;
    }

    public float getPrepareRV() {
        return prepareRV;
    }

    public float getRV() {
        return RV;
    }

    public float getSatelliteLogitude() {
        return satelliteLongitude;
    }

    public float getThreshold() {
        return threshold;
    }

    public int getBdState() {
        return gnssState;
    }
    // 故障状态
    public int getFaultCondition() {
        return faultCondition;
    }

    public int getFlag() {
        return flag;
    }

    public int getPlMode() {
        return plMode;
    }

    public int getTraceMode() {
        return traceMode;
    }

    public int getTraceState() {
        return traceState;
    }
}
