package com.edroplet.sanetel.beans;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by qxs on 2017/9/15.
 * 位置信息
 */

public class LocationInfo implements Parcelable {
    public static final String citiesJsonFile = "city_location.json";
    public String mId;             // 序号
    private String province;        // 省份
    private static final String KeyProvince = "KeyProvince";
    private String name;            // 城市
    private static final String KeyName = "KeyName";
    private float longitude;        // 经度
    private static final String KeyLongitude = "KeyLongitude";
    private int longitudeUnitPosition;   // 经度单位
    private static final String KeyLongitudeUnitPosition = "KeyLongitudeUnitPosition";
    private float latitude;         // 纬度
    private static final String KeyLatitude = "KeyLatitude";
    private int latitudeUnitPosition;    // 纬度单位
    private static final String KeyLatitudeUnitPosition = "KeyLatitudeUnitPosition";

    public static final String objectKey = "city";
    public static final String positionKey = "position";

    // 字段的key
    public static final String JSON_ID_KEY = "id";
    private static final String JSON_PROVENCE_NAME = "省份";
    private static final String JSON_CITY_NAME = "地名";
    private static final String JSON_CITY_LATITUDE = "纬度°";
    private static final String JSON_CITY_LONGITUDE = "经度°";

    public void setmId(String mId) {
        this.mId = mId;
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
        dest.writeInt(longitudeUnitPosition);
        dest.writeFloat(latitude);
        dest.writeInt(latitudeUnitPosition);
    }

    public static final Parcelable.Creator<LocationInfo> CREATOR = new Creator<LocationInfo>(){
        @Override
        public LocationInfo createFromParcel(Parcel source) {
            String id = source.readString();
            String province = source.readString();
            String name = source.readString();
            float longitude = source.readFloat();
            int longitudeUnitPosition = source.readInt();
            float latitude  = source.readFloat();
            int latitudeUnitPosition = source.readInt();
            LocationInfo li = new LocationInfo(id, province, name, latitude, latitudeUnitPosition, longitude, longitudeUnitPosition);
            return li;
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public LocationInfo(String name, float latitude, int latitudeUnitPosition,  float longitude, int longitudeUnitPosition){
        this(null, name, latitude, latitudeUnitPosition, longitude, longitudeUnitPosition);
    }

    public LocationInfo(String province, String name, float latitude, int latitudeUnitPosition,  float longitude, int longitudeUnitPosition){
        this(UUID.randomUUID().toString(), province, name, latitude, latitudeUnitPosition, longitude, longitudeUnitPosition);
    }

    public LocationInfo(String id, String province, String name, float latitude, int latitudeUnitPosition,  float longitude, int longitudeUnitPosition){
        this.mId = id;
        this.province = province;
        this.name = name;
        this.latitude = latitude;
        this.latitudeUnitPosition = latitudeUnitPosition;
        this.longitude = longitude;
        this.longitudeUnitPosition = longitudeUnitPosition;

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
                if (jsonCityLatitude.endsWith(latitudeString[1])) {
                    this.latitudeUnitPosition = 1;
                    jsonCityLatitude = jsonCityLatitude.substring(0,jsonCityLatitude.length()-2);
                } else if (jsonCityLatitude.endsWith(latitudeString[0])) {
                    this.latitudeUnitPosition = 0;
                    jsonCityLatitude = jsonCityLatitude.substring(0,jsonCityLatitude.length()-2);
                }

                jsonCityLatitude = StringFilter(jsonCityLatitude);
                // this.latitude = (float) json.getDouble(JSON_CITY_LATITUDE);
                this.latitude = ConvertUtil.convertToFloat(StringFilter(jsonCityLatitude), 0.0f);
            }else {
                this.latitude = 0;
            }
            if (json.has(JSON_CITY_LONGITUDE)) {
                String jsonCityLongitude = json.getString(JSON_CITY_LONGITUDE);
                if (jsonCityLongitude.endsWith(longitudeString[1])) {
                    this.longitudeUnitPosition = 1;
                    jsonCityLongitude = jsonCityLongitude.substring(0,jsonCityLongitude.length()-2);
                } else if (jsonCityLongitude.endsWith(longitudeString[0])){
                    this.longitudeUnitPosition = 0;
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
        json.put(JSON_CITY_LATITUDE, this.latitude + latitudeString[this.latitudeUnitPosition]);
        json.put(JSON_CITY_LONGITUDE, this.longitude + longitudeString[this.longitudeUnitPosition]);
        return json;
    }

    public static final String[] longitudeString = {"°E", "°W"};
    public static final String[] latitudeString = {"°N", "°S"};

    public static class GnssState {
        public static final int NOTLOCATED = 0;
        public static  final int LOCATED = 1;
    }

    private static final String KEY_GNSS_STATE="KEY_GNSS_STATE";
    public static int getGnssState(Context context){
        return CustomSP.getInt(context,KEY_GNSS_STATE, GnssState.NOTLOCATED);
    }
    public static void setGnssState(Context context, int gnssState){
        CustomSP.getInt(context,KEY_GNSS_STATE, gnssState);
    }

    private static SparseArray<String> longitudeUnitArray = new SparseArray<>(2);
    private static SparseArray<String> latitudeUnitArray = new SparseArray<>(2);

    public static SparseArray<String> getLongitudeUnitArray(){
        longitudeUnitArray.clear();
        longitudeUnitArray.put(0, "°E");
        longitudeUnitArray.put(1, "°W");
        return longitudeUnitArray;
    }
    public static SparseArray<String> getLatiitudeUnitArray(){
        latitudeUnitArray.clear();
        latitudeUnitArray.put(0, "°N");
        latitudeUnitArray.put(1, "°S");
        return latitudeUnitArray;
    }

    public static float
    getLatitude(Context context) {
        return CustomSP.getFloat(context, KeyLatitude, 0f);
    }

    public static float
    getLongitude(Context context) {
        return CustomSP.getFloat(context, KeyLongitude, 0f);
    }

    public static int
    getLatitudeUnitPosition(Context context) {
        return CustomSP.getInt(context, KeyLatitudeUnitPosition, 0);
    }

    public static int
    getLongitudeUnitPosition(Context context) {
        return CustomSP.getInt(context, KeyLongitudeUnitPosition, 0);
    }

    public static void setLatitude(Context context, float latitude) {
        CustomSP.putFloat(context, KeyLatitude, latitude);
    }

    public static void setLongitude(Context context, float longitude) {
        CustomSP.putFloat(context, KeyLongitude, longitude);
    }

    public static void setName(Context context, String name) {
        String myName = name == null ? "" : name;
        CustomSP.putString(context, KeyName, myName);
    }

    public static String
    getName(Context context) {
        return CustomSP.getString(context, KeyName, "");
    }

    public static void setProvince(Context context, String province) {
        CustomSP.putString(context, KeyProvince, province);
    }

    public static String
    getProvince(Context context) {
        return CustomSP.getString(context, KeyProvince, "");
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public int getLongitudeUnitPosition() {
        return longitudeUnitPosition;
    }

    public int getLatitudeUnitPosition() {
        return latitudeUnitPosition;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLatitudeUnitPosition(int latitudeUnitPosition) {
        this.latitudeUnitPosition = latitudeUnitPosition;
    }

    public void setLongitudeUnitPosition(int longitudeUnitPosition) {
        this.longitudeUnitPosition = longitudeUnitPosition;
    }
}
