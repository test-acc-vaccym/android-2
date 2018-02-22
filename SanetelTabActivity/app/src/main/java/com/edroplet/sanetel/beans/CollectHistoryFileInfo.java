package com.edroplet.sanetel.beans;

import android.content.Context;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.DateTime;
import com.edroplet.sanetel.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by qxs on 2017/10/21.
 * 历史文件记录列表
 */

public class CollectHistoryFileInfo {
    private String fileName;
    private String dateTime;
    private Context context;
    private static final String historyJsonFileName = "historyFileInfo.json";
    public static final String KEY_NEWEST_COLLECT_FILE = "KEY_NEWEST_COLLECT_FILE";

    private String CollectFileFullPath;
    public static final String CollectFilePath="/logs/sanetel/";

    public CollectHistoryFileInfo setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public CollectHistoryFileInfo setFileName(String fileName) {
        if (fileName == null){
            return this;
        }
        CollectFileFullPath = FileUtils.getDataDir(context) + CollectFilePath;
        if (!FileUtils.isFileExist(CollectFileFullPath)){
            FileUtils.createDir(CollectFileFullPath);
        }

        if (fileName.startsWith(CollectFileFullPath)){
            this.fileName = fileName;
        } else {
            this.fileName = CollectFileFullPath + fileName;
        }
        return this;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public String getFileName() {
        return this.fileName;
    }

    public CollectHistoryFileInfo(Context context){
        this.context = context;
    }

    public CollectHistoryFileInfo(Context context, String fileName, String dateTime){
        this.dateTime = dateTime;
        CollectFileFullPath = FileUtils.getDataDir(context) + CollectFilePath;
        if (!FileUtils.isFileExist(CollectFileFullPath)){
            FileUtils.createDir(CollectFileFullPath);
        }
        if (fileName.startsWith(CollectFileFullPath)){
            this.fileName = fileName;
        } else {
            this.fileName = CollectFileFullPath + fileName;
        }
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
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveHistoryFileList(List <CollectHistoryFileInfo> collectHistoryFileInfos) throws JSONException, IOException{
        JSONArray array = new JSONArray();
        for (CollectHistoryFileInfo chf : collectHistoryFileInfos)
            array.put(chf.toJson());

        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(historyJsonFileName,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static final String SAMPLEDATA=", new file\r\n";

    public List<CollectHistoryFileInfo> getList(){
        List<CollectHistoryFileInfo> collectHistoryFileInfos = new ArrayList<>();
        try {
            JSONArray jsonArray = read();
            if (jsonArray != null){
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject inf_Array = jsonArray.getJSONObject(i);
                    collectHistoryFileInfos.add(new CollectHistoryFileInfo(context, inf_Array.getString("fileName"), inf_Array.getString("dateTime")));
                }
            }
        }catch (JSONException je){
            je.printStackTrace();
        }
        return collectHistoryFileInfos;
    }

    public void save() throws IOException{

        JSONArray array = new JSONArray();

        List<CollectHistoryFileInfo> l = getList();
        List<String> lNames = new ArrayList<>();
        // 去重
        if (l != null && l.size() > 0) {
            for (CollectHistoryFileInfo collectHistoryFileInfo : l) {
                String nFile = collectHistoryFileInfo.getFileName();
                if (lNames.indexOf(nFile) == -1) {
                    lNames.add(nFile);
                    array.put(collectHistoryFileInfo.toJson());
                }
            }
        }
        if (lNames.indexOf(getFileName()) == -1) {
            array.put(toJson());
            // 创建新文件
            FileUtils.saveFile(this.fileName, DateTime.getCurrentDateTime() + SAMPLEDATA, false);
        }else {
            Toast.makeText(context, context.getString(R.string.main_collect_data_file_exist_prompt), Toast.LENGTH_SHORT);
        }

        // 保存列表文件
        FileUtils.savePrivateFile(context, historyJsonFileName, Context.MODE_PRIVATE, array.toString());

        setNewestCollectFile();

    }

    private void setNewestCollectFile(){
        CustomSP.putString(context, KEY_NEWEST_COLLECT_FILE, this.fileName);
    }

    public String getNewestCollectFile(){
        return CustomSP.getString(context, KEY_NEWEST_COLLECT_FILE, null);
    }

    public void clearFile() throws IOException{
        String newestFilename = getNewestCollectFile();
         FileUtils.saveFile( newestFilename, "", false);
    }
}
