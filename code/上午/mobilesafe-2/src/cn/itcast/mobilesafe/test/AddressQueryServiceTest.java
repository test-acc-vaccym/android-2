package cn.itcast.mobilesafe.test;

import cn.itcast.mobilesafe.engine.AddressQueryService;
import android.test.AndroidTestCase;
import android.util.Log;

public class AddressQueryServiceTest extends AndroidTestCase {

	
	public void testQuery() throws Exception{
		AddressQueryService service = new AddressQueryService();
		String address = service.query("0105441053");
		Log.i("i", " address " + address);
	}
}
