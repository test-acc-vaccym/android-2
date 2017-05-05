package top.edroplet.encdec.activities.system;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        bindViews();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 此处的判定是主要问题，API23之后需要先判断之后才能调用locationManager中的方法
        // 包括这里的getLastKnewnLocation方法和requestLocationUpdates方法
        if(!checkPermission()) {
            return;
        }

        if (!isGpsAble(lm)) {
            Toast.makeText(this, "请打开GPS~", Toast.LENGTH_SHORT).show();
            openGPS();
        }

        //从GPS获取最近的定位信息
        Location lc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateShow(lc);
        //设置间隔两秒获得一次GPS定位信息
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 8, locationListener);
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 当GPS定位信息发生改变时，更新定位
            updateShow(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            // 当GPS LocationProvider可用时，更新定位
            if(checkPermission()) {
                updateShow(lm.getLastKnownLocation(provider));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateShow(null);
        }
    };

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
                String provider = "";
                pNames.clear();
                //获取当前可用的位置控制器
                pNames = lm.getAllProviders();
                if (pNames.contains(LocationManager.GPS_PROVIDER)) {
                    //是否为GPS位置控制器
                    provider = LocationManager.GPS_PROVIDER;
                } else if (pNames.contains(LocationManager.NETWORK_PROVIDER)) {
                    //是否为网络位置控制器
                    provider = LocationManager.NETWORK_PROVIDER;
                } else {
                    Toast.makeText(this, "请检查网络或GPS是否打开", Toast.LENGTH_LONG).show();
                    break;
                }
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

    private boolean isGpsAble(LocationManager lm){
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)?true:false;
    }

    private boolean checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                Toast.makeText(this,"没有GPS权限！", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void openGPS(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            openGPSSetting();
        }else{
            openGPSForce(this);
        }
    }

    //强制帮用户打开GPS 5.0以前可用
    private void openGPSForce(Context context){
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    //打开位置信息设置页面让用户自己设置
    private void openGPSSetting(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,0);
    }

    //遍历数组返回字符串的方法
    private String getProvider(){
        StringBuilder sb = new StringBuilder();
        for (String s : pNames) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }

    //定义一个更新显示的方法
    private void updateShow(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("当前的位置信息：\n");
            sb.append("精度：" + location.getLongitude() + "\n");
            sb.append("纬度：" + location.getLatitude() + "\n");
            sb.append("高度：" + location.getAltitude() + "\n");
            sb.append("速度：" + location.getSpeed() + "\n");
            sb.append("方向：" + location.getBearing() + "\n");
            sb.append("定位精度：" + location.getAccuracy() + "\n");
            tv_result.setText(sb.toString());
        } else {
            tv_result.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //api23需要这样写
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            if (lm != null) {
                lm.removeUpdates(locationListener);
            }
        }
    }
}
