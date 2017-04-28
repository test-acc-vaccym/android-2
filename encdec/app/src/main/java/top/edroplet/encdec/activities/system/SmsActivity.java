package top.edroplet.encdec.activities.system;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import top.edroplet.encdec.R;
import top.edroplet.encdec.utils.sms.GetMessagesUtil;
import top.edroplet.encdec.utils.sms.SendMessageUtil;
import top.edroplet.encdec.utils.sms.SmsData;

/**
 * Created by xw on 2017/4/28.
 */

public class SmsActivity extends Activity implements View.OnClickListener {
    LayoutInflater inflater;
    private ArrayList<SmsData> sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity);

        findViewById(R.id.mainButtonSmsSend).setOnClickListener(this);
        ListView lvSms = (ListView) findViewById(R.id.smsActivitySmsList);
        GetMessagesUtil gmu = new GetMessagesUtil(this);
        sms = gmu.getSmsFromPhone();
        Collections.sort(sms, new SmsDataComparator());
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lvSms.setAdapter(new SmsListAdapter());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mainButtonSmsSend:
                SmsManager smsManager = SmsManager.getDefault();
                EditText etSmsNumber, etSmsContent;
                etSmsNumber = (EditText) findViewById(R.id.mainEditTextSmsNumber);
                etSmsContent = (EditText) findViewById(R.id.mainEditTextSmsContent);
                String smsNumber = etSmsNumber.getText().toString();
                String smsContent = etSmsContent.getText().toString();
                if (smsNumber.isEmpty()) {
                    Toast.makeText(this, "号码为空", Toast.LENGTH_LONG).show();
                    break;
                }
                if (smsContent.isEmpty()) {
                    Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
                    break;
                }
                //smsManager.sendTextMessage(smsNumber,null,smsContent,null,null);
                SendMessageUtil smu = new SendMessageUtil();
                smu.sendMessage(this, smsNumber, smsContent);
                break;
        }
    }

    private class SmsDataComparator implements Comparator {
        @Override
        public int compare(Object p1, Object p2) {
            SmsData sd1 = (SmsData) p1;
            SmsData sd2 = (SmsData) p2;

            if (sd1.getDate().compareTo(sd2.getDate()) < 0) {
                return -1;
            }
            return 1;
        }
    }

    private class SmsListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
        @Override
        public int getCount() {
            return sms.size();
        }

        @Override
        public long getItemId(int i) {
            return sms.get(i).hashCode();
        }

        @Override
        public Object getItem(int i) {
            return sms.get(i);
        }

        @Override
        public boolean isEmpty() {
            return sms.isEmpty();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SmsData data = (SmsData) sms.get(i);
            if (view == null) {
                view = inflater.inflate(R.layout.sensor_list_item, null);
            }
            TextView subject = (TextView) view.findViewById(R.id.smsListItemSubject);
            subject.setText(data.getSubject());

            TextView person = (TextView) view.findViewById(R.id.smsListItemFrom);
            person.setText(data.getPerson());

            TextView body = (TextView) view.findViewById(R.id.smsListItemBody);
            body.setText(data.getBoby());

            ((TextView) view.findViewById(R.id.smsListItemBody)).setText(data.getDate());

            return view;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == -1) {
                // 点击head view 或者 foot view
                return;
            }
            int realPosition = (int) l;
            SmsData item = (SmsData) adapterView.getItemAtPosition(realPosition);
            Toast.makeText(null, item.getDate_sent(), Toast.LENGTH_SHORT).show();
        }
    }

}
