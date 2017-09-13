package com.edroplet.qxx.saneteltabactivity.beans;

/**
 * Created by qxs on 2017/9/11.
 */

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * A  Satellite Parameter item representing a piece of content.
 */
public class SatelliteParameterItem {
    // 字段的key
    public static final String JSON_ID = "序号";
    public static final String JSON_NAME = "卫星名称";
    public static final String JSON_POLARIZATION = "极化方式";
    public static final String JSON_LONGITUDE = "卫星经度";
    public static final String JSON_BEACON = "信标频率";
    public static final String JSON_THRESHOLD = "门限";
    public static final String JSON_SYMBOL_RATE = "符号率";
    public static final String JSON_COMMENT = "备注";

    // 包含的字段
    public final UUID mId;
    public final String id;             // 序号
    public final String name;           // 卫星名称
    public final String polarization;   // 极化
    public final String longitude;      // 经度
    public final String beacon;         // 信标
    public final String threshold;      // 门限
    public final String symbolRate;     // 符号率
    public final String comment;        // 备注

    public SatelliteParameterItem(String id, String name, String polarization, String longitude, @Nullable String beacon, String threshold, @Nullable String symbolRate, @Nullable String comment) {
        mId = UUID.randomUUID();
        this.id = id;
        this.name = name;
        this.polarization = polarization;
        this.longitude = longitude;
        this.beacon = beacon;
        this.threshold = threshold;
        this.symbolRate = symbolRate;
        this.comment = comment;
    }
    public SatelliteParameterItem(JSONObject json) throws JSONException{
        mId = UUID.randomUUID();
        id = json.getString(JSON_ID);
        name = json.getString(JSON_NAME);
        polarization = json.getString(JSON_POLARIZATION);
        longitude = json.getString(JSON_LONGITUDE);
        if (json.has(JSON_BEACON))
            beacon = json.getString(JSON_BEACON);
        else
            beacon = "";
        if (json.has(JSON_THRESHOLD))
            threshold = json.getString(JSON_THRESHOLD);
        else
            threshold = "";
        if (json.has(JSON_SYMBOL_RATE))
            symbolRate = json.getString(JSON_SYMBOL_RATE);
        else
            symbolRate = "";
        if (json.has(JSON_COMMENT))
            comment = json.getString(JSON_COMMENT);
        else
            comment="";
    }

    @Override
    public String toString() {
        return name;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
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
}
