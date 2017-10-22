package com.edroplet.qxx.saneteltabactivity.beans;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Created by qxs on 2017/10/21.
 */

public class CollectHistoryFileInfo {
    private String fileName;
    private String dateTime;
    private Context context;
    private static final String historyJsonFileName = "historyFileInfo.json";

    public CollectHistoryFileInfo setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public CollectHistoryFileInfo setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public CollectHistoryFileInfo(Context context){
        this.context = context;
    }

    public CollectHistoryFileInfo(String fileName, String dateTime){
        this.dateTime = dateTime;
        this.fileName = fileName;
    }

    public JSONObject toJson(){
        JSONObject historyRecord = new JSONObject();
        try {
            historyRecord.put("fileName", this.fileName);
            historyRecord.put("dateTime",this.dateTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return historyRecord;
    }

    public String toString(){
        return toJson().toString();
    }

    public JSONArray read(){
        FileInputStream in = null;
        Scanner s = null;
        StringBuffer sb = new StringBuffer();
        try {
            in = context.openFileInput(historyJsonFileName);
            s = new Scanner(in);
            while (s.hasNext()) {
                sb.append(s.next());
            }
            JSONArray jsonArray = new JSONArray(sb.toString());
            return jsonArray;
        } catch (JSONException je){
            je.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CollectHistoryFileInfo> getList(){
        List<CollectHistoryFileInfo> collectHistoryFileInfos = null;
        try {
            JSONArray jsonArray = read();
            if (jsonArray != null){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject inf_Array = jsonArray.getJSONObject(i);
                    collectHistoryFileInfos.add(new CollectHistoryFileInfo(inf_Array.getString("fileName"), inf_Array.getString("dateTime")));
                }
            }
        }catch (JSONException je){
            je.printStackTrace();
        }
        return collectHistoryFileInfos;
    }

    public void save(){
        FileOutputStream out = null;
        PrintStream ps = null;
        try {
            out = context.openFileOutput(historyJsonFileName, Activity.MODE_APPEND);
            ps = new PrintStream(out);
            ps.println(toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                    ps.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
