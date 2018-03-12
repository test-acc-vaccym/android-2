package com.example.mytt.dao;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.mytt.dao.DBContent.DeviceInfo;
import com.example.mytt.dao.SQLiteTemplate.RowMapper;
import com.example.tcpdemo.BaseVolume;
import com.example.tcpdemo.CommandInfoCache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CommandInfoDao {

	static SQLiteDatabase mDB;
	DBBaseDao mBaseDao;
	
	public CommandInfoDao(Context context) {
		String filePath = context.getFilesDir().getAbsolutePath() +"/" + BaseVolume.DB_NAME;
		mDB = SQLiteDatabase.openOrCreateDatabase(filePath,null);
		if (mDB != null){ 
			this.mBaseDao = new DBBaseDao(mDB);
		}
		if (!mBaseDao.tabIsExist(DeviceInfo.COM_NAME)) {
			mDB.execSQL(DeviceInfo.getCreateCommandSQL());
		}
	}
	
	public ArrayList<CommandInfoCache> featchDeviceByMac(){
		String sql = "select * from CommandInfo" ;
		ArrayList<CommandInfoCache> temp  =  mBaseDao.queryForListBySql(sql, mRowMapper_MessageData, null);
		return temp;
		
	}
	
	/**
	 * 锟斤拷询锟斤拷锟斤拷锟借备
	 * @param areaName
	 * @return
	 */
	public ArrayList<CommandInfoCache> queryAllDevice(){
		String sql = "select * from DeviceInfo";
		return mBaseDao.queryForListBySql(sql, mRowMapper_MessageData, null);
	}
	
	private static final RowMapper<CommandInfoCache> mRowMapper_MessageData = new RowMapper<CommandInfoCache>() {
		public CommandInfoCache mapRow(Cursor cursor, int rowNum) {
			int ID = cursor.getInt(cursor.getColumnIndex(DeviceInfo.VCommandData.id));
			String mac = cursor.getString(cursor.getColumnIndex(DeviceInfo.VCommandData.ComMac));
			String Name = cursor.getString(cursor.getColumnIndex(DeviceInfo.VCommandData.ComName));
			String data = cursor.getString(cursor.getColumnIndex(DeviceInfo.VCommandData.ComData));
			
			CommandInfoCache command = new CommandInfoCache(ID, Name, mac, data);
			
			return command;
		}
	};
	
	/*****
	 * 锟斤拷锟斤拷璞�
	 * @param data
	 * @return
	 */
		public int insertSingleData(CommandInfoCache data) {
			int result = 0;
			try {
				mDB.insert(DeviceInfo.COM_NAME,null,makeValues(data));
			} catch (Exception e) {
				e.printStackTrace();
				result = -1;
			}
			return result;
		} 
		
		private ContentValues makeValues(CommandInfoCache temp) { 
			ContentValues initialValues = new ContentValues();
			initialValues.put(DeviceInfo.VCommandData.ComMac, temp.getMac());
			initialValues.put(DeviceInfo.VCommandData.ComName, temp.getName());
			initialValues.put(DeviceInfo.VCommandData.ComData, temp.getData());
			return initialValues;
		}
		
		public void closeDb() {
			mDB.close();
		}
		
		/**
		 * 删锟斤拷某锟斤拷锟借备
		 */
		public int deleteData(CommandInfoCache data) {
			String whereClause = "ComName = ? ";
			int i;
			try {
				i = mDB.delete(DeviceInfo.COM_NAME,  whereClause, new String[]{data.getName()});
				return i;
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
	
}