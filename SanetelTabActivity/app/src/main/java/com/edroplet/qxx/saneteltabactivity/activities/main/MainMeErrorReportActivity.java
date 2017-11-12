package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsCollectHistoryFileListActivity;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.mail.MailUtil;
import com.edroplet.qxx.saneteltabactivity.utils.mail.Send2EmailUtil;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;
import com.ssa.afilechooser.FileChooserActivity2;
import com.ssa.afilechooser.utils.FileUtils2;
import com.yongchun.library.view.ImageSelectorActivity;

import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsCollectHistoryFileListActivity.KEY_IS_SELECT;
import static com.edroplet.qxx.saneteltabactivity.utils.CustomSP.WifiSettingsNameKey;
import static com.yongchun.library.view.ImageSelectorActivity.REQUEST_OUTPUT;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeErrorReportActivity extends AppCompatActivity implements View.OnClickListener{
    private int REQUESTFileChooserActivity = 1000;
    private int REQUEST_HISTORY_FILES = 1001;
    ArrayList<String> selectedImages;
    ArrayList<File> files;
    Uri attache;

    Timer timer = new Timer();

    @BindId(R.id.main_me_error_report_email_receive)
    private static CustomTextView errorReportEmailReceive;

    @BindId(R.id.main_me_error_report_email_send_address)
    private static CustomEditText errorReportEmailSend;

    @BindId(R.id.main_me_error_report_name)
    private static CustomEditText errorReportName;

    @BindId(R.id.main_me_error_report_phone)
    private static CustomEditText errorReportPhone;

    @BindId(R.id.main_me_error_report_serial_number)
    private static CustomEditText errorReportSerialNumber;

    @BindId(R.id.main_me_error_report_subject)
    private static CustomEditText errorReportFileName;

    @BindId(R.id.main_me_error_report_attach_files)
    private static CustomTextView errorReportAttach;

    @BindId(R.id.main_me_error_report_history_files)
    private static CustomTextView errorReportHistoryFiles;

    @BindId(R.id.main_me_error_report_photo_files)
    private static CustomTextView errorReportPhoto;

    @BindId(R.id.main_me_error_report_description)
    private static CustomEditText errorReportDescription;

    @BindId(R.id.main_me_error_report_email_customer)
    private static CustomEditText errorReportCustomer;

    // 按键区
    @BindId(R.id.main_me_error_report_attach)
    CustomButton errorReportAttachButton;

    @BindId(R.id.main_me_error_report_photo)
    CustomButton errorReportPhotoButton;

    @BindId(R.id.main_me_error_report_history)
    CustomButton errorReportHistoryhButton;

    @BindId(R.id.main_me_error_report_return)
    CustomButton errorReportReturnButton;

    @BindId(R.id.main_me_error_report_save)
    CustomButton errorReportSaveButton;

    @BindId(R.id.main_me_error_report_commit)
    CustomButton errorReportCommitButton;

    private static final String KEY_ERROR_REPORT_EMAIL_RECEIVE = "KEY_ERROR_REPORT_EMAIL_RECEIVE";
    private static final String KEY_ERROR_REPORT_EMAIL_SEND = "KEY_ERROR_REPORT_EMAIL_SEND";
    private static final String KEY_ERROR_REPORT_NAME= "KEY_ERROR_REPORT_NAME";
    private static final String KEY_ERROR_REPORT_ATTACH_FILES= "KEY_ERROR_REPORT_ATTACH_FILES";
    private static final String KEY_ERROR_REPORT_HISTORY_FILES= "KEY_ERROR_REPORT_HISTORY_FILES";
    private static final String KEY_ERROR_REPORT_PHOTO= "KEY_ERROR_REPORT_PHOTO";
    private static final String KEY_ERROR_REPORT_PHONE= "KEY_ERROR_REPORT_PHONE";
    private static final String KEY_ERROR_REPORT_SERIAL_NUMBER= "KEY_ERROR_REPORT_SERIAL_NUMBER";
    private static final String KEY_ERROR_REPORT_FILENAME= "KEY_ERROR_REPORT_FILENAME";
    private static final String KEY_ERROR_REPORT_DESCRIPTION= "KEY_ERROR_REPORT_DESCRIPTION";
    private static final String KEY_ERROR_REPORT_CUSTOMER= "KEY_ERROR_REPORT_CUSTOMER";

    private static int schedule;
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_error_report);

        ViewInject.inject(this, this);
        context = this;
        schedule = getResources().getInteger(R.integer.save_data_schedule_timer);

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
        errorReportEmailReceive.setText(CustomSP.getString(this, KEY_ERROR_REPORT_EMAIL_RECEIVE,
                getString(R.string.main_me_error_report_email_receive_address)));
        errorReportEmailSend.setText(CustomSP.getString(this, KEY_ERROR_REPORT_EMAIL_SEND,getString(R.string.main_me_advice_email_send_address)));
        errorReportFileName.setText(CustomSP.getString(this, KEY_ERROR_REPORT_FILENAME,""));
        errorReportName.setText(CustomSP.getString(this, KEY_ERROR_REPORT_NAME,""));
        errorReportPhone.setText(CustomSP.getString(this, KEY_ERROR_REPORT_PHONE,""));
        errorReportSerialNumber.setText(CustomSP.getString(this, WifiSettingsNameKey,getString(R.string.main_me_error_report_serial_number_hint)));
        errorReportPhoto.setText(CustomSP.getString(this, KEY_ERROR_REPORT_PHOTO,""));
        errorReportAttach.setText(CustomSP.getString(this, KEY_ERROR_REPORT_ATTACH_FILES,""));
        errorReportHistoryFiles.setText(CustomSP.getString(this, KEY_ERROR_REPORT_HISTORY_FILES,""));
        errorReportCustomer.setText(CustomSP.getString(this, KEY_ERROR_REPORT_CUSTOMER,""));

        errorReportReturnButton.setOnClickListener(this);
        errorReportSaveButton.setOnClickListener(this);
        errorReportCommitButton.setOnClickListener(this);
        errorReportPhotoButton.setOnClickListener(this);
        errorReportAttachButton.setOnClickListener(this);
        errorReportHistoryhButton.setOnClickListener(this);

        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                handler.sendMessage(message);
                try {
                    Thread.sleep(schedule);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, schedule);
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
                onSave();
                break;
            case R.id.main_me_error_report_commit:
                // todo 提交
                try {
                    sendMailTask = new SendMailTask();
                    sendMailTask.execute(view);
                }catch (Exception e){
                    e.printStackTrace();
                }
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
            case R.id.main_me_error_report_history:
                noResult = false;
                intent = new Intent(this, FunctionsCollectHistoryFileListActivity.class);
                intent.putExtra(KEY_IS_SELECT, true);
                startActivityForResult(intent, REQUEST_HISTORY_FILES);
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
            if (null != data) {
                selectedImages = (ArrayList<String>) data.getSerializableExtra(REQUEST_OUTPUT);
                errorReportPhoto.setText(selectedImages.toString());
            }

        }else if(resultCode == RESULT_OK && requestCode == REQUESTFileChooserActivity){
            if (null != data) {
                // @SuppressWarnings("unchecked")
                try {
                    files = (ArrayList<File>) data.getSerializableExtra(FileChooserActivity2.PATHS);//返回的一个ArrayList<File>
                    errorReportAttach.setText(files.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
             }
            attache = data.getData();
        }else if(resultCode == RESULT_OK && requestCode == REQUEST_HISTORY_FILES){
            if (null != data) {
                try {
                    ArrayList<String> historyFiles = (ArrayList<String>) data.getSerializableExtra(FileChooserActivity2.PATHS);//返回的一个ArrayList<File>
                    errorReportHistoryFiles.setText(historyFiles.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            attache = data.getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static void onSave(){

        // 保存到本地缓存
        CustomSP.putString(context, KEY_ERROR_REPORT_PHOTO, errorReportPhoto.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_PHONE, errorReportPhone.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_NAME, errorReportName.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_FILENAME, errorReportFileName.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_EMAIL_SEND, errorReportEmailSend.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_EMAIL_RECEIVE, errorReportEmailReceive.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_DESCRIPTION, errorReportDescription.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_ATTACH_FILES, errorReportAttach.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_HISTORY_FILES, errorReportHistoryFiles.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_SERIAL_NUMBER, errorReportSerialNumber.getText().toString());
        CustomSP.putString(context, KEY_ERROR_REPORT_CUSTOMER,errorReportCustomer.getText().toString());
    }

    private final Handler handler = new ErrorReportHandler();

    private static class ErrorReportHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            onSave();
        }
    }

    @Override
    protected void onDestroy() {
        if (timer !=null){
            timer.purge();
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    private SendMailTask sendMailTask;

    private class SendMailTask extends AsyncTask<Object, String, String>{
        @Override
        protected String doInBackground(Object... objects) {

            ArrayList<String> al = new ArrayList<String>();
            String attach = errorReportAttach.getText().toString();
            if (attach != null && attach.length() > 0) {
                al.addAll(ConvertUtil.string2List(attach,","));
            }
            String photo = errorReportPhoto.getText().toString();
            if (photo != null && photo.length() > 0) {
                al.addAll(ConvertUtil.string2List(photo,","));
            }
            String history = errorReportHistoryFiles.getText().toString();
            if (history != null && history.length() > 0) {
                al.addAll(ConvertUtil.string2List(history,","));
            }

            String content = getString(R.string.main_me_error_report_name) + ": " + errorReportName.getText().toString() + "\n"; // 姓名
            content = content + getString(R.string.main_me_email_customer) + ": " + errorReportCustomer.getText().toString() + "\n"; // 用户单位
            content = content + getString(R.string.main_me_error_report_phone) + ": " + errorReportPhone.getText().toString() + "\n"; // 电话
            content = content + getString(R.string.main_me_error_report_serial_number) + ": " + errorReportSerialNumber.getText().toString() + "\n"; // 序列号
            content = content + errorReportDescription.getText().toString();

            String subject = errorReportFileName.getText().toString();
            if (subject == null || subject.length() ==0){
                subject = getString(R.string.main_me_error_report_title);
            }

            try {
                /*
                   String[] to = new String[]{"3328018955@qq.com","sanetel_user@126.com"};
                   boolean sucess =  Send2EmailUtil.getInstance().sendMail("sanetel_user@126.com", to, null, subject, content, al);
                   if (sucess){
                       Toast.makeText(MainMeErrorReportActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                   }else {
                       Toast.makeText(MainMeErrorReportActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                   }
                Send2EmailUtil.getInstance().send_email(content);
                */


                MailUtil.sendMailMultiAttach(MainMeErrorReportActivity.this,
                        errorReportEmailSend.getText().toString().split(";"),
                        null, // 抄送
                        null, // 密送
                        subject, // 主题
                        content, // 内容
                        al); // 附件

            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
