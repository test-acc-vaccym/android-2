package com.edroplet.qxx.bluetoothcommunication;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import com.edroplet.qxx.bluetoothcommunication.MainActivity.Type;

public class DeviceActivity extends AppCompatActivity {
    private static final String TAG = "DeviceActivity";
    private static final int REQUEST_PERMISSION_LOCATION = 255;

    private ListView mListView;
    //数据
    private ArrayList<DeviceBean> mDatas;
    private Button mBtnSearch, mBtnService;
    private ChatListAdapter mAdapter;
    //蓝牙适配器
    private BluetoothAdapter mBtAdapter;
    private BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        // 查看是否有必要的权限
        mayRequestLocation();
        // 获取上次连接的设备
        String deviceAddress = GetLastDeviceAddress();
        if (deviceAddress!=null && deviceAddress.length() > 2){

        }
        initDatas();
        initViews();
        registerBroadcast();
        init();
    }


    private void initDatas() {
        mDatas = new ArrayList<DeviceBean>();
        mAdapter = new ChatListAdapter(this, mDatas);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 列出所有的蓝牙设备
     */
    private void init() {
        Log.i("tag", "mBtAdapter=="+ mBtAdapter);
        //根据适配器得到所有的设备信息
        Set<BluetoothDevice> deviceSet = mBtAdapter.getBondedDevices();
        if (deviceSet.size() > 0) {
            for (BluetoothDevice device : deviceSet) {
                mDatas.add(new DeviceBean(device.getName() + "\n" + device.getAddress(), true));
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }
        } else {
            mDatas.add(new DeviceBean("没有配对的设备", true));
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mDatas.size() - 1);
        }
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        //设备被发现广播
        IntentFilter discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, discoveryFilter);

        // 设备发现完成
        IntentFilter foundFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, foundFilter);
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);


        mListView.setOnItemClickListener(mDeviceClickListener);

        mBtnSearch = (Button) findViewById(R.id.start_seach);
        mBtnSearch.setOnClickListener(mSearchListener);


        mBtnService = (Button) findViewById(R.id.start_service);
        mBtnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MainActivity.mType = Type.SERVICE;
                MainActivity.mTabHost.setCurrentTab(1);
                mBtAdapter.enable();
            }
        });

    }

    // 将数据保存至SharedPreferences
    private void SaveLastDeviceAddress(String address) {
        SharedPreferences.Editor editor=getSharedPreferences("device",MODE_PRIVATE).edit();
        editor.putString("address",address);
        boolean commitResult = editor.commit();
        if(!commitResult){
            Toast.makeText(this,"保存数据失败",Toast.LENGTH_LONG).show();
        }
    }

    //    从SharedPreferences获取数据:
    private String GetLastDeviceAddress() {
        SharedPreferences preferences = getSharedPreferences("device", Context.MODE_PRIVATE);
        String address = preferences.getString("address", "");
        return address;
    }

    /**
     * 搜索监听
     */
    private View.OnClickListener mSearchListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
                mBtnSearch.setText("重新搜索");
            } else {
                mDatas.clear();
                mAdapter.notifyDataSetChanged();

                init();

				/* 开始搜索 */
                mBtAdapter.startDiscovery();
                mBtnSearch.setText("ֹͣ停止搜索");
            }
        }
    };

    /**
     * 点击设备监听
     */
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DeviceBean bean = mDatas.get(position);
            String info = bean.message;
            String address = info.substring(info.length() - 17);
            MainActivity.BlueToothAddress = address;

            AlertDialog.Builder stopDialog = new AlertDialog.Builder(DeviceActivity.this);
            stopDialog.setTitle("连接");//标题
            stopDialog.setMessage(bean.message);
            stopDialog.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mBtAdapter.cancelDiscovery();
                    mBtnSearch.setText("重新搜索");

                    MainActivity.mType = Type.CILENT;
                    MainActivity.mTabHost.setCurrentTab(1);

                    dialog.cancel();
                }
            });
            stopDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.BlueToothAddress = null;
                    dialog.cancel();
                }
            });
            stopDialog.show();
        }
    };

    /**
     * 发现设备广播
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 获得设备信息
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 如果绑定的状态不一样
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mDatas.add(new DeviceBean(device.getName() + "\n" + device.getAddress(), false));
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(mDatas.size() - 1);
                }
                // 如果搜索完成了
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                if (mListView.getCount() == 0) {
                    mDatas.add(new DeviceBean("û没有发现蓝牙设备", false));
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(mDatas.size() - 1);
                }
                mBtnSearch.setText("重新搜索");
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 3);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }

    public  void autoConnect(){
        String deviceAddress= GetLastDeviceAddress();
        if (deviceAddress!=null){
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBtAdapter
                    .getBondedDevices();// 获取本机已配对设备
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device1 : pairedDevices) {
                    if (device1.getAddress().equals(deviceAddress))
                        device=device1;
                    break;
                }
            }
        }
    }

    // 自动连接上次连接的蓝牙设备 http://blog.csdn.net/u010955636/article/details/52026767
    private void getDeviceAndConnect(){
        final Intent intent = this.getIntent();
        device =intent.getParcelableExtra("device");
        if (device==null){
            autoConnect();
        }
        if (device!=null){
//            progressDialog.show();
//            new ConnectThread(device).start();
        }
    }
    // 通过Intent启动设置蓝牙可见性
    private void ensureBluetoothDiscoverable() {
        if(mBtAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3);
            startActivity(intent);
        }
    }

    // 通过系统setting强制开启蓝牙可见性
    public void setDiscoverableTimeout(int timeout) {
        BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode =BluetoothAdapter.class.getMethod("setScanMode", int.class,int.class);
            setScanMode.setAccessible(true);
            setDiscoverableTimeout.invoke(adapter, timeout);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 关闭蓝牙可见性
    private void closeBluetoothDiscoverable(){
        //尝试关闭蓝牙可见性
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode =BluetoothAdapter.class.getMethod("setScanMode", int.class,int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(mBtAdapter, 1);
            setScanMode.invoke(mBtAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Android6.0蓝牙搜索需要定位权限,具体博文请看Android6.0权限详解， 蓝牙搜索使用的权限申请方法
    private void mayRequestLocation() {
        Log.d(TAG, "mayRequestLocation: androidSDK--" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 23) {
            //6.0以上设备
            int checkCallPhonePermission = checkSelfPermission(Manifest.permission.
                    ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "mayRequestLocation: 请求粗略定位的权限");
                requestPermissions(new String[]{Manifest.permission.
                        ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
                return;
            }
        }
    }
}
