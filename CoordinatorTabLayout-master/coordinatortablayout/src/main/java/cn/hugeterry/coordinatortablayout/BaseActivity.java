package cn.hugeterry.coordinatortablayout;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.hugeterry.coordinatortablayout.services.ReceiveMessage;

/***
 * http://blog.csdn.net/sdvch/article/details/13615521
 * Android TCP 客户端实现
 */

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    /**
     * 通过ServiceConnection的内部类实现来连接Service和Activity
     *
     */
    public static final String TAG = "AnetTest";
    private static final boolean DEBUG = true;// false
    private String msg = "";
    private UpdateReceiver mReceiver;
    private Context mContext;
    private ReceiveMessage mReceiveMessage;

    // 实现一个 BroadcastReceiver，用于接收指定的 Broadcast
    public class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DEBUG)
                Log.d(TAG, "onReceive: " + intent);
            msg = intent.getStringExtra("msg");
            System.out.println("recv:" + msg);
            Log.i(TAG, "onReceive: "+ msg);

        }

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mReceiveMessage = ((ReceiveMessage.LocalBinder) service)
                    .getService();
            if (DEBUG)
                Log.d(TAG, "on serivce connected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mReceiveMessage = null;
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.main);

        // 实例化自定义的 BroadcastReceiver
        mReceiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        // 为 BroadcastReceiver 指定 action ，使之用于接收同 action 的广播
        filter.addAction("com.archfree.demo.msg");

        // 以编程方式注册 BroadcastReceiver 。配置方式注册 BroadcastReceiver 的例子见
        // AndroidManifest.xml 文件
        // 一般在 OnStart 时注册，在 OnStop 时取消注册
        this.registerReceiver(mReceiver, filter);
        mContext = BaseActivity.this;

    }
    public void SendMessage(String message){
        mReceiveMessage.SendMessageToServer(message);
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        /**
         * Activity和本地服务交互,需要使用bind和unbind方法
         * */
        mContext.unbindService(serviceConnection);
        unbindService(serviceConnection);
        unregisterReceiver(mReceiver);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        /**
         * Activity和本地服务交互,需要使用bind和unbind方法
         * */
//        Intent i = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putString("chatmessage",message);
//        i.putExtras(bundle);
        bindService(new Intent("com.archfree.demo.ReceiveMessage"),
                serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        if (id == R.id.nav_monitor) {
            // Handle the camera action
        } else if (id == R.id.nav_communication) {
            Toast.makeText(this,"nav_communication clicked",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_system_settings) {

        } else if (id == R.id.nav_satellite) {

        } else if (id == R.id.nav_calibration) {

        } else if (id == R.id.nav_manual) {

        } else if (id == R.id.nav_power) {

        } else if (id == R.id.nav_system_info) {

        } else if (id == R.id.nav_database) {

        } else if (id == R.id.nav_logs) {

        } else if (id == R.id.nav_update) {

        } else if (id == R.id.nav_help) {
            Snackbar.make(null,"help clicked", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
