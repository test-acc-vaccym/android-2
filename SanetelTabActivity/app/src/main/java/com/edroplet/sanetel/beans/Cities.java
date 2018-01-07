package com.edroplet.sanetel.beans;

import android.content.Context;
import android.util.Log;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.JsonLoad;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private static Map<String, ArrayList<LocationInfo>> provinceObjectMap = new HashMap<>();
    private JsonLoad jl;

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
            jl = new JsonLoad(context, LocationInfo.citiesJsonFile);
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
            jl = new JsonLoad(context, LocationInfo.citiesJsonFile);
            cities = jl.loadCities();
            provinceObjectMap.put(mContext.getString(R.string.key_province_all),cities);
            // Add some sample items.
            for (int i = 0; i < getItemCounts(); i++) {
                LocationInfo iLocationInfo = cities.get(i);
                addItem(iLocationInfo, false);
            }
        }
    }

    public void addItem(LocationInfo item, boolean isNew) {
        // 城市是唯一的，所以没有的城市才会添加
        String cityName = item.getName();
        if (ITEM_MAP.get(cityName) == null) {
            ITEMS.add(item);
            ITEM_MAP.put(cityName, item);

            // 添加省份城市map数据
            String province = item.getProvince();
            ArrayList<LocationInfo> arrayList = provinceObjectMap.get(province);
            if (arrayList == null) {
                ArrayList<LocationInfo> arrayList1 = new ArrayList<LocationInfo>();
                arrayList1.add(item);
                provinceObjectMap.put(province, arrayList1);
            } else if (!arrayList.contains(item)) {
                arrayList.add(item);
                provinceObjectMap.put(province, arrayList);
            }
            if (isNew) {
                cities.add(item);
                provinceObjectMap.put(mContext.getString(R.string.key_province_all), cities);
            }
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


            // 修改省份城市map数据
            LocationInfo item = cities.get(itemIndex);
            String province = item.getProvince();
            ArrayList<LocationInfo> arrayList = provinceObjectMap.get(province);
            provinceObjectMap.put(mContext.getString(R.string.key_province_all),cities);
            // 如果包含有该map数据
            if (arrayList != null){
                // 获取position
                for (LocationInfo locationInfo1: arrayList){
                    // 找到城市名
                    if (locationInfo1.getName().equals(item.getName())){
                        // 修改节点数据
                        arrayList.set(arrayList.indexOf(locationInfo1), item);
                    }
                }
            }
        }
    }

    public String[] getProvinceArray(){
        Set<String> set = provinceObjectMap.keySet();
        String[] arr = new String[set.size()];
        set.toArray(arr);
        return arr;
    }

    public String[] getCitiesArray(String province){
        ArrayList<LocationInfo>  locationInfos = provinceObjectMap.get(province);
        String[] array = new String[locationInfos.size()];
        int i = 0;
        for (LocationInfo locationInfo : locationInfos) {
            array[i++] = locationInfo.getName();
        }
        return array;
    }

    public LocationInfo getLocationInfoByProvinceCity(String province, String city){
        ArrayList<LocationInfo>  locationInfos = provinceObjectMap.get(province);
        if (locationInfos != null && locationInfos.size() > 0){
            for (LocationInfo locationInfo : locationInfos) {
                if (locationInfo.getName(mContext).equals(city)) {
                    return locationInfo;
                }
            }
        }
        return null;
    }

    public List<LocationInfo> getLocationInfosByProvince(String province){
        ArrayList<LocationInfo>  locationInfos = provinceObjectMap.get(province);
        return locationInfos;
    }

    public void deleteItem(LocationInfo locationInfo){
        ITEMS.remove(locationInfo);
        ITEM_MAP.remove(locationInfo.getName());
        cities.remove(locationInfo);
        provinceObjectMap.put(mContext.getString(R.string.key_province_all),cities);

        String province = locationInfo.getProvince();
        ArrayList<LocationInfo> arrayList = provinceObjectMap.get(province);
        // 如果包含有该map数据
        if (arrayList != null && arrayList.contains(locationInfo)){
            // 删除该节点
            arrayList.remove(locationInfo);
            // 删除后节点还有数据
            if (arrayList.size() > 0) {
                provinceObjectMap.put(province, arrayList);
            }else {
                // 删除后节点没有数据，删除该map节点
                provinceObjectMap.remove(province);
            }
        }
    }

    public void deleteItem(int position){
        ITEMS.remove(position);
        ITEM_MAP.remove(cities.get(position).getName());
        cities.remove(position);

        // 删除省份城市map数据
        provinceObjectMap.put(mContext.getString(R.string.key_province_all),cities);
        LocationInfo item = cities.get(position);
        String province = item.getProvince();
        ArrayList<LocationInfo> arrayList = provinceObjectMap.get(province);
        // 如果包含有该map数据
        if (arrayList != null && arrayList.contains(item)){
            // 删除该节点
            arrayList.remove(item);
            // 删除后节点还有数据
            if (arrayList.size() > 0) {
                provinceObjectMap.put(province, arrayList);
            }else {
                // 删除后节点没有数据，删除该map节点
                provinceObjectMap.remove(province);
            }
        }

    }

    public void clear(){
        cities.clear();
        ITEMS.clear();
        ITEM_MAP.clear();
        provinceObjectMap.clear();
    }

    public void save() throws JSONException, IOException{
        if (jl != null && getItemCounts() > 0){
            jl.saveCities(cities);
        }
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
