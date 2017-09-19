package com.edroplet.qxx.saneteltabactivity.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qxs on 2017/9/15.
 */

public class LocationInfo implements Parcelable {
    private float longitude;
    private  float latitude;

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
    }

    public static final Parcelable.Creator<LocationInfo> CREATOR = new Creator<LocationInfo>(){
        @Override
        public LocationInfo createFromParcel(Parcel source) {
            LocationInfo li = new LocationInfo();
            li.longitude = source.readFloat();
            li.latitude  = source.readFloat();
            return li;
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };
}
