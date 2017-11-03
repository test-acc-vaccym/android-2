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

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;
import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;

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
    private CustomEditText adviceEmailReceive;

    @BindId(R.id.main_me_advice_email_send_address)
    private CustomEditText adviceEmailSend;

    @BindId(R.id.main_me_advice_name)
    private CustomEditText adviceName;

    @BindId(R.id.main_me_advice_phone)
    private CustomEditText advicePhone;

    @BindId(R.id.main_me_advice_filename)
    private CustomEditText adviceFileName;

    @BindId(R.id.main_me_advice_photo_list)
    private CustomTextView advicePhoto;

    @BindId(R.id.main_me_advice_description)
    private CustomEditText adviceDescription;

    private static final String KEY_ADVICE_EMAIL_RECEIVE = "KEY_ADVICE_EMAIL_RECEIVE";
    private static final String KEY_ADVICE_EMAIL_SEND = "KEY_ADVICE_EMAIL_RECEIVE";
    private static final String KEY_ADVICE_NAME= "KEY_ADVICE_EMAIL_RECEIVE";
    private static final String KEY_ADVICE_PHOTO= "KEY_ADVICE_EMAIL_RECEIVE";
    private static final String KEY_ADVICE_PHONE= "KEY_ADVICE_EMAIL_RECEIVE";
    private static final String KEY_ADVICE_FILENAME= "KEY_ADVICE_EMAIL_RECEIVE";
    private static final String KEY_ADVICE_DESCRIPTION= "KEY_ADVICE_EMAIL_RECEIVE";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_me_advice);
        ViewInject.inject(this, this);

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
        adviceEmailReceive.setText(CustomSP.getString(this, KEY_ADVICE_EMAIL_RECEIVE,getString(R.string.main_me_advice_email_receive)));
        adviceEmailSend.setText(CustomSP.getString(this, KEY_ADVICE_EMAIL_SEND,""));
        adviceFileName.setText(CustomSP.getString(this, KEY_ADVICE_FILENAME,""));
        adviceName.setText(CustomSP.getString(this, KEY_ADVICE_NAME,""));
        advicePhone.setText(CustomSP.getString(this, KEY_ADVICE_PHONE,""));
        advicePhoto.setText(CustomSP.getString(this, KEY_ADVICE_PHOTO,""));

        findViewById(R.id.main_me_advice_return).setOnClickListener(this);
        findViewById(R.id.main_me_advice_save).setOnClickListener(this);
        findViewById(R.id.main_me_advice_commit).setOnClickListener(this);
        findViewById(R.id.main_me_advice_photo).setOnClickListener(this);
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
                CustomSP.putString(this, KEY_ADVICE_PHOTO, advicePhoto.getText().toString());
                CustomSP.putString(this, KEY_ADVICE_PHONE, advicePhone.getText().toString());
                CustomSP.putString(this, KEY_ADVICE_NAME, adviceName.getText().toString());
                CustomSP.putString(this, KEY_ADVICE_FILENAME, adviceFileName.getText().toString());
                CustomSP.putString(this, KEY_ADVICE_EMAIL_SEND, adviceEmailSend.getText().toString());
                CustomSP.putString(this, KEY_ADVICE_EMAIL_RECEIVE, adviceEmailReceive.getText().toString());
                CustomSP.putString(this, KEY_ADVICE_DESCRIPTION, adviceDescription.getText().toString());
                break;
            case R.id.main_me_advice_commit:
                // todo 提交
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
}
