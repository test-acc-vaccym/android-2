/*
 * ���ݿ��࣬���еĲ��������û��Ĵ�������¼��������Ϣ�Ķ�ȡ��ӡ�������Ϣ�Ķ�ȡ�����
 * ��Ҫע����ǣ������ݿ�������PHP������Ϣ������PHP�����ݿ���в���
 */
package com.example.CommonFunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.CommonFunction.PHPOperator;//��PHP���Ϳ���ָ��

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
	private String userName ="";//�û���
	private String passwordv ="";//����
	private String cityName= "";//������
	private String latitude="";//����
	private String longitude ="";//γ��
	private String statllite ="";//������
	private String orbit ="";//���ǹ����Ϣ
	private String httpURL="";//���ݿ��ļ����ڵ�λ��http://192.168.33.3
	private String folder="php";//php�ļ������ļ��е�ַ
	private String fileName="mysqlTest.php";//PHP�ļ���
	String url_1 = "";//httpURL+"/"+folder+"/"+fileName+"?"
	
	/*
	 * ���캯��
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
	 * ����û����Ƿ������ݿ���
	 * ���ݺ���Ϊ�û���������
	 */
	public JSONObject CheckUser(String userName,String password)
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";//folder+"/"+
		JSONObject json=new JSONObject();//������յ�������
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��JAVA�Ŀ�
		
        params.add(new BasicNameValuePair("action", "login"));  //���������ݿ�д����
        params.add(new BasicNameValuePair("userName", userName));  //���������ݿ�д����
        params.add(new BasicNameValuePair("password", password));  
        
        String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		PHPOperator operator=new PHPOperator();
		json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;//����JSON����
		
	}
	
	/*
	 * �����û���Ϣ 
	 */
	public JSONObject AddUser(String userName,String password)
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONObject json=new JSONObject();//������յ�������
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��JAVA�Ŀ�
        params.add(new BasicNameValuePair("action", "register"));  //���������ݿ�д����
        params.add(new BasicNameValuePair("userName", userName));  //���������ݿ�д����
        params.add(new BasicNameValuePair("password", password));  
        
		PHPOperator operator=new PHPOperator();
		
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPCommunication(params, url_1, fileName);
		return json;//����JSON����
	}

	/*
	 * ��ѯ������Ϣ
	 * ����JSONArray����
	 */
	public JSONArray CheckCity()
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONArray json=new JSONArray();//������յ�������
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��JAVA�Ŀ�
        params.add(new BasicNameValuePair("action", "cityEnquire"));  //���������ݿ�д����
        //params.add(new BasicNameValuePair("cityName", cityName));  //���������ݿ�д����

		PHPOperator operator=new PHPOperator();
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPOperator(params, url_1, fileName);
		//json=operator.PHPCommunication(params, url, url);
		return json;//����JSON����
	}
	
	/*
	 * �����ݿ������ӳ�����Ϣ
	 */
	public JSONObject AddCity(String cityName,String latitude,String longitude)
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��JAVA�Ŀ�
        params.add(new BasicNameValuePair("action", "cityAdd"));  //���������ݿ�д����
        params.add(new BasicNameValuePair("cityName", cityName));  //���������ݿ�д����
        params.add(new BasicNameValuePair("latitude", latitude));  //���������ݿ�д����
        params.add(new BasicNameValuePair("longitude", longitude));  //���������ݿ�д����

		PHPOperator operator=new PHPOperator();
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPCommunication(params, url_1, fileName);
		return json;//����JSON����
	}
	
	
	
	/*
	 * ��ѯ������Ϣ,�������е�������Ϣ
	 */
	public JSONArray CheckSatllite()
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONArray json=new JSONArray();//������յ�������
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��JAVA�Ŀ�
        params.add(new BasicNameValuePair("action", "satInfor"));  //���������ݿ�д����
		
		PHPOperator operator=new PHPOperator();
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPOperator(params, url_1, fileName);
		return json;//����JSON����
	}
	
	/**
	 * 
	 * @param satName ��������
	 * @param latitude ���Ǿ���
	 * @param satOldNam ���Ǿ�����
	 * @param verticalValue ���Ǵ�ֱ����
	 * @param horizontalValue ����ˮƽ����
	 * @param remark ��ע
	 * @return
	 */
	public JSONObject AddSatellite(String satlliteName,String satLongitude,String satNameOld, String verticalValue,
			String horizontalValue,String remark)
	{
		//url_1= "http://"+Data.GetIP()+"/"+fileName+"?";
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��JAVA�Ŀ�
        params.add(new BasicNameValuePair("action", "satAdd"));  //���������ݿ�д����
        params.add(new BasicNameValuePair("satlliteName", satlliteName));  //���������ݿ�д����
        params.add(new BasicNameValuePair("satNameOld", satNameOld));  //���������ݿ�д����
        params.add(new BasicNameValuePair("satLongitude", satLongitude));  //���������ݿ�д����
        params.add(new BasicNameValuePair("verticalValue", verticalValue));  //���������ݿ�д����
        params.add(new BasicNameValuePair("horizontalValue", horizontalValue));  //���������ݿ�д����
        params.add(new BasicNameValuePair("remark", remark));  //���������ݿ�д����
        
		PHPOperator operator=new PHPOperator();
		String url = url_1+URLEncodedUtils.format(params, HTTP.UTF_8);
		json=operator.PHPCommunication(params, url_1, fileName);
        
		return json;//����JSON����
	}
}