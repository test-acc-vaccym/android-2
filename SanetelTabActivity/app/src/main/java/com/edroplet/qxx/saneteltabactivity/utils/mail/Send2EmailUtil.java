package com.edroplet.qxx.saneteltabactivity.utils.mail;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * Created by qxs on 2017/11/12.
 */

public class Send2EmailUtil {
    public static String username = "sanetel_user@126.com";     // 服务邮箱(from邮箱)
    public static String password = "sanetel828415";              // 邮箱授权码
    public static String senderNick = "星网卫通测试";             // 发件人昵称

    private Properties props;     // 系统属性
    private Session session;      // 邮件会话对象
    private MimeMessage mimeMsg;   // MIME邮件对象
    private Multipart mp;         // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

    private static Send2EmailUtil instance = null;

    public Send2EmailUtil() {
        props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        // props.put("mail.smtp.host", "smtp.mxhichina.com");// 注意，如果你是阿里的服务邮箱才用这个
        props.put("mail.smtp.host", "smtp.126.com");// 注意，如果你是阿里的服务邮箱才用这个

        props.put("mail.smtp.port", "25");
        props.put("username", username);
        props.put("password", password);
        // 建立会话
        session = Session.getDefaultInstance(props);
        session.setDebug(false);
    }

    public static Send2EmailUtil getInstance() {
        if (instance == null) {
            instance = new Send2EmailUtil();
        }
        return instance;
    }


    public static void send_email(String from, String to, String[] copyto, String subject, String content, ArrayList<String> fileList) throws IOException, MessagingException{

        Properties properties = new Properties();
        // String to = "3328018955@qq.com";
        // String subject ="提交日志";



        // properties.put("mail.smtp.host", "smtp.qq.com");
        // properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.host", "smtp.126.com");
        properties.put("mail.smtp.port", "25");
        properties.setProperty("mail.debug", "false");
        properties.setProperty("mail.smtp.auth", "true");

        // 协议名称设置为smtps，会使用SSL
        // properties.setProperty("mail.transport.protocol", "smtps");
        // properties.put("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.transport.protocol", "smtp");

        // properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // properties.put("mail.smtp.socketFactory.fallback", "false");
        // properties.put("mail.smtp.socketFactory.port", properties.get("mail.smtp.port").toString());
        // MyAuthenticator authenticator = new MyAuthenticator("sanetel_user@126.com", "sanetel828415");
        MyAuthenticator authenticator = new MyAuthenticator(username, password);
        // javax.mail.Session sendMailSession = javax.mail.Session.getDefaultInstance(properties, authenticator);
        javax.mail.Session sendMailSession = javax.mail.Session.getInstance(properties, authenticator);
        MimeMessage mailMessage = new MimeMessage(sendMailSession);
        mailMessage.setFrom(new InternetAddress(username));

        mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        mailMessage.setSubject(subject, "UTF-8");
        mailMessage.setSentDate(new Date());
        mailMessage.setText(content);
        Transport.send(mailMessage);
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param to 收件人
     * @param copyto 抄送
     * @param subject 主题
     * @param content 内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String from, String[] to, String[] copyto, String subject, String content, ArrayList<String> fileList) throws Exception {
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart();

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
            mimeMsg.setFrom(new InternetAddress(from, nick));
            // 设置收件人
            if (to != null && to.length > 0) {
                String toListStr = getMailList(to);
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toListStr));
            }
            // 设置抄送人
            if (copyto != null && copyto.length > 0) {
                String ccListStr = getMailList(copyto);
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccListStr));
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart();
            // 如果是text/html;charset=UTF-8换行符就是<br>
            bp.setContent(content.replace("\n","<br>"), "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
            if (fileList != null && fileList.size() > 0) {
                for (String f: fileList) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(f);
                    bp.setDataHandler(new DataHandler(fds));
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp);
                }
            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp");
                transport.connect((String)props.get("mail.smtp.host"), (String)props.get("username"), (String)props.get("password"));
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
//              transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close();
            } else {
                Transport.send(mimeMsg);
            }
            Log.e("邮件发送成功","邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param to 收件人, 多个Email以英文逗号分隔
     * @param cc 抄送, 多个Email以英文逗号分隔
     * @param subject 主题
     * @param content 内容
     * @param fileList 附件列表
     * @return
     */
    public boolean sendMail(String from, String to, String cc, String subject, String content, String[] fileList) throws Exception{
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart();

            // 自定义发件人昵称
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 设置发件人
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(from, nick));
            // 设置收件人
            if (to != null && to.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            // 设置抄送人
            if (cc != null && cc.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            // 设置主题
            mimeMsg.setSubject(subject);
            // 设置正文
            BodyPart bp = new MimeBodyPart();
            // 如果是text/html;charset=UTF-8换行符就是<br>
            bp.setContent(content.replace("\n","<br>"), "application/octet-stream;charset=utf-8");
            mp.addBodyPart(bp);
            // 设置附件
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]);
                    bp.setDataHandler(new DataHandler(fds));
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp);
                }
            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            // 发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp");
//                这边是服务邮箱以及密码
                transport.connect((String)props.get("smtp.mxhichina.com"), (String)props.get("xxx.com"), (String)props.get("xxx"));
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close();
            } else {
                Transport.send(mimeMsg);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }

            }
        }
        return toList.toString();
    }

    private static void send(String toAddress) throws Exception{
        String from = username;
        String[] to = {toAddress};
        String[] copyto = {};
        String subject = "标题";
        String content = "内容";
        File file = new File("你所要发的图片的位置");
        File[] files = file.listFiles();
        String[] fileList = new String[files.length];
        if (files != null) {
            int count = files.length;// 文件个数
            for (int i = 0; i < count; i++) {
                File file1 = files[i];
                String path = file1.getPath();
                fileList[i] = path;
            }
            // Send2EmailUtil.getInstance().sendMail(from, to, copyto, subject, content, fileList);
            //todo  发送之后删除
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }

    }

    public static class MyAuthenticator extends Authenticator {
        String userName = null;
        String password = null;
        public MyAuthenticator() {
        }
        public MyAuthenticator(String username, String password) {
            this.userName = username;
            this.password = password;
        }
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password);
        }
    }
}
