package com.edroplet.qxx.saneteltabactivity.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.download.DownloadAdapter;
import com.edroplet.qxx.saneteltabactivity.control.DLFrameCallback;
import com.edroplet.qxx.saneteltabactivity.services.down.DLDownloadListener;
import com.edroplet.qxx.saneteltabactivity.services.down.DownloadInit;
import com.edroplet.qxx.saneteltabactivity.utils.downloadmanager.fileload.FileCallback;
import com.edroplet.qxx.saneteltabactivity.utils.downloadmanager.fileload.FileResponseBody;
import com.tamic.rx.fastdown.callback.IDLCallback;
import com.tamic.rx.fastdown.client.DLClientFactory;
import com.tamic.rx.fastdown.client.Type;
import com.tamic.rx.fastdown.content.DownLoadInfo;
import com.tamic.rx.fastdown.core.DownLoadInfoFactory;
import com.tamic.rx.fastdown.core.Download;
import com.tamic.rx.fastdown.core.Priority;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

import static com.tamic.rx.fastdown.client.Type.NORMAL;


/**
 * Created by zs on 2016/7/8.
 */
public class DownLoadService extends Service {

    /**
     * 目标文件存储的文件夹路径
     */
    private String  destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "M_DEFAULT_DIR";
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "4b6d9a8a-c32a-11e7-b07b-90e2ba73b3f0.zip";

    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private Retrofit.Builder retrofit;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        init();
        loadFile();
        return super.onStartCommand(intent, flags, startId);
    }

    public int getPreProgress() {
        return preProgress;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // private static final String baseUrl = "http://112.124.9.133:8080/parking-app-admin-1.0/android/manager/adminVersion/";
    private static final String baseUrl = "http://123.59.23.183/assets/2000004/"; // 4b6d9a8a-c32a-11e7-b07b-90e2ba73b3f0.zip

    public static DownloadAdapter downloadAdapter;
    private void init() {

        DownloadInit.init(getBaseContext());

        new Download.ConfigBuilder<>()
                .addMaxCount(5)
                .downloadListener(new DLDownloadListener(this.getBaseContext()))
                .baseClient(DLClientFactory.createClient(Type.NORMAL, getBaseContext()))
                .newbuild(this, downloadAdapter);
       /* RxDownloadManager manager = RxDownloadManager.getInstance();
        manager.init(getBaseContext(), null);
        manager.setContext(getBaseContext());
        manager.setListener(new DLDownloadListener(getBaseContext()));


        DLNormalCallback normalCallback = new DLNormalCallback();
        if (manager.getClient() != null) {
            manager.getClient().setCallback(normalCallback);
        }*/

    }
    /**
     * 下载文件
     */
    private void loadFile1() {
        initNotification();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        retrofit.baseUrl(baseUrl)
                .client(initOkHttpClient())
                .build()
                .create(IFileLoad.class)
                .loadFile()
                .enqueue(new FileCallback(destFileDir, destFileName) {

                    @Override
                    public void onSuccess(File file) {
                        Log.e("zs", "请求成功");
                        // 安装软件
                        cancelNotification();
                        // installApk(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        Log.e("zs", progress + "----" + total);
                        updateNotification(progress * 100 / total);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("zs", "请求失败");
                        cancelNotification();
                    }
                });
    }

    void loadFile() {
        initNotification();
        new Download.Builder()
                .url(baseUrl+destFileName)
                .priority(Priority.HIGH)
                .savepath(destFileDir)
                .isImplicit(false) // 是否显示UI
                .channel(3000)
                .client(DLClientFactory.createClient(NORMAL, this))
                // .setCallback(new DLFrameCallback())
                .setCallback(idlCallback)
                .build(this)
                .start();
    }

    public interface IFileLoad {
        @GET("download")
        Call<ResponseBody> loadFile();
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行意图进行安装
        mContext.startActivity(install);
    }

    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

    /**
     * 初始化Notification通知
     */
    public void initNotification() {
        builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("0%")
                .setContentTitle("星网卫通APP下载进度")
                .setProgress(100, 0, false);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    /**
     * 更新通知
     */
    public void updateNotification(long progress) {
        int currProgress = (int) progress;
        if (preProgress < currProgress) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, (int) progress, false);
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
        preProgress = (int) progress;
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }

    private IDLCallback idlCallback =  new IDLCallback() {
        @Override
        public void onStart(String key, long fileLength, long downloaded, String savePath, String filenNme) {

        }

        @Override
        public void onSuccess(String key, long fileLength, long downloaded, String savePath, String filenNme, long aSpeed, String aAppiconName) {
            cancelNotification();
        }

        @Override
        public void onAppSuccess(String key, long fileLength, long downloaded, String savePath, String filenNme, long aSpeed, String aAppiconName, int downloadType, int appType) {

        }

        @Override
        public void onFail(String key, long downloaded, String savePath, String filenNme, String aErrinfo) {
            cancelNotification();
        }

        @Override
        public void onCancel(String key, long fileLength, long downloaded, String savePath, String filenNme) {
            cancelNotification();
        }

        @Override
        public void onPause(String key, long fileLength, long downloaded, String savePath, String filenNme) {

        }

        @Override
        public void onDownloading(String key, long fileLength, long downloadLength, long speed, String fileName, int downloadType) {

            Log.e("DownloadService", downloadLength + "----" + fileLength);
            updateNotification(downloadLength * 100 / fileLength);
        }

        @Override
        public void onRefresh(List<DownLoadInfo> infos) {

        }
    };
}
