package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.utils.MD5;

public class LostProtectedActivity extends Activity {

	private SharedPreferences sp;
	private LayoutInflater mInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String pwd = sp.getString("pwd", "");
		if("".equals(pwd)){
			//��һ�ν����ֻ���������
			showFirstEntryDialog();
		}else{
			//�����Ľ����ֻ���������
			showNormalEntryDialog();
		}
		
	}


	//�����Ľ����ֻ���������
	private void showNormalEntryDialog() {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.normal_entry_dialog, null);
		final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd_normal_entry_dialog);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok_normal_entry_dialog);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel_normal_entry_dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("����������");
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
					//�����ֻ��������򵼽���
					loadGuideUI();
					dialog.dismiss();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "����������벻��ȷ", 1).show();
				}
			}
		});
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
	}



	//��һ�ν����ֻ���������
	private void showFirstEntryDialog() {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.first_entry_dialog, null);
		final EditText et_pwd_first_entry_dialog = (EditText) view.findViewById(R.id.et_pwd_first_entry_dialog);
		final EditText et_repwd_first_entry_dialog = (EditText) view.findViewById(R.id.et_repwd_first_entry_dialog);
		Button bt_ok_first_entry_dialog = (Button) view.findViewById(R.id.bt_ok_first_entry_dialog);
		Button bt_cancel_first_entry_dialog = (Button) view.findViewById(R.id.bt_cancel_first_entry_dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle("��������");
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		dialog.show();
		bt_ok_first_entry_dialog.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String pwd = et_pwd_first_entry_dialog.getText().toString();
				String repwd = et_repwd_first_entry_dialog.getText().toString();
				if("".equals(pwd)||"".equals(repwd)){
					Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 1).show();
				}else if(pwd.equals(repwd)){
					//��������м���
					String md5_pwd = MD5.getData(pwd);
					//������ͬ��������
					Editor editor = sp.edit();
					editor.putString("pwd", md5_pwd);
					editor.commit();
					
					dialog.dismiss();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "������������벻��ͬ", 1).show();
				}
				
			}
		});
		bt_cancel_first_entry_dialog.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
	}
	
	
	//�����ֻ��򵼽���  ����򵼽����Ѿ�������ɣ�ֱ�ӽ����ֻ��������ý���
	private void loadGuideUI(){
		//�Ƿ����������
		boolean issetup = sp.getBoolean("issetup", false);
		if(issetup){
			//ֱ�ӽ����ֻ��������ý���
			Intent intent = new Intent(this,LostProtectedSettingActivity.class);
			startActivity(intent);
			
		}else{
			//��������򵼽���
			Intent intent = new Intent(this,Setup1ConfigActivity.class);
			startActivity(intent);
		}
	}
}
