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
public class Satellites {
    private Context mContext;
    private static ArrayList<SatelliteInfo> satellites;
    /**
     * An array of sample (dummy) items.
     */
    private static final List<SatelliteInfo> ITEMS = new ArrayList<SatelliteInfo>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SatelliteInfo> ITEM_MAP = new HashMap<String, SatelliteInfo>();

    public List<SatelliteInfo> getITEMS() {
        return ITEMS;
    }

    public int getItemCounts(){
        return satellites.size();
    }

    public Satellites(Context context) throws JSONException, IOException{
        mContext = context;
        if (satellites == null || satellites.size() == 0) {
            JsonLoad jl = new JsonLoad(context, SatelliteInfo.satelliteJsonFile);
            satellites = jl.loadSatellite();
            for (int i = 0; i < getItemCounts(); i++) {
                addItem(satellites.get(i), false);
            }
        }
    }

    public Satellites(Context context, boolean reload) throws JSONException, IOException {
        if (reload || satellites == null || satellites.size() == 0) {
            JsonLoad jl = new JsonLoad(context, SatelliteInfo.satelliteJsonFile);
            satellites = jl.loadSatellite();
            for (int i = 0; i < getItemCounts(); i++) {
                addItem(satellites.get(i), false);
            }
        }
    }
    public void addItem(SatelliteInfo item, boolean isNew) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mId.toString(), item);
        if (isNew)
            satellites.add(item);
    }

    public void clear(){
        satellites.clear();
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    private int getIndex(String id){
        int index = -1;
        for (SatelliteInfo satelliteInfo: satellites){
            index++;
            if (id.equals(satelliteInfo.mId.toString())){
                return index;
            }
        }
        return -1;
    }

    public void update(String id, SatelliteInfo item){
        int index = satellites.indexOf(item);
        if (index == -1){
            index = getIndex(id);
            if (index == -1){
                addItem(item,true);
                return;
            }
        }
        satellites.set(index, item);
        ITEMS.set(index, item);
    }

    public void deleteItem(SatelliteInfo satelliteInfo){
        ITEMS.remove(satelliteInfo);
        ITEM_MAP.remove(satelliteInfo.mId.toString());
        satellites.remove(satelliteInfo);
    }

    public void deleteItem(int position){
        ITEMS.remove(position);
        ITEM_MAP.remove(satellites.get(position).mId.toString());
        satellites.remove(position);
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
