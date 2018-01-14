package com.edroplet.sanetel.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.mail.MailUtil;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomTextView;
import com.yongchun.library.view.ImageSelectorActivity;

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
    private Context context;
    private Timer timer = new Timer();

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_advice);

        ButterKnife.bind(this);

        context = this;
        schedule = getResources().getInteger(R.integer.save_data_schedule_timer);

        Toolbar toolbar = findViewById(R.id.main_me_advice_toolbar);
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
                ArrayList<String> al = new ArrayList<>();
                String photoList = advicePhoto.getText().toString();
                if (photoList.length() > 0) {
                    al.addAll(ConvertUtil.string2List(photoList,","));
                }

                String content = getString(R.string.main_me_advice_name) + ": " + adviceName.getText().toString() + "\n"; // 姓名
                content = content + getString(R.string.main_me_error_report_user_mail) + ": " + adviceUserEmail.getText().toString() + "\n"; // 用户邮箱
                content = content + getString(R.string.main_me_email_customer) + ": " + adviceCustomer.getText().toString() + "\n"; // 用户单位
                content = content + getString(R.string.main_me_advice_phone) + ": " + advicePhoto.getText().toString() + "\n"; // 用户电话
                content = content + adviceDescription.getText().toString();

                String subject = adviceSubject.getText().toString();
                if (subject.length() ==0){
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
                ImageSelectorActivity.start(MainMeAdviceActivity.this, 9, ImageSelectorActivity.MODE_MULTIPLE, true,true, false);
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
}
