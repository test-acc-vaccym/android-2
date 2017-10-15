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
public class CityElements {
    private Context mContext;
    private static ArrayList<LocationInfo> cities;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<LocationInfo> ITEMS = new ArrayList<LocationInfo>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, LocationInfo> ITEM_MAP = new HashMap<String, LocationInfo>();

    public int getItemCounts(){
        return cities.size();
    }

    public CityElements(Context context) throws JSONException, IOException{
        mContext = context;
        JsonLoad jl = new JsonLoad(context, "city_location.json");
        cities = jl.loadCities();
        // Add some sample items.
        for (int i = 0; i < getItemCounts(); i++) {
            addItem(createLocationInfo(i));
        }
    }

    private void addItem(LocationInfo item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getName(), item);
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
