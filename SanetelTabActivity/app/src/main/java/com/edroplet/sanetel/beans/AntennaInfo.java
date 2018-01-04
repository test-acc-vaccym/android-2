package com.edroplet.sanetel.beans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.edroplet.sanetel.services.CommunicateWithDeviceService;
import com.edroplet.sanetel.services.communicate.CommunicateDataReceiver;
import com.edroplet.sanetel.utils.CustomSP;

import static com.edroplet.sanetel.beans.AntennaInfo.AntennaStatus.INIT;

/**
 * Created by qxs on 2017/9/15.
 * 天线信息
 */

public class AntennaInfo implements Parcelable {
    private float azimuth; // 方位角
    private float pitch;   // 俯仰角
    private float polarization; // 极化角
    private float agcLevel; // AGC电平

    public float getAgcLevel() {
        return agcLevel;
    }

    public void setAgcLevel(float agcLevel) {
        this.agcLevel = agcLevel;
    }

    public float getAzimuth() {
        return azimuth;
    }

    public float getPitch() {
        return pitch;
    }

    public float getPolarization() {
        return polarization;
    }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setPolarization(float polarization) {
        this.polarization = polarization;
    }

    public static final Parcelable.Creator<AntennaInfo> CREATOR = new Creator<AntennaInfo>(){
        @Override
        public AntennaInfo createFromParcel(Parcel source) {
            AntennaInfo ai= new AntennaInfo();
            ai.azimuth = source.readFloat();
            ai.pitch = source.readFloat();
            ai.polarization = source.readFloat();
            ai.agcLevel = source.readFloat();
            return ai;
        }

        @Override
        public AntennaInfo[] newArray(int size) {
            return new AntennaInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(azimuth);
        dest.writeFloat(pitch);
        dest.writeFloat(polarization);
        dest.writeFloat(agcLevel);
    }

    /**
     * 4.8.2	说明
     最大字节个数不能超过290。
     天线状态：
     0—收藏：
     1—展开中：
     2—已展开：
     3—初始：寻零过程。
     4--寻星：
     5—手动：发送手动控制指令之后的天线状态
     6—锁定：
     7—收藏中；
     8—失锁
     9---异常： 此情况并不是天线给出的状态，而是上位机APP等发现数据混乱，显示的内容。
     10--停止：收到停止指令后，处于的状态。
     11 –复位中
     12—已复位完成；
     */
    public static class AntennaStatus{
        public static final int FOLDED = 0; // 收藏
        public static final int EXPLODING = 1; // 展开中
        public static final int EXPLODED = 2; // 已展开
        public static final int INIT = 3; // 初始：寻零过程
        public static final int SEARCHING = 4; // 寻星
        public static final int MANUAL = 5; // 手动 发送手动控制指令之后的天线状态
        public static final int LOCKED = 6; // 锁定
        public static final int FOLDING = 7; // 收藏中
        public static final int LOSED = 8; // 失锁
        public static final int ABNORMAL= 9; // 异常： 此情况并不是天线给出的状态，而是上位机APP等发现数据混乱，显示的内容。
        public static final int PAUSE = 10; // 停止：收到停止指令后，处于的状态
        public static final int RECYCLING = 11; // 复位中
        public static final int RECYCLED = 12; // 已复位完成
    }

    private static final String KEY_ANTENNA_STATE="KEY_ANTENNA_STATE";

    public static int getAntennaState(Context context){
        return CustomSP.getInt(context,KEY_ANTENNA_STATE,INIT);
    }
    public static void setAntennaState(Context context, int antennaState){
        CustomSP.putInt(context,KEY_ANTENNA_STATE,antennaState);
    }

    public static void getAntennaInfoFromServer(Context context){
        Protocol.sendMessage(context, Protocol.cmdGetEquipmentInfo);
    }
}
