package com.example.DataSave; 


//import java.io.File; 
//import java.io.FileInputStream; 
//import java.io.FileOutputStream; 
//import java.io.IOException; 
//import java.io.StringWriter; 
//import java.net.Inet6Address; 
//import java.net.InetAddress; 
//import java.net.NetworkInterface; 
//import java.net.SocketException; 
//import java.util.Enumeration; 
//import javax.xml.parsers.DocumentBuilder; 
//import javax.xml.parsers.DocumentBuilderFactory; 
//import android.os.Environment; 
//import android.util.Log; 
//import org.w3c.dom.Document; 
//import org.w3c.dom.Element; 
//import org.w3c.dom.NodeList; 
//import org.xmlpull.v1.XmlPullParserFactory; 
//import org.xmlpull.v1.XmlSerializer; 
//
///** 
// * ����xml�ļ�
// * 
// */
//public class Config  
//{ 
// //������Ϣ 
// private Config_Info config_info = new Config_Info(); 
// 
// 
// /** 
//  * ���캯�� 
//  */
// public Config() { 
//  boolean ok; 
//  File sd_path; 
//  File file_cfg_dir; 
//  File file_cfg; 
//  FileOutputStream out; 
//  String str; 
//  FileInputStream in; 
//  //�õ�����ip��ַ 
//  config_info.local_ip = getLocalIpAddress(); 
//  System.out.printf("����ip:%s\n", config_info.local_ip); 
//  //��ȡSD��Ŀ¼ 
//  sd_path = Environment.getExternalStorageDirectory(); 
//  //�ж��ļ����Ƿ���� 
//  file_cfg_dir = new File(sd_path.getPath() + "//Remote_Meeting");
//  if (!file_cfg_dir.exists() && !file_cfg_dir.isDirectory()) { 
//   System.out.println("�����ļ���Remote_Meeting������!"); 
//   ok = file_cfg_dir.mkdirs(); 
//   if (ok) { 
//    System.out.println("�����ļ��гɹ�!"); 
//   } else { 
//    System.out.println("�����ļ���ʧ��!");      
//   } 
//  } 
//  //�ж������ļ��Ƿ���� 
//  file_cfg = new File(file_cfg_dir.getPath(),"cfg.xml");
//  if (!file_cfg.exists()) 
//  { 
//   System.out.println("�����ļ�cfg.xml������!");
//   try { 
//    file_cfg.createNewFile(); 
//    System.out.println("�����ļ�cfg.xml�ɹ�!");
//    //���ɳ�ʼ������������ 
//    try { 
//     out = new FileOutputStream(file_cfg);
//     //����Ĭ������ 
//     config_info.title = "Զ����Ƶ���ϵͳ";
//     config_info.local_port = 12600; 
//     config_info.schedule_server_ip = "10.58.1.59";
//     config_info.schedule_server_port = 12601;
//     str = produce_xml_string(config_info);
//     out.write(str.getBytes()); 
//     out.close(); 
//    } catch (IOException e) { 
//     // TODO Auto-generated catch block
//     e.printStackTrace(); 
//    } 
//   } catch (IOException e) { 
//    // TODO Auto-generated catch block
//    e.printStackTrace(); 
//   } 
//  } 
//  config_info.title = "Զ��"; 
//  config_info.local_port = 126; 
//  config_info.schedule_server_ip = "10.5";
//  config_info.schedule_server_port = 12;
//  System.out.printf("----222222222%s,%d,%s,%d\n",config_info.title,config_info.local_port,
//  config_info.schedule_server_ip,config_info.schedule_server_port);
//  //����xml�ļ�
//  try {
//   in = new FileInputStream(file_cfg);
//   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
//   DocumentBuilder builder = factory.newDocumentBuilder();
//   Document document = builder.parse(in);
//   // ��ȡ���ڵ� 
//   Element root = document.getDocumentElement();
//   NodeList node = root.getChildNodes(); 
//   //��õ�1�ӽڵ�:���� 
//   config_info.title = node.item(0).getFirstChild().getNodeValue(); 
//   //��õ�2�ӽڵ�:�����˿� 
//   config_info.local_port = Integer.parseInt(node.item(1).getFirstChild().getNodeValue());
//   //��õ�3�ӽڵ�:���ȷ�����ip 
//   config_info.schedule_server_ip = node.item(2).getFirstChild().getNodeValue(); 
//   //��õ�4�ӽڵ�:���ȷ������˿� 
//   config_info.schedule_server_port = Integer.parseInt(node.item(3).getFirstChild().getNodeValue());  
//   System.out.printf("----222222222%s,%d,%s,%d\n",config_info.title,config_info.local_port, 
//     config_info.schedule_server_ip,config_info.schedule_server_port); 
//  } catch (Exception e) { 
//   e.printStackTrace(); 
//  } 
// } 
// @Override
// public Config_Info get_config_info() { 
//  return config_info; 
// } 
// /** 
//  * �õ�����ip��ַ 
//  * @return ����ip��ַ 
//  */
// private String getLocalIpAddress() { 
//  try { 
//   for (Enumeration<NetworkInterface> en = NetworkInterface 
//     .getNetworkInterfaces(); en.hasMoreElements();) { 
//    NetworkInterface intf = en.nextElement(); 
//    for (Enumeration<InetAddress> enumIpAddr = intf 
//      .getInetAddresses(); enumIpAddr.hasMoreElements();) { 
//     InetAddress inetAddress = enumIpAddr.nextElement(); 
//     //if (!inetAddress.isLoopbackAddress()) { 
//      if (!inetAddress.isLoopbackAddress() && !(inetAddress instanceof Inet6Address)) { 
//      return inetAddress.getHostAddress().toString(); 
//     } 
//    } 
//   } 
//  } catch (SocketException ex) { 
//   Log.e("WifiPreference IpAddress", ex.toString()); 
//  } 
//  return null; 
// } 
// /** 
//  * ����xml�����ļ���String������ 
//  * Config_Info�ı���ip��Ϣ���ᱣ�� 
//  * @param info:������Ϣ 
//  * @return xml��String������ 
//  */
// private String produce_xml_string(Config_Info info) { 
//  StringWriter stringWriter = new StringWriter(); 
//  try { 
//   // ��ȡXmlSerializer���� 
//   XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
//   XmlSerializer xmlSerializer = factory.newSerializer(); 
//   // ������������� 
//   xmlSerializer.setOutput(stringWriter); 
//   //��ʼ��ǩ 
//   xmlSerializer.startDocument("utf-8", true); 
//   xmlSerializer.startTag(null, "config"); 
//   //���� 
//   xmlSerializer.startTag(null, "title"); 
//   xmlSerializer.text(info.title); 
//   xmlSerializer.endTag(null, "title"); 
//   //�����˿� 
//   xmlSerializer.startTag(null, "local_port"); 
//   xmlSerializer.text(Integer.toString(info.local_port)); 
//   xmlSerializer.endTag(null, "local_port"); 
//   //���ȷ�����ip 
//   xmlSerializer.startTag(null, "schedule_service_ip"); 
//   xmlSerializer.text(info.schedule_server_ip); 
//   xmlSerializer.endTag(null, "schedule_service_ip"); 
//   //���ȷ������˿� 
//   xmlSerializer.startTag(null, "schedule_service_port"); 
//   xmlSerializer.text(Integer.toString(info.schedule_server_port)); 
//   xmlSerializer.endTag(null, "schedule_service_port"); 
//   xmlSerializer.endTag(null, "config"); 
//   xmlSerializer.endDocument(); 
//  } catch (Exception e) { 
//   e.printStackTrace(); 
//  } 
//  return stringWriter.toString(); 
// } 
//}
