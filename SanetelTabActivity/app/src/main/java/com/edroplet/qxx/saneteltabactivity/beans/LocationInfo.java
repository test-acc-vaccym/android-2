package com.edroplet.qxx.saneteltabactivity.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by qxs on 2017/9/15.
 */

public class LocationInfo implements Parcelable {
    private String name;           // 卫星名称
    private float longitude;
    private  float latitude;

    // 字段的key
    public static final String JSON_CITY_NAME = "地名";
    public static final String JSON_CITY_LATITUDE = "北纬°";
    public static final String JSON_CITY_LONGITUDE = "东经°";

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
        dest.writeString(name);
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
    }

    public static final Parcelable.Creator<LocationInfo> CREATOR = new Creator<LocationInfo>(){
        @Override
        public LocationInfo createFromParcel(Parcel source) {
            String name = source.readString();
            float longitude = source.readFloat();
            float latitude  = source.readFloat();
            LocationInfo li = new LocationInfo(name, latitude, longitude);
            return li;
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public LocationInfo(String name, float latitude, float longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationInfo(JSONObject json) {
        try {
            name = json.getString(JSON_CITY_NAME);
            latitude = (float)json.getDouble(JSON_CITY_LATITUDE);
            longitude = (float)json.getDouble(JSON_CITY_LONGITUDE);
        }catch (JSONException je){
            je.printStackTrace();
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_CITY_NAME, name);
        json.put(JSON_CITY_LATITUDE, latitude);
        json.put(JSON_CITY_LONGITUDE, longitude);
        return json;
    }

    public static class BDState {
        public static final int NONLOCATED = 0;
        public static  final int LOCATED = 1;
    }
}
