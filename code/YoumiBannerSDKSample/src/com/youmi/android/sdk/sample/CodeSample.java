package com.youmi.android.sdk.sample;

import net.youmi.android.AdView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.NinePatch;
import android.os.Bundle; 
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


/**
 * 代码布局示例
 * 适用于习惯使用代码布局的开发者
 * @author 有米广告
 *
 */
public class CodeSample extends Activity { 
 			
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
				
		
	    
	    LinearLayout layout=new LinearLayout(this); 
		layout.setOrientation(LinearLayout.VERTICAL); 
		layout.setBackgroundResource(R.drawable.bg);	
				
		// 初始化广告条，可以使用其他的构造函数设置文字类型广告的背景色、透明度及字体颜色
		 
		AdView adView = new AdView(this); 
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);		
		layout.addView(adView, params); 
				
		setContentView(layout);
	}  
	
}
