package cn.itcast.mobilesafe.test;

import java.util.List;

import cn.itcast.mobilesafe.dao.BlackNumberDao;
import android.test.AndroidTestCase;
import android.util.Log;

public class BlackNumberDaoTest extends AndroidTestCase {

	
	public void testAdd() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		for(int i = 0;i< 10;i++){
			dao.add("555" + i);
		}
	}
	public void testIsBlackNumber() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		boolean result = dao.isBlackNumber("5558");
		Log.i("i", " is black number " + result);
	}
	
	
	public void testDelete() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete("5558");
	}
	
	public void testUpdate() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update(1, "2222");
	}
	
	public void testQueryId() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		Log.i("i", "_id = " + dao.queryId("8888"));
	}
	
	public void testFindAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<String> list = dao.findAll();
		for(int i = 0;i< list.size();i++){
			Log.i("i", list.get(i));
		}
	}
	
	
}
