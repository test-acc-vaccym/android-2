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
public class Cities {
    private Context mContext;
    private static ArrayList<LocationInfo> cities;
    private static ArrayList<Map<String,LocationInfo>> provinceObject;
    /**
     * An array of sample (dummy) items.
     */
    private static final List<LocationInfo> ITEMS = new ArrayList<LocationInfo>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, LocationInfo> ITEM_MAP = new HashMap<String, LocationInfo>();

    public int getItemCounts(){
        return cities.size();
    }

    public List<LocationInfo> getITEMS() {
        return ITEMS;
    }

    public Cities(Context context) throws JSONException, IOException{
        mContext = context;
        if (cities == null || cities.size() == 0) {
            JsonLoad jl = new JsonLoad(context, LocationInfo.citiesJsonFile);
            cities = jl.loadCities();
            // Add some sample items.
            for (int i = 0; i < getItemCounts(); i++) {
                addItem(cities.get(i), false);
            }
        }
    }
    public Cities(Context context, boolean reload) throws JSONException, IOException{
        mContext = context;
        if (reload || cities == null || cities.size() == 0) {
            JsonLoad jl = new JsonLoad(context, LocationInfo.citiesJsonFile);
            cities = jl.loadCities();
            // Add some sample items.
            for (int i = 0; i < getItemCounts(); i++) {
                addItem(cities.get(i), false);
            }
        }
    }
    public void addItem(LocationInfo item, boolean isNew) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getName(), item);
        if (isNew){
            cities.add(item);
        }
    }

    private int getIndex(String id){
        int index = -1;
        for (LocationInfo locationInfo: cities){
            index++;
            if (id.equals(locationInfo.getmId())){
                return index;
            }
        }
        return -1;
    }

    public void update(String id, LocationInfo locationInfo){
        if (locationInfo !=null) {
            int itemIndex = cities.indexOf(locationInfo);
            if (itemIndex == -1){
                itemIndex = getIndex(id);
                if (itemIndex == -1){
                    addItem(locationInfo, true);
                    return;
                }
            }
            ITEM_MAP.put(id, locationInfo);
            ITEMS.set(itemIndex, locationInfo);
            cities.set(itemIndex, locationInfo);
        }
    }

    public void deleteItem(LocationInfo locationInfo){
        ITEMS.remove(locationInfo);
        ITEM_MAP.remove(locationInfo.getName());
        cities.remove(locationInfo);
    }

    public void deleteItem(int position){
        ITEMS.remove(position);
        ITEM_MAP.remove(cities.get(position).getName());
        cities.remove(position);
    }

    public void clear(){
        cities.clear();
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    private static LocationInfo createLocationInfo(int position) {
        try {
            return new LocationInfo(cities.get(position).toJSON());
        }catch (JSONException je){
            Log.e(TAG, "createLocationInfo: LocationInfo error");
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
