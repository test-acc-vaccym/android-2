package com.edroplet.sanetel.activities.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.CustomSP;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMeLanguageActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_LANGUAGE = "com.edroplet.sanetel.key.language";
    public static final String LANGUAGE_CHINESE = "zh_CN";
    public static final String LANGUAGE_ENGLISH = "en"; // "ja", "de"

    @BindView(R.id.language_chinese)
    RadioButton langCh;
    @BindView(R.id.language_english)
    RadioButton langEn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me_language);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        langCh.setOnClickListener(this);
        langEn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.language_chinese:
                setLanguageLocal(this,LANGUAGE_CHINESE);
                break;
            case R.id.language_english:
                setLanguageLocal(this, LANGUAGE_ENGLISH);
                break;
        }
    }

    public static void setLanguageLocal(Context context, String language){
        CustomSP.putString(context,KEY_LANGUAGE, language);
        EventBus.getDefault().post("EVENT_REFRESH_LANGUAGE");
    }

    public static String getLanguageLocal(Context context){
        return CustomSP.getString(context, KEY_LANGUAGE, LANGUAGE_CHINESE);
    }
}
