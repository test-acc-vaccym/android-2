package com.edroplet.qxx.saneteltabactivity.utils.mail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by qxs on 2017/9/24.
 */

public class PopupAuthenticator extends Authenticator {
    public PasswordAuthentication getPasswordAuthentication() {
        String username = "cqhcp"; //163邮箱登录帐号
        String pwd = "12345"; //登录密码
        return new PasswordAuthentication(username, pwd);
    }
}
