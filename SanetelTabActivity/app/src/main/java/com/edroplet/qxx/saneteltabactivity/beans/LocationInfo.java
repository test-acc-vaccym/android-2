package com.edroplet.qxx.saneteltabactivity.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;

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
        dest.writeString(longitudeUnit);
        dest.writeFloat(latitude);
        dest.writeString(latitudeUnit);
    }

    public static final Parcelable.Creator<LocationInfo> CREATOR = new Creator<LocationInfo>(){
        @Override
        public LocationInfo createFromParcel(Parcel source) {
            String id = source.readString();
            String province = source.readString();
            String name = source.readString();
            float longitude = source.readFloat();
            String longitudeUnit = source.readString();
            float latitude  = source.readFloat();
            String latitudeUnit = source.readString();
            LocationInfo li = new LocationInfo(id, province, name, latitude, latitudeUnit, longitude, longitudeUnit);
            return li;
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public LocationInfo(String name, float latitude, String latitudeUnit,  float longitude, String longitudeUnit){
        this(null, name, latitude, latitudeUnit, longitude, longitudeUnit);
    }

    public LocationInfo(String province, String name, float latitude, String latitudeUnit,  float longitude, String longitudeUnit){
        this(UUID.randomUUID().toString(), province, name, latitude, latitudeUnit, longitude, longitudeUnit);
    }

    public LocationInfo(String id, String province, String name, float latitude, String latitudeUnit,  float longitude, String longitudeUnit){
        this.mId = id;
        this.province = province;
        this.name = name;
        this.latitude = latitude;
        this.latitudeUnit = latitudeUnit;
        this.longitude = longitude;
        this.longitudeUnit = longitudeUnit;

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
                if (jsonCityLatitude.endsWith("°S")) {
                    this.latitudeUnit = "°S";
                    jsonCityLatitude = jsonCityLatitude.substring(0,jsonCityLatitude.length()-2);
                } else if (jsonCityLatitude.endsWith("°N")) {
                    this.latitudeUnit = "°N";
                    jsonCityLatitude = jsonCityLatitude.substring(0,jsonCityLatitude.length()-2);
                }
                int index = jsonCityLatitude.indexOf(".");
//                for (int i = index; i < jsonCityLatitude.length(); i ++){
//                    if (jsonCityLatitude.)
//                }
                jsonCityLatitude = StringFilter(jsonCityLatitude);
                // this.latitude = (float) json.getDouble(JSON_CITY_LATITUDE);
                this.latitude = ConvertUtil.convertToFloat(StringFilter(jsonCityLatitude), 0.0f);
            }else {
                this.latitude = 0;
            }
            if (json.has(JSON_CITY_LONGITUDE)) {
                String jsonCityLongitude = json.getString(JSON_CITY_LONGITUDE);
                if (jsonCityLongitude.endsWith("°W")) {
                    this.longitudeUnit = "°W";
                    jsonCityLongitude = jsonCityLongitude.substring(0,jsonCityLongitude.length()-2);
                } else if (jsonCityLongitude.endsWith("°E")){
                    this.longitudeUnit = "°E";
                    jsonCityLongitude = jsonCityLongitude.substring(0,jsonCityLongitude.length()-2);
                }
                jsonCityLongitude = StringFilter(jsonCityLongitude);
                //  json.getString(JSON_CITY_LONGITUDE)
                this.longitude = ConvertUtil.convertToFloat(jsonCityLongitude,0.0f);
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
