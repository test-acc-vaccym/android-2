package com.edroplet.sanetel;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.edroplet.sanetel.activities.main.MainMeLanguageActivity;
import com.edroplet.sanetel.fragments.HintDialogFragment;

import java.util.Locale;

/**
 * Created on 2018/1/18.
 *
 * @author qxs
 */

public class BaseActivity extends AppCompatActivity implements HintDialogFragment.DialogFragmentCallback {

    public static final int REQUEST_PERMISSION = 0xF0;
    public static final int HINT_DIALOG_EXPLAIN_CALENDAR_PERMISSION_REQUEST_CODE = 0xF1;
    private String permission;

    public boolean getPermission(@NonNull String permission){
        this.permission = permission;
        // sdk 23 以后需要运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // Explain to the user why we need to read the contacts
                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.permission_description_title,R.string.permission_description_reason,HINT_DIALOG_EXPLAIN_CALENDAR_PERMISSION_REQUEST_CODE);
                    FragmentManager fragmentManager =  getFragmentManager();
                    newFragment.show(fragmentManager, HintDialogFragment.class.getSimpleName());
                }else{ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION);}
            }
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EventBus.getDefault().register(this);
        changeAppLanguage();
    }

    /*
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String str) {
        switch (str) {
            case "EVENT_REFRESH_LANGUAGE":
                changeAppLanguage();
                recreate();//刷新界面
                break;
        }
    }
    */


    public void changeAppLanguage() {
        String sta = MainMeLanguageActivity.getLanguageLocal(this);
        if(sta != null && !"".equals(sta)){
            // 本地语言设置
            Locale myLocale = new Locale(sta);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
                // User refused to grant permission.
                finish();
            }
        }
    }


    @Override
    public void doPositiveClick(int requestCode) {
        if (HINT_DIALOG_EXPLAIN_CALENDAR_PERMISSION_REQUEST_CODE == requestCode) {
            // Confirmed from the explanation, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void doNegativeClick(int requestCode) {

    }
}
