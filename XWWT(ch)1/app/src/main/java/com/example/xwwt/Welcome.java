//����������
package com.example.xwwt;

import android.app.Activity;
//import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;


public class Welcome extends Activity implements Runnable 
{

	//�Ƿ��ǵ�һ��ʹ��
	private boolean isFirstUse;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_page);
		/**
		 * ����һ���ӳ��߳�
		 */
		new Thread(this).start();
	}

	public void run() 
	{
		try {
			Thread.sleep(1500);// �ӳ�1.5�����

			// ��ȡSharedPreferences����Ҫ������
			SharedPreferences preferences = getSharedPreferences("isFirstUse", MODE_WORLD_READABLE);
			isFirstUse = preferences.getBoolean("isFirstUse", true);

			/**
			 * ����û����ǵ�һ��ʹ����ֱ�ӵ�ת����ʾ����,�����ת����������
			 */
			if (isFirstUse){
				Intent aIntent = new Intent(Welcome.this, guide_activity.class);
				startActivity(aIntent);
			} 
			else {
				Intent aIntent = new Intent();
				aIntent.setClass(Welcome.this, Login_Activity.class);
				startActivity(aIntent);
			}
			finish();

			// ʵ����Editor����
			Editor editor = preferences.edit();
			// ��������
			editor.putBoolean("isFirstUse", false);
			// �ύ�޸�
			editor.commit();

		} catch (InterruptedException e){
		
		}
	}
}