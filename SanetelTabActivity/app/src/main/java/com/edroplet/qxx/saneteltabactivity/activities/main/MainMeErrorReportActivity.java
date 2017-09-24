package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.mail.MailUtil;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeErrorReportActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> images;
//    public static MainMeErrorReportActivity newInstance(String info) {
//        Bundle args = new Bundle();
//        MainMeErrorReportActivity fragment = new MainMeErrorReportActivity();
//        args.putString("info", info);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_me_error_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_me_error_report_toolbar);
        toolbar.setTitle(R.string.main_me_error_report_title);toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.main_me_error_report_return).setOnClickListener(this);
        findViewById(R.id.main_me_error_report_save).setOnClickListener(this);
        findViewById(R.id.main_me_error_report_commit).setOnClickListener(this);
        findViewById(R.id.main_me_error_report_photo).setOnClickListener(this);
        findViewById(R.id.main_me_error_report_attach).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        boolean noResult=true;
        switch(view.getId()){
            case R.id.main_me_error_report_return:
                this.finish();
                break;
            case R.id.main_me_error_report_save:
                // TODO 保存到本地缓存
                break;
            case R.id.main_me_error_report_commit:
                // todo 提交
                ArrayList<String> al = new ArrayList<String>();
                String attach = ((CustomEditText) findViewById(R.id.main_me_error_report_attach)).getText().toString();
                if (attach != null && attach.length() > 0) {
                    al.add(attach);
                }
                String photo = ((CustomEditText) findViewById(R.id.main_me_error_report_photo)).getText().toString();
                if (photo != null && photo.length() > 0) {
                    al.add(photo);
                }

                MailUtil.sendMailMultiAttach(this,
                        ((CustomEditText) findViewById(R.id.main_me_error_report_email_send_address)).getText().toString().split(";"),
                        null,
                        null,
                        getString(R.string.main_me_error_report_title),
                        ((CustomEditText) findViewById(R.id.main_me_error_report_description)).getText().toString(),
                        al);
                break;
            case R.id.main_me_error_report_photo:
                noResult = false;
                ImageSelectorActivity.start(MainMeErrorReportActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE, true,true, false);
                break;
            case R.id.main_me_error_report_attach:
                // intent = new Intent(getContext(), Context.AUDIO_SERVICE);
                break;
            default:
                break;
        }
        if (noResult && intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // todo get images then do something
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
