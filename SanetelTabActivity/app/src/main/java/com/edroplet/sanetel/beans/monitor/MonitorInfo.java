package com.edroplet.sanetel.beans.monitor;

import android.content.Context;

import com.edroplet.sanetel.beans.AntennaInfo;
import com.edroplet.sanetel.beans.LocationInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.beans.status.FaultCondition;
import com.edroplet.sanetel.beans.status.RunningInfo;
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


    private static final String KeyPrepareAZ = "KeyPrepareAZ";
    private static final String KeyPrepareEL = "KeyPrepareEL";
    private static final String KeyPrepareRV = "KeyPrepareRV";
    private static final String KeyPreparePOL = "KeyPreparePOL";
    private static final String KeyAZ = "KeyAZ";
    private static final String KeyEL = "KeyEL";
    private static final String KeyRV = "KeyRV";
    private static final String KeyPOL = "KeyPOL";

    private static final String KeyTraceState = "KeyTraceState";
    private static final String KeyGnssState = "KeyGnssState";

    private static final String KeyLongitude = "KeyLongitude";
    private static final String KeyLatitude = "KeyLatitude";
    private static final String KeyHeight = "KeyHeight";

    private static final String KeyTraceMode = "KeyTraceMode";
    private static final String KeyAgc = "KEY_MONITOR_LAST_AGC";
    private static final String KeySatelliteLongitude = "KeySatelliteLongitude";
    private static final String KeyPlMode = "KeyPlMode";
    private static final String KeyThreshold = "KeyThreshold";
    private static final String KeyBeacon = "KeyBeacon";
    private static final String KeyCarrier = "KeyCarrier";
    private static final String KeyDvb = "KeyDvb";
    private static final String KeyFaultCondition = "KeyFaultCondition";
    private static final String KeyFlag = "KeyFlag";


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
        Protocol.sendMessage(context,Protocol.cmdGetSystemState);
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
        CustomSP.putString(context,KeyPrepareAZ, String.valueOf(monitorInfo.prepareAZ));
        monitorInfo.prepareEL = (Float) o[1];
        CustomSP.putString(context,KeyPrepareEL, String.valueOf(monitorInfo.prepareEL));
        monitorInfo.prepareRV = (Float) o[2];
        CustomSP.putString(context,KeyPrepareRV, String.valueOf(monitorInfo.prepareRV));
        monitorInfo.preparePOL = (Float) o[3];
        CustomSP.putString(context,KeyPreparePOL, String.valueOf(monitorInfo.preparePOL));

        monitorInfo.AZ = (Float) o[4];
        CustomSP.putString(context,KeyAZ, String.valueOf(monitorInfo.AZ));
        AntennaInfo antennaInfo = new AntennaInfo();
        antennaInfo.setAzimuth(context, monitorInfo.AZ);
        monitorInfo.EL = (Float) o[5];
        CustomSP.putString(context,KeyEL, String.valueOf(monitorInfo.EL));
        antennaInfo.setPitch(context, monitorInfo.EL);
        monitorInfo.RV = (Float) o[6];
        CustomSP.putString(context,KeyRV, String.valueOf(monitorInfo.RV));
        monitorInfo.POL = (Float) o[7];
        CustomSP.putString(context,KeyPOL, String.valueOf(monitorInfo.POL));
        antennaInfo.setPolarization(context,monitorInfo.POL);

        monitorInfo.traceState = (Integer) o[8];
        CustomSP.putInt(context,KeyTraceState, monitorInfo.traceState);
        AntennaInfo.setAntennaState(context,monitorInfo.traceState);
        monitorInfo.latitude = (Float) o[9];
        CustomSP.putString(context,KeyLatitude, String.valueOf(monitorInfo.latitude));
        LocationInfo.setLatitude(context,monitorInfo.latitude);
        monitorInfo.longitude = (Float) o[10];
        CustomSP.putString(context,KeyLongitude, String.valueOf(monitorInfo.longitude));
        LocationInfo.setLongitude(context,monitorInfo.longitude);
        monitorInfo.height = (Float) o[11];
        CustomSP.putString(context,KeyHeight, String.valueOf(monitorInfo.height));

        monitorInfo.gnssState = (Integer) o[12];
        CustomSP.putInt(context,KeyGnssState, monitorInfo.gnssState);
        LocationInfo.setGnssState(context, monitorInfo.gnssState);
        monitorInfo.traceMode = (Integer) o[13];
        CustomSP.putInt(context,KeyTraceMode, monitorInfo.traceMode);
        monitorInfo.satelliteLongitude = (Float) o[14];
        CustomSP.putString(context,KeySatelliteLongitude, String.valueOf(monitorInfo.satelliteLongitude));
        monitorInfo.plMode = (Integer) o[15];
        CustomSP.putInt(context,KeyPlMode, monitorInfo.plMode);

        monitorInfo.threshold = (Float) o[16];
        CustomSP.putString(context,KeyThreshold, String.valueOf(monitorInfo.threshold));
        SatelliteInfo.setThreshold(context,String.valueOf(monitorInfo.threshold));
        monitorInfo.agc = (Float) o[17];
        CustomSP.putString(context,KeyAgc, String.valueOf(monitorInfo.agc));
        SatelliteInfo.setAgc(context, String.valueOf(monitorInfo.agc));
        monitorInfo.beacon = (Float) o[18];
        CustomSP.putString(context,KeyBeacon, String.valueOf(monitorInfo.beacon));
        SatelliteInfo.setBeacon(context,String.valueOf(monitorInfo.beacon));
        monitorInfo.carrier = (Float) o[19];
        CustomSP.putString(context,KeyCarrier, String.valueOf(monitorInfo.carrier));
        SatelliteInfo.setCarrier(context,String.valueOf(monitorInfo.carrier));
        monitorInfo.dvb = (Float) o[20];
        CustomSP.putString(context,KeyDvb, String.valueOf(monitorInfo.dvb));
        SatelliteInfo.setSymbolRate(context, String.valueOf(monitorInfo.dvb));

        monitorInfo.faultCondition = (Integer) o[21];
        FaultCondition.parseFaultCondition(context, String.valueOf(monitorInfo.faultCondition));
        monitorInfo.flag = (Integer) o[22];
        RunningInfo.parseRunningInfo(context, String.valueOf(monitorInfo.flag));

        return monitorInfo;
    }

    public int
    getTraceMode(Context context) {
        int defaultVal = 0;
        traceMode = CustomSP.getInt(context, KeyTraceMode, defaultVal);
        return traceMode;
    }

    public int
    getPlMode(Context context) {
        int defaultVal = 0;
        plMode = CustomSP.getInt(context, KeyPlMode, defaultVal);
        return plMode;
    }

    public int
    getFlag(Context context) {
        int defaultVal = 0;
        flag = CustomSP.getInt(context, KeyFlag, defaultVal);
        return flag;
    }

    public int
    getFaultCondition(Context context) {
        int defaultVal = 0;
        faultCondition = CustomSP.getInt(context, KeyFaultCondition, defaultVal);
        return faultCondition;
    }

    public float
    getThreshold(Context context) {
        float defaultVal = 0;
        threshold = CustomSP.getFloat(context, KeyThreshold, defaultVal);
        return threshold;
    }

    public float
    getRV(Context context) {
        float defaultVal = 0;
        RV = CustomSP.getFloat(context, KeyRV, defaultVal);
        return RV;
    }

    public float
    getPrepareRV(Context context) {
        float defaultVal = 0;
        prepareRV = CustomSP.getFloat(context, KeyPrepareRV, defaultVal);
        return prepareRV;
    }

    public float
    getPreparePOL(Context context) {
        float defaultVal = 0;
        preparePOL = CustomSP.getFloat(context, KeyPreparePOL, defaultVal);
        return preparePOL;
    }

    public float
    getPrepareEL(Context context) {
        float defaultVal = 0;
        prepareEL = CustomSP.getFloat(context, KeyPrepareEL, defaultVal);
        return prepareEL;
    }

    public float
    getPrepareAZ(Context context) {
        float defaultVal = 0;
        prepareAZ = CustomSP.getFloat(context, KeyPrepareAZ, defaultVal);
        return prepareAZ;
    }

    public int
    getTraceState(Context context) {
        int defaultVal = 0;
        traceState = AntennaInfo.getAntennaState(context);
        return traceState;
    }

    public float
    getPOL(Context context) {
        POL = new AntennaInfo().getPolarization(context);
        return POL;
    }

    public float
    getLongitude(Context context) {
        float defaultVal = 0;
        longitude = CustomSP.getFloat(context, KeyLongitude, defaultVal);
        return longitude;
    }

    public float
    getLatitude(Context context) {
        float defaultVal = 0;
        latitude = CustomSP.getFloat(context, KeyLatitude, defaultVal);
        return latitude;
    }

    public float
    getSatelliteLongitude(Context context) {
        float defaultVal = 0;
        satelliteLongitude = CustomSP.getFloat(context, KeySatelliteLongitude, defaultVal);
        return satelliteLongitude;
    }

    public float
    getHeight(Context context) {
        float defaultVal = 0;
        height = CustomSP.getFloat(context, KeyHeight, defaultVal);
        return height;
    }

    public float
    getEL(Context context) {
        EL = new AntennaInfo().getPitch(context);
        return EL;
    }

    public float
    getDvb(Context context) {
        float defaultVal = 0;
        dvb = CustomSP.getFloat(context, KeyDvb, defaultVal);
        return dvb;
    }

    public float
    getCarrier(Context context) {
        float defaultVal = 0;
        carrier = CustomSP.getFloat(context, KeyCarrier, defaultVal);
        return carrier;
    }

    public float
    getBeacon(Context context) {
        float defaultVal = 0;
        beacon = CustomSP.getFloat(context, KeyBeacon, defaultVal);
        return beacon;
    }

    public float
    getAZ(Context context) {
        AZ = new AntennaInfo().getAzimuth(context);
        return AZ;
    }

    public float
    getAgc(Context context) {
        agc = new AntennaInfo().getAgcLevel(context);
        return agc;
    }

    public int
    getGnssState(Context context) {
        gnssState = LocationInfo.getGnssState(context);
        return gnssState;
    }

}
