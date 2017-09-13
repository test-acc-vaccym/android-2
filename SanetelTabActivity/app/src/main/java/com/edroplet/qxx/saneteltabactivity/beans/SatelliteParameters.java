package com.edroplet.qxx.saneteltabactivity.beans;

import android.content.Context;
import android.util.Log;

import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SatelliteParameters {
    private Context mContext;
    private static ArrayList<SatelliteParameterItem> satellites;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<SatelliteParameterItem> ITEMS = new ArrayList<SatelliteParameterItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SatelliteParameterItem> ITEM_MAP = new HashMap<String, SatelliteParameterItem>();

    public int getItemCounts(){
        return satellites.size();
    }

    public SatelliteParameters(Context context) throws JSONException, IOException{
        mContext = context;
        JsonLoad jl = new JsonLoad(context, "satellite.json");
        satellites = jl.loadSatellite();
        // Add some sample items.
        /* 表头单独处理
        addItem(new SatelliteParameterItem(SatelliteParameterItem.JSON_ID,
                SatelliteParameterItem.JSON_NAME,
                SatelliteParameterItem.JSON_POLARIZATION,
                SatelliteParameterItem.JSON_LONGITUDE,
                SatelliteParameterItem.JSON_BEACON,
                SatelliteParameterItem.JSON_THRESHOLD,
                SatelliteParameterItem.JSON_SYMBOL_RATE,
                SatelliteParameterItem.JSON_COMMENT
                ));
         */
        for (int i = 0; i < getItemCounts(); i++) {
            addItem(createSatelliteParameterItem(i));
        }
    }

    private void addItem(SatelliteParameterItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mId.toString(), item);
    }

    private static SatelliteParameterItem createSatelliteParameterItem(int position) {
        try {
            return new SatelliteParameterItem(satellites.get(position).toJSON());
        }catch (JSONException je){
            Log.e(TAG, "createSatelliteParameterItem: SatelliteParameterItem error");
        }
        return null;

    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

}
