package cn.itcast.mobilesafe.test;

import java.util.List;

import cn.itcast.mobilesafe.domain.ContactInfo;
import cn.itcast.mobilesafe.engine.ContactInfoService;
import android.test.AndroidTestCase;
import android.util.Log;

public class ContactInfoServiceTest extends AndroidTestCase {

	public void testGetContacts() throws Exception{
		ContactInfoService service = new ContactInfoService(getContext());
		List<ContactInfo> contacts = service.getContacts();
		for(ContactInfo info:contacts){
			Log.i("i", info.toString());
		}
			 
	}
}
