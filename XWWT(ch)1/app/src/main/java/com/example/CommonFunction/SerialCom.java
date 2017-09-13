/*
 * 串口操作类，向PHP发送指令
 */
package com.example.CommonFunction;
import com.example.CommonFunction.PHPOperator;

import java.util.ArrayList;
import java.util.List;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class SerialCom {
	private String operation = "";//操作名称
	//private String fileName = "DIOFinal.php";//文件名称
	//private String httpURL = "http://192.168.33.1/web";//webserver所在的地址,，需要在最终的程序中固定
	
	private String httpURL="http://";//数据库文件所在的位置"http://192.168.33.1"
	private String folder="php";//php文件所在文件夹地址
	private String fileName="DIOFinal.php";//PHP文件名
	String url_1 = "";//httpURL+"/"+folder+"/"+"/"+fileName+"?";
	
	
	//construct function
	public SerialCom()
	{
		//this.url_1= "http://"+Data.GetIP()+"/"+folder+"/"+fileName+"?";
		this.url_1= "http://"+Data.GetIP()+"/"+folder+"/"+fileName+"?";
	}
	
	/*
	 * 查询监控信息
	 */
	public JSONObject CheckMonitor(String actionName)
	{
		//url_1=httpURL+Data.GetIP()+":8080"+"/"+folder+"/"+fileName+"?";//+"folder"+"/"
		JSONObject json=new JSONObject();//定义接收到的数据
		
		//向串口发送的参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //使用JAVA的库
		
        params.add(new BasicNameValuePair("action", "readInfo"));  //用于向数据库写数据
        
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
        //json=operator.PHPCommunicationURL(params, httpURL, fileName);
        
		return json;//返回JSON数据
	}
	
	/*
	 * 设置参考星
	 */
	public JSONObject  SetReferenceStar(String frequency,String satLongitude,String polarization)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "referenceStar"));
		params.add(new BasicNameValuePair("frequency",frequency));
		params.add(new BasicNameValuePair("satLongitude",satLongitude));
		params.add(new BasicNameValuePair("polarization",polarization));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 读取参考星
	 */
	public JSONObject  ReadReferenceStar()
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "readRefInfo"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 读取寻星门限,不再设置参数
	 */
	public JSONObject ReadThreshhold()
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "readThreshhold"));
		//params.add(new BasicNameValuePair("threshholdValue", threshholdValue));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		return json;
	}
	
	/*
	 * 设置寻星门限值
	 */
	public JSONObject SetThreshhold(String setThreshold,String threshholdValue)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "setThreshold"));
		params.add(new BasicNameValuePair("threshholdData", threshholdValue));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 自动寻星指令
	 * 参数有极化频率、卫星经度、极化方式
	 */
	public JSONObject AutoSearch(String frequency,String satLongitude,String polarization)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "autoSearch"));
		
		params.add(new BasicNameValuePair("beaconFrequency",frequency));
		params.add(new BasicNameValuePair("satLongitude",satLongitude));
		params.add(new BasicNameValuePair("polarization",polarization));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 手动搜索指令
	 * 包含的参数有调整方式，角速度等
	 */
	public JSONObject ManualSearch(String adjustMethod, String angularSpeed)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "manualSearch"));	
		params.add(new BasicNameValuePair("adjustMethod",adjustMethod));
		params.add(new BasicNameValuePair("angularSpeed",angularSpeed));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 手动位置控制指令
	 * 包含的参数有方位角、俯仰角、发射极化角、接收极化角
	 */
	public JSONObject ManualControl(String azimuth,String pitch,String rollEmit,String rollReceive)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "manualControl"));	
		params.add(new BasicNameValuePair("azimuth",azimuth));
		params.add(new BasicNameValuePair("pitch",pitch));
		params.add(new BasicNameValuePair("rollEmit",rollEmit));
		params.add(new BasicNameValuePair("rollReceive",rollReceive));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 停止寻星指令
	 * 唯一的参数是行为标示
	 * 此函数用处较为特别
	 */
	public JSONObject StopSearch()
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "stopSearch"));
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 输入经纬度指令
	 * 参数为纬度 经度
	 */
	public JSONObject Position(String longitude, String latitude)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "position"));
		
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 天线标定指令
	 */
	public JSONObject AntennaCalib(String azimuth,String pitch,String rollEmit,String rollReceive)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "antannaCalib"));
		params.add(new BasicNameValuePair("azimuth",azimuth));
		params.add(new BasicNameValuePair("pitch",pitch));
		params.add(new BasicNameValuePair("rollEmit",rollEmit));
		params.add(new BasicNameValuePair("rollReceive",rollReceive));
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 读取天线标定指令值
	 */
	public JSONObject ReadAntennaCalib()
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "readAntannaCalib"));
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		return json;
	}
	
	/*
	 * 惯导标定
	 */
	public JSONObject IMUCalib(String heading,String pitch,String roll)
	{
		//url_1=httpURL+Data.GetIP()+":8080"+"/"+folder+"/"+fileName+"?";
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "INSCalib"));
		
		params.add(new BasicNameValuePair("heading", heading));
		params.add(new BasicNameValuePair("pitch", pitch));
		params.add(new BasicNameValuePair("roll", roll));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 读取惯导标定指令
	 */
	public JSONObject ReadIMUCalib()
	{
		//url_1=httpURL+Data.GetIP()+":8080"+"/"+folder+"/"+fileName+"?";
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "readINSCalib"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 打开天线调试指令
	 */
	public JSONObject OpenAntennaDebug()
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "antannaDebugOpen"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 关闭天线调试指令
	 */
	public JSONObject CloseAntennaDebug()
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "closeDebug"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	/*
	 * 打开导航调试指令
	 */
	public JSONObject OpenNavDebug()
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "navDebugOpen"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 关闭导航调试
	 */
	public JSONObject CloseNavDebug()
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "closeNavDebug"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * 设置网络
	 */
	public JSONObject SetNetwork(String ip,String netmask,String gateway)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "netConfig"));
		params.add(new BasicNameValuePair("ip",ip));
		params.add(new BasicNameValuePair("netmask",netmask));
		params.add(new BasicNameValuePair("gateway",gateway));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		return json;
	}
	
	/*
	 * 切换控制方式
	 */
	public JSONObject ControlMode(String mode)
	{
		JSONObject json=new JSONObject();//定义接收到的数据
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "controlMode"));
		params.add(new BasicNameValuePair("mode",mode));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		return json;
	}
		
	
}
