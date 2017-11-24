package cn.itcast.mobilesafe.engine;

import java.io.File;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 1 webservice �ֻ���������  
 * 2 ���Է���һ���������ֻ����� 
 *   2.1 ���԰���Դ����assetsĿ¼����  ���ܴ���1m
 *   2.2 �������ļ��ֳɺܶ��  ������res/raw
 *   2.3 �ӷ���������һ�����ݿ�������ֻ���sdcard
 *
 */
public class AddressQueryService {

	/**
	 * �жϹ��������ݿ��Ƿ����
	 * @return
	 */
	public boolean isExist(){
		File file = new File(Environment.getExternalStorageDirectory(), "address.db");
		return file.exists();
	}
	
	
	public String query(String number){
		String address = null;
		File file = new File(Environment.getExternalStorageDirectory(), "address.db");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if(db.isOpen()){
			//�Ƿ����ֻ�����
			String regularExpression = "^1[358]\\d{9}$";
			boolean isphone = number.matches(regularExpression);
			if(isphone){
				String prefix_number = number.substring(0, 7);
				Cursor c = db.query("info",
						new String[]{"city"},
						" mobileprefix = ? ",
						new String[]{prefix_number}, null, null, null);
				if(c.moveToFirst()){
					address = c.getString(0);
				}
				c.close();
			}else{
				//����绰����
				//3λ���� + 7λ�绰����
				if(number.length() == 10){
					String area = number.substring(0, 3);
					Cursor c = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area}, null, null, null);
					if(c.moveToFirst()){
						address = c.getString(0);
					}
					c.close();
				}else if(number.length() == 11){
					//3λ���� + 8λ�绰����
					String area1 = number.substring(0, 3);
					Cursor c1 = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area1}, null, null, null);
					if(c1.moveToFirst()){
						address = c1.getString(0);
					}
					c1.close();
					
					//4λ���� + 7λ�绰����
					String area2 = number.substring(0, 4);
					Cursor c2 = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area2}, null, null, null);
					if(c2.moveToFirst()){
						address = c2.getString(0);
					}
					c2.close();
				}else if(number.length() == 12){
					//4λ���� + 8λ�绰����
					String area = number.substring(0, 4);
					Cursor c = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area}, null, null, null);
					if(c.moveToFirst()){
						address = c.getString(0);
					}
					c.close();
				}else if(number.length() == 7 || number.length() == 8){
					address = "���غ���";
				}else if(number.length() == 4){
					address = "ģ����";
				}else if(number.length() == 3){
					address = "��������";
				}
			}

			db.close();
		}
		
		if(address == null){
			address = "δ֪����";
		}
		
		return address;
	}
}
