package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.services.DownLoadService;
import com.edroplet.qxx.saneteltabactivity.utils.DateTime;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.BrowseUrl;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.KEY_DOWNLOAD_URL;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.KEY_PDF_NAME;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.P120PdfName;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.SanetenDownloadUrl;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeAppActivity extends AppCompatActivity implements View.OnClickListener{

    private DownLoadService downLoadService;

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
                if (updateProgress <= 0) {
                    activity.appUpdateState.setText(activity.getString(R.string.main_me_app_update_state_checking));
                }else if (updateProgress >= 100){
                    activity.appUpdateState.setText(activity.getString(R.string.main_me_app_update_state_complete));
                }else {
                    dots = dots + ".";

                    String progress = String.format(activity.getString(R.string.main_me_app_update_state_downloading),
                            updateProgress, dots);

                    activity.appUpdateState.setText(progress);
                }
            }
        }
    }

    private  final updateHandler handler = new updateHandler(this);

    private  final Runnable runner = new Runnable() {
        @Override
        public void run() {
            int updateProgress = 0;
            if (downLoadService != null) {
                updateProgress = downLoadService.getPreProgress();
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_UPDATE_STATE, updateProgress);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    };

    private Timer mTimer;
    private void setTimerTask() {
        if (null == mTimer) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int updateProgress = 0;
                if (downLoadService != null) {
                    updateProgress = downLoadService.getPreProgress();
                }
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
                downloadIntent = new Intent(MainMeAppActivity.this,DownLoadService.class);
                startService(downloadIntent);
                setTimerTask();
                //  handler.postDelayed(runner, 1000);
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
            stopService(downloadIntent);
        super.onDestroy();
    }
}
