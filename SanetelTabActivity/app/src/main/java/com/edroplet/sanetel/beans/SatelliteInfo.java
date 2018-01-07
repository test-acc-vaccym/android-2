package com.edroplet.sanetel.beans;

/**
 * Created by qxs on 2017/9/11.
 */

import android.content.Context;
import android.support.annotation.Nullable;

import com.edroplet.sanetel.utils.CustomSP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

/**
 * A  Satellite Parameter item representing a piece of content.
 */
public class SatelliteInfo implements Serializable {

    private static final long serialVersionUID = -6919461967497580385L;
    public static final String satelliteJsonFile = "satellite.json";
    public static final String objectKey = "satellite";
    public static final String positionKey = "position";
    public static final String uuidKey = "uuid";

    // 字段的key
    private static final String JSON_UUID = "唯一序号";
    private static final String JSON_ID = "序号";
    private static final String JSON_NAME = "卫星名称";
    private static final String JSON_POLARIZATION = "极化方式";
    private static final String JSON_LONGITUDE = "卫星经度";
    private static final String JSON_BEACON = "信标频率";
    private static final String JSON_THRESHOLD = "门限";
    private static final String JSON_CARRIER = "载波";
    private static final String JSON_SYMBOL_RATE = "符号率";
    private static final String JSON_AGC = "AGC";
    private static final String JSON_COMMENT = "备注";

    // 包含的字段
    private UUID mId;
    private String id;             // 序号
    public String name;           // 卫星名称
    public String polarization;   // 极化
    public String longitude;      // 经度
    public String beacon;         // 信标
    public String threshold;      // 门限
    public String carrier;        // 载波
    public String symbolRate;     // 符号率
    public String comment;        // 备注
    public String agc;           // AGC电平
    public String mode;           // 寻星方式

    private static final String KeyName = "KeyName";
    private static final String KeyPolarization = "KeyPolarization";
    private static final String KeyLongitude = "KeyLongitude";
    private static final String KeyBeacon = "KeyBeacon";
    private static final String KeyThreshold = "KeyThreshold";
    private static final String KeyCarrier = "KeyCarrier";
    private static final String KeySymbolRate = "KeySymbolRate";
    private static final String KeyComment = "KeyComment";
    private static final String KeyAgc = "KeyAgc";
    private static final String KeyMode = "KeyMode";


    public SatelliteInfo(String id, String name, String polarization, String longitude,
                         @Nullable String beacon, String threshold,
                         @Nullable String symbolRate,
                         @Nullable String comment,
                         @Nullable String carrier,
                         @Nullable String agc) {
        mId = UUID.randomUUID();
        this.id = id;
        this.name = name;
        this.polarization = polarization;
        this.longitude = longitude;
        this.beacon = beacon;
        this.threshold = threshold;
        this.symbolRate = symbolRate;
        this.comment = comment;
        this.carrier = carrier;
        this.agc = agc;
    }

    public SatelliteInfo(){
        return;
    }
    public SatelliteInfo(String uuid, String id, String name, String polarization, String longitude,
                         @Nullable String beacon, String threshold,
                         @Nullable String symbolRate,
                         @Nullable String comment,
                         @Nullable String carrier,
                         @Nullable String agc) {
        mId = UUID.fromString(uuid);
        this.id = id;
        this.name = name;
        this.polarization = polarization;
        this.longitude = longitude;
        this.beacon = beacon;
        this.threshold = threshold;
        this.symbolRate = symbolRate;
        this.comment = comment;
        this.carrier = carrier;
        this.agc = agc;
    }

