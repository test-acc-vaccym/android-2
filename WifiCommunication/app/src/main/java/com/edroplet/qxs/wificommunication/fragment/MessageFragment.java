package com.edroplet.qxs.wificommunication.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.edroplet.qxs.wificommunication.R;
import com.edroplet.qxs.wificommunication.adapter.DeviceListAdapter;
import com.edroplet.qxs.wificommunication.bean.DeviceBean;

import java.util.ArrayList;

/**
 * Created by qxs on 2017/7/25.
 */

public class MessageFragment extends BaseFragment {
    private static final int STATUS_CONNECT = 0x11;

    private ListView mListView;
    private ArrayList<DeviceBean> mDatas;
    private Button mBtnSend;// 发送按钮
    private Button mBtnDisconn;// 断开连接
    private EditText mEtMsg;
    private DeviceListAdapter mAdapter;

    /* 一些常量，代表服务器的名称 */
    public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
    public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";

    // 蓝牙服务端socket
    private BluetoothServerSocket mServerSocket;
    // 蓝牙客户端socket
    private BluetoothSocket mSocket;
    // 设备
    private BluetoothDevice mDevice;
    private BluetoothAdapter mBluetoothAdapter;

    // --线程类-----------------
//    private ServerThread mServerThread;
//    private ClientThread mClientThread;
//    private ReadThread mReadThread;
    @Override
    protected void initView() {
        mListView = (ListView) this.mView.findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);

        mEtMsg = (EditText) this.mView.findViewById(R.id.MessageText);
        mEtMsg.clearFocus();

        mBtnSend = (Button) this.mView.findViewById(R.id.btn_msg_send);
        mBtnDisconn = (Button) this.mView.findViewById(R.id.btn_disconnect);
    }

    private void initDatas() {
        mDatas = new ArrayList<DeviceBean>();
        mAdapter = new DeviceListAdapter(this.mContext, mDatas);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }
    @Override
    protected void getDataFromServer() {
        Toast.makeText(mContext, "DeviceFragment 页面请求数据了", Toast.LENGTH_SHORT).show();
    }
}
