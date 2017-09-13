package com.example.CommonFunction;

//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlSerializer;
//
//import android.R.xml;
//import android.test.AndroidTestCase;
//import android.util.Xml;
//
//
//import com.example.serverxml.Server;
//
///**
// * 对XML文件的操作,默认IP地址为192.168.33.1,默认端口号为80
// * @author zxl
// *
// */
//public class XMLOperator extends AndroidTestCase
//{
//	
//	//使用DOM方式进行数据更新
//	public void update(String index, int count, String value, boolean outOrno)  
//	{
//		String path = "file://data/data/com.example.servexml/files/serverinfo.xml"; 
//		Document document = null;
//		try
//		{
//			//File xmlFile = new File(getContext().getFilesDir(), "serverinfo.xml");
//			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();    
//			DocumentBuilder builder=factory.newDocumentBuilder(); 
//			document =builder.parse(new InputSource("serverinfo.xml"));
//			//document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("serverinfo.xml");  
//			Element root = document.getDocumentElement(); 
//            root.getElementsByTagName(index).item(count).setTextContent(value);  
//            output(root, "path");  
//            if (outOrno) 
//            {  
//              System.out.println("-------------------------使用DOM方法输出-------------------------");  
//              output(root, null);  
//           } 
//
//		}
//		catch (SAXException e) {  
//			// TODO Auto-generated catch block  
//			e.printStackTrace();  
//		} catch (IOException e) {  
//			// TODO Auto-generated catch block  
//			e.printStackTrace();  
//		} catch (ParserConfigurationException e) {  
//			// TODO Auto-generated catch block  
//			e.printStackTrace();  
//		} 
//
//	}
//	
//    //写回XML文件，保存修改或打印到控制台  
//      public static void output(Node node, String filename) {  
//           TransformerFactory transFactory = TransformerFactory.newInstance();  
//            try {  
//              Transformer transformer = transFactory.newTransformer();  
//              // 设置各种输出属性  
//              //transformer.setOutputProperty("encoding", "gb2312");  
//              transformer.setOutputProperty("indent", "yes");  
//              DOMSource source = new DOMSource();  
//              // 将待转换输出节点赋值给DOM源模型的持有者(holder)  
//              source.setNode(node);  
//              StreamResult result = new StreamResult();  
//             if (filename == null) {  
//                // 设置标准输出流为transformer的底层输出目标  
//               result.setOutputStream(System.out);  
//             } else {  
//                result.setOutputStream(new FileOutputStream(filename));  
//              }  
//              // 执行转换从源模型到控制台输出流  
//              transformer.transform(source, result);  
//           } catch (TransformerConfigurationException e) {  
//              e.printStackTrace();  
//            } catch (TransformerException e) {  
//              e.printStackTrace();  
//            } catch (FileNotFoundException e) {  
//              e.printStackTrace();  
//            }  
//          } 
//
//
//		public static List<Server> GetServer(InputStream xml) throws Exception {		
//			List<Server> servers = null;
//			Server server = null;
//			Document document = null;
//            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("my_xml.xml");  
//            Element root = document.getDocumentElement();  
//
//			XmlPullParser pullParser = Xml.newPullParser();
//			pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据		
//			int event = pullParser.getEventType();
//			
//			while (event != XmlPullParser.END_DOCUMENT){
//				
//				switch (event) {
//				
//				case XmlPullParser.START_DOCUMENT:
//					servers = new ArrayList<Server>();				
//					break;	
//				case XmlPullParser.START_TAG:
//					if("server".equals(pullParser.getName()))
//					{
//						Integer id=Integer.valueOf(pullParser.getAttributeValue(0));
//						server = new Server();
//						server.SetID(id);//获取XML文件中的IP地址
//					}
//					
//					if ("server_ip".equals(pullParser.getName())){
//						String ip = pullParser.nextText();
//						server.SetIP(ip);
//						//server.setId(id);
//					}
//					if ("server_port".equals(pullParser.getName())){
//						String port = pullParser.nextText();
//						server.SetPort(port);
//					}								
//					break;
//					
//				case XmlPullParser.END_TAG:
//					if ("server".equals(pullParser.getName())){
//						servers.add(server);				
//						server = null;
//					}
//					break;					
//				}
//				event = pullParser.next();
//			}			
//			return servers;
//		}
//		
//		/**
//		 * 保存数据到xml文件中
//		 * @param persons
//		 * @param out
//		 * @throws Exception
//		 */
//		public static void SaveServer(List<Server> server, OutputStream out) throws Exception {
//			XmlSerializer serializer = Xml.newSerializer();
//			serializer.setOutput(out, "UTF-8");
//			serializer.startDocument("UTF-8", true);
//			serializer.startTag(null, "server");		
//			for (Server server1 : server) {
//				serializer.startTag(null, "server");			
//				serializer.attribute(null, "id", server1.GetID().toString());			
//				serializer.startTag(null, "server_ip");			
//				serializer.text(server1.GetIP().toString());
//				serializer.endTag(null, "server_ip");			
//				serializer.startTag(null, "server_port");			
//				serializer.text(server1.GetPort().toString());
//				serializer.endTag(null, "server_port");			
//				serializer.endTag(null, "server");
//			}		
//			serializer.endTag(null, "servers");
//			serializer.endDocument();
//			out.flush();
//			out.close();
//		}
//
//}


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class XMLOperator {
	public static List<Server> getServers(InputStream xml) throws Exception {		
		List<Server> servers = null;
		Server server = null;
		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setInput(xml, "UTF-8"); //为Pull解释器设置要解析的XML数据		
		int event = pullParser.getEventType();
		
		while (event != XmlPullParser.END_DOCUMENT){
			
			switch (event) {			
			case XmlPullParser.START_DOCUMENT:
				servers = new ArrayList<Server>();				
				break;	
			case XmlPullParser.START_TAG:	
				if ("server".equals(pullParser.getName())){
					int id = Integer.valueOf(pullParser.getAttributeValue(0));
					server = new Server();
					server.SetID(id);
				}
				if ("server_ip".equals(pullParser.getName())){
					String ip = pullParser.nextText();
					server.SetIP(ip);
				}								
				if ("server_port".equals(pullParser.getName())){
					String port = pullParser.nextText();
					server.SetPort(port);
				}
				break;
				
			case XmlPullParser.END_TAG:
				if ("server".equals(pullParser.getName())){
					servers.add(server);
					server = null;
				}
				break;
				
			}
			
			event = pullParser.next();
		}
		
		
		return servers;
	}
	
	/**
	 * 保存数据到xml文件中
	 * @param persons
	 * @param out
	 * @throws Exception
	 */
	public static void save(List<Server> servers, OutputStream out) throws Exception {
		//XmlSerializer serializer = Xml.newSerializer();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        org.xmlpull.v1.XmlSerializer serializer = factory.newSerializer();
		
		serializer.setOutput(out, "UTF-8");
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
		out.flush();
		out.close();
	}
}

