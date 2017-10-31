package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.downloadmanager.manager.UpdateManager;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.joanzapata.pdfview.PDFView;

public class MainMeAboutBrowserActivity extends AppCompatActivity {

    @BindId(R.id.main_me_about_pdf_viewer)
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_about_browser);
        // 下载到file目录
        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkUpdate(false);
        //加载assets下的文件
        pdfView.fromAsset("sample.pdf")
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
