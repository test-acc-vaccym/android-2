package com.edroplet.qxx.saneteltabactivity.utils.mail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.FileUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * Created by qxs on 2017/9/24.
 * http://blog.csdn.net/ml3947/
 * http://blog.csdn.net/wingfourever/article/details/7954250
 */

public class MailUtil {
    public static boolean sendMail(Activity activity, String[] receiver, String subject, String content){
        Intent email = new Intent(Intent.ACTION_SENDTO  ); //  无附件的发送
        email.setType("message/rfc822");
        // email.setData(Uri.parse("mailto:455245521@qq.com"));
        // 设置邮件发收人
        email.putExtra(Intent.EXTRA_EMAIL, receiver); // 收件者
        // 设置邮件标题
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        // 设置邮件内容
        email.putExtra(Intent.EXTRA_TEXT, content);

        // 调用系统的邮件系统
        activity.startActivity(Intent.createChooser(email, activity.getString(R.string.choose_mail_client)));
        return true;
    }

    // 单个附件的发送
    public static void sendMailOneAttach(Activity activity, String[] to,String[] cc, String[] bcc, String subject, String content, String filePath){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, to);    // 收件者
        intent.putExtra(Intent.EXTRA_CC, cc);       // 抄送者
        intent.putExtra(Intent.EXTRA_BCC, bcc);     // 密送者
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath)); // "file:///sdcard/Chrysanthemum.jpg"
        intent.setType("image/*");
        intent.setType("message/rfc882");
        Intent.createChooser(intent, activity.getString(R.string.choose_mail_client));
        activity.startActivity(intent);
    }

    // 多个附件的发送
    public static void sendMailMultiAttach(Activity activity, String[] to,String[] cc, String[] bcc, String subject, String content, ArrayList<String> filePath){
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_EMAIL, to);    // 收件者
        intent.putExtra(Intent.EXTRA_CC, cc);       // 抄送者
        intent.putExtra(Intent.EXTRA_BCC, bcc);     // 密送者
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        ArrayList<Uri> fileUris = new ArrayList<Uri>();
        for (String file: filePath) {
            if (!file.startsWith("file://")){
                file = "file://" + file;
            }
            fileUris.add(Uri.parse(file));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);
        intent.setType("*");
        intent.setType("message/rfc882");
        Intent.createChooser(intent, activity.getString(R.string.choose_mail_client));
        // intent.setClassName(com.android.email, com.android.email.activity.ComposeActivityEmail);
        activity.startActivity(intent);
    }

    public static boolean sendMail(Context context, String username, String password, String smtpHost, String from, String to, String [] copy, @Nullable String name, String content, String title, String[] attach){
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost); // "smtp.126.com"
            props.put("mail.smtp.port", "25");
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("username", username);
            props.put("password", password);

            PopupAuthenticator auth = new PopupAuthenticator();
            Session session = Session.getInstance(props, auth);

            MimeMessage message = new MimeMessage(session);
            Address addressFrom = new InternetAddress(from,name);
            Address addressTo = new InternetAddress(to, name);
            message.setSubject(title);
            message.setFrom(addressFrom);
            message.addRecipient(Message.RecipientType.TO, addressTo);

            if ( copy != null){
                for (String c:copy){
                    Address addressCopy = new InternetAddress(c, name);
                    message.addRecipient(Message.RecipientType.CC, addressCopy);
                }
            }

            // 设置正文
            Multipart mp = new MimeMultipart("mixed");         // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
            BodyPart bp = new MimeBodyPart();
            // 如果是text/html;charset=UTF-8换行符就是<br>
            bp.setContent(content.replace("\n","<br>"), "text/plain;charset=utf-8");//或者使用message.setText("Hello");
            mp.addBodyPart(bp);
            if (attach !=null && attach.length >0)
            for (String file: attach) {
                BodyPart bp1 = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(file);
                bp.setDataHandler(new DataHandler(fds));
                bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                mp.addBodyPart(bp1);
            }
            MimeMultipart mp2 = new MimeMultipart();

            //代表正文的bodypart
            MimeBodyPart mixedContent = new MimeBodyPart();
            mixedContent.setContent(mp);
            mp2.addBodyPart(mixedContent);
            message.setContent(mp2);
            // 设置正文结束

            // 保存邮件对象
            message.saveChanges();
            // message.writeTo(new FileOutputStream(FileUtils.getSDPath()+"/1.eml"));

            // 建立连接
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp");
                transport.connect((String)props.get("mail.smtp.host"), (String)props.get("username"), (String)props.get("password"));
                transport.send(message);
                transport.close();
            } else {
                Transport.send(message);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
