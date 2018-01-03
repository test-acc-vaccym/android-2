package com.edroplet.qxx.saneteltabactivity.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.mail.MailUtil;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;
import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainMeAdviceActivity extends AppCompatActivity implements View.OnClickListener{
//
//    public static MainMeAdviceActivity newInstance(String info) {
//        Bundle args = new Bundle();
//        MainMeAdviceActivity fragment = new MainMeAdviceActivity();
//        args.putString("info", info);
//        fragment.setArguments(args);
//        return fragment;
//    }
    ArrayList<String> images;
    @BindId(R.id.main_me_advice_email_receive)
    private static CustomTextView adviceEmailReceive;

    @BindId(R.id.main_me_advice_email_send_address)
    private static CustomEditText adviceEmailSend;

    @BindId(R.id.main_me_advice_user_mail)
    private static CustomEditText adviceUserEmail;

    @BindId(R.id.main_me_advice_name)
    private static CustomEditText adviceName;

    @BindId(R.id.main_me_advice_phone)
    private static CustomEditText advicePhone;

    @BindId(R.id.main_me_advice_subject)
    private static CustomEditText adviceSubject;

    @BindId(R.id.main_me_advice_photo_list)
    private static CustomTextView advicePhoto;

    @BindId(R.id.main_me_advice_description)
    private static CustomEditText adviceDescription;

    @BindId(R.id.main_me_advice_email_customer)
    private static CustomEditText adviceCustomer;

    private static final String KEY_ADVICE_EMAIL_RECEIVE = "KEY_ADVICE_EMAIL_RECEIVE";
    private static final String KEY_ADVICE_EMAIL_SEND = "KEY_ADVICE_EMAIL_SEND";
    private static final String KEY_ADVICE_NAME= "KEY_ADVICE_NAME";
    private static final String KEY_ADVICE_PHOTO= "KEY_ADVICE_PHOTO";
    private static final String KEY_ADVICE_PHONE= "KEY_ADVICE_PHONE";
    private static final String KEY_ADVICE_FILENAME= "KEY_ADVICE_FILENAME";
    private static final String KEY_ADVICE_DESCRIPTION= "KEY_ADVICE_DESCRIPTION";
    private static final String KEY_ADVICE_CUSTOMER= "KEY_ADVICE_CUSTOMER";
    private static final String KEY_ADVICE_USER_MAIL= "KEY_ADVICE_USER_MAIL";

    private static int schedule;
    private static Context context;
    private Timer timer = new Timer();

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_advice);
        ViewInject.inject(this, this);
        context = this;
        schedule = getResources().getInteger(R.integer.save_data_schedule_timer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_me_advice_toolbar);
        toolbar.setTitle(R.string.main_me_advice_title);
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
        adviceSubject.setText(CustomSP.getString(this, KEY_ADVICE_FILENAME,""));
        adviceName.setText(CustomSP.getString(this, KEY_ADVICE_NAME,""));
        advicePhone.setText(CustomSP.getString(this, KEY_ADVICE_PHONE,""));
        advicePhoto.setText(CustomSP.getString(this, KEY_ADVICE_PHOTO,""));
        adviceCustomer.setText(CustomSP.getString(this, KEY_ADVICE_CUSTOMER,""));

        findViewById(R.id.main_me_advice_return).setOnClickListener(this);
        findViewById(R.id.main_me_advice_save).setOnClickListener(this);
        findViewById(R.id.main_me_advice_commit).setOnClickListener(this);
        findViewById(R.id.main_me_advice_photo).setOnClickListener(this);

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
            case R.id.main_me_advice_return:
                // 返回
                this.finish();
                break;
            case R.id.main_me_advice_save:
                // 保存到本地缓存
                onSave();
                break;
            case R.id.main_me_advice_commit:
                // todo 提交
                ArrayList<String> al = new ArrayList<String>();
                String photoList = advicePhoto.getText().toString();
                if (advicePhoto != null && photoList.length() > 0) {
                    al.addAll(ConvertUtil.string2List(photoList,","));
                }

                String content = getString(R.string.main_me_advice_name) + ": " + adviceName.getText().toString() + "\n"; // 姓名
                content = content + getString(R.string.main_me_error_report_user_mail) + ": " + adviceUserEmail.getText().toString() + "\n"; // 用户邮箱
                content = content + getString(R.string.main_me_email_customer) + ": " + adviceCustomer.getText().toString() + "\n"; // 用户单位
                content = content + getString(R.string.main_me_advice_phone) + ": " + advicePhoto.getText().toString() + "\n"; // 用户电话
                content = content + adviceDescription.getText().toString();

                String subject = adviceSubject.getText().toString();
                if (subject == null || subject.length() ==0){
                    subject = getString(R.string.main_me_error_report_title);
                }
                MailUtil.sendMailMultiAttach(this,
                        adviceEmailReceive.getText().toString().split(";"),
                        null, // 抄送
                        null, // 密送
                        subject, // 主题
                        content, // 内容
                        al); // 附件

                break;
            case R.id.main_me_advice_photo:
                // intent = new Intent(getContext(), Context.AUDIO_SERVICE);
                noResult = false;
                ImageSelectorActivity.start(MainMeAdviceActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE, true,true, false);
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
            advicePhoto.setText(images.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static void onSave(){
        CustomSP.putString(context, KEY_ADVICE_PHOTO, advicePhoto.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_PHONE, advicePhone.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_NAME, adviceName.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_FILENAME, adviceSubject.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_EMAIL_SEND, adviceEmailSend.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_USER_MAIL, adviceUserEmail.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_EMAIL_RECEIVE, adviceEmailReceive.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_DESCRIPTION, adviceDescription.getText().toString());
        CustomSP.putString(context, KEY_ADVICE_CUSTOMER,adviceCustomer.getText().toString());
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
}
