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
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.mail.MailUtil;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ssa.afilechooser.FileChooserActivity2;
import com.ssa.afilechooser.utils.FileUtils2;
import com.yongchun.library.view.ImageSelectorActivity;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeErrorReportActivity extends AppCompatActivity implements View.OnClickListener{
    private int REQUESTFileChooserActivity = 1000;
    ArrayList<String> images;
    Uri attache;
//    public static MainMeErrorReportActivity newInstance(String info) {
//        Bundle args = new Bundle();
//        MainMeErrorReportActivity fragment = new MainMeErrorReportActivity();
//        args.putString("info", info);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    @Nullable

    @BindId(R.id.main_me_error_report_email_receive)
    private CustomEditText errorReportEmailReceive;

    @BindId(R.id.main_me_error_report_email_send_address)
    private CustomEditText errorReportEmailSend;

    @BindId(R.id.main_me_error_report_name)
    private CustomEditText errorReportName;

    @BindId(R.id.main_me_error_report_phone)
    private CustomEditText errorReportPhone;

    @BindId(R.id.main_me_error_report_filename)
    private CustomEditText errorReportFileName;

    @BindId(R.id.main_me_error_report_attach_files)
    private CustomEditText errorReportAttach;

    @BindId(R.id.main_me_error_report_photo_files)
    private CustomEditText errorReportPhoto;

    @BindId(R.id.main_me_error_report_description)
    private CustomEditText errorReportDescription;

    private static final String KEY_ERROR_REPORT_EMAIL_RECEIVE = "errorReportEmailReceive";
    private static final String KEY_ERROR_REPORT_EMAIL_SEND = "errorReportEmailSend";
    private static final String KEY_ERROR_REPORT_NAME= "errorReportName";
    private static final String KEY_ERROR_REPORT_PHOTO= "errorReportPhoto";
    private static final String KEY_ERROR_REPORT_PHONE= "errorReportPhone";
    private static final String KEY_ERROR_REPORT_FILENAME= "errorReportFileName";
    private static final String KEY_ERROR_REPORT_DESCRIPTION= "errorReportDescription";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_me_error_report);

        ViewInject.inject(this, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_me_error_report_toolbar);
        toolbar.setTitle(R.string.main_me_error_report_title);toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 初始化
        // 从缓存读取数据
        errorReportDescription.setText(CustomSP.getString(this, KEY_ERROR_REPORT_DESCRIPTION,""));
        errorReportEmailReceive.setText(CustomSP.getString(this, KEY_ERROR_REPORT_EMAIL_RECEIVE,getString(R.string.main_me_error_report_email_send)));
        errorReportEmailSend.setText(CustomSP.getString(this, KEY_ERROR_REPORT_EMAIL_SEND,""));
        errorReportFileName.setText(CustomSP.getString(this, KEY_ERROR_REPORT_FILENAME,""));
        errorReportName.setText(CustomSP.getString(this, KEY_ERROR_REPORT_NAME,""));
        errorReportPhone.setText(CustomSP.getString(this, KEY_ERROR_REPORT_PHONE,""));
        errorReportPhoto.setText(CustomSP.getString(this, KEY_ERROR_REPORT_PHOTO,""));

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
                // 保存到本地缓存
                CustomSP.putString(this, KEY_ERROR_REPORT_PHOTO, errorReportPhoto.getText().toString());
                CustomSP.putString(this, KEY_ERROR_REPORT_PHONE, errorReportPhone.getText().toString());
                CustomSP.putString(this, KEY_ERROR_REPORT_NAME, errorReportName.getText().toString());
                CustomSP.putString(this, KEY_ERROR_REPORT_FILENAME, errorReportFileName.getText().toString());
                CustomSP.putString(this, KEY_ERROR_REPORT_EMAIL_SEND, errorReportEmailSend.getText().toString());
                CustomSP.putString(this, KEY_ERROR_REPORT_EMAIL_RECEIVE, errorReportEmailReceive.getText().toString());
                CustomSP.putString(this, KEY_ERROR_REPORT_DESCRIPTION, errorReportDescription.getText().toString());

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
                noResult = false;
//                intent = new Intent(MainMeErrorReportActivity.this, FileChooserActivity.class);
//                startActivityForResult(intent, REQUESTFileChooserActivity);
                FileUtils2.mFileFileterBySuffixs.acceptSuffixs("log|txt|amr|mp3");//过江哪些格式的文件，用“|”分隔（英文），如果不加这句代码，默认显示所有文件。
                intent = new Intent(this, FileChooserActivity2.class);
                startActivityForResult(intent, REQUESTFileChooserActivity);
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
        }else if(resultCode == RESULT_OK && requestCode == REQUESTFileChooserActivity){
            if (null != data) {
                @SuppressWarnings("unchecked")
                ArrayList<File> files = (ArrayList<File>) data.getSerializableExtra(FileChooserActivity2.PATHS);//返回的一个ArrayList<File>
             }

            attache = data.getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
