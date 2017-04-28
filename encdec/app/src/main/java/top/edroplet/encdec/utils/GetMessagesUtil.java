package top.edroplet.encdec.utils; /**
 * Created by xw on 2017/4/28.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony.TextBasedSmsColumns;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetMessagesUtil {
    private static final String TAG = "GetMessagesUtil";
    private final Uri SMS_INBOX = Uri.parse("content://sms/");
    private Context context;
    private Handler smsHandler = new Handler() {
        // 这里可以进行回调的操作
        public void handleMessage(android.os.Message msg) {
            System.out.println("smsHandler 执行了.....");
        }
    };


    public GetMessagesUtil(Context context) {
        this.context = context;
    }

    public ArrayList<SmsData> getSmsFromPhone() {

        ArrayList al;
        SmsObserver smsObserver;
        Cursor cur;
        al = new ArrayList<>();

        smsObserver = new SmsObserver(context, smsHandler);
        ContentResolver cr = context.getContentResolver();
        cr.registerContentObserver(SMS_INBOX, true, smsObserver);
        String[] projection = new String[]{TextBasedSmsColumns.BODY, TextBasedSmsColumns.ADDRESS,
                TextBasedSmsColumns.PERSON, TextBasedSmsColumns.CREATOR, TextBasedSmsColumns.DATE,
                TextBasedSmsColumns.DATE_SENT, TextBasedSmsColumns.ERROR_CODE, TextBasedSmsColumns.REPLY_PATH_PRESENT,
                TextBasedSmsColumns.PROTOCOL, TextBasedSmsColumns.LOCKED, TextBasedSmsColumns.TYPE};// "_id", "address", "person", "body", "date", "type
        String where = " date >  " + (System.currentTimeMillis() - 10 * 60 * 1000);

        try {

            cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
            if (null == cur) {
                return al;
            }
            if (cur.moveToNext()) {
                String number = cur.getString(cur.getColumnIndex("address"));// 手机号
                String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
                String body = cur.getString(cur.getColumnIndex("body"));
                SmsData sd = new SmsData();
                sd.address = number;
                sd.person = name;
                sd.boby = body;
                al.add(0, sd);
                System.out.println(">>>>>>>>>>>>>>>>手机号：" + number);
                System.out.println(">>>>>>>>>>>>>>>>联系人姓名列表：" + name);
                System.out.println(">>>>>>>>>>>>>>>>短信的内容：" + body);
                showToast(number);
                showToast(name);
                showToast(body);
                // 这里我是要获取自己短信服务号码中的验证码~~
                Pattern pattern = Pattern.compile("[a-zA-Z0-9]{5}");
                Matcher matcher = pattern.matcher(body);//String body="测试验证码2346ds";
                if (matcher.find()) {
                    String res = matcher.group().substring(0, 5);// 获取短信的内容
                    showToast(res);
                    System.out.println(res);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return al;
    }

    protected void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    class SmsData {
        String address;
        String person;
        String boby;
        String _id;
        String date;
        String type;
        String creator;
        String date_sent;
        String error_code;
        String locked;
        String protocol;
        String read;
        String reply_path_present;
        String seen;
        String service_center;
        String status;
        String subject;
        int sub_id;
        int thread_id;
    }

    class SmsObserver extends ContentObserver {
        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone();
        }
    }
}
