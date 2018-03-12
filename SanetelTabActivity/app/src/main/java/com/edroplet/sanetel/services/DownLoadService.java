package com.edroplet.sanetel.services;

import android.app.DownloadManager;
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
import android.webkit.MimeTypeMap;

import com.edroplet.sanetel.activities.main.MainMeAboutBrowserActivity;
import com.edroplet.sanetel.activities.main.MainMeAppActivity;
import com.edroplet.sanetel.adapters.download.DownloadAdapter;
import com.edroplet.sanetel.beans.AppVersion;
import com.edroplet.sanetel.services.down.DLDownloadListener;
import com.edroplet.sanetel.services.down.DLNormalCallback;
import com.edroplet.sanetel.utils.FileUtils;
import com.edroplet.sanetel.services.network.SystemServices;
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
 * 下载服务
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
        if(intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                AppVersion appVersion = (AppVersion) bundle.getSerializable("appVersion");
                if (appVersion != null) {
                    destFileName = appVersion.getApkName();
                    sha1 = appVersion.getSha1();
                    baseUrl = appVersion.getUrl();
                    downloadUrl = baseUrl + destFileName + "?hash=" + sha1 + "&tag=" + tagUuid;
                }
                String pdfDownloadUrl = bundle.getString(KEY_DOWNLOAD_URL);
                if (pdfDownloadUrl != null && !pdfDownloadUrl.isEmpty()) {
                    downloadUrl = pdfDownloadUrl;
                    downloadPdf = true;
                }
            }
            init();
        }
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

        try {
            // 发送广播通知Activity下载路径
            Intent sendPathIntent = new Intent(MainMeAppActivity.SERVICE_DOWNLOAD_RECEIVER);
            sendPathIntent.putExtra(MainMeAppActivity.DOWNLOAD_SAVE_PATH_KEY, apkFileFullPath);
            getApplicationContext().sendBroadcast(sendPathIntent);
            // 系统下载管理器
            // 创建下载任务,downloadUrl就是下载链接
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
            // 漫游网络是否可以下载
            request.setAllowedOverRoaming(false);
            //设置文件类型，可以在下载结束后自动打开该文件
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
            request.setMimeType(mimeString);

            //在通知栏中显示，默认就是显示的
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setVisibleInDownloadsUi(true);
            //指定下载路径和下载文件名
            //sdcard的目录下的download文件夹，必须设置
            request.setDestinationInExternalPublicDir("/download/", destFileName);
            //request.setDestinationInExternalFilesDir(apkFileFullPath),也可以自己制定下载路径

            //获取下载管理器
            DownloadManager downloadManager= (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            //将下载任务加入下载队列，否则不会进行下载
            long mTaskId  = downloadManager.enqueue(request);
            // 发送广播通知Activity
            Intent sendIntent = new Intent(MainMeAppActivity.SERVICE_DOWNLOAD_RECEIVER);
            sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_TASK_ID_KEY, mTaskId);
            getApplicationContext().sendBroadcast(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
            //设置点击栏目知想打开的页面
            RxConstants.CLASSNAME = "MainMeAppActivity";

            final RxDownloadManager manager = RxDownloadManager.getInstance();
            manager.init(mContext, new DownloadAdapter());
            manager.setContext(mContext);
            manager.setListener(new DLDownloadListener(mContext));
            DLNormalCallback normalCallback = new DLNormalCallback() {
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
                    if (downloadPdf) {
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
                            SystemServices.installApk(mContext, apkFile);
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
                    } else if (downloadPdf) {
                        // 发送广播通知Activity
                        Intent sendIntent = new Intent(MainMeAboutBrowserActivity.SERVICE_PDF_DOWNLOAD_RECEIVER);
                        sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, -1);
                        getApplicationContext().sendBroadcast(sendIntent);
                    } else {
                        try {
                            // 发送广播通知Activity
                            Intent sendIntent = new Intent(MainMeAppActivity.SERVICE_DOWNLOAD_RECEIVER);
                            sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, -1);
                            getApplicationContext().sendBroadcast(sendIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                            SystemServices.installApk(mContext, apkFile);
                        } else {
                            sendIntent.putExtra(MainMeAppActivity.DOWNLOAD_PROCESS_KEY, -3);
                            getApplicationContext().sendBroadcast(sendIntent);
                        }
                    } else if (downloadPdf) {
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
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }

}
