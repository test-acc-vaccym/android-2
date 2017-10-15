package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.Nullable;

import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameterItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by qxs on 2017/9/11.
 */

public class JsonLoad {
    private String mJsonFileName;
    private Context mContext;

    public JsonLoad(Context context, String fileName){
        this.mContext = context;
        this.mJsonFileName =  fileName;
    }

    public ArrayList<SatelliteParameterItem> loadSatellite() throws IOException, JSONException, NullPointerException {
        ArrayList<SatelliteParameterItem> objects = new ArrayList<SatelliteParameterItem>();
        BufferedReader reader = null;
        assert  mContext!= null;
        try {
            //获取assets资源管理器
            AssetManager assetManager = mContext.getAssets();
            InputStream in;
            // in = mContext.openFileInput(this.mJsonFileName);
            in = assetManager.open(this.mJsonFileName);

            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                objects.add(new SatelliteParameterItem(array.getJSONObject(i)));
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if (reader != null) {
                reader.close();
            }
        }
        return objects;
    }

    public void saveSatellites(ArrayList <SatelliteParameterItem> satelliteParameterItems) throws JSONException, IOException{
        assert  mContext!= null;
        JSONArray array = new JSONArray();
        for (SatelliteParameterItem s : satelliteParameterItems)
            array.put(s.toJSON());

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mJsonFileName,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<LocationInfo> loadCities() throws IOException, JSONException, NullPointerException {
        ArrayList<LocationInfo> objects = new ArrayList<LocationInfo>();
        BufferedReader reader = null;
        assert  mContext!= null;
        try {
            //获取assets资源管理器
            AssetManager assetManager = mContext.getAssets();
            InputStream in;
            // in = mContext.openFileInput(this.mJsonFileName);
            in = assetManager.open(this.mJsonFileName);

            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                objects.add(new LocationInfo(array.getJSONObject(i)));
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if (reader != null) {
                reader.close();
            }
        }
        return objects;
    }
    public void saveCities(ArrayList <LocationInfo> locationInfo) throws JSONException, IOException{
        assert  mContext!= null;
        JSONArray array = new JSONArray();
        for (LocationInfo l : locationInfo)
            array.put(l.toJSON());

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mJsonFileName,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
