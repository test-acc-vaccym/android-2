package com.edroplet.sanetel.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.services.DownLoadService;
import com.edroplet.sanetel.utils.FileUtils;
import com.edroplet.sanetel.utils.SystemServices;
import com.edroplet.sanetel.view.ViewInject;
import com.edroplet.sanetel.view.annotation.BindId;
import com.joanzapata.pdfview.PDFView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static com.edroplet.sanetel.activities.main.MainMeAppActivity.DOWNLOAD_PROCESS_KEY;
import static com.edroplet.sanetel.services.DownLoadService.KEY_TARGET_DIR;

public class MainMeAboutBrowserActivity extends AppCompatActivity {
    public static final String SERVICE_PDF_DOWNLOAD_RECEIVER = "com.edroplet.pdf.download.receiver";
    public static final String KEY_PDF_NAME = "pdfName";
    public static final String KEY_DOWNLOAD_URL = "pdfDownloadUrl";
    public static final String BrowseUrl = "http://www.sanetel.com/Content.aspx?PartNodeId=24";
    public static final String P120PdfName = "20170617170632522.pdf";
    public static final String SanetenDownloadUrl = "http://www.sanetel.com/upload/editor/files/20170617170632522.pdf";

    @BindId(R.id.main_me_about_pdf_viewer)
    private PDFView pdfView;
    @BindId(R.id.main_me_device_download_progress)
    ProgressBar downloadProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_about_browser);
        ViewInject.inject(this, this);
        String pdfName = getIntent().getStringExtra(KEY_PDF_NAME);
        String downloadUrl = getIntent().getStringExtra(KEY_DOWNLOAD_URL);
        File filesDir = getFilesDir();
        String filesPath = filesDir.getAbsolutePath();
        // Toast.makeText(this, filesDir.toString(), Toast.LENGTH_SHORT).show();
        String absoluteFilePath = filesPath + "/" + pdfName;
        Toast.makeText(this, absoluteFilePath, Toast.LENGTH_SHORT).show();
        // Toast.makeText(this, filesPath, Toast.LENGTH_SHORT).show();
        AssetManager assetManager = getAssets();
        try {
            assetManager.open(pdfName);
            SystemServices.copyAssetsFiles2FileDir(this, pdfName);
        } catch (IOException ioe) {
            Toast.makeText(this, ioe.toString(),Toast.LENGTH_LONG).show();
            // 首先检查网络是否连接
            boolean isConnect = SystemServices.isConnectedToInternet(this);
            if (isConnect && null != downloadUrl) {
                // 下载到file目录
                pdfView.setVisibility(View.GONE);
                downloadProcess.setVisibility(View.VISIBLE);
                downloadProcess.setProgress(0);

                // 动态注册广播接收器
                pdfDownloadReceiver = new PdfDownloadReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(SERVICE_PDF_DOWNLOAD_RECEIVER);
                registerReceiver(pdfDownloadReceiver, intentFilter);
                // 启动服务
                downloadIntent = new Intent(MainMeAboutBrowserActivity.this, DownLoadService.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_DOWNLOAD_URL, SanetenDownloadUrl);
                downloadIntent.putExtras(bundle);
                startService(downloadIntent);
                setTimerTask();
                pdfName = "";
            }else {
                pdfName = "";
            }
        }
        if (pdfName != null && pdfName.length() > 0) {
            pdfView.setVisibility(View.VISIBLE);
            downloadProcess.setVisibility(View.GONE);
            // 下载成功后打开
            //加载私有目录files下的文件
            File outFile = new File(getCacheDir(), pdfName + "-pdfview.pdf");
            try {
                FileInputStream fileInputStream = openFileInput(pdfName);
                FileUtils.inputStreamToFile(fileInputStream,outFile);
                // FileUtils.copy(openFileInput(pdfName), outFile);
            }catch (Exception e){
                e.printStackTrace();
            }

            // pdfView.fromFile(outFile)
                    pdfView.fromAsset(pdfName)
                    //.fromFile("")指定加载某个文件
                    //指定加载某一页
                /*.pages(0, 1,2, 3, 4, 5)*/
                    .defaultPage(1)
                    .showMinimap(false)
                    .enableSwipe(true)
                /* .onDraw(onDraw)
                .onLoad(onLoadCompleteListener)
                .onPageChange(onPageChangeListener)*/
                    .load();
        }
    }

    private int updateProgress;
    private PdfDownloadReceiver pdfDownloadReceiver;
    Intent downloadIntent;
    private String targetDir;

    public class PdfDownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*处理接收到的广播内容*/
            if (intent != null){
                updateProgress = intent.getIntExtra(DOWNLOAD_PROCESS_KEY,0);
                if (updateProgress == 100 ){
                    targetDir = intent.getStringExtra(KEY_TARGET_DIR);
                }
            }
        }
    }

    Timer mTimer;
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

    @Override
    protected void onDestroy() {
        if (mTimer != null){
            mTimer.purge();
            mTimer.cancel();
            mTimer = null;
        }

        // 停止服务
        if (null != downloadIntent)
            stopService(downloadIntent);

        // 注销广播
        if (pdfDownloadReceiver!= null)
            unregisterReceiver(pdfDownloadReceiver);

        super.onDestroy();
    }

    PdfDownloadHandler handler = new PdfDownloadHandler(this);

    private class PdfDownloadHandler extends Handler {
        private final WeakReference<MainMeAboutBrowserActivity> mActivity;
        PdfDownloadHandler(MainMeAboutBrowserActivity activity){
            mActivity = new WeakReference<MainMeAboutBrowserActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            int  updateProgress = msg.what;
            downloadProcess.setProgress(updateProgress);
            if (updateProgress == -1){
                Toast.makeText(MainMeAboutBrowserActivity.this, getString(R.string.download_failed_notification),Toast.LENGTH_SHORT).show();
            }else if (updateProgress == 100){
                // 打开文件
                if (!targetDir.isEmpty()) {
                    File pdfFile = new File(targetDir+P120PdfName);
                    pdfView.fromFile(pdfFile)
                            .defaultPage(1)
                            .showMinimap(false)
                            .enableSwipe(true)
                            .load();
                }
            }
        }
    }
}
