package com.edroplet.qxx.saneteltabactivity.beans;

import android.os.Parcel;
import android.os.Parcelable;

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
        public static final int EXPLODED = 0;
        public static final int FOLDED = 1;
        public static final int PAUSE = 2;
        public static final int SEARCHING = 3;
        public static final int RECYCLED = 4;
        public static final int EXPLODING = 5;

    }
}
