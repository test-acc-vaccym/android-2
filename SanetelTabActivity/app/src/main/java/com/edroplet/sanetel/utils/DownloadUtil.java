package com.edroplet.sanetel.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by qxs on 2017/10/30.
 */

public class DownloadUtil {
    private Context mContext;
    private long mTaskId;
    private DownloadManager downloadManager;
    private String downloadPath;

    public DownloadUtil(Context context){
        mContext = context;
    }

    public void getFile(String downloadUrl, String saveFileName) {
        // 使用的是系统提供的下载管理器进行下载，从API 9就开始支持了，所以不用担心在兼容问题
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("/download/", saveFileName);
        //获取下载管理器
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
    }

    //使用系统下载器下载
    public void downloadAPK(String versionUrl, String versionName) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/download/", versionName);
        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        //将下载请求加入下载队列
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);
        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }
    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };
    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    MLog.i(">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    MLog.i(">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    MLog.i(">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    MLog.i(">>>下载完成");
                    //下载完成安装APK
                    //downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + versionName;
                    installAPK(new File(downloadPath));
                    break;
                case DownloadManager.STATUS_FAILED:
                    MLog.i(">>>下载失败");
                    break;
            }
        }
    }

    //下载到本地后执行安装
    protected void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


}
