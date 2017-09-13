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
// * 配置xml文件
// * 
// */
//public class Config  
//{ 
// //配置信息 
// private Config_Info config_info = new Config_Info(); 
// 
// 
// /** 
//  * 构造函数 
//  */
// public Config() { 
//  boolean ok; 
//  File sd_path; 
//  File file_cfg_dir; 
//  File file_cfg; 
//  FileOutputStream out; 
//  String str; 
//  FileInputStream in; 
//  //得到本机ip地址 
//  config_info.local_ip = getLocalIpAddress(); 
//  System.out.printf("本机ip:%s\n", config_info.local_ip); 
//  //获取SD卡目录 
//  sd_path = Environment.getExternalStorageDirectory(); 
//  //判断文件夹是否存在 
//  file_cfg_dir = new File(sd_path.getPath() + "//Remote_Meeting");
//  if (!file_cfg_dir.exists() && !file_cfg_dir.isDirectory()) { 
//   System.out.println("配置文件夹Remote_Meeting不存在!"); 
//   ok = file_cfg_dir.mkdirs(); 
//   if (ok) { 
//    System.out.println("创建文件夹成功!"); 
//   } else { 
//    System.out.println("创建文件夹失败!");      
//   } 
//  } 
//  //判断配置文件是否存在 
//  file_cfg = new File(file_cfg_dir.getPath(),"cfg.xml");
//  if (!file_cfg.exists()) 
//  { 
//   System.out.println("配置文件cfg.xml不存在!");
//   try { 
//    file_cfg.createNewFile(); 
//    System.out.println("创建文件cfg.xml成功!");
//    //生成初始化的配置数据 
//    try { 
//     out = new FileOutputStream(file_cfg);
//     //保存默认配置 
//     config_info.title = "远程视频会见系统";
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
//  config_info.title = "远程"; 
//  config_info.local_port = 126; 
//  config_info.schedule_server_ip = "10.5";
//  config_info.schedule_server_port = 12;
//  System.out.printf("----222222222%s,%d,%s,%d\n",config_info.title,config_info.local_port,
//  config_info.schedule_server_ip,config_info.schedule_server_port);
//  //解析xml文件
//  try {
//   in = new FileInputStream(file_cfg);
//   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
//   DocumentBuilder builder = factory.newDocumentBuilder();
//   Document document = builder.parse(in);
//   // 获取根节点 
//   Element root = document.getDocumentElement();
//   NodeList node = root.getChildNodes(); 
//   //获得第1子节点:标题 
//   config_info.title = node.item(0).getFirstChild().getNodeValue(); 
//   //获得第2子节点:本机端口 
//   config_info.local_port = Integer.parseInt(node.item(1).getFirstChild().getNodeValue());
//   //获得第3子节点:调度服务器ip 
//   config_info.schedule_server_ip = node.item(2).getFirstChild().getNodeValue(); 
//   //获得第4子节点:调度服务器端口 
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
//  * 得到本机ip地址 
//  * @return 本机ip地址 
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
//  * 生成xml配置文件的String数据流 
//  * Config_Info的本机ip信息不会保存 
//  * @param info:配置信息 
//  * @return xml的String数据流 
//  */
// private String produce_xml_string(Config_Info info) { 
//  StringWriter stringWriter = new StringWriter(); 
//  try { 
//   // 获取XmlSerializer对象 
//   XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); 
//   XmlSerializer xmlSerializer = factory.newSerializer(); 
//   // 设置输出流对象 
//   xmlSerializer.setOutput(stringWriter); 
//   //开始标签 
//   xmlSerializer.startDocument("utf-8", true); 
//   xmlSerializer.startTag(null, "config"); 
//   //标题 
//   xmlSerializer.startTag(null, "title"); 
//   xmlSerializer.text(info.title); 
//   xmlSerializer.endTag(null, "title"); 
//   //本机端口 
//   xmlSerializer.startTag(null, "local_port"); 
//   xmlSerializer.text(Integer.toString(info.local_port)); 
//   xmlSerializer.endTag(null, "local_port"); 
//   //调度服务器ip 
//   xmlSerializer.startTag(null, "schedule_service_ip"); 
//   xmlSerializer.text(info.schedule_server_ip); 
//   xmlSerializer.endTag(null, "schedule_service_ip"); 
//   //调度服务器端口 
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
