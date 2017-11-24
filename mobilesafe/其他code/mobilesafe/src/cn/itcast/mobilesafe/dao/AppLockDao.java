package cn.itcast.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.itcast.mobilesafe.db.AppLockDBHelper;

public class AppLockDao {

	private SQLiteOpenHelper mOpenHelper;
	
	public AppLockDao(Context context) {
		// TODO Auto-generated constructor stub
		mOpenHelper = AppLockDBHelper.getInstance(context);
	}
	
	/**
	 * ���Ӧ�ó��򵽳�����
	 * @param packageName
	 */
	public void add(String packageName){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put("packageName", packageName);
			db.insert("applock", "_id", values);
			db.close();
		}
	}
	
	/**
	 *�ӳ�������ɾ��ָ����Ӧ�ó���
	 * @param packageName
	 */
	public void delete(String packageName){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete("applock", "packageName = ?", new String[]{packageName});
			db.close();
		}
	}
	
	/**
	 * �ж�Ӧ�ó����Ƿ��ڳ�������
	 * @param packageName
	 * @return
	 */
	public boolean isLockApp(String packageName){
		boolean isLockApp = false;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor c = db.query("applock", null, "packageName = ?", new String[]{packageName}, null, null, null);
			if(c.moveToNext()){
				isLockApp = true;
			}
			c.close();
			db.close();
		}
		return isLockApp;
	}
	
	/**
	 * �õ����еļ�������
	 * @return
	 */
	public List<String> findAll(){
		List<String> applocks = new ArrayList<String>();
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor c = db.query("applock", new String[]{"packageName"}, null, null, null, null, null);
			while(c.moveToNext()){
				String packageName = c.getString(0);
				applocks.add(packageName);
			}
			c.close();
			db.close();
		}
		return applocks;
	}
	
}
