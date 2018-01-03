package com.edroplet.qxx.saneteltabactivity.beans;

import android.content.Context;

import com.edroplet.qxx.saneteltabactivity.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qxs on 2017/12/30.
 */

public class PolarizationMode {
    static Map<String, Integer> mapPolarizationMode = new HashMap<>(4);
    public static Map<String, Integer> getMap(Context context){
        mapPolarizationMode.put(context.getString(R.string.satellite_polarization_horizontal), 0);
        mapPolarizationMode.put(context.getString(R.string.satellite_polarization_vertical), 1);
        mapPolarizationMode.put(context.getString(R.string.satellite_polarization_left_hand), 2);
        mapPolarizationMode.put(context.getString(R.string.satellite_polarization_right_hand), 3);
        return mapPolarizationMode;
    }
}
