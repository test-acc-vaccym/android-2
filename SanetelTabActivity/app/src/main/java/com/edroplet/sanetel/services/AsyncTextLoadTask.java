package com.edroplet.sanetel.services;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.view.View;

import com.edroplet.sanetel.activities.functions.ReaderTextActivity;

import java.io.*;

/**
 * Created by qxx on 2017/11/2.
 */

public class AsyncTextLoadTask extends AsyncTask<Object, String, String> {
    public static final String CollectFileLoadKey="CollectFileLoadPage";
    private Context context;
    private ReaderTextActivity activity;
    private BufferedReader br;
    private int page;
    private static LruCache<String, StringBuilder> mMemoryCache;

    public AsyncTextLoadTask(Context context, BufferedReader br, int page) {
        this.context = context;
        this.br = br;
        this.page = page;
        if (mMemoryCache == null){
            // 获取应用程序最大可用内存
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 16;
            // 设置文字缓存大小为程序最大可用内存的1/16
            mMemoryCache = new LruCache<String, StringBuilder>(cacheSize) {
                @Override
                protected int sizeOf(String key, StringBuilder sb) {
                    return sb.toString().length();
                }
            };
        }
        activity = (ReaderTextActivity)context;
    }

    @Override
    protected String doInBackground(Object... params) {
        StringBuilder paragraph = new StringBuilder();
        try {
            StringBuilder paragraphCache = mMemoryCache.get(CollectFileLoadKey + page);
            if (paragraphCache == null) {
                String line = "";
                int index = 0;
                Log.e("ReaderText", "============doInBackground: " + page);
                while (index < 50 && (line = br.readLine()) != null) {
                    paragraph.append(line + "\n");
                    index++;
                    Log.e("ReaderText", "============doInBackground while: " + line);
                }
                mMemoryCache.put(CollectFileLoadKey + page, paragraph);
            }else {
                return paragraphCache.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return paragraph.toString();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result.length() > 0) {
            activity.getReaderTextContent().setText(result);
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                activity.getReadTextScroll().scrollTo(0, 0); // 记载完新数据后滚动到顶部
            }
        }, 100);
        activity.setLoading(false);
    }
}
