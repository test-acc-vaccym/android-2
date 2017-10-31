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
import java.util.Set;

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

    private static Map<String, ArrayList<SatelliteInfo>> satellitesObjectMap = new HashMap<>();
    
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

        // 添加卫星极化map数据
        String satelliteName = item.name;
        ArrayList<SatelliteInfo> arrayList = satellitesObjectMap.get(satelliteName);
        if (arrayList == null){
            ArrayList<SatelliteInfo> arrayList1 = new ArrayList<SatelliteInfo>();
            arrayList1.add(item);
            satellitesObjectMap.put(satelliteName, arrayList1);
        }else if (!arrayList.contains(item)) {
            arrayList.add(item);
            satellitesObjectMap.put(satelliteName, arrayList);
        }
        
        if (isNew)
            satellites.add(item);
    }

    public void clear(){
        satellites.clear();
        ITEMS.clear();
        ITEM_MAP.clear();
        satellitesObjectMap.clear();
    }

    public String[] getSatelliteNameArray(){
        Set<String> set = satellitesObjectMap.keySet();
        String[] arr = new String[set.size()];
        set.toArray(arr);
        return arr;
    }

    public String[] getCitiesArray(String satelliteName){
        ArrayList<SatelliteInfo>  locationInfos = satellitesObjectMap.get(satelliteName);
        String[] array = new String[locationInfos.size()];
        int i = 0;
        for (SatelliteInfo satelliteInfo : locationInfos) {
            array[i++] = satelliteInfo.name;
        }
        return array;
    }

    public SatelliteInfo getSatelliteInfoBySatelliteNameCity(String satelliteName, String city){
        ArrayList<SatelliteInfo>  locationInfos = satellitesObjectMap.get(satelliteName);
        if (locationInfos != null && locationInfos.size() > 0){
            for (SatelliteInfo satelliteInfo : locationInfos) {
                if (satelliteInfo.name.equals(city)) {
                    return satelliteInfo;
                }
            }
        }
        return null;
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
        ITEM_MAP.put(id, item);

        // 修改省份城市map数据
        LocationInfo item = cities.get(itemIndex);
        String province = item.getSatelliteName();
        ArrayList<LocationInfo> arrayList = provinceObjectMap.get(province);
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

    public void deleteItem(SatelliteInfo satelliteInfo){
        ITEMS.remove(satelliteInfo);
        ITEM_MAP.remove(satelliteInfo.mId.toString());
        satellites.remove(satelliteInfo);

        // 删除卫星极化map数据
        LocationInfo item = cities.get(position);
        String province = item.getSatelliteName();
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
