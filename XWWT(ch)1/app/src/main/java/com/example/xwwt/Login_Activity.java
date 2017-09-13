package com.example.xwwt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.example.CommonFunction.Dao;
import com.example.CommonFunction.Data;
import com.example.CommonFunction.SerialCom;
import com.example.CommonFunction.Server;
import com.example.CommonFunction.ServerIP;
import com.example.CommonFunction.XMLOperator;

import com.example.flatbuttonlib.FButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xwwt.R;

public class Login_Activity extends Activity
{
	public static final String USER_NAME="username";
	public static final String PASS_WORD="password";
	
	//用户输入的用户名及密码
	private EditText userName,password;
	private String thisName,thisPassword;
	
	private JSONObject json;
	
	private EditText ip,port,type;//输入的服务器IP,PORT及产品型号等信息
	
	private String status;
	
	private View view_more;
    private View menu_more;
    private ImageView img_more_up;
    private boolean isShowMenu = false;
	
	private Button btnLogin;//定义进入主程序方法
	
	String result= "";  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);//login.activity.xml文件     
    }
    

    public void Login(View v) throws XmlPullParserException{
    	
    	ip=(EditText)findViewById(R.id.editText03);
    	port=(EditText)findViewById(R.id.editText04);
    	type=(EditText)findViewById(R.id.editText05);
    	
    	//读取输入的ip及port
    	if("".equals(ip.getText().toString())&&"".equals(port.getText().toString()))
    	{
    		//获取服务器IP
    		ServerIP newIP=new ServerIP(Login_Activity.this);
    		String ip=newIP.GetIP();
    		Data.SetIP(ip);   		
    	}
    	else
    	{
    		ServerIP newIP=new ServerIP(Login_Activity.this);//Login_Activity.this
    		newIP.SetIP(ip.getText().toString(), port.getText().toString());
    		Data.SetIP(ip.getText().toString());
    	}
       
    	//查询数据库，判断是否满足数据库用户要求
    	userName=(EditText)findViewById(R.id.editText01);
    	password=(EditText)findViewById(R.id.editText02);
    	
    	//进度条
//        final ProgressDialog progressDialog = new ProgressDialog(Login_Activity.this,
//                R.style.AppTheme);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
    	
    	//以下代码为测试使用
//    	if("sys".equals(userName.getText().toString())&&"111111".equals(password.getText().toString()))
//    	{
//
//    		Intent aIntent = new Intent();
//    		aIntent.setClass(Login_Activity.this,MainUI.class);
//    		startActivity(aIntent);
//    		this.finish();
//    	}
//    	else
//    	{
//    		//提示服务器的IP地址等信息，如果确定继续，否则等待输入
//			AlertDialog dialog =new AlertDialog.Builder(Login_Activity.this)
//					.setTitle("Alert")
//					.setMessage("UserName or Password Failure")
//					.setPositiveButton("OK",null)
//					.show();
//    	}
    	//以下代码需要最后测试时使用

    	//需要更改，提供服务器ip
    	
		new Thread()
		{
			@Override 
			public void run()
			{
		    	Dao db=new Dao();
		        json=db.CheckUser(userName.getText().toString(), password.getText().toString());
		        try {
		        	if(json.length()!=0)
		        	{
		        	if(json.getString("success").equals("0")||json.getString("success").equals("2"))
		        	{
						Looper.prepare();
						Toast.makeText(Login_Activity.this, R.string.assert11, Toast.LENGTH_SHORT).show();
						Looper.loop();
		        	}
		        	else
		        	{
					   status=json.getString("success");
		        	}
		        	}
		        	else
		        	{
						Looper.prepare();
						Toast.makeText(Login_Activity.this, R.string.assert11, Toast.LENGTH_SHORT).show();
						Looper.loop();
		        	}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 延迟1.5秒进入
      	if("1".equals(status))
      	{
        	Intent aIntent = new Intent();
          	aIntent.setClass(Login_Activity.this,MainUI.class);
    		startActivity(aIntent);
            this.finish();
      	}
   	
    }
    
    //进入注册新账户页面
    public void NewAccount(View v)
    {
      	Intent aIntent = new Intent();
      	aIntent.setClass(Login_Activity.this,Register_Activity.class);
		startActivity(aIntent);
        //this.finish();
    }
    

    //弹出更多的设置信息
    public void MoreSet(View v)
    {
		img_more_up = (ImageView)findViewById(R.id.imageView01);
				
		menu_more = findViewById(R.id.menuView01);
		
		if(isShowMenu)
		{
			menu_more.setVisibility(View.GONE);
			img_more_up.setImageResource(R.drawable.login_more_up);
			isShowMenu = false;
		}
		else{
			menu_more.setVisibility(View.VISIBLE);
			img_more_up.setImageResource(R.drawable.login_more);
			isShowMenu = true;
		}
    }
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	public void showMoreMenu(boolean show){
		if(show)
		{
			menu_more.setVisibility(View.GONE);
			img_more_up.setImageResource(R.drawable.login_more_up);
			isShowMenu = false;
		}
		else{
			menu_more.setVisibility(View.VISIBLE);
			img_more_up.setImageResource(R.drawable.login_more);
			isShowMenu = true;
		}
	}
   
}