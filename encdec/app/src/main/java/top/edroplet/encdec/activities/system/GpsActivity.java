package top.edroplet.encdec.activities.system;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import top.edroplet.encdec.R;
import top.edroplet.encdec.service.ProximityReceiver;

public class GpsActivity extends Activity implements View.OnClickListener {
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1;                // in meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 5000;                   // in Milliseconds
    private static final long PROX_ALERT_EXPIRATION = -1;                           // -1 is never expires
    private static final String PROX_ALERT_INTENT = "top.edroplet.encdec.service.ProximityReceiver.ProximityAlert";
    public String[] Screen;
    // setting default screen text
    public TextView txtName;
    public TextView txtInfo;
    public TextView txtClue;
    //public static final int KEY_LOCATION_CHANGED = 0;
    double latitude, longitude;
    private Button btn_one;
    private Button btn_two;
    private Button btn_three;
    private TextView tv_result;
    //private String[] locationList;
    private LocationManager lm;
    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 当GPS定位信息发生改变时，更新定位
            //String provider = location.getProvider();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.i("TEST", "lat: " + latitude + " lng: " + longitude + " " + PROX_ALERT_INTENT);
            updateShow(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            // 当GPS LocationProvider可用时，更新定位
            if (checkPermission()) {
                updateShow(lm.getLastKnownLocation(provider));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateShow(null);
        }
    };
    private List<String> pNames = new ArrayList<String>(); // 存放LocationProvider名称的集合
    private ProximityReceiver proximityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        @SuppressWarnings("unused")
        Resources res = getResources();

        txtName = (TextView) findViewById(R.id.txtName);
        Screen = getResources().getStringArray(R.array.first);
        txtName.setText(Screen[0]);


        txtInfo = (TextView) findViewById(R.id.txtInfo);
        Screen = getResources().getStringArray(R.array.first);
        txtInfo.setText(Screen[1]);



        txtClue = (TextView)findViewById(R.id.txtClue);
        Screen = getResources().getStringArray(R.array.first);
        txtClue.setText(Screen[2]);


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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 4, locationListener);
        //设置间隔两秒获得一次网络定位信息
        // lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 4, locationListener);

        // 临近警告(地理围栏)
        //定义固定点的经纬度
        double longitude = 112.943285;
        double latitude = 28.149268;
        float radius = 10;     //定义半径，米
        addProximityAlert(latitude, longitude, radius, -1, 0);
        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        proximityReceiver = new ProximityReceiver(this);                      // registers ProximityIntentReceiver
        registerReceiver(proximityReceiver, filter);

        addProximityAlerts();
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
            sb.append("经度：" + location.getLongitude() + "\n");
            sb.append("纬度：" + location.getLatitude() + "\n");
            sb.append("高度：" + location.getAltitude() + "\n");
			float speed = location.getSpeed();
            sb.append("速度：" + speed + "(m/s)="+speed*3.6+"(km/h)\n");
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
        Log.i("TEST", "Close Out");
        unregisterReceiver(proximityReceiver);
    }
    private void addProximityAlert(Double latitude, Double longtitude, float radius, int expire, int productId) {
        Intent intent = new Intent(PROX_ALERT_INTENT);
        intent.putExtra("productId", productId);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if(!checkPermission()) {
            return;
        }
        if (!isGpsAble(lm)) {
            Toast.makeText(this, "请打开GPS~", Toast.LENGTH_SHORT).show();
            openGPS();
        }
        if (isGpsAble(lm)) {
            if(false){
                intent = new Intent(this, ProximityReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                lm.addProximityAlert(latitude, longtitude, radius, expire, pi);
            }else {
                lm.addProximityAlert(latitude, longtitude, radius, expire, proximityIntent);
                IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
                registerReceiver(new ProximityReceiver(this), filter);
            }
        }
    }

    private void addProximityAlerts(){
        if (!checkPermission()) {
            return;
        }
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null)
            Toast.makeText(this, "No location", Toast.LENGTH_SHORT).show();
        else
            addProximityAlert(loc.getLatitude(), loc.getLongitude(), 1, 0);

        addProximityAlert (38.042015, -84.492637, 10, 27);                 // test Awesome
        addProximityAlert (38.085705, -84.561101, 10, 26);                 // Test Home Location
        addProximityAlert (38.152649, -84.895205, 10, 25);                 // Test Office Location
        addProximityAlert (38.197871, -84.866924, 3, 1);                   // Information Center
        addProximityAlert (38.196001, -84.867435, 6, 2);                   // Goebel
        addProximityAlert (38.203191, -84.867674, 7, 3);                   // Chapel
        addProximityAlert (38.192173, -84.870451, 6, 4);                   // Confederate Cemetery
        addProximityAlert (38.193455, -84.868534, 2, 5);                   // O'Bannon
        addProximityAlert (38.193815, -84.864904, 2, 6);                   // Henry Clay Jr
        addProximityAlert (38.087388, -84.547503, 2, 7);                   // O'Hara
        addProximityAlert (38.191642, -84.870967, 5, 8);                   // Daniel Boone
    }


    private void addProximityAlert(double latitude, double longitude, int radius, int ID) {
        Log.i("TEST", "addProximityAlert "+latitude+", "+longitude+", "+radius+", " +ID+", " + PROX_ALERT_EXPIRATION);
        Intent intent = new Intent(PROX_ALERT_INTENT);
        intent.putExtra("ID", ID);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //lm.addProximityAlert(latitude, longitude, radius, ID, proximityIntent);
        if (!checkPermission()) {
            return;
        }
        lm.addProximityAlert(latitude, longitude, radius, PROX_ALERT_EXPIRATION, proximityIntent);

    }

    public void onProximityAlert(int ID, boolean entering) {
        Log.i("TEST", "LOC " +latitude+", "+longitude);
        Log.i("TEST", "onProximityAlert ID="+ID+" entering: "+entering);


        switch (ID){
            case 1:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.start);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated"+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.start);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.start);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 2:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.goebel);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.goebel);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.goebel);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 3:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.church);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.church);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.church);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 4:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.confederate);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.confederate);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.confederate);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 5:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.obannon);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.obannon);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.obannon);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 6:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.hcj);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.hcj);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.hcj);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 7:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.ohara);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.ohara);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.ohara);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 8:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.danielboone);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.danielboone);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.danielboone);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 25:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.toffice);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.toffice);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.toffice);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 26:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.thome);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.thome);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.thome);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            case 27:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.tawesome);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.tawesome);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.tawesome);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
            default:
                txtName = (TextView) findViewById(R.id.txtName);
                Screen = getResources().getStringArray(R.array.first);
                txtName.setText(Screen[0]);
                Log.i("txtName", "populated "+ID);

                txtInfo = (TextView) findViewById(R.id.txtInfo);
                Screen = getResources().getStringArray(R.array.first);
                txtInfo.setText(Screen[1]);
                Log.i("txtInfo", "populated "+ID);

                txtClue = (TextView)findViewById(R.id.txtClue);
                Screen = getResources().getStringArray(R.array.first);
                txtClue.setText(Screen[2]);
                Log.i("txtClue", "populated "+ID);
                break;
        }
    }

}
