package cn.itcast.mobilesafe.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.UpdateInfo;
import cn.itcast.mobilesafe.engine.AddressQueryService;
import cn.itcast.mobilesafe.engine.UpdateInfoService;
import cn.itcast.mobilesafe.utils.DownloadManager;

public class SplashActivity extends Activity {
	
	private static final int ERROR_GET_VERSION = 0;
	public static final int ERROR_GET_UPDATEINFO = 1;
	public static final int SHOW_UPDATE_DIALOG = 2;
	public static final int NOT_SHOW_UPDATE_DIALOG = 3;
	protected static final int ERROR_DOWNLOAD_APK = 4;
	public static final int SUCCESS_DOWNLOAD_APK = 5;
	public static final int SDCARD_NOT_EXIST = 6;
	public static final int ERROR_DOWNLOAD_DB = 7;
	public static final int SUCCESS_DOWNLOAD_DB = 8;
	private TextView tv_version;
	private PackageManager pm;//����������ϵͳ�ṩ�õķ������ܹ���ȡ�������ǣ�Manifest.xml�ļ�����  manifest�ڵ�������Ϣ��
	private String version;
	private UpdateInfoService updateInfoService;
	private UpdateInfo updateInfo;
	private ProgressDialog mPd;
	private RelativeLayout rl_splash;
	private long startTime;
	private SharedPreferences sp;
	private AddressQueryService addressQueryService;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ERROR_GET_VERSION:
				Toast.makeText(getApplicationContext(), "��ȡӦ�ó���İ汾��ʧ��", 1).show();
				break;
			case ERROR_GET_UPDATEINFO:
				Toast.makeText(getApplicationContext(), "��ȡ�������ϵ����°汾��Ϣʧ��", 1).show();
				loginMainUI();
				break;
			case SHOW_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case NOT_SHOW_UPDATE_DIALOG:
				loginMainUI();
				break;
			case ERROR_DOWNLOAD_APK:
				mPd.dismiss();
				Toast.makeText(getApplicationContext(), "��������apkʧ��", 1).show();
				loginMainUI();
				break;
			case SUCCESS_DOWNLOAD_APK:
				mPd.dismiss();
				//��װӦ�ó���
				installApk();
				break;
			case SDCARD_NOT_EXIST:
				mPd.dismiss();
				Toast.makeText(getApplicationContext(), "sdcard ������", 1).show();
				loginMainUI();
				break;
			case ERROR_DOWNLOAD_DB:
				mPd.dismiss();
				Toast.makeText(getApplicationContext(), "���������ݿ�����ʧ��", 1).show();
				loginMainUI();
				break;
			case SUCCESS_DOWNLOAD_DB:
				mPd.dismiss();
				//�ж�Ӧ�ó����Ƿ������°汾
				new Thread(new CheckVersionTask()).start();
			    break;
			default:
				break;
			}
		};
	};
	
	private void installApk(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		String name = DownloadManager.getFileName(updateInfo.getUrl());
		File file = new File(Environment.getExternalStorageDirectory(),name);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	//��ʾ���µ���ʾ�Ի���
	private void showUpdateDialog(){
		/**
		 * 1 ����Builder
		 * 2 ��builder ��������  :���� ��ʾ��Ϣ  ͼ��  ��ť
		 * 3 ����Dialog
		 * 4 ��ʾdialog
		 */
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("������");
		builder.setMessage(updateInfo.getDescription());
		builder.setCancelable(false);//���κ��˰�ť
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				    mPd = new ProgressDialog(SplashActivity.this);
				    mPd.setMessage("�����������µ�apk");
				    mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				    mPd.show();
					new Thread(new DownloadApkTask()).start();
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				loginMainUI();
			}
		});
		AlertDialog dialog  = builder.create();
		dialog.show();
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen); ͨ�����������������Ҫ��onCreate()֮ǰ
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ������
		setContentView(R.layout.splash);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ��״̬��
		rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
		tv_version = (TextView) findViewById(R.id.tv_version);
		pm = getPackageManager();
		updateInfoService = new UpdateInfoService();
		
		version = getVersion();
		tv_version.setText("�汾��:" + version);
		
		//ִ��һ��͸���ȸı䶯��
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(2000);
		rl_splash.startAnimation(animation);
		startTime = System.currentTimeMillis();
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		addressQueryService = new AddressQueryService();
		
		//�жϹ��������ݿ��Ƿ����
		boolean isExist = addressQueryService.isExist();
		if(isExist){
			//�ж�Ӧ�ó����Ƿ������°汾
			new Thread(new CheckVersionTask()).start();
		}else{
			mPd = new ProgressDialog(this);
			mPd.setTitle("���ڼ��ع��������ݿ�");
			mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mPd.show();
			//���ع��������ݿ�
			new Thread(new DownloadDBTask()).start();
		}
		

	}
	
	//���ع��������ݿ������
	private final class DownloadDBTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			try {
				boolean result = DownloadManager.download(getString(R.string.address_url), mPd);
				if(result){
					Message msg = new Message();
					msg.what = SUCCESS_DOWNLOAD_DB;
					mHandler.sendMessage(msg);
				}else{
					Message msg = new Message();
					msg.what = ERROR_DOWNLOAD_DB;
					mHandler.sendMessage(msg);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message msg = new Message();
				msg.what = ERROR_DOWNLOAD_DB;
				mHandler.sendMessage(msg);
			}
		}
		
	}
	
	
	//����apk������
	private final class DownloadApkTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			//�ж�sdcard�Ƿ����
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				try {
					boolean result = DownloadManager.download(updateInfo.getUrl(),mPd);
					if(result){
						Message msg = new Message();
						msg.what = SUCCESS_DOWNLOAD_APK;
						mHandler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what = ERROR_DOWNLOAD_APK;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Message msg = new Message();
					msg.what = ERROR_DOWNLOAD_APK;
					mHandler.sendMessage(msg);
				}
			}else{
				Message msg = new Message();
				msg.what = SDCARD_NOT_EXIST;
				mHandler.sendMessage(msg);
			}
		}
		
	}
	
	//���汾������
	private final class CheckVersionTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			long endTime = System.currentTimeMillis();
			long sleepTime = endTime - startTime;
			if(sleepTime < 2000){
				SystemClock.sleep(2000 - sleepTime);
			}
			boolean isautoupdate = sp.getBoolean("isautoupdate", true);
			if(isautoupdate){
				try {
					updateInfo = updateInfoService.getUpdateInfo(getString(R.string.updateinfo_url));
					if(updateInfo == null){
						Log.i("i", "��ȡ���°汾��Ϣʧ��");
						Message msg = new Message();
						msg.what = ERROR_GET_UPDATEINFO;
						mHandler.sendMessage(msg);
					}else{
						Log.i("i", updateInfo.toString());
						if(version.equals(updateInfo.getVersion())){
							//������ʾ���µĶԻ���
							Message msg = new Message();
							msg.what = NOT_SHOW_UPDATE_DIALOG;
							mHandler.sendMessage(msg);
						}else{
							//��ʾ���µĶԻ���
							Message msg = new Message();
							msg.what = SHOW_UPDATE_DIALOG;
							mHandler.sendMessage(msg);
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i("i", "��ȡ���°汾��Ϣʧ��");
					Message msg = new Message();
					msg.what = ERROR_GET_UPDATEINFO;
					mHandler.sendMessage(msg);
				}
			}else{
				loginMainUI();
			}

		}
		
	}
	
	/**
	 * ��ȡӦ�ó���İ汾��
	 * @return
	 */
	private String getVersion(){
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg = new Message();
			msg.what = ERROR_GET_VERSION;
			mHandler.sendMessage(msg);
		}
		return null;
	}
	
	
	private void loginMainUI(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}
}
