package com.edroplet.qxx.bluetoothcommunication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import com.edroplet.qxx.bluetoothcommunication.MainActivity.Type;

/**
 * 会话界面
 *
 * @Project App_Bluetooth
 * @Package com.android.bluetooth
 * @author chenlin
 * @version 1.0
 * @Date 2013年3月2日
 * @Note TODO
 */

public class ChatActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener, View.OnClickListener {
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
    private ServerThread mServerThread;
    private ClientThread mClientThread;
    private ReadThread mReadThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initDatas();
        initViews();
        initEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.isOpen) {
            Toast.makeText(this, "连接已经打开，可以通信。如果要再建立连接，请先断开", Toast.LENGTH_SHORT).show();
            return;
        }
        if (MainActivity.mType == Type.CILENT) {
            String address = MainActivity.BlueToothAddress;
            if (!"".equals(address)) {
                mDevice = mBluetoothAdapter.getRemoteDevice(address);
                mClientThread = new ClientThread();
                mClientThread.start();
                MainActivity.isOpen = true;
            } else {
                Toast.makeText(this, "address is null !", Toast.LENGTH_SHORT).show();
            }
        } else if (MainActivity.mType == Type.SERVICE) {
            mServerThread = new ServerThread();
            mServerThread.start();
            MainActivity.isOpen = true;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MainActivity.mType == Type.CILENT) {
            shutdownClient();
        } else if (MainActivity.mType == Type.SERVICE) {
            shutdownServer();
        }
        MainActivity.isOpen = false;
        MainActivity.mType = Type.NONE;
    }

    /* 停止服务器 */
    private void shutdownServer() {
        new Thread() {
            public void run() {
                if (mServerThread != null) {
                    mServerThread.interrupt();
                    mServerThread = null;
                }
                if (mReadThread != null) {
                    mReadThread.interrupt();
                    mReadThread = null;
                }
                try {
                    if (mSocket != null) {
                        mSocket.close();
                        mSocket = null;
                    }
                    if (mServerSocket != null) {
                        mServerSocket.close();
                        mServerSocket = null;
                    }
                } catch (IOException e) {
                    Log.e("server", "mserverSocket.close()", e);
                }
            };
        }.start();
    }

    /* ͣ停止客户端连接 */
    private void shutdownClient() {
        new Thread() {
            public void run() {
                if (mClientThread != null) {
                    mClientThread.interrupt();
                    mClientThread = null;
                }
                if (mReadThread != null) {
                    mReadThread.interrupt();
                    mReadThread = null;
                }
                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mSocket = null;
                }
            };
        }.start();
    }


    /**
     * 信息处理
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;
            switch (msg.what) {
                case STATUS_CONNECT:
                    Toast.makeText(ChatActivity.this, info, Toast.LENGTH_SHORT).show();
                    break;
            }

            if (msg.what == 1) {
                mDatas.add(new DeviceBean(info, true));
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }else {
                mDatas.add(new DeviceBean(info, false));
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }
        }

    };

    // 客户端线程
    private class ClientThread extends Thread {
        public void run() {
            try {
                mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                Message msg = new Message();
                msg.obj = "请稍候，正在连接服务器:" + MainActivity.BlueToothAddress;
                msg.what = STATUS_CONNECT;
                mHandler.sendMessage(msg);

                mSocket.connect();

                msg = new Message();
                msg.obj = "已经连接上服务端！可以发送信息。";
                msg.what = STATUS_CONNECT;
                mHandler.sendMessage(msg);
                // 启动接受数据
                mReadThread = new ReadThread();
                mReadThread.start();
            } catch (IOException e) {
                Message msg = new Message();
                msg.obj = "连接服务端异常！断开连接重新试一试。";
                msg.what = STATUS_CONNECT;
                mHandler.sendMessage(msg);
            }
        }
    };

    // 开启服务器
    private class ServerThread extends Thread {
        public void run() {
            try {
                // 创建一个蓝牙服务器 参数分别：服务器名称、UUID
                mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                Message msg = new Message();
                msg.obj = "请稍候，正在等待客户端的连接...";
                msg.what = STATUS_CONNECT;
                mHandler.sendMessage(msg);

				/* 接受客户端的连接请求 */
                mSocket = mServerSocket.accept();

                msg = new Message();
                msg.obj = "客户端已经连接上！可以发送信息。";
                msg.what = STATUS_CONNECT;
                mHandler.sendMessage(msg);
                // 启动接受数据
                mReadThread = new ReadThread();
                mReadThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 读取数据
    private class ReadThread extends Thread {
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream is = null;
            try {
                is = mSocket.getInputStream();
                while (true) {
                    if ((bytes = is.read(buffer)) > 0) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        Message msg = new Message();
                        msg.obj = s;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    private void initDatas() {
        mDatas = new ArrayList<DeviceBean>();
        mAdapter = new DeviceListAdapter(this, mDatas);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);

        mEtMsg = (EditText) findViewById(R.id.MessageText);
        mEtMsg.clearFocus();

        mBtnSend = (Button) findViewById(R.id.btn_msg_send);
        mBtnDisconn = (Button) findViewById(R.id.btn_disconnect);
    }
    private void initEvents() {
        mListView.setOnItemClickListener(this);

        // 发送信息
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String text = mEtMsg.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    // 发送信息
                    sendMessageHandle(text);

                    mEtMsg.setText("");
                    mEtMsg.clearFocus();
                    // 隐藏软键盘
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(mEtMsg.getWindowToken(), 0);
                } else
                    Toast.makeText(ChatActivity.this, "发送内容不能为空！", Toast.LENGTH_SHORT).show();
            }
        });

        // 关闭会话
        mBtnDisconn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.mType == Type.CILENT) {
                    shutdownClient();
                } else if (MainActivity.mType == Type.SERVICE) {
                    shutdownServer();
                }
                MainActivity.isOpen = false;
                MainActivity.mType = Type.NONE;
                Toast.makeText(ChatActivity.this, "已断开连接！", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // 发送数据
    private void sendMessageHandle(String msg) {
        if (mSocket == null) {
            Toast.makeText(this, "没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            OutputStream os = mSocket.getOutputStream();
            os.write(msg.getBytes());

            mDatas.add(new DeviceBean(msg, false));
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mDatas.size() - 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
