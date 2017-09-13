package com.sanetel.control.bean;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by qxs on 2017/8/24.
 */

public class LoginAccount {
    private String account;
    private Context context;
    private static SharedPreferences userLogin;

    private static final String userKey = "user";

    public LoginAccount(Context ctx){
        this.context =  ctx;
    }

    public void SaveAccount(String account){
        this.account = account;
        //  打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        userLogin = context.getSharedPreferences("login_setting", 0);
        //  让setting处于编辑状态
        SharedPreferences.Editor editor = userLogin.edit();
        //  存放数据
        editor.putString(userKey,account);
        editor.apply();
    }

    public String GetAccount(){
        if (userLogin == null){
            //  打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
            userLogin = context.getSharedPreferences("login_setting", 0);
        }
        return userLogin.getString(userKey,"");
    }
}
