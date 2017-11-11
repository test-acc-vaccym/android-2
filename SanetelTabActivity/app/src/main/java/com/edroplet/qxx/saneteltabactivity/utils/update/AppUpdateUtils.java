package com.edroplet.qxx.saneteltabactivity.utils.update;

/**
 * Created by qxs on 2017/11/10.
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.edroplet.qxx.saneteltabactivity.R;

public class AppUpdateUtils {
    private static Context mContext;
    private static int mVersionCode;
    private static AppVersion mAppVersion;
    private static String mVersionName;
    private static String mFileName;
    private static ProgressDialog mProgressDialog;
    private static String mLocalFilepath;
    private static DownloadFileAsyncTask mDownloadFileAsyncTask;
    private static boolean hasCancel = false;
    public static final String TAG = "AppUpDate";
    // 如果是自动更新，那么就不跳出已经是最新版本的对话框。否则用户点击更新应用按钮，弹出已经是最新版本的提示框
    private static boolean mIsAuto = false;
    private static boolean mShowProgressDialog = false;
    private static final int NOTIFY_ID = 54;
    private static NotificationManager mNotificationManager;
    private static NotificationCompat.Builder mNotification;

    /**
     * 初始化
     *
     * @param context
     *            执行上下文
     * @param newAv
     *            对比版本
     * @param isauto
     *            指示是否是自动升级，当版本号没变时，true无响应，false弹出已是最新版的对话框。
     * @param showProgressDialog
     *            true弹出下载进度对话框，false为通知消息提示进度
     */
    public static void init(Context context, AppVersion newAv, boolean isauto,
                            boolean showProgressDialog) {
        mIsAuto = isauto;
        mShowProgressDialog = showProgressDialog;
        mContext = context;
        initNotification(context);
        mVersionCode = getVerCode(mContext);
        mVersionName = getVerName(mContext);
        mAppVersion = newAv;
        mFileName = mAppVersion.getApkName();
        File sdDir = null;

        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            // 获取根目录
            sdDir = Environment.getExternalStorageDirectory();
        }

        // 注意FileOutputStream不会自动创建路径，所以初始化的时候要主动创建路径。
        String dirpath;
        if (sdDir != null) {
            // AppName为你想保存的路径，一般为应用目录
            dirpath = sdDir.toString() + "/AppName/";
        } else {
            dirpath = "/AppName/";
        }
        File dir = new File(dirpath);
        if (!dir.exists()) {
            dir.mkdir();// 如果路径不存在就先创建路径
        }
        mLocalFilepath = dirpath + mFileName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    private static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    private static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verName;
    }

    /**
     * 更新应用
     */
    public static void upDate() {
        if (mVersionCode < mAppVersion.getVerCode()) {
            doNewVersionUpdate();
        } else {
            notNewVersionShow();
        }
    }

    /**
     * 不执行更新
     */
    private static void notNewVersionShow() {
        // 如果不是自动升级
        if (!mIsAuto) {
            StringBuffer sb = new StringBuffer();
            sb.append("当前版本:");
            sb.append(mVersionName);
            sb.append(",\n已是最新版,无需更新。");
            // 这里的提示框是我自定义的
            AlertDialog ad = new AlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setMessage(sb.toString())
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // 取消
                                    dialog.dismiss();
                                }
                            }).create();
            ad.show();
        }
    }

    /**
     * 更新应用提示
     */
    private static void doNewVersionUpdate() {
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(mVersionName);
        sb.append(", 发现新版本:");
        sb.append(mAppVersion.getVersionName()).append("\n");
        sb.append("更新内容：\n");
        // 这里我们使用;作为分隔符，显示多条跟新内容信息。
        if (mAppVersion.getContent().contains(";")) {
            String[] up = mAppVersion.getContent().split(";");
            for (String s : up) {
                sb.append(s).append("\n");
            }
        } else {
            sb.append(mAppVersion.getContent()).append("\n");
        }
        sb.append("是否更新?");
        AlertDialog ad = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage(sb.toString())
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        updateFile(mAppVersion.getUrl());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("暂不更新",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 点击"取消"按钮之后退出程序
                                dialog.dismiss();
                            }
                        }).create();// 创建
        // 显示对话框
        ad.show();
    }

    // 执行应用更新
    private static void updateFile(final String path) {
        // pBar.show();

        File file = new File(mLocalFilepath);

        // 根据服务器传回的更新包sha1进行比对,判断是否下载完成，如果不匹配，则重新下载，否则直接打开。
        if (mAppVersion.getSha1() != null) {
            if (getFileSHA1(file).toUpperCase().equals(mAppVersion.getSha1())) {
                openFile(file);
            } else {
                try {
                    // downloadFile(path, localFilepath);
                    mDownloadFileAsyncTask = new DownloadFileAsyncTask();
                    mDownloadFileAsyncTask.execute(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                mDownloadFileAsyncTask = new DownloadFileAsyncTask();
                mDownloadFileAsyncTask.execute(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消更新
     */
    public void cancelUpDate() {
        if (mDownloadFileAsyncTask != null) {
            if (!mDownloadFileAsyncTask.isCancelled()) {
                mDownloadFileAsyncTask.cancel(true);
            }
        }
    }

    /**
     * 应用下载Task
     *
     */
    private static class DownloadFileAsyncTask extends
            AsyncTask<String, Integer, String> {

        // 后台下载任务
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                URLConnection connection;
                connection = url.openConnection();
                // 2.2以上默认用“gzip”，这里要取消使用，否则大小永远为-1.
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();
                int length = connection.getContentLength();
                InputStream input = new BufferedInputStream(
                        connection.getInputStream());
                FileOutputStream output = new FileOutputStream(mLocalFilepath);
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (!hasCancel) {
                        total += count;
                        publishProgress((int) (total * 100 / length));
                        output.write(data, 0, count);
                    } else {
                        cancel(true);
                        break;
                    }
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        // 进度更新
        private int tempv;

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            // 如果需要用到进度提示框，则执行
            if (mShowProgressDialog) {
                if (values[0] != tempv) {
                    mProgressDialog.setProgress(values[0]);
                }
            } else {
                // 防止多次重复提示，影响性能
                if (values[0] != tempv) {
                    RemoteViews contentView = mNotification.getContentView();
                    contentView.setTextViewText(R.id.rate,
                            String.valueOf(values[0]) + "%");
                    contentView.setProgressBar(R.id.progress, 100, values[0],
                            false);
                    mNotificationManager.notify(NOTIFY_ID, mNotification.build());
                    tempv = values[0];
                }
            }
        }

        // 下载完成后执行
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (mShowProgressDialog) {
                openFile(new File(mLocalFilepath));
            } else {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_VIEW);
                // 设定intent的file与MimeType
                intent.setDataAndType(Uri.fromFile(new File(mLocalFilepath)),
                        "application/vnd.android.package-archive");

                // 下载完毕后变换通知形式
                mNotification.setAutoCancel(true);
                mNotification.setCustomContentView(null);
                // Intent intent = new Intent(mContext, FileMgrActivity.class);
                // // 告知已完成
                // intent.putExtra("completed", "yes");
                // //更新参数,注意flags要使用FLAG_UPDATE_CURRENT
                PendingIntent contentIntent = PendingIntent.getActivity(
                        mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mNotification.setContentText("下载完成\n"+
                        "文件已下载完毕，点击进行安装。");
                mNotification.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFY_ID, mNotification.build());
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            hasCancel = false;
            if (mShowProgressDialog) {
                showProgressDialog();
            } else {
                tempv = 0;
                int icon = R.drawable.ic_launcher;
                CharSequence tickerText = "开始下载";
                long when = System.currentTimeMillis();
                initNotification(mContext);
               // 在通知栏上点击此通知后自动清除此通知
                mNotification.setAutoCancel(true);

                RemoteViews contentView = new RemoteViews(
                        mContext.getPackageName(), R.layout.notification_update);
                contentView.setTextViewText(R.id.fileName, "更新文件下载中……");
                // 指定个性化视图
                mNotification.setCustomContentView(contentView) ;
                Intent intent = new Intent();
                PendingIntent contentIntent = PendingIntent.getActivity(
                        mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                // 指定内容意图
                mNotification.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFY_ID, mNotification.build());
            }
        }
    }

    // 在手机上打开文件
    private static void openFile(File f) {
        // mProgressDialog.dismiss();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        // 设定intent的file与MimeType，安装文件
        intent.setDataAndType(Uri.fromFile(f),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    // 使用自带算法 获取文件的SHA1
    private static String getFileSHA1(File file) {
        if (file.exists()) {
            MessageDigest digest = null;
            byte buffer[] = new byte[1024];
            int len;
            try {
                digest = MessageDigest.getInstance("SHA-1");// ("SHA-1");
                FileInputStream in = new FileInputStream(file);
                while ((len = in.read(buffer, 0, 1024)) != -1) {
                    digest.update(buffer, 0, len);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

            // 直接用这玩意转换成16进制，碉堡了、
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);

        } else {
            return "";
        }
    }

    /**
     * 显示下载进度对话框，可以取消下载任务。
     */
    private static void showProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (mDownloadFileAsyncTask != null) {
                    if (!mDownloadFileAsyncTask.isCancelled()) {
                        mDownloadFileAsyncTask.cancel(true);
                        hasCancel = true;
                    }
                }
            }
        });
        mProgressDialog.setTitle("正在下载");
        mProgressDialog.setMax(100);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("请稍候...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }


    /**
     * 初始化Notification通知
     */
    public static void initNotification(Context mContext) {
        mNotification = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("0%")
                .setContentTitle("星网卫通APP下载进度")
                .setProgress(100, 0, false);
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFY_ID, mNotification.build());
    }

}