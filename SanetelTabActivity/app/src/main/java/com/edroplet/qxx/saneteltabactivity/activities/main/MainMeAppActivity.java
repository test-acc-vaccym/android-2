package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.BrowseUrl;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.KEY_DOWNLOAD_URL;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.KEY_PDF_NAME;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.P120PdfName;
import static com.edroplet.qxx.saneteltabactivity.activities.main.MainMeAboutBrowserActivity.SanetenDownloadUrl;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeAppActivity extends AppCompatActivity implements View.OnClickListener{
//
//    public static MainMeAdviceActivity newInstance(String info) {
//        Bundle args = new Bundle();
//        MainMeAdviceActivity fragment = new MainMeAdviceActivity();
//        args.putString("info", info);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        setContentView(R.layout.fragment_main_me_app);
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

    @Override
    public void onClick(View view) {
        Intent intent = null;
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
                // todo 提交
                break;
            case R.id.main_me_app_update:
                // intent = new Intent(getContext(), Context.AUDIO_SERVICE);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
