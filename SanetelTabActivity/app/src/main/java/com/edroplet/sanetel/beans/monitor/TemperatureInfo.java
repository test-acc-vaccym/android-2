package com.edroplet.sanetel.beans.monitor;

import android.content.Context;

import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.sscanf.Sscanf;

/**
 * Created by qxs on 2018/1/4.
 * 温度湿度信息
 */

public class TemperatureInfo {
    public static final String TemperatureInfoAction = "com.edroplet.sanetel.TemperatureInfoAction";
    public static final String TemperatureInfoData = "com.edroplet.sanetel.TemperatureInfoData";

    private static final String KeyTemperature = "temperature";
    private static final String KeyHumidity = "humidity";

    private String temperature;
    private String humidity;
    Context context;

    TemperatureInfo(Context ctx){this.context = ctx;}

    public String getHumidity() {
        humidity = CustomSP.getString(context,KeyHumidity, humidity);
        return humidity;
    }

    public String getTemperature() {
        temperature = CustomSP.getString(context,KeyTemperature,temperature);
        return temperature;
    }

    public static TemperatureInfo processTemperatureInfo(Context context, String data){
        TemperatureInfo temperatureInfo = new TemperatureInfo(context);
        Object[] objects = Sscanf.scan(data, Protocol.cmdGetEquipmentInfoResult,temperatureInfo.temperature, temperatureInfo.humidity);
        temperatureInfo.temperature = (String) objects[0];
        CustomSP.putString(context, KeyTemperature, temperatureInfo.temperature);
        temperatureInfo.humidity = (String) objects[1];
        CustomSP.putString(context, KeyHumidity,temperatureInfo.humidity);
        return temperatureInfo;
    }
}
