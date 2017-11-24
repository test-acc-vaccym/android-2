package cn.itcast.mobilesafe.test;

import com.jayway.android.robotium.solo.Solo;

import cn.itcast.mobilesafe.activity.MainActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.Smoke;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	//������
	private Solo solo;
	
	public MainActivityTest() {
		super("cn.itcast.mobilesafe", MainActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	//��ʼ����  
	 public void setUp() throws Exception {
		 //��ʼ��������
		 solo = new Solo(getInstrumentation(), getActivity());
		  }
	 
	 @Smoke
	 public void testOnItemClick(){
		 solo.clickLongOnScreen(70, 120);
		 solo.goBack();
	 }

		//ֹͣ����
		@Override
		public void tearDown() throws Exception {
			try {
				//�����˵Ļ���
				solo.finalize(); 	//Robotium will finish all the activities that have been open
			} catch (Throwable e) {
				e.printStackTrace();
			}
			//�رյ�ǰ��activity
			getActivity().finish();
			super.tearDown();
		} 
}
