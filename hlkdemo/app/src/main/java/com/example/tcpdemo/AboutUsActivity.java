package com.example.tcpdemo;

import com.example.udp_tcp_demo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutUsActivity extends BaseActivity {

	private TextView m_tvVersion;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		
		findViewById(R.id.tvDelete).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		findViewById(R.id.btnCallGicisky).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 Uri uri = Uri.parse("http://www.gicisky.com");  
				 Intent it = new Intent(Intent.ACTION_VIEW, uri);  
				 startActivity(it);
			}
		});
		findViewById(R.id.btnCallPhone).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialPhoneNumber("15889299007");
//				dialPhoneNumber("13823274562");
			}
		});
		
		m_tvVersion = (TextView) findViewById(R.id.tvVersion);
		String ver = getVersion(this);
		m_tvVersion.setText("HLK-M30 Android V"+ver);
		
	}
	
	private void dialPhoneNumber(String phoneNumber) {  
        Intent intent = new Intent(Intent.ACTION_CALL);  
        intent.setData(Uri.parse("tel:" + phoneNumber));  
        if (intent.resolveActivity(getPackageManager()) != null) {  
            startActivity(intent); 
        }  
    } 
	
	/**
	 * ��ȡ�汾��
	 * @return ��ǰӦ�õİ汾��
	 */
	public static String getVersion(Context con) {
	    try {
	        PackageManager manager = con.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(con.getPackageName(), 0);
	        String version = info.versionName;
	        return  version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "0.0";
	    }
	}
	
}
