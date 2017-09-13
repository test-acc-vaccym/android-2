/*
 * ��������PHP���Ϳ���ָ��
 */
package com.example.CommonFunction;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;



public class PHPOperator{

	private String httpURL = "http://192.168.33.1/web";//�����������վ���ļ����ڵ�λ��"http://192.168.33.1/design/dio-test"
    //private String fileName="sqlTest.php";//���������վ���PHP�ļ�������Ҫ�������ݿ��ļ��������ļ�����
    private String operatorName="";//�����������
    private URL urlServer;//ʹ��URLConnection����ͨ��
    
    String result= ""; 
    HttpURLConnection httpURLConnection;
    InputStream inputStream = null;
    
    public PHPOperator()//���캯����ֵ��ע�����JAVAû���ṩ��������
    {
    }
    
    public enum JSON_TYPE{
        /**JSONObject*/    
        JSON_TYPE_OBJECT,
        /**JSONArray*/
        JSON_TYPE_ARRAY,
        /**����JSON��ʽ���ַ���*/
        JSON_TYPE_ERROR
    }
    
    //���ݿ��з��ص�JSON����
    public JSONArray PHPOperator(List<NameValuePair> params,String httpURL,String fileName)
    {
    	StringBuilder str = new StringBuilder();
    	JSONArray jsonArray=new JSONArray();	
    	String url=httpURL+URLEncodedUtils.format(params, HTTP.UTF_8);
    	HttpResponse httpResponse=GetResponse(params,url,fileName);
    	if(httpResponse!=null)
    	{
    		BufferedReader buffer;
    		try {
    			buffer = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

    			for(String s = buffer.readLine(); s != null ; s = buffer.readLine())
    			{
    				str.append(s);
    			}
    			try
    			{
    				jsonArray=new JSONArray(str.toString());         		                          
    			}
    			catch(JSONException e)
    			{
    				e.printStackTrace();
    			}
    			buffer.close();//����ر�
    		} catch (IllegalStateException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}

    	}
    	return jsonArray;

    }
    
