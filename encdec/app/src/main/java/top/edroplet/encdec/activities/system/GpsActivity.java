package top.edroplet.encdec.activities.system;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import top.edroplet.encdec.R;

public class GpsActivity extends Activity implements View.OnClickListener {
    private Button btn_one;
    private Button btn_two;
    private Button btn_three;
    private TextView tv_result;
    private LocationManager lm;
    private List<String> pNames = new ArrayList<String>(); // 存放LocationProvider名称的集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void bindViews() {
        btn_one = (Button) findViewById(R.id.gpsActivityButtonAll);
        btn_two = (Button) findViewById(R.id.gpsActivityButtonFilter);
        btn_three = (Button) findViewById(R.id.gpsActivityButtonSpecial);
        tv_result = (TextView) findViewById(R.id.gpsActivityTextviewResult);

        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gpsActivityButtonAll:
                pNames.clear();
                pNames = lm.getAllProviders();
                tv_result.setText(getProvider());
                break;
            case R.id.gpsActivityButtonFilter:
                pNames.clear();
                Criteria criteria = new Criteria();
                criteria.setCostAllowed(false);   //免费
                criteria.setAltitudeRequired(true);  //能够提供高度信息
                criteria.setBearingRequired(true);   //能够提供方向信息
                pNames = lm.getProviders(criteria, true);
                tv_result.setText(getProvider());
                break;
            case R.id.gpsActivityButtonSpecial:
                pNames.clear();
                pNames.add(lm.getProvider(LocationManager.GPS_PROVIDER).getName()); //指定名称
                tv_result.setText(getProvider());
                break;
        }
    }

    //遍历数组返回字符串的方法
    private String getProvider(){
        StringBuilder sb = new StringBuilder();
        for (String s : pNames) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }
}
