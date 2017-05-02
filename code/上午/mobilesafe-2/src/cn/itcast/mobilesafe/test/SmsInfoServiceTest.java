package cn.itcast.mobilesafe.test;

import java.util.List;

import cn.itcast.mobilesafe.domain.SmsInfo;
import cn.itcast.mobilesafe.engine.SmsInfoService;
import android.test.AndroidTestCase;
import android.util.Log;

public class SmsInfoServiceTest extends AndroidTestCase {

	public void testGetSmsInfos() throws Exception{
		SmsInfoService service = new SmsInfoService(getContext());
		List<SmsInfo> smsInfos = service.getSmsInfos();
		for(SmsInfo info:smsInfos){
			Log.i("i", info.toString());
		}
	}
	
	public void testCreateXml() throws Exception{
		SmsInfoService service = new SmsInfoService(getContext());
		List<SmsInfo> smsInfos = service.getSmsInfos();
		service.createXml(smsInfos);
	}
}
