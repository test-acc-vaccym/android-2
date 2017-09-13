package com.example.CommonFunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;

public class ServerIP extends Activity
{
	final Context context;
	//private static ServerIP sInstance; 
	
	public ServerIP(Context context)
	{
		this.context=context;
	}
	
//	public static synchronized ServerIP getInstance(Context context)
//	{
//        if (sInstance == null)  
//        {  
//            sInstance = new ServerIP(context.getApplicationContext());  
//        }  
//       return sInstance;  
//
//	}
	
	public String GetIP()
	{
		List<Server> servers;
		File xmlFile = new File(context.getFilesDir().getPath(), "server.xml");
		String ip="";
		//如果XML文件不存在，读取预置的XML文件
		if(!xmlFile.exists())
		{  		
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("server.xml");
			try {
				servers = XMLOperator.getServers(inputStream);
				for (Server server : servers){
					ip=server.GetIP();
					//Toast.makeText(getApplicationContext(), server.toString(), Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    	
		}
		//读取创建的XML文件
		else
		{	
			try {
				InputStream inputStream = new FileInputStream(xmlFile);
				servers = XMLOperator.getServers(inputStream);
				
				for (Server server : servers){
					ip=server.GetIP();
	    			//Toast.makeText(getApplicationContext(), server.toString(), Toast.LENGTH_SHORT).show();
	    		}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}		
        return ip;
	}
	
	public void SetIP(String IP,String port) throws XmlPullParserException
	{
		//String ip_Save=ip.getText().toString();
		//String port_Save=port.getText().toString();
		List<Server> servers = new ArrayList<Server>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        org.xmlpull.v1.XmlSerializer serializer = factory.newSerializer();
		String path="";
		servers.add(new Server(1, IP, port));
		try {
			File xmlFile = new File(context.getFilesDir().getPath(), "server.xml");
			path=xmlFile.toString();
			FileOutputStream outStream = new FileOutputStream(xmlFile);//openFileOutput(xmlFile.toString(), MODE_WORLD_READABLE);
			//XMLOperator.save(servers, outStream);
			
			serializer.setOutput(outStream, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "servers");		
			for (Server server : servers) {
				serializer.startTag(null, "server");			
				serializer.attribute(null, "id", server.GetID().toString());			
				serializer.startTag(null, "server_ip");			
				serializer.text(server.GetIP().toString());
				serializer.endTag(null, "server_ip");			
				serializer.startTag(null, "server_port");			
				serializer.text(server.GetPort().toString());
				serializer.endTag(null, "server_port");			
				serializer.endTag(null, "server");
			}		
			serializer.endTag(null, "servers");
			serializer.endDocument();
			outStream.flush();
			outStream.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}