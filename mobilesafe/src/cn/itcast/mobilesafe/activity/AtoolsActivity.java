package cn.itcast.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.SmsInfo;
import cn.itcast.mobilesafe.engine.SmsInfoService;
import cn.itcast.mobilesafe.service.BackupSmsService;
import cn.itcast.mobilesafe.utils.MD5;

public class AtoolsActivity extends Activity implements OnClickListener{

	private TextView tv_atools_add_ipcall;
	private TextView tv_atools_address_query;
	private TextView tv_atools_sms_backup;
	private TextView tv_atools_sms_restore;
	private TextView tv_atools_app_lock;
	private ProgressDialog mPd;
	private SmsInfoService smsInfoService;
	
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.atools);
		
		tv_atools_add_ipcall = (TextView) findViewById(R.id.tv_atools_add_ipcall);
		tv_atools_add_ipcall.setOnClickListener(this);
		
		tv_atools_address_query = (TextView) findViewById(R.id.tv_atools_address_query);
		tv_atools_address_query.setOnClickListener(this);
		
		tv_atools_sms_backup = (TextView) findViewById(R.id.tv_atools_sms_backup);
		tv_atools_sms_backup.setOnClickListener(this);
		
		tv_atools_sms_restore = (TextView) findViewById(R.id.tv_atools_sms_restore);
		tv_atools_sms_restore.setOnClickListener(this);
		
		tv_atools_app_lock = (TextView) findViewById(R.id.tv_atools_app_lock);
		tv_atools_app_lock.setOnClickListener(this);
		
		smsInfoService = new SmsInfoService(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		Intent intent = null;
		switch (id) {
		case R.id.tv_atools_add_ipcall:
			intent = new Intent(this,AddIpCallActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_atools_address_query:
			intent = new Intent(this,AddressQueryActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_atools_sms_backup:
			intent = new Intent(this,BackupSmsService.class);
			startService(intent);
			break;
		case R.id.tv_atools_sms_restore:
			//1 ɾ�����еĶ���
			//2 ��xml��������ݲ��뵽���ŵ����ݿ�
			   //2.1 �Ƚ���xml�ļ�
			   //2.2 ��������
			
			mPd = new ProgressDialog(this);
			mPd.setTitle("����ɾ��ԭ���Ķ���");
			mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mPd.show();
			
			new Thread(){
				public void run() {
					try {
						Uri uri = Uri.parse("content://sms");
						getContentResolver().delete(uri, null, null);
						mPd.setTitle("���ڻ�ԭ����");
						List<SmsInfo> smsInfos = smsInfoService.getSmsInfosFromXml();
						mPd.setMax(smsInfos.size());
						for(SmsInfo smsInfo:smsInfos){
							ContentValues values = new ContentValues();
							values.put("address", smsInfo.getAddress());
							values.put("date", smsInfo.getDate());
							values.put("type", smsInfo.getType());
							values.put("body", smsInfo.getBody());
							getContentResolver().insert(uri, values);
							SystemClock.sleep(2000);
							mPd.incrementProgressBy(1);//ÿ�ν������̶�ֵ��1
							
						}
						mPd.dismiss();
						Looper.prepare();
						Toast.makeText(getApplicationContext(), "���Ż�ԭ�ɹ�", 1).show();
						Looper.loop();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mPd.dismiss();
						Looper.prepare();
						Toast.makeText(getApplicationContext(), "���Ż�ԭʧ��", 1).show();
						Looper.loop();
					}
					
				};
			}.start();

			
			
			break;
		case R.id.tv_atools_app_lock:
			View view = View.inflate(this, R.layout.normal_entry_dialog, null);
			final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd_normal_entry_dialog);
			Button bt_ok = (Button) view.findViewById(R.id.bt_ok_normal_entry_dialog);
			Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel_normal_entry_dialog);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("��˽����");
			builder.setView(view);
			final AlertDialog dialog = builder.create();
			dialog.show();
			
			bt_ok.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String pwd = et_pwd.getText().toString();
					String md5_pwd = MD5.getData(pwd);
					String old_pwd = sp.getString("pwd", "");
					if("".equals(pwd)){
						Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 1).show();
					}else if(old_pwd.equals(md5_pwd)){
						//��������������ɾ������
						Intent intent = new Intent(getApplicationContext(),AppLockManagerActivity.class);
						startActivity(intent);
						dialog.dismiss();
					}else{
						Toast.makeText(getApplicationContext(), "�������벻��ȷ", 1).show();
					}
					
					
				}
			});
			bt_cancel.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		default:
			break;
		}
	}
}
