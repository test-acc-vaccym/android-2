package com.youmi.android.sdk.sample;

import net.youmi.android.AdView;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * XML布局示例
 * 然后在xml布局中添加net.youmi.android.AdViewLayout。
 * @author 有米广告
 *
 */
public class XmlSample extends Activity {
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.xmlsample); 
		
		LinearLayout adViewLayout = (LinearLayout) findViewById(R.id.adViewLayout);
		adViewLayout.addView(new AdView(this), 
		        new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
	}
	
	
}
