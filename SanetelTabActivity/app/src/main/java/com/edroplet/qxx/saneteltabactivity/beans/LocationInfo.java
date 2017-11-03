package com.edroplet.qxx.saneteltabactivity.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by qxs on 2017/9/15.
 */

public class LocationInfo implements Parcelable {
    public static final String citiesJsonFile = "city_location.json";
    private String mId;             // 序号
    private String province;        // 省份
    private String name;            // 城市
    private float longitude;        // 经度
    private String longitudeUnit;   // 经度单位
    private float latitude;         // 纬度
    private String latitudeUnit;    // 纬度单位

    public static final String objectKey = "city";
    public static final String positionKey = "position";

    // 字段的key
    public static final String JSON_ID_KEY = "id";
    public static final String JSON_PROVENCE_NAME = "省份";
    public static final String JSON_CITY_NAME = "地名";
    public static final String JSON_CITY_LATITUDE = "纬度°";
    public static final String JSON_CITY_LONGITUDE = "经度°";

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setmId(String mId) {
        this.mId = mId;
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

    public String getLatitudeUnit() {
        return latitudeUnit;
    }

    public String getLongitudeUnit() {
        return longitudeUnit;
    }

    public void setLatitudeUnit(String latitudeUnit) {
        this.latitudeUnit = latitudeUnit;
    }

    public void setLongitudeUnit(String longitudeUnit) {
        this.longitudeUnit = longitudeUnit;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getProvince() {
        return province;
    }

    public String getmId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(province);
        dest.writeString(name);
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
    }

    public static final Parcelable.Creator<LocationInfo> CREATOR = new Creator<LocationInfo>(){
        @Override
        public LocationInfo createFromParcel(Parcel source) {
            String id = source.readString();
            String province = source.readString();
            String name = source.readString();
            float longitude = source.readFloat();
            float latitude  = source.readFloat();
            LocationInfo li = new LocationInfo(id, province, name, latitude, longitude);
            return li;
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public LocationInfo(String name, float latitude, float longitude){
        this(null, name, latitude, longitude);
    }

    public LocationInfo(String province, String name, float latitude, float longitude){
        this(UUID.randomUUID().toString(), province, name, latitude, longitude);
    }

    public LocationInfo(String id, String province, String name, float latitude, float longitude){
        this.mId = id;
        this.province = province;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[^0-9.]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public LocationInfo(JSONObject json) {
        try {
            if (json.has(JSON_PROVENCE_NAME)) {
                this.province = json.getString(JSON_PROVENCE_NAME);
            }else {
                this.province = "";
            }
            if (json.has(JSON_CITY_NAME)) {
                this.name = json.getString(JSON_CITY_NAME);
            }else {
                this.name = "";
            }
            if (json.has(JSON_CITY_LATITUDE)) {
                String jsonCityLatitude = json.getString(JSON_CITY_LATITUDE);
                if (jsonCityLatitude.contains("S")) {
                    this.latitudeUnit = "S";
                    jsonCityLatitude = jsonCityLatitude.substring(0,jsonCityLatitude.length()-2);
                } else if (jsonCityLatitude.contains("N")) {
                    this.latitudeUnit = "N";
                    jsonCityLatitude = jsonCityLatitude.substring(0,jsonCityLatitude.length()-2);
                }
                int index = jsonCityLatitude.indexOf(".");
//                for (int i = index; i < jsonCityLatitude.length(); i ++){
//                    if (jsonCityLatitude.)
//                }
                jsonCityLatitude = StringFilter(jsonCityLatitude);
                // this.latitude = (float) json.getDouble(JSON_CITY_LATITUDE);
                this.latitude = Float.parseFloat(StringFilter(jsonCityLatitude));
            }else {
                this.latitude = 0;
            }
            if (json.has(JSON_CITY_LONGITUDE)) {
                String jsonCityLongitude = json.getString(JSON_CITY_LONGITUDE);
                if (jsonCityLongitude.contains("W")) {
                    this.longitudeUnit = "W";
                    jsonCityLongitude = jsonCityLongitude.substring(0,jsonCityLongitude.length()-2);
                } else if (jsonCityLongitude.contains("E")){
                    this.longitudeUnit = "E";
                    jsonCityLongitude = jsonCityLongitude.substring(0,jsonCityLongitude.length()-2);
                }
                jsonCityLongitude = StringFilter(jsonCityLongitude);
                //  json.getString(JSON_CITY_LONGITUDE)
                this.longitude = Float.parseFloat(jsonCityLongitude);
            }else {
                this.longitude = 0;
            }
            if (json.has(JSON_ID_KEY)){
                this.mId = json.getString(JSON_ID_KEY);
            }else {
                this.mId = UUID.randomUUID().toString();
            }
        }catch (JSONException je){
            je.printStackTrace();
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_PROVENCE_NAME, this.province);
        json.put(JSON_CITY_NAME, this.name);
        json.put(JSON_CITY_LATITUDE, this.latitude + this.latitudeUnit);
        json.put(JSON_CITY_LONGITUDE, this.longitude + this.longitudeUnit);
        return json;
    }

    public static class BDState {
        public static final int NOTLOCATED = 0;
        public static  final int LOCATED = 1;
    }
}