    public HttpResponse GetResponse(List<NameValuePair> params,String httpURL,String fileName) 
    {
    	StringBuilder str = new StringBuilder();
    	HttpResponse httpResponse = null;
		JSONObject json=new JSONObject();//������յ�������
     
        //String url = httpURL+"/"+"diotest"+"/"+"DIOFinal.php"+"?"+URLEncodedUtils.format(params, HTTP.UTF_8);
		//String url = httpURL+"/"+"DIOFinal.php"+"?"+URLEncodedUtils.format(params, HTTP.UTF_8);
		//String url=httpURL+URLEncodedUtils.format(params, HTTP.UTF_8);;
		

		try {
			urlServer=new URL(httpURL);
			HttpPost httpRequest=new HttpPost(httpURL);
			HttpParams params_2 = new BasicHttpParams();
			params_2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpClient httpclient = new DefaultHttpClient(params_2);

			httpResponse = httpclient.execute(httpRequest);
        	double status=httpResponse.getStatusLine().getStatusCode();
			if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				httpRequest.abort();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpResponse;
     
	}

    /*
     * ������PHP����ͨ�ŵķ���
     * ������û������Activity��
     * ���ݲ���ΪJSON���ݣ�URL��ַ���ļ�����
     * ����ֵΪJSON��ʽ����
     */
    public JSONObject PHPCommunication( List<NameValuePair> params,String httpURL,String fileName)
    {  	
		StringBuilder str = new StringBuilder();
		//tv2.setText(tv1.getText().toString());
		
		JSONObject json=new JSONObject();//������յ�������
        //String url = httpURL+"/"+"diotest"+"/"+"DIOFinal.php"+"?"+URLEncodedUtils.format(params, HTTP.UTF_8);
		//String url = httpURL+"/"+"DIOFinal.php"+"?"+URLEncodedUtils.format(params, HTTP.UTF_8);
		String url=httpURL+URLEncodedUtils.format(params, HTTP.UTF_8);;//+"action"+"="+params.get(0).getValue();
		
       try{
    	    urlServer=new URL(url);
        	//HttpGet httpRequest = new HttpGet(url);
        	HttpPost httpRequest=new HttpPost(url);
        	HttpParams params_2 = new BasicHttpParams();
        	params_2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        	HttpClient httpclient = new DefaultHttpClient(params_2);
        	//HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
        	httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
        	httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
        	HttpResponse httpResponse=httpclient.execute(httpRequest);

        	double status=httpResponse.getStatusLine().getStatusCode();
     	
        	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        	{
        		BufferedReader buffer = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                for(String s = buffer.readLine(); s != null ; s = buffer.readLine())
                {
                    str.append(s);
                }
                try
                {
                	
                	if(getJSONType(str.toString())==JSON_TYPE.JSON_TYPE_OBJECT)
                	{
                		 JSONObject jsonData = new JSONObject(str.toString());
                		 json=jsonData;
                	}
                	else if(getJSONType(str.toString())==JSON_TYPE.JSON_TYPE_ARRAY)
                	{
                		JSONArray jsonArray=new JSONArray(str.toString());
                		//int s=ja.length();
                		
                		for(int i=0;i<jsonArray.length();i++)
                		{
                			 JSONObject jsonData= (JSONObject) jsonArray.get(i);
                			 String nn="";
                		}
                		json=jsonArray.toJSONObject(jsonArray);
             		
                	}
                                
                }
                catch(JSONException e)
                {
                e.printStackTrace();
                }
                buffer.close();//����ر�
        	}
        	else//��������λ����������ʧ����Ϣ
        	{
        		JSONObject jsonData = new JSONObject();
      		
        		httpRequest.abort(); 
        	}
        }
        catch(ClientProtocolException e){
            e.printStackTrace();  
            //result = e.getMessage().toString();  
        }
        catch(IOException e){
            e.printStackTrace();  
            //Log.i("1", e.getMessage().toString());
			//json.put("", result);
        }
       return json;//���ز�ѯ����JSON����
    }
    
    /**
     * ʹ���̶߳�ȡ����
     * @param params
     * @param httpURL
     * @param fileName
     * @return
     * @throws MalformedURLException 
     */
/*    public JSONObject PHPCommunicationThread( List<NameValuePair> params,String httpURL,String fileName)
    {
		final StringBuilder str = new StringBuilder();

		final String url_2 = httpURL+"/"+"DIOFinal.php"+"?"+URLEncodedUtils.format(params, HTTP.UTF_8);
		final JSONObject json_2=new JSONObject();//������յ�������
		final HttpResponse httpResponse;
		HttpGet httpRequest;
		
		Thread thread=new Thread()
				{
			@Override
			public void run()
			{
	            try{
	        	    //urlServer=new URL(url);
	            	HttpGet httpRequest = new HttpGet(url_2); 
	            	httpResponse=new DefaultHttpClient().execute(httpRequest);
	            	
	            	double status=httpResponse.getStatusLine().getStatusCode();
	         	
	            }
	            catch (IllegalArgumentException ec) {
	            	httpResponse = null;
	            	interrupted();
	            } catch (ClientProtocolException e) {
	            	httpResponse = null;
	            	interrupted();;
	            } catch (IOException e) {
	            	httpResponse = null;
	            	interrupted();
	            }
	            }
				};
				thread.start();

        	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
        	{
        		BufferedReader buffer = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                for(String s = buffer.readLine(); s != null ; s = buffer.readLine())
                {
                    str.append(s);
                }
                try
                {
                    JSONObject jsonData = new JSONObject(str.toString());
                    json_2=jsonData;                
                }
                catch(JSONException e)
                {
                e.printStackTrace();
                }
                buffer.close();//����ر�
        	}
        	else
        	{
        		httpRequest.abort(); 
        	}
    	 return json_2;//���ز�ѯ����JSON����
    }*/

    /*
     * ���·�����ʹ��URLconnection������������
     *
     */
    public JSONObject PHPCommunicationURL( List<NameValuePair> params,String httpURL,String fileName)
    {
    	JSONObject json=new JSONObject();//������յ�������
    
		StringBuilder str = new StringBuilder();

     
		String path = httpURL+"/"+"DIOFinal.php"+"?"+URLEncodedUtils.format(params, HTTP.UTF_8);
    	
        try
        {
            URL url = new URL(path);
            if (null != url)
            {
                httpURLConnection = (HttpURLConnection) url.openConnection();

                // ������������ĳ�ʱʱ��
                httpURLConnection.setConnectTimeout(5000);

                // ��������
                httpURLConnection.setDoInput(true);

                // ���ñ���Http����ʹ�õķ���
                httpURLConnection.setRequestMethod("GET");

                if (200 == httpURLConnection.getResponseCode())
                {
                    // �ӷ��������һ��������
                    inputStream = httpURLConnection.getInputStream();
                    httpURLConnection.getContent();

                }

            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }	
    	return json;
    }
    

    
    //�жϽ��յ�������jsonobject����jsonarray
    public static JSON_TYPE getJSONType(String str){
        if(TextUtils.isEmpty(str)){
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
        
        final char[] strChar = str.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];
        
        //LogUtils.d(JSONUtil.class, "getJSONType", " firstChar = "+firstChar);
        
        if(firstChar == '{'){
            return JSON_TYPE.JSON_TYPE_OBJECT;
        }else if(firstChar == '['){
            return JSON_TYPE.JSON_TYPE_ARRAY;
        }else{
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
    }
}