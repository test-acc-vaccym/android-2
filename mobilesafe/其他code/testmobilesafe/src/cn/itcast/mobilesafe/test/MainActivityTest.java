package cn.itcast.mobilesafe.test;

import com.jayway.android.robotium.solo.Solo;

import cn.itcast.mobilesafe.activity.MainActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.Smoke;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	//机器人
	private Solo solo;
	
	public MainActivityTest() {
		super("cn.itcast.mobilesafe", MainActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	//开始测试  
	 public void setUp() throws Exception {
		 //初始化机器人
		 solo = new Solo(getInstrumentation(), getActivity());
		  }
	 
	 @Smoke
	 public void testOnItemClick(){
		 solo.clickLongOnScreen(70, 120);
		 solo.goBack();
	 }

		//停止测试
		@Override
		public void tearDown() throws Exception {
			try {
				//机器人的回收
				solo.finalize(); 	//Robotium will finish all the activities that have been open
			} catch (Throwable e) {
				e.printStackTrace();
			}
			//关闭当前的activity
			getActivity().finish();
			super.tearDown();
		} 
}