    public SatelliteInfo(JSONObject json) throws JSONException{

        if (json.has(JSON_UUID)) {
            String jsonUuid = json.getString(JSON_UUID);
            if (jsonUuid.length() > 0) {
                mId = UUID.fromString(json.getString(JSON_UUID));
            }else {
                mId = UUID.randomUUID();
            }
        }else {
            mId = UUID.randomUUID();
        }

        if (json.has(JSON_ID)) {
            id = json.getString(JSON_ID);
        }else {
            id = "";
        }
        if (json.has(JSON_NAME)) {
            name = json.getString(JSON_NAME);
        }else {
            name = "";
        }
        if (json.has(JSON_POLARIZATION)) {
            polarization = json.getString(JSON_POLARIZATION);
        }else {
            polarization = "";
        }
        if (json.has(JSON_LONGITUDE)) {
            longitude = json.getString(JSON_LONGITUDE);
        }else {
            longitude = "0.0";
        }
        if (json.has(JSON_BEACON))
            beacon = json.getString(JSON_BEACON);
        else
            beacon = "0.000";
        if (json.has(JSON_THRESHOLD))
            threshold = json.getString(JSON_THRESHOLD);
        else
            threshold = "0.00";
        if (json.has(JSON_CARRIER))
            carrier = json.getString(JSON_CARRIER);
        else
            carrier = "0.000";
        if (json.has(JSON_SYMBOL_RATE))
            symbolRate = json.getString(JSON_SYMBOL_RATE);
        else
            symbolRate = "0";
        if (json.has(JSON_COMMENT))
            comment = json.getString(JSON_COMMENT);
        else
            comment="";

        if (json.has(JSON_AGC))
            agc = json.getString(JSON_AGC);
        else
            agc = "0.00";
    }

    @Override
    public String toString() {
        return name;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        // json.put(JSON_UUID, mId.toString());
        json.put(JSON_ID, id);
        json.put(JSON_NAME, name);
        json.put(JSON_POLARIZATION, polarization);
        json.put(JSON_LONGITUDE, longitude);
        json.put(JSON_BEACON, beacon);
        json.put(JSON_THRESHOLD, threshold);
        json.put(JSON_SYMBOL_RATE, symbolRate);
        json.put(JSON_COMMENT, comment);
        return json;
    }

    public static String
    getName(Context context) {
        return CustomSP.getString(context, KeyName, "");
    }

    public static String
    getBeacon(Context context) {
        return CustomSP.getString(context, KeyBeacon, "");
    }

    public static String
    getCarrier(Context context) {
        return CustomSP.getString(context, KeyCarrier, "");
    }

    public static String
    getAgc(Context context) {
        return CustomSP.getString(context, KeyAgc, "");
    }

    public static String
    getComment(Context context) {
        return CustomSP.getString(context, KeyComment, "");
    }

    public static String
    getLongitude(Context context) {
        return CustomSP.getString(context, KeyLongitude, "");
    }

    public static String
    getMode(Context context) {
        return CustomSP.getString(context, KeyMode, "");
    }

    public static String
    getPolarization(Context context) {
        return CustomSP.getString(context, KeyPolarization, "");
    }

    public static String
    getSymbolRate(Context context) {
        return CustomSP.getString(context, KeySymbolRate, "");
    }

    public static String
    getThreshold(Context context) {
        return CustomSP.getString(context, KeyThreshold, "");
    }

    public static void setThreshold(Context context, String threshold) {
        CustomSP.putString(context, KeyThreshold, threshold);
    }

    public static void setName(Context context, String name) {
        CustomSP.putString(context, KeyName, name);
    }

    public static void setLongitude(Context context, String longitude) {
        CustomSP.putString(context, KeyLongitude, longitude);
    }

    public static void setBeacon(Context context, String beacon) {
        CustomSP.putString(context, KeyBeacon, beacon);
    }

    public static void setCarrier(Context context, String carrier) {
        CustomSP.putString(context, KeyCarrier, carrier);
    }

    public static void setComment(Context context, String comment) {
        CustomSP.putString(context, KeyComment, comment);
    }

    public static void setAgc(Context context, String agc) {
        CustomSP.putString(context, KeyAgc, agc);
    }

    public static void setPolarization(Context context, String polarization) {
        CustomSP.putString(context, KeyPolarization, polarization);
    }

    public static void setMode(Context context, String mode) {
        CustomSP.putString(context, KeyMode, mode);
    }

    public static void setSymbolRate(Context context, String symbolRate) {
        CustomSP.putString(context, KeySymbolRate, symbolRate);
    }

    public String getPolarization() {
        return polarization;
    }

    public String getThreshold() {
        return threshold;
    }

    public String getSymbolRate() {
        return symbolRate;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getMode() {
        return mode;
    }

    public String getComment() {
        return comment;
    }

    public String getBeacon() {
        return beacon;
    }

    public String getAgc() {
        return agc;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public void setSymbolRate(String symbolRate) {
        this.symbolRate = symbolRate;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPolarization(String polarization) {
        this.polarization = polarization;
    }

    public void setAgc(String agc) {
        this.agc = agc;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }
}
