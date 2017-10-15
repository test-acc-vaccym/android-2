package com.edroplet.qxx.saneteltabactivity.activities.guide;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;

import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.List;

public class WifiManagerActivity extends AppCompatActivity  implements View.OnClickListener {
    SystemServices.WifiAdmin mWifiAdmin;
    public static final String TAG = "WifiManagerActivity";
    private ListView mListView;
    public int level;
    protected String ssid;
    private List<ScanResult> mWifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_manager);
        mWifiAdmin = new SystemServices.WifiAdmin(WifiManagerActivity.this);
        initViews();
        IntentFilter filter = new IntentFilter(
                WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //="android.net.wifi.STATE_CHANGE"  监听wifi状态的变化
        registerReceiver(mReceiver, filter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AlertDialog.Builder alert=new AlertDialog.Builder(WifiManagerActivity.this);
                ssid=mWifiList.get(position).SSID;
                alert.setTitle(ssid);
                alert.setMessage("输入密码");
                final EditText et_password=new EditText(WifiManagerActivity.this);
                final SharedPreferences preferences=getSharedPreferences("wifi_password",Context.MODE_PRIVATE);
                et_password.setText(preferences.getString(ssid, ""));
                alert.setView(et_password);
                //alert.setView(view1);
                alert.setPositiveButton("连接", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pw = et_password.getText().toString();
                        if(null == pw  || pw.length() < 8){
                            Toast.makeText(WifiManagerActivity.this, "密码至少8位", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString(ssid, pw);   //保存密码
                        editor.commit();
                        mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(ssid, et_password.getText().toString(), 3));
                    }
                });
                alert.setNegativeButton("取消", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        //mWifiAdmin.removeWifi(mWifiAdmin.getNetworkId());
                    }
                });
                alert.create();
                alert.show();

            }
        });
    }

    private void initViews(){
        CustomButton check_wifi,open_wifi,close_wifi,scan_wifi;
        check_wifi=(CustomButton) findViewById(R.id.check_wifi);
        open_wifi=(CustomButton) findViewById(R.id.open_wifi);
        close_wifi=(CustomButton) findViewById(R.id.close_wifi);
        scan_wifi=(CustomButton) findViewById(R.id.scan_wifi);
        mListView=(ListView) findViewById(R.id.wifi_list);
        check_wifi.setOnClickListener(WifiManagerActivity.this);
        open_wifi.setOnClickListener(WifiManagerActivity.this);
        close_wifi.setOnClickListener(WifiManagerActivity.this);
        scan_wifi.setOnClickListener(WifiManagerActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_wifi:
                mWifiAdmin.checkState(WifiManagerActivity.this);
                break;
            case R.id.open_wifi:
                mWifiAdmin.openWifi(WifiManagerActivity.this);
                break;
            case R.id.close_wifi:
                mWifiAdmin.closeWifi(WifiManagerActivity.this);
                break;
            case R.id.scan_wifi:
                mWifiAdmin.startScan(WifiManagerActivity.this);
                mWifiList=mWifiAdmin.getWifiList();
                if(mWifiList!=null){
                    mListView.setAdapter(new WifiManagerAdapter(this,mWifiList));
                    new Utility().setListViewHeightBasedOnChildren(mListView);
                }
                break;
            default:
                break;
        }
    }

    private class WifiManagerAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<ScanResult> list;
        private WifiManagerAdapter(Context context, List<ScanResult> list){
            this.inflater= LayoutInflater.from(context);
            this.list=list;
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @SuppressLint({ "ViewHolder", "InflateParams" })
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.wifi_listitem, null);
            ScanResult scanResult = list.get(position);
            CustomTextView wifi_ssid = view.findViewById(R.id.ssid);
            ImageView wifi_level = view.findViewById(R.id.wifi_level);
            wifi_ssid.setText(scanResult.SSID);
            Log.i(TAG, "scanResult.SSID="+scanResult);
            level=WifiManager.calculateSignalLevel(scanResult.level,5);
            if(scanResult.capabilities.contains("WEP")||scanResult.capabilities.contains("PSK")||
                    scanResult.capabilities.contains("EAP")){
                wifi_level.setImageResource(R.drawable.wifi_signal_open); // wifi_signal_lock
            }else{
                wifi_level.setImageResource(android.R.drawable.ic_lock_silent_mode_off); // wifi_signal_open
            }
            wifi_level.setImageLevel(level);
            //判断信号强度，显示对应的指示图标
            return view;
        }
    }

    /*设置listView的高度*/
    private class Utility {
        private void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }
    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(wifiInfo.isConnected()){
                WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                String wifiSSID = wifiManager.getConnectionInfo()
                        .getSSID();
                Toast.makeText(context, wifiSSID+"连接成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
