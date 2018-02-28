package com.edroplet.sanetel.services;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

/**
 * Created on 2018/2/23.
 *
 * @author qxs
 */

public class DownloadObserver extends ContentObserver {
    private Handler mHandler;
    private Context mContext;
    private int progress;
    private DownloadManager mDownloadManager;
    private DownloadManager.Query query;
    private Cursor cursor;
    @SuppressLint("NewApi")
    public DownloadObserver(Handler handler, Context context, long downId) {
        super(handler);
        this.mHandler = handler;
        this.mContext = context;
        mDownloadManager =  (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        query = new DownloadManager.Query().setFilterById(downId);
    }
    @SuppressLint("NewApi")
    @Override
    public void onChange(boolean selfChange) {
        // 每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
        super.onChange(selfChange);
        //
        boolean downloading = true;
        while (downloading) {
            cursor  = mDownloadManager.query(query);
            cursor.moveToFirst();
            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            progress = (int) ((bytes_downloaded * 100) / bytes_total);
            cursor.close();
            mHandler.sendEmptyMessageDelayed(progress, 100);
            if (cursor.getInt(
                    cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false;
            }
        }
    }
}
