package com.edroplet.sanetel.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.edroplet.sanetel.beans.LocationInfo;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.services.network.SystemServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

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

    public ArrayList<SatelliteInfo> loadSatellite() throws IOException, JSONException, NullPointerException {
        ArrayList<SatelliteInfo> objects = new ArrayList<SatelliteInfo>();
        BufferedReader reader = null;
        assert  mContext!= null;
        try {
            boolean firstRead =  CustomSP.getBoolean(mContext, CustomSP.firstReadSatellites, true);

            StringBuilder jsonString = new StringBuilder();
            if (firstRead) {
                //获取assets资源管理器
                AssetManager assetManager = mContext.getAssets();
                InputStream in;
                in = assetManager.open(this.mJsonFileName);
                CustomSP.putBoolean(mContext, CustomSP.firstReadSatellites, false);
                reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                SystemServices.copyAssetsFiles2FileDir(mContext,this.mJsonFileName);
            }else {
                FileInputStream fin = mContext.openFileInput(this.mJsonFileName);
                Scanner s = new Scanner(fin);
                while (s.hasNext()) {
                    jsonString.append(s.next());
                }
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                objects.add(new SatelliteInfo(array.getJSONObject(i)));
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

    public void saveSatellites(ArrayList <SatelliteInfo> satelliteInfos) throws JSONException, IOException{
        assert  mContext!= null;
        JSONArray array = new JSONArray();
        for (SatelliteInfo s : satelliteInfos)
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
            boolean firstRead =  CustomSP.getBoolean(mContext, CustomSP.firstReadCities, true);

            StringBuilder jsonString = new StringBuilder();
            if (firstRead) {
                //获取assets资源管理器
                AssetManager assetManager = mContext.getAssets();
                InputStream in;
                in = assetManager.open(this.mJsonFileName);
                CustomSP.putBoolean(mContext, CustomSP.firstReadCities, false);
                reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                SystemServices.copyAssetsFiles2FileDir(mContext,this.mJsonFileName);
            }else {
                FileInputStream fin = mContext.openFileInput(this.mJsonFileName);
                Scanner s = new Scanner(fin);
                while (s.hasNext()) {
                    jsonString.append(s.next());
                }
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
