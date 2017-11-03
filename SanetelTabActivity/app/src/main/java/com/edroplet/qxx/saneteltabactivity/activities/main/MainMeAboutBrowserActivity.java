package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.FileUtils;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.utils.downloadmanager.manager.UpdateManager;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.joanzapata.pdfview.PDFView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainMeAboutBrowserActivity extends AppCompatActivity {
    public static final String KEY_PDF_NAME = "pdfName";
    public static final String KEY_DOWNLOAD_URL = "downloadUrl";
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
                UpdateManager updateManager = new UpdateManager(this);
                updateManager.checkUpdate(false);
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
                FileUtils.inputstreamtofile(fileInputStream,outFile);
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
}
