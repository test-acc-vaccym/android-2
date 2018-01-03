package com.edroplet.qxx.saneteltabactivity.services;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.activities.functions.ReaderTextActivity;

import java.io.*;

/**
 * Created by qxx on 2017/11/2.
 */

public class AsyncTextLoadTask extends AsyncTask<Object, String, String> {
    private Context context;
    private ReaderTextActivity activity;
    private BufferedReader br;

    public AsyncTextLoadTask(Context context, BufferedReader br) {
        this.context = context;
        this.br = br;
        activity = (ReaderTextActivity)context;
    }

    @Override
    protected String doInBackground(Object... params) {
        StringBuilder paragraph = new StringBuilder();
        try {

            String line = "";

            int index = 0;
            while(index < 50 && (line = br.readLine()) != null){
                paragraph.append(line + "\n");
                index++;
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
