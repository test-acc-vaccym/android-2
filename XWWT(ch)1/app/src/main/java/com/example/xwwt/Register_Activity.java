package com.example.xwwt;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.example.CommonFunction.Dao;
import com.example.CommonFunction.Data;
import com.example.CommonFunction.ServerIP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xwwt.R;
//定义注册类
public class Register_Activity extends Activity
{
	private View registerActivity;

	private EditText etUser,etPassword1,etPassword2;
	private EditText ip,port;
	
	private Button btnCreate,btnCancel;
	
	private Boolean status=false;
	
	private int successFlag=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.register_activity);
		initialView();
		
	}
	
//	public ViewonCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState)
//	{		
//		registerActivity = inflater.inflate(R.layout.register_activity,container,false);
//	    //initialView(registerActivity);
//		return registerActivity;
//	}
	
	public void initialView()
	{
		etUser=(EditText)findViewById(R.id.editText01);
		ip=(EditText)findViewById(R.id.editText04);
		port=(EditText)findViewById(R.id.editText05);
		//检测用户名是否有输入
		etPassword1=(EditText)findViewById(R.id.editText02);		
		etPassword1.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				if("".equals(etUser))
				{
					AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
							.setTitle("Alert")
							.setMessage(R.string.assert01)
							.setPositiveButton("OK",null)
							.show();
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		
		});
		etPassword2=(EditText)Register_Activity.this.findViewById(R.id.editText03);
		etPassword2.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				if("".equals(etPassword1)||"".equals(etUser))
				{
					AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
							.setTitle("Alert")
							.setMessage(R.string.assert02)
							.setPositiveButton("OK",null)
							.show();
				}
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
/*				if(!etPassword1.equals(etPassword2))
				{
					AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
							.setTitle("Alert")
							.setMessage("assert03")
							.setPositiveButton("OK",null)
							.show();
				}
				else
				{
					status=true;
				}*/
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//检测两次密码输入是否相等
				
			}
		
		});
		//进行登陆相关检测
		btnCreate=(Button)Register_Activity.this.findViewById(R.id.button01);
		
		btnCreate.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(etPassword1.getText().toString().equals(etPassword2.getText().toString()))
				{
					if("".equals(ip.getText().toString())&&"".equals(port.getText().toString())){
						AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
								.setTitle("Alert")
								.setMessage(R.string.assert05)
								.setPositiveButton("OK",null)
								.show();
						
			    		//获取服务器IP
			    		ServerIP newIP=new ServerIP(Register_Activity.this);
			    		String ip=newIP.GetIP();
			    		Data.SetIP(ip);  
						
					}
					else{
		    		try {
		    			ServerIP newIP=new ServerIP(Register_Activity.this);
						newIP.SetIP(ip.getText().toString(), port.getText().toString());
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		Data.SetIP(ip.getText().toString());
					
				}
					new Thread()
					{
						@Override 
						public void run()
						{
							Dao db=new Dao();//Register_Activity.this
							JSONObject json=db.AddUser(etUser.getText().toString(), etPassword1.getText().toString());
						try {
							if(json.length()==0||json.getString("success").equals("0")||json.getString("success").equals("2"))
							{
								Looper.prepare();
								try
								{
								Toast.makeText(Register_Activity.this, R.string.assert09, Toast.LENGTH_SHORT).show();
								}
								catch(Resources.NotFoundException e)
								{}
								Looper.loop();
							}
							else
							{						
								successFlag=1;
								Intent aIntent = new Intent();
								aIntent.setClass(Register_Activity.this,MainUI.class);
								startActivity(aIntent);
								Register_Activity.this.finish();
								Looper.prepare();
								try
								{
								Toast.makeText(Register_Activity.this, R.string.assert10, Toast.LENGTH_SHORT).show();
								}
								catch(Resources.NotFoundException e)
								{}
								Looper.loop();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
						//
					}.start();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//					if(successFlag==1)
//					{
//						Intent aIntent = new Intent();
//						aIntent.setClass(Register_Activity.this,MainUI.class);
//						startActivity(aIntent);
//						Register_Activity.this.finish();
//					}
//					else
//					{
//						Toast.makeText(Register_Activity.this, R.string.assert09, Toast.LENGTH_SHORT).show();
//					}
				
			}
				else
				{
					AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
							.setTitle("Alert")
							.setMessage(R.string.assert03)
							.setPositiveButton("OK",null)
							.show();
				}
			}
			
		
				});
		
//		btnCancel=(Button)Register_Activity.this.findViewById(R.id.button02);
//		btnCancel.setOnClickListener(new View.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			}
//			});
	}
	
	//初始化操作
//	public void initialView(View view)
//	{
//		etUser=(EditText)view.findViewById(R.id.EditText01);
//		//检测用户名是否有输入
//		etPassword1=(EditText)view.findViewById(R.id.EditText02);		
//		etPassword1.addTextChangedListener(new TextWatcher(){
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//				// TODO Auto-generated method stub
//				if("".equals(etUser))
//				{
//					AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
//							.setTitle("Alert")
//							.setMessage("assert01")
//							.setPositiveButton("OK",null)
//							.show();
//				}
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				
//			}
//		
//		});
//		etPassword2=(EditText)view.findViewById(R.id.EditText03);
//		etPassword2.addTextChangedListener(new TextWatcher(){
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//				// TODO Auto-generated method stub
//				if("".equals(etPassword1)||"".equals(etUser))
//				{
//					AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
//							.setTitle("Alert")
//							.setMessage("assert02")
//							.setPositiveButton("OK",null)
//							.show();
//				}
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				//检测两次密码输入是否相等
//				if(!etPassword1.equals(etPassword2))
//				{
//					AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
//							.setTitle("Alert")
//							.setMessage("assert03")
//							.setPositiveButton("OK",null)
//							.show();
//				}
//				else
//				{
//					status=true;
//				}
//			}
//		
//		});
//		//进行登陆相关检测
//		btnCreate=(Button)view.findViewById(R.id.button01);
//		btnCreate.setOnClickListener(new View.OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(status==true)
//				{
//					Dao db=new Dao();
//					db.AddUser(etUser.toString(), etPassword1.toString());
//				}
//				else
//				{
//					AlertDialog dialog =new AlertDialog.Builder(Register_Activity.this)
//							.setTitle("Alert")
//							.setMessage("assert04")
//							.setPositiveButton("OK",null)
//							.show();
//				}
//			}
//			
//		
//				});
//	}
}