package top.edroplet.encdec.utils.sms;

/**
 * Created by xw on 2017/4/28.
 */

public class SmsData {
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

    public String get_id() {
        return _id;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getBoby() {
        return boby;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public String getType() {
        return type;
    }

    public String getPerson() {
        return person;
    }

    public String getCreator() {
        return creator;
    }

    public String getError_code() {
        return error_code;
    }

    public String getSubject() {
        return subject;
    }
}
