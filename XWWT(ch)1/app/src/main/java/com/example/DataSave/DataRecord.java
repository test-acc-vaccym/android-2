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
 * ����ȡ���������ļ�д��xml�ļ�
 * @author zxl
 *
 */
public class DataRecord 
{
	String filePath;//�����ļ��洢��·��
	String nameData="ND";//�����ļ���
	String nameSet="NS";//�����ļ���
	//���캯��
	public DataRecord(String flag)
	{
		//�ж�·���е��ļ��Ƿ����
		if (flag=="data")
		{
			filePath="";
			
		}
	}
	
	//��ȡxml�ļ��е�����
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
	
	//����ļ��Ƿ����
	public static File checkExist(String filepath) throws Exception
	{
		File file=new File(filepath);
		if (file.exists()) {//�ж��ļ�Ŀ¼�Ĵ���
			System.out.println("�ļ��д��ڣ�");
			if(file.isDirectory()){//�ж��ļ��Ĵ�����      
				System.out.println("�ļ����ڣ�");      
			}else{
				file.createNewFile();//�����ļ�
				System.out.println("�ļ������ڣ������ļ��ɹ���"   );      
			}
		}else {
			System.out.println("�ļ��в����ڣ�");
			File file2=new File(file.getParent());
			file2.mkdirs();
			System.out.println("�����ļ��гɹ���");
			if(file.isDirectory()){      
				System.out.println("�ļ����ڣ�");       
			}else{      
				file.createNewFile();//�����ļ� 
				System.out.println("�ļ������ڣ������ļ��ɹ���"   );      
			}
		}
		return file;
	}
}