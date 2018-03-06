package com.edroplet.sanetel.activities.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.edroplet.sanetel.BaseActivity;
import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.mail.MailUtil;
import com.edroplet.sanetel.utils.mail.Send2EmailUtil;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomTextView;
import com.ssa.afilechooser.FileChooserActivity2;
import com.ssa.afilechooser.utils.FileUtils2;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 * 建议UI
 */

public class MainMeAdviceActivity extends BaseActivity implements View.OnClickListener{
//
//    public static MainMeAdviceActivity newInstance(String info) {
//        Bundle args = new Bundle();
//        MainMeAdviceActivity fragment = new MainMeAdviceActivity();
//        args.putString("info", info);
//        fragment.setArguments(args);
//        return fragment;
//    }
    private static final int REQUESTFileChooserActivity = 0xE0;
    ArrayList<File> files;
    Uri attache;

    ArrayList<String> images;
    @BindView(R.id.main_me_advice_email_receive)
    CustomTextView adviceEmailReceive;

    @BindView(R.id.main_me_advice_email_send_address)
    CustomEditText adviceEmailSend;

    @BindView(R.id.main_me_advice_user_mail)
    CustomEditText adviceUserEmail;

    @BindView(R.id.main_me_advice_name)
    CustomEditText adviceName;

    @BindView(R.id.main_me_advice_phone)
    CustomEditText advicePhone;

    @BindView(R.id.main_me_advice_subject)
    CustomEditText adviceSubject;

    @BindView(R.id.main_me_advice_photo_list)
    CustomTextView advicePhoto;

    @BindView(R.id.main_me_advice_description)
    CustomEditText adviceDescription;

    @BindView(R.id.main_me_advice_email_customer)
    CustomEditText adviceCustomer;

    @BindView(R.id.main_me_advice_toolbar)
    Toolbar toolbar;

    @BindView(R.id.submit_result)
    CustomTextView submitResult;

    @BindView(R.id.main_me_advice_attach_files)
    CustomTextView adviceAttach;

    @BindView(R.id.main_me_advice_attach)
    CustomButton adviceAttachButton;

    private static final String KEY_ADVICE_EMAIL_RECEIVE = "KEY_ADVICE_EMAIL_RECEIVE";
    private static final String KEY_ADVICE_EMAIL_SEND = "KEY_ADVICE_EMAIL_SEND";
    private static final String KEY_ADVICE_NAME= "KEY_ADVICE_NAME";
    private static final String KEY_ADVICE_PHOTO= "KEY_ADVICE_PHOTO";
    private static final String KEY_ADVICE_PHONE= "KEY_ADVICE_PHONE";
    private static final String KEY_ADVICE_FILENAME= "KEY_ADVICE_FILENAME";
    private static final String KEY_ADVICE_DESCRIPTION= "KEY_ADVICE_DESCRIPTION";
    private static final String KEY_ADVICE_CUSTOMER= "KEY_ADVICE_CUSTOMER";
    private static final String KEY_ADVICE_USER_MAIL= "KEY_ADVICE_USER_MAIL";
    private static final String KEY_ADVICE_ATTACH_FILES= "KEY_ADVICE_ATTACH_FILES";

    private static int schedule;
    private Context context;
    private Timer timer = new Timer();

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_advice);

        ButterKnife.bind(this);

        getPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        context = this;
        schedule = getResources().getInteger(R.integer.save_data_schedule_timer);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 初始化
        // 从缓存读取数据
        adviceDescription.setText(CustomSP.getString(this, KEY_ADVICE_DESCRIPTION,""));
        adviceEmailReceive.setText(CustomSP.getString(this, KEY_ADVICE_EMAIL_RECEIVE,getString(R.string.main_me_advice_email_receive_address)));
        adviceEmailSend.setText(CustomSP.getString(this, KEY_ADVICE_EMAIL_SEND,getString(R.string.main_me_advice_email_send_address)));
        adviceUserEmail.setText(CustomSP.getString(this, KEY_ADVICE_USER_MAIL,""));
        adviceAttach.setText(CustomSP.getString(this, KEY_ADVICE_ATTACH_FILES,""));
        adviceSubject.setText(CustomSP.getString(this, KEY_ADVICE_FILENAME,""));
        adviceName.setText(CustomSP.getString(this, KEY_ADVICE_NAME,""));
        advicePhone.setText(CustomSP.getString(this, KEY_ADVICE_PHONE,""));
        advicePhoto.setText(CustomSP.getString(this, KEY_ADVICE_PHOTO,""));
        adviceCustomer.setText(CustomSP.getString(this, KEY_ADVICE_CUSTOMER,""));

        findViewById(R.id.main_me_advice_return).setOnClickListener(this);
        findViewById(R.id.main_me_advice_save).setOnClickListener(this);
        findViewById(R.id.main_me_advice_commit).setOnClickListener(this);
        findViewById(R.id.main_me_advice_photo).setOnClickListener(this);
        adviceAttachButton.setOnClickListener(this);

        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                Message message = new Message();
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
        switch(view.getId()){
            case R.id.main_me_advice_return:
                // 返回
                this.finish();
                break;
            case R.id.main_me_advice_save:
                // 保存到本地缓存
                onSave();
                break;
            case R.id.main_me_advice_commit:
                // 提交
                try {
                    sendMailTask = new MainMeAdviceActivity.SendMailTask();
                    sendMailTask.execute(view);
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.main_me_advice_photo:
                ImageSelectorActivity.start(MainMeAdviceActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE, true,true, false);
                break;
            case R.id.main_me_error_report_attach:
//                intent = new Intent(MainMeAdviceActivity.this, FileChooserActivity.class);
//                startActivityForResult(intent, REQUESTFileChooserActivity);
                FileUtils2.mFileFileterBySuffixs.acceptSuffixs("log|txt|amr|mp3");//过江哪些格式的文件，用“|”分隔（英文），如果不加这句代码，默认显示所有文件。
                intent = new Intent(this, FileChooserActivity2.class);
                startActivityForResult(intent, REQUESTFileChooserActivity);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            //get images then do something
            advicePhoto.setText(images.toString());
        }else if(resultCode == RESULT_OK && requestCode == REQUESTFileChooserActivity){
            if (null != data) {
                // @SuppressWarnings("unchecked")
                try {
                    files = (ArrayList<File>) data.getSerializableExtra(FileChooserActivity2.PATHS);//返回的一个ArrayList<File>
                    adviceAttach.setText(files.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            attache = data.getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void onSave(){
        CustomSP.putString(context, KEY_ADVICE_PHOTO, advicePhoto.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_PHONE, advicePhone.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_NAME, adviceName.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_FILENAME, adviceSubject.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_EMAIL_SEND, adviceEmailSend.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_USER_MAIL, adviceUserEmail.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_EMAIL_RECEIVE, adviceEmailReceive.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_DESCRIPTION, adviceDescription.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_CUSTOMER,adviceCustomer.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_ATTACH_FILES, adviceAttach.getText().toString());
    }

    private final Handler handler = new ErrorReportHandler(this);

    private static class ErrorReportHandler extends Handler {
        private final WeakReference<MainMeAdviceActivity> mTarget;
        ErrorReportHandler(MainMeAdviceActivity target){
            mTarget = new WeakReference<>(target);
        }
        @Override
        public void handleMessage(Message msg) {
            MainMeAdviceActivity target = mTarget.get();
            if (target != null) {
              target.onSave();
            }
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

    private class SendMailTask extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... objects) {

            ArrayList<String> al = new ArrayList<String>();
            // 文件
            String attach = adviceAttach.getText().toString();
            if (attach != null && attach.length() > 0) {
                al.addAll(ConvertUtil.string2List(attach,","));
            }
            // 照片
            String photo = advicePhoto.getText().toString();
            if (photo != null && photo.length() > 0) {
                al.addAll(ConvertUtil.string2List(photo,","));
            }
            // 姓名
            String content = getString(R.string.main_me_error_report_name) + ": " + adviceName.getText().toString() + "\n";
            // 用户邮箱
            content = content + getString(R.string.main_me_error_report_user_mail) + ": " + adviceUserEmail.getText().toString() + "\n";
            // 用户单位
            content = content + getString(R.string.main_me_email_customer) + ": " + adviceCustomer.getText().toString() + "\n";
            // 电话
            content = content + getString(R.string.main_me_error_report_phone) + ": " + advicePhone.getText().toString() + "\n";
            content = content + adviceDescription.getText().toString();

            String subject = adviceSubject.getText().toString();
            if (subject == null || subject.length() ==0){
                subject = getString(R.string.main_me_error_report_title);
            }

            try {
                if (false) {
                    String[] to = new String[]{"3328018955@qq.com","sanetel_user@126.com"};
                    boolean sucess =  Send2EmailUtil.getInstance().sendMail("sanetel_user@126.com", to, null, subject, content, al);
                    if (sucess){
                        Toast.makeText(MainMeAdviceActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainMeAdviceActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                    }
                    /*
                    Send2EmailUtil.getInstance().send_email(content);
                    */
                }else {
                    if (true) {
                        MailUtil.sendMailMultiAttach(MainMeAdviceActivity.this,
                                adviceEmailReceive.getText().toString().split(";"),
                                null, // 抄送
                                null, // 密送
                                subject, // 主题
                                content, // 内容
                                al); // 附件
                    }else {
                        String[] array = (String[])al.toArray(new String[al.size()]);
                        boolean result = MailUtil.sendMail(getBaseContext(), "sanetel_user@126.com",
                                "sanetel_!@#","smtp.126.com", "sanetel_user@126.com",
                                "3328018955@qq.com", null, "故障报告", content, subject, array);
                        if (result){
                            submitResult.setVisibility(View.VISIBLE);
                            submitResult.setText("发送成功");
                            return "发送成功";
                        }else {
                            submitResult.setVisibility(View.VISIBLE);
                            submitResult.setText("发送失败");
                            return "发送失败";
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                return e.toString();
            }
            return null;
        }
    }
}
