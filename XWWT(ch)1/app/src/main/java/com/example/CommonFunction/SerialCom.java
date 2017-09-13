/*
 * ���ڲ����࣬��PHP����ָ��
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
	private String operation = "";//��������
	//private String fileName = "DIOFinal.php";//�ļ�����
	//private String httpURL = "http://192.168.33.1/web";//webserver���ڵĵ�ַ,����Ҫ�����յĳ����й̶�
	
	private String httpURL="http://";//���ݿ��ļ����ڵ�λ��"http://192.168.33.1"
	private String folder="php";//php�ļ������ļ��е�ַ
	private String fileName="DIOFinal.php";//PHP�ļ���
	String url_1 = "";//httpURL+"/"+folder+"/"+"/"+fileName+"?";
	
	
	//construct function
	public SerialCom()
	{
		//this.url_1= "http://"+Data.GetIP()+"/"+folder+"/"+fileName+"?";
		this.url_1= "http://"+Data.GetIP()+"/"+folder+"/"+fileName+"?";
	}
	
	/*
	 * ��ѯ�����Ϣ
	 */
	public JSONObject CheckMonitor(String actionName)
	{
		//url_1=httpURL+Data.GetIP()+":8080"+"/"+folder+"/"+fileName+"?";//+"folder"+"/"
		JSONObject json=new JSONObject();//������յ�������
		
		//�򴮿ڷ��͵Ĳ���
		List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��JAVA�Ŀ�
		
        params.add(new BasicNameValuePair("action", "readInfo"));  //���������ݿ�д����
        
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
        //json=operator.PHPCommunicationURL(params, httpURL, fileName);
        
		return json;//����JSON����
	}
	
	/*
	 * ���òο���
	 */
	public JSONObject  SetReferenceStar(String frequency,String satLongitude,String polarization)
	{
		JSONObject json=new JSONObject();//������յ�������
		
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
	 * ��ȡ�ο���
	 */
	public JSONObject  ReadReferenceStar()
	{
		JSONObject json=new JSONObject();//������յ�������
		
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "readRefInfo"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * ��ȡѰ������,�������ò���
	 */
	public JSONObject ReadThreshhold()
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "readThreshhold"));
		//params.add(new BasicNameValuePair("threshholdValue", threshholdValue));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		return json;
	}
	
	/*
	 * ����Ѱ������ֵ
	 */
	public JSONObject SetThreshhold(String setThreshold,String threshholdValue)
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "setThreshold"));
		params.add(new BasicNameValuePair("threshholdData", threshholdValue));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * �Զ�Ѱ��ָ��
	 * �����м���Ƶ�ʡ����Ǿ��ȡ�������ʽ
	 */
	public JSONObject AutoSearch(String frequency,String satLongitude,String polarization)
	{
		JSONObject json=new JSONObject();//������յ�������
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
	 * �ֶ�����ָ��
	 * �����Ĳ����е�����ʽ�����ٶȵ�
	 */
	public JSONObject ManualSearch(String adjustMethod, String angularSpeed)
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "manualSearch"));	
		params.add(new BasicNameValuePair("adjustMethod",adjustMethod));
		params.add(new BasicNameValuePair("angularSpeed",angularSpeed));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * �ֶ�λ�ÿ���ָ��
	 * �����Ĳ����з�λ�ǡ������ǡ����伫���ǡ����ռ�����
	 */
	public JSONObject ManualControl(String azimuth,String pitch,String rollEmit,String rollReceive)
	{
		JSONObject json=new JSONObject();//������յ�������
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
	 * ֹͣѰ��ָ��
	 * Ψһ�Ĳ�������Ϊ��ʾ
	 * �˺����ô���Ϊ�ر�
	 */
	public JSONObject StopSearch()
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "stopSearch"));
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * ���뾭γ��ָ��
	 * ����Ϊγ�� ����
	 */
	public JSONObject Position(String longitude, String latitude)
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "position"));
		
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * ���߱궨ָ��
	 */
	public JSONObject AntennaCalib(String azimuth,String pitch,String rollEmit,String rollReceive)
	{
		JSONObject json=new JSONObject();//������յ�������
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
	 * ��ȡ���߱궨ָ��ֵ
	 */
	public JSONObject ReadAntennaCalib()
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "readAntannaCalib"));
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		return json;
	}
	
	/*
	 * �ߵ��궨
	 */
	public JSONObject IMUCalib(String heading,String pitch,String roll)
	{
		//url_1=httpURL+Data.GetIP()+":8080"+"/"+folder+"/"+fileName+"?";
		JSONObject json=new JSONObject();//������յ�������
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
	 * ��ȡ�ߵ��궨ָ��
	 */
	public JSONObject ReadIMUCalib()
	{
		//url_1=httpURL+Data.GetIP()+":8080"+"/"+folder+"/"+fileName+"?";
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "readINSCalib"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * �����ߵ���ָ��
	 */
	public JSONObject OpenAntennaDebug()
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "antannaDebugOpen"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * �ر����ߵ���ָ��
	 */
	public JSONObject CloseAntennaDebug()
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "closeDebug"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	/*
	 * �򿪵�������ָ��
	 */
	public JSONObject OpenNavDebug()
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "navDebugOpen"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * �رյ�������
	 */
	public JSONObject CloseNavDebug()
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "closeNavDebug"));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		
		return json;
	}
	
	/*
	 * ��������
	 */
	public JSONObject SetNetwork(String ip,String netmask,String gateway)
	{
		JSONObject json=new JSONObject();//������յ�������
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
	 * �л����Ʒ�ʽ
	 */
	public JSONObject ControlMode(String mode)
	{
		JSONObject json=new JSONObject();//������յ�������
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("action", "controlMode"));
		params.add(new BasicNameValuePair("mode",mode));
		
        PHPOperator operator=new PHPOperator();
        json=operator.PHPCommunication(params, url_1, fileName);
		return json;
	}
		
	
}
