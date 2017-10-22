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
    private static ArrayList<SatelliteInfo> satellites;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<SatelliteInfo> ITEMS = new ArrayList<SatelliteInfo>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SatelliteInfo> ITEM_MAP = new HashMap<String, SatelliteInfo>();

    public int getItemCounts(){
        return satellites.size();
    }

    public SatelliteParameters(Context context) throws JSONException, IOException{
        mContext = context;
        JsonLoad jl = new JsonLoad(context, SatelliteInfo.satelliteJsonFile);
        satellites = jl.loadSatellite();
        // Add some sample items.
        /* 表头单独处理
        addItem(new SatelliteInfo(SatelliteInfo.JSON_ID,
                SatelliteInfo.JSON_NAME,
                SatelliteInfo.JSON_POLARIZATION,
                SatelliteInfo.JSON_LONGITUDE,
                SatelliteInfo.JSON_BEACON,
                SatelliteInfo.JSON_THRESHOLD,
                SatelliteInfo.JSON_SYMBOL_RATE,
                SatelliteInfo.JSON_COMMENT
                ));
         */
        for (int i = 0; i < getItemCounts(); i++) {
            addItem(createSatelliteParameterItem(i));
        }
    }

    private void addItem(SatelliteInfo item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mId.toString(), item);
    }

    private static SatelliteInfo createSatelliteParameterItem(int position) {
        try {
            return new SatelliteInfo(satellites.get(position).toJSON());
        }catch (JSONException je){
            Log.e(TAG, "createSatelliteParameterItem: SatelliteInfo error");
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
