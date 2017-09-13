package com.example.DataSave;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * 将读取到的数据文件写入xml文件
 * @author zxl
 *
 */
public class DataRecord 
{
	String filePath;//定义文件存储的路径
	String nameData="ND";//数据文件名
	String nameSet="NS";//设置文件名
	//构造函数
	public DataRecord(String flag)
	{
		//判断路径中的文件是否存在
		if (flag=="data")
		{
			filePath="";
			
		}
	}
	
	//读取xml文件中的数据
	public void readData()
	{
		
	}
	public void WriteData()
	{
		
	}
	public void WriteSet()
	{
		
	}
	public void ReadSet()
	{
		
	}
	
	//检查文件是否存在
	public static File checkExist(String filepath) throws Exception
	{
		File file=new File(filepath);
		if (file.exists()) {//判断文件目录的存在
			System.out.println("文件夹存在！");
			if(file.isDirectory()){//判断文件的存在性      
				System.out.println("文件存在！");      
			}else{
				file.createNewFile();//创建文件
				System.out.println("文件不存在，创建文件成功！"   );      
			}
		}else {
			System.out.println("文件夹不存在！");
			File file2=new File(file.getParent());
			file2.mkdirs();
			System.out.println("创建文件夹成功！");
			if(file.isDirectory()){      
				System.out.println("文件存在！");       
			}else{      
				file.createNewFile();//创建文件 
				System.out.println("文件不存在，创建文件成功！"   );      
			}
		}
		return file;
	}
}