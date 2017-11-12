package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.AppVersion;
import com.edroplet.qxx.saneteltabactivity.services.DownLoadService;
import com.edroplet.qxx.saneteltabactivity.utils.FileUtils;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.BrowseUrl;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.KEY_DOWNLOAD_URL;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.KEY_PDF_NAME;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.P120PdfName;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.SanetenDownloadUrl;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeAppActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String SERVICE_DOWNLOAD_RECEIVER = "com.edroplet.download.receiver";
    public static final String DOWNLOAD_PROCESS_KEY = "DOWNLOAD_PROCESS_KEY";
    private MsgReceiver msgReceiver;
    AppVersion appVersion;
    /**
     * 返回应用程序的版本号
     *
     * @return
     */
    private String getVersion(){
        //用来管理手机的APK(包管理器)
        PackageManager pm = getPackageManager();
        try {
            //得到指定APK的功能清单文件
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void getRemoteVersion(){
        HttpDownloaderTask httpDownloaderTask = new HttpDownloaderTask(getString(R.string.app_update_version_file_download_url));
        httpDownloaderTask.execute();
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_app);
        ViewInject.inject(this,this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_me_app_toolbar);
        toolbar.setTitle(R.string.main_me_app_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置版本号
        String input = getResources().getString(R.string.main_me_app_version);
        String ouput = String.format(input, String.format("%6s", getVersion()));
        ((CustomTextView) findViewById(R.id.main_me_app_version)).setText(ouput);
        findViewById(R.id.main_me_app_update).setOnClickListener(this);
        findViewById(R.id.main_me_app_recovery).setOnClickListener(this);
        findViewById(R.id.main_me_app_browse).setOnClickListener(this);
        findViewById(R.id.main_me_app_download).setOnClickListener(this);
    }

    public static void gotoURL(AppCompatActivity activity ,String url){
        Uri uri = Uri.parse(url);
        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @BindId(R.id.main_me_app_update_state)
    private CustomTextView appUpdateState;


    public static final String KEY_UPDATE_STATE = "KEY_UPDATE_STATE";

    private static class updateHandler extends Handler{
        private final WeakReference<MainMeAppActivity> mActivity;
        updateHandler(MainMeAppActivity activity){
            mActivity = new WeakReference<MainMeAppActivity>(activity);
        }
        private static String dots = ".";
        @Override
        public void handleMessage(Message msg) {
            MainMeAppActivity activity = mActivity.get();
            int  updateProgress = 0;
            if (msg.what == 0) {
                updateProgress = msg.getData().getInt(KEY_UPDATE_STATE);
            }else{
                updateProgress = msg.what;
            }

            if (activity != null){
                if (updateProgress == -3){
                    // 校验失败
                    activity.appUpdateState.setText(R.string.main_me_app_update_state_verify_failed);
                } else if (updateProgress == -2){
                    // 安装失败
                    activity.appUpdateState.setText(R.string.main_me_app_update_state_install_failed);
                }else if (updateProgress == -1){
                    // 下载失败
                    activity.appUpdateState.setText(R.string.main_me_app_update_state_download_failed);
                }else if (updateProgress == 0) {
                    // 检查中
                    activity.appUpdateState.setText(activity.getString(R.string.main_me_app_update_state_checking));
                }else if (updateProgress >= 100){
                    // 下载完成
                    activity.appUpdateState.setText(activity.getString(R.string.main_me_app_update_state_complete));
                } else {
                    // 下载中
                    if (dots.length() < 6) {
                        dots = dots + ".";
                    } else {
                        dots = ".";
                    }

                    String progress = String.format(activity.getString(R.string.main_me_app_update_state_downloading),
                            updateProgress, dots);

                    activity.appUpdateState.setText(progress);
                    activity.appUpdateState.setTextColor(Color.RED);
                    activity.appUpdateState.setGravity(Gravity.CENTER_VERTICAL);
                }
            }
        }
    }

    private  final updateHandler handler = new updateHandler(this);

    private Timer mTimer;
    private int updateProgress;
    private void setTimerTask() {
        if (null == mTimer) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO: 2017/11/10 获取下载进度
                // 通过广播获取进度
                Message message = new Message();
                message.what = updateProgress;
                handler.sendMessage(message);
            }
        }, 1000, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }


    Intent downloadIntent;
    @Override
    public void onClick(View view) {
        Intent intent = null;
        boolean skip = false;
        switch(view.getId()){
            case R.id.main_me_app_browse:
                // 20170617170632522.pdf
                gotoURL(this, BrowseUrl);
                break;
            case R.id.main_me_app_download:

                // http://www.sanetel.com/Content.aspx?PartNodeId=24
                intent = new Intent(MainMeAppActivity.this, MainMeAboutBrowserActivity.class);
                intent.putExtra(KEY_PDF_NAME,  P120PdfName);
                intent.putExtra(KEY_DOWNLOAD_URL, SanetenDownloadUrl);
                break;
            case R.id.main_me_app_recovery:
                SystemServices.restoreAPP(this, 2000);
                break;
            case R.id.main_me_app_update:
                skip = true;
                // todo 升级
                appUpdateState.setVisibility(View.VISIBLE);
                // 判断版本号
                getRemoteVersion();

                break;
            default:
                break;
        }
        if (!skip && intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null){
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }
        appUpdateState.setVisibility(View.GONE);
        if (null != downloadIntent)
            // 停止服务
            stopService(downloadIntent);

        // 注销广播
        if (msgReceiver!= null)
            unregisterReceiver(msgReceiver);

        super.onDestroy();
    }

    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*处理接收到的广播内容*/
            if (intent != null){
                updateProgress = intent.getIntExtra(DOWNLOAD_PROCESS_KEY,0);
            }
        }
    }

    public class HttpDownloaderTask extends AsyncTask<Object, String, String> {
        private URL url = null;
        private final String TAG = "TAG";
        private String urlString;

        public HttpDownloaderTask(String urlString){
            this.urlString = urlString;
        }

        @Override
        protected String doInBackground(Object... objects) {
            return download(urlString);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String updateVersion) {
            appVersion = new AppVersion();
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();  //取得SAXParserFactory实例
                SAXParser  parser = factory.newSAXParser();                  //从factory获取SAXParser实例
                AppVersion.AppVersionHandler appVersionHandler = new AppVersion.AppVersionHandler();
                parser.parse(new ByteArrayInputStream(updateVersion.getBytes()) , appVersionHandler);
                appVersion = appVersionHandler.getAppVersion().get(0);
                String currentVersion = getVersion();
                if (appVersion.getVersionName().compareToIgnoreCase(currentVersion) <= 0){
                    appUpdateState.setText(R.string.up_to_date);
                } else {
                    // 动态注册广播接收器
                    msgReceiver = new MsgReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(SERVICE_DOWNLOAD_RECEIVER);
                    registerReceiver(msgReceiver, intentFilter);
                    // 启动服务
                    downloadIntent = new Intent(MainMeAppActivity.this, DownLoadService.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("appVersion", appVersion);
                    downloadIntent.putExtras(bundle);
                    startService(downloadIntent);
                    setTimerTask();
                    //  handler.postDelayed(runner, 1000);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        /**
         * 读取文本文件
         * @param urlStr url路径
         * @return 文本信息
         * 根据url下载文件，前提是这个文件中的内容是文本，
         * 1.创建一个URL对象
         * 2.通过URL对象，创建一个Http连接
         * 3.得到InputStream
         * 4.从InputStream中得到数据
         */
        public String download(String urlStr) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            BufferedReader bufferedReader = null;

            try {
                url = new URL(urlStr);
                //创建http连接
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                //int statusCode = urlConn.getResponseCode();
                //使用IO流读取数据
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.e("TAG","下载txt文件");
            Log.e("TAG",sb.toString());
            return sb.toString();
        }

        /**
         * 读取任何文件
         * 返回-1 ，代表下载失败。返回0，代表成功。返回1代表文件已经存在
         *
         * @param urlStr
         * @param path
         * @param fileName
         * @return
         */
        public int downloadFile(String urlStr, String path, String fileName) {
            InputStream input = null;


            try {
                FileUtils fileUtil = new FileUtils();
                if (fileUtil.isFileExist(path + fileName)) {
                    return 1;
                } else {
                    input = getInputStreamFromUrl(urlStr);
                    File resultFile = fileUtil.write2SDFromInput(path,fileName,input);
                    if (resultFile == null)
                        return -1;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            finally {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  0;
        }

        public InputStream getInputStreamFromUrl(String urlStr) throws IOException {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            InputStream input = urlConn.getInputStream();
            return input;
        }
    }

}
