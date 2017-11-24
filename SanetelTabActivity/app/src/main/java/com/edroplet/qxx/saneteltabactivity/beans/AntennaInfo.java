package com.edroplet.qxx.saneteltabactivity.beans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.edroplet.qxx.saneteltabactivity.services.CommunicateWithDeviceService;
import com.edroplet.qxx.saneteltabactivity.services.communicate.CommunicateDataReceiver;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;

import static com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo.AntennaStatus.INIT;

/**
 * Created by qxs on 2017/9/15.
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

    public static class AntennaStatus{
        public static final int INIT = 0;
        public static final int EXPLODING = 1;
        public static final int EXPLODED = 2;
        public static final int FOLDING = 3;
        public static final int FOLDED = 4;
        public static final int PAUSE = 5;
        public static final int SEARCHING = 6;
        public static final int RECYCLED = 7;
    }
    private static final String KEY_ANTENNA_STATE="KEY_ANTENNA_STATE";
    public static int getAntennaState(Context context){
        return CustomSP.getInt(context,KEY_ANTENNA_STATE,INIT);
    }
    public static void setAntennaState(Context context, int antennaState){
        CustomSP.putInt(context,KEY_ANTENNA_STATE,antennaState);
    }

    public void getAntennaInfoFromServer(Context context){
        Intent intent = new Intent(CommunicateDataReceiver.ACTION_RECEIVE_DATA);
        intent.putExtra(CommunicateWithDeviceService.EXTRA_PARAM_SEND_CMD, Protocol.cmdGeteEquipmentInfo);
        context.sendBroadcast(intent);
    }
}
