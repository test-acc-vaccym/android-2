/*
 * 数据库类，进行的操作包括用户的创建、登录、卫星信息的读取添加、城市信息的读取与添加
 * 需要注意的是，本数据库类是向PHP发送信息，而由PHP对数据库进行操作
 */
package com.example.CommonFunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.CommonFunction.PHPOperator;//向PHP发送控制指令

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dao{
	private String userName ="";//用户名
	private String passwordv ="";//密码
	private String cityName= "";//城市名
	private String latitude="";//经度
	private String longitude ="";//纬度
	private String statllite ="";//卫星名
	private String orbit ="";//卫星轨道信息
	private String httpURL="";//数据库文件所在的位置http://192.168.33.3
	private String folder="php";//php文件所在文件夹地址
	private String fileName="mysqlTest.php";//PHP文件名
	String url_1 = "";//httpURL+"/"+folder+"/"+fileName+"?"
	
	/*
	 * 构造函数
	 */
	public Dao()//
	{
//		ServerIP newIP=new ServerIP(context);
//	    this.httpURL="http://"+newIP.GetIP();//+"/"+"web";
//	    this.url_1 =httpURL+"/"+folder+"/"+fileName+"?";
		//this.url_1= "http://"+Data.GetIP()+"/"+folder+"/"+fileName+"?";
		this.url_1= "http://"+Data.GetIP()+"/"+folder+"/"+fileName+"?";
	}
	
	/*
	 * 检测用户名是否在数据库中
	 * 传递函数为用户名，密码
	 */
	public JSONObject CheckUser(String userName,String password)
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";//folder+"/"+
		JSONObject json=new JSONObject();//定义接收到的数据
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用JAVA的库
		
        params.add(new BasicNameValuePair("action", "login"));  //用于向数据库写数据
        params.add(new BasicNameValuePair("userName", userName));  //用于向数据库写数据
        params.add(new BasicNameValuePair("password", password));  
        
        String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		PHPOperator operator=new PHPOperator();
		json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;//返回JSON数据
		
	}
	
	/*
	 * 增加用户信息 
	 */
	public JSONObject AddUser(String userName,String password)
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONObject json=new JSONObject();//定义接收到的数据
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用JAVA的库
        params.add(new BasicNameValuePair("action", "register"));  //用于向数据库写数据
        params.add(new BasicNameValuePair("userName", userName));  //用于向数据库写数据
        params.add(new BasicNameValuePair("password", password));  
        
		PHPOperator operator=new PHPOperator();
		
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPCommunication(params, url_1, fileName);
		return json;//返回JSON数据
	}

	/*
	 * 查询城市信息
	 * 返回JSONArray数据
	 */
	public JSONArray CheckCity()
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONArray json=new JSONArray();//定义接收到的数据
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用JAVA的库
        params.add(new BasicNameValuePair("action", "cityEnquire"));  //用于向数据库写数据
        //params.add(new BasicNameValuePair("cityName", cityName));  //用于向数据库写数据

		PHPOperator operator=new PHPOperator();
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPOperator(params, url_1, fileName);
		//json=operator.PHPCommunication(params, url, url);
		return json;//返回JSON数据
	}
	
	/*
	 * 在数据库中增加城市信息
	 */
	public JSONObject AddCity(String cityName,String latitude,String longitude)
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用JAVA的库
        params.add(new BasicNameValuePair("action", "cityAdd"));  //用于向数据库写数据
        params.add(new BasicNameValuePair("cityName", cityName));  //用于向数据库写数据
        params.add(new BasicNameValuePair("latitude", latitude));  //用于向数据库写数据
        params.add(new BasicNameValuePair("longitude", longitude));  //用于向数据库写数据

		PHPOperator operator=new PHPOperator();
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPCommunication(params, url_1, fileName);
		return json;//返回JSON数据
	}
	
	
	
	/*
	 * 查询卫星信息,包含所有的卫星信息
	 */
	public JSONArray CheckSatllite()
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONArray json=new JSONArray();//定义接收到的数据
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用JAVA的库
        params.add(new BasicNameValuePair("action", "satInfor"));  //用于向数据库写数据
		
		PHPOperator operator=new PHPOperator();
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPOperator(params, url_1, fileName);
		return json;//返回JSON数据
	}
	
	/**
	 * 
	 * @param satName 卫星名称
	 * @param latitude 卫星经度
	 * @param satOldNam 卫星旧名称
	 * @param verticalValue 卫星垂直极化
	 * @param horizontalValue 卫星水平极化
	 * @param remark 备注
	 * @return
	 */
	public JSONObject AddSatellite(String satlliteName,String satLongitude,String satNameOld, String verticalValue,
			String horizontalValue,String remark)
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用JAVA的库
        params.add(new BasicNameValuePair("action", "satAdd"));  //用于向数据库写数据
        params.add(new BasicNameValuePair("satlliteName", satlliteName));  //用于向数据库写数据
        params.add(new BasicNameValuePair("satNameOld", satNameOld));  //用于向数据库写数据
        params.add(new BasicNameValuePair("satLongitude", satLongitude));  //用于向数据库写数据
        params.add(new BasicNameValuePair("verticalValue", verticalValue));  //用于向数据库写数据
        params.add(new BasicNameValuePair("horizontalValue", horizontalValue));  //用于向数据库写数据
        params.add(new BasicNameValuePair("remark", remark));  //用于向数据库写数据
        
		PHPOperator operator=new PHPOperator();
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPCommunication(params, url_1, fileName);
        
		return json;//返回JSON数据
	}
}