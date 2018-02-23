package com.edroplet.sanetel.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.main.MainMeAboutBrowserActivity;
import com.edroplet.sanetel.activities.main.MainMeAppActivity;
import com.edroplet.sanetel.adapters.download.DownloadAdapter;
import com.edroplet.sanetel.beans.AppVersion;
import com.edroplet.sanetel.control.DLFrameCallback;
import com.edroplet.sanetel.services.down.DLDownloadListener;
import com.edroplet.sanetel.services.down.DLNormalCallback;
import com.edroplet.sanetel.services.down.DownloadInit;
import com.edroplet.sanetel.utils.FileUtils;
import com.tamic.rx.fastdown.RxConstants;
import com.tamic.rx.fastdown.client.DLClientFactory;
import com.tamic.rx.fastdown.core.Download;
import com.tamic.rx.fastdown.core.Priority;
import com.tamic.rx.fastdown.core.RxDownLoadCenter;
import com.tamic.rx.fastdown.core.RxDownloadManager;

import java.io.File;
import java.util.UUID;

import static com.edroplet.sanetel.activities.main.MainMeAboutBrowserActivity.KEY_DOWNLOAD_URL;
import static com.tamic.rx.fastdown.client.Type.NORMAL;


/**
 * Created by zs on 2016/7/8.
 */
public class DownLoadService extends Service {

    public static final String KEY_TARGET_DIR="KEY_TARGET_DIR";
    /**
     * 目标文件存储的文件夹路径
     */
    private String  destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "Download";
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "4b6d9a8a-c32a-11e7-b07b-90e2ba73b3f0.zip";

    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    private String downloadUrl;
    private  String baseUrl = "http://123.59.23.183/assets/000/"; // 4b6d9a8a-c32a-11e7-b07b-90e2ba73b3f0.zip
    String sha1 = "";
    String apkFileFullPath;
    boolean downloadPdf;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            AppVersion appVersion = (AppVersion)bundle.getSerializable("appVersion");
            if (appVersion != null) {
                destFileName = appVersion.getApkName();
                sha1 = appVersion.getSha1();
                baseUrl = appVersion.getUrl();
                downloadUrl = baseUrl+destFileName + "?hash=" + sha1+"&tag="+tagUuid;
            }
            String pdfDownloadUrl = bundle.getString(KEY_DOWNLOAD_URL);
            if (pdfDownloadUrl !=null && !pdfDownloadUrl.isEmpty()){
                downloadUrl = pdfDownloadUrl;
                downloadPdf = true;
            }
        }
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    private final String tagUuid = UUID.randomUUID().toString();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // public static DownloadAdapter downloadAdapter;
    private void init() {

        apkFileFullPath = destFileDir+"/"+destFileName;

        //设置点击栏目知想打开的页面
        RxConstants.CLASSNAME = "MainMeAppActivity";

        final RxDownloadManager manager = RxDownloadManager.getInstance();
        manager.init(mContext, new DownloadAdapter());
        manager.setContext(mContext);
        manager.setListener(new DLDownloadListener(mContext));
        DLNormalCallback normalCallback = new DLNormalCallback(){
            @Override
            public void onDownloading(String key, long filelength, long downloaded, long speed, String filename, int downloadType) {
                super.onDownloading(key, filelength, downloaded, speed, filename, downloadType);
                // 只有本文件下载的时候才广播
                if (key.contains(tagUuid)) {
                    preProgress = (int) (downloaded * 100 / filelength);
                    // 发送广播通知Activity
                    Intent sendIntent = new Intent(MainMeAppActivity.SERVICE_DOWNLOAD_RECEIVER);
                    sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, preProgress);
                    getApplicationContext().sendBroadcast(sendIntent);
                }
                if (downloadPdf){
                    preProgress = (int) (downloaded * 100 / filelength);
                    // 发送广播通知Activity
                    Intent sendIntent = new Intent(MainMeAboutBrowserActivity.SERVICE_PDF_DOWNLOAD_RECEIVER);
                    sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, preProgress);
                    getApplicationContext().sendBroadcast(sendIntent);
                }
            }

            @Override
            public void onAppSuccess(String tag, long fileLength, long downloaded, String savePath, String filenNme, long aSpeed, String aAppiconName, int downloadType, int appType) {
                super.onAppSuccess(tag, fileLength, downloaded, savePath, filenNme, aSpeed, aAppiconName, downloadType, appType);
                if (!downloadPdf && tag != null && tag.contains(tagUuid)) {
                    // 发送广播通知Activity
                    Intent sendIntent = new Intent(MainMeAppActivity.SERVICE_DOWNLOAD_RECEIVER);
                    if (FileUtils.getFileSHA1(apkFileFullPath).equals(sha1)) {
                        sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, 100);
                        getApplicationContext().sendBroadcast(sendIntent);
                        File apkFile = new File(apkFileFullPath);
                        installApk(mContext, apkFile);
                    } else {
                        sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, -3);
                        getApplicationContext().sendBroadcast(sendIntent);
                    }
                }
            }

            @Override
            public void onFail(String tag, long downloaded, String aFilepath, String aFilename, String aErrinfo) {
                super.onFail(tag, downloaded, aFilepath, aFilename, aErrinfo);
                if (tag != null && tag.contains(tagUuid)) {
                    // 发送广播通知Activity
                    Intent sendIntent = new Intent(MainMeAppActivity.SERVICE_DOWNLOAD_RECEIVER);
                    sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, -1);
                    getApplicationContext().sendBroadcast(sendIntent);
                }else if (downloadPdf){
                    // 发送广播通知Activity
                    Intent sendIntent = new Intent(MainMeAboutBrowserActivity.SERVICE_PDF_DOWNLOAD_RECEIVER);
                    sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, -1);
                    getApplicationContext().sendBroadcast(sendIntent);
                }

            }

            @Override
            public void onSuccess(String tag, long fileLength, long downloaded, String savePath, String filenNme, long aSpeed, String aAppiconName) {
                super.onSuccess(tag, fileLength, downloaded, savePath, filenNme, aSpeed, aAppiconName);
                if (!downloadPdf && tag != null && tag.contains(tagUuid)) {
                    // 发送广播通知Activity
                    Intent sendIntent = new Intent(MainMeAppActivity.SERVICE_DOWNLOAD_RECEIVER);
                    if (FileUtils.getFileSHA1(apkFileFullPath).equals(sha1)) {
                        sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, 100);
                        getApplicationContext().sendBroadcast(sendIntent);
                        File apkFile = new File(apkFileFullPath);
                        installApk(mContext, apkFile);
                    } else {
                        sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, -3);
                        getApplicationContext().sendBroadcast(sendIntent);
                    }
                }else if (downloadPdf){
                    // 发送广播通知Activity
                    Intent sendIntent = new Intent(MainMeAboutBrowserActivity.SERVICE_PDF_DOWNLOAD_RECEIVER);
                    sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, 100);
                    sendIntent.putExtra(KEY_TARGET_DIR, destFileDir);
                    getApplicationContext().sendBroadcast(sendIntent);
                }
            }
        };
        if (manager.getClient() != null) {
            manager.getClient().setCallback(normalCallback);
        }
        RxDownLoadCenter.getInstance(mContext).loadTask();

        // 如果存在文件删除
        FileUtils.deleteFile(apkFileFullPath);

        /**
         * 下载文件
         */

        new Download.Builder()
                .url(downloadUrl)
                .priority(Priority.HIGH)
                .savepath(destFileDir)
                .isImplicit(false) // 是否显示UI
                .channel(3000)
                .tag(tagUuid)
                .client(DLClientFactory.createClient(NORMAL, this))
                //  .setCallback(idlCallback)
                .build(this)
                .start();
    }
    /**
     * 安装软件
     *
     * @param file
     */
    public static void installApk(Context context, File file) {
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行意图进行安装
        context.startActivity(install);
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }

}
