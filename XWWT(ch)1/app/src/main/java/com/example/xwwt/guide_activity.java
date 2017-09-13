/*��������*/
package com.example.xwwt;



import java.util.ArrayList;
import java.util.Locale;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.viewpagerindicator.CirclePageIndicator;

import com.example.xwwt.R;
/**
 * ��һ��ʹ�õ���������
 * @author lei
 *
 */
public class guide_activity extends Activity implements OnPageChangeListener{
	// ����ViewPager����
	private ViewPager viewPager;

	// ����ViewPager������
	private ViewPagerAdapter vpAdapter;
	
	private CirclePageIndicator indicator;

	// ����һ��ArrayList�����View
	private ArrayList<View> views;

	// �����������View����
	private View view1, view2, view3, view4;
	
	//���忪ʼ��ť����
	private Button startBt;
	
	private Button regisBt;//ע�ᰴť
	
	private Button btnEnter;//����Ӧ�ó���
	
	//����checkboxѡ��
	private CheckBox checkBoxCh,checkBoxEn;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		initView();
		
		//initData();
	}

	/**
	 * ��ʼ�����
	 */
	private void initView() {
		//ʵ������������Ĳ��ֶ��� 
		
		//view1=findViewById(R.layout.guide_view01);
		//view2=findViewById(R.layout.guide_view02);
		//view3=findViewById(R.layout.guide_view03);
		//view4=findViewById(R.layout.guide_view04);
		
		
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.guide_view01, null);
		view2 = mLi.inflate(R.layout.guide_view02, null);
		view3 = mLi.inflate(R.layout.guide_view03, null);
		view4 = mLi.inflate(R.layout.guide_view04, null);
	
		// ʵ����ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		indicator = (CirclePageIndicator) findViewById(R.id.main_viewPager_indicator);

		// ʵ����ArrayList����
		views = new ArrayList<View>();

		// ʵ����ViewPager������
		vpAdapter = new ViewPagerAdapter(views);
		
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		
		viewPager.setAdapter(vpAdapter);
		viewPager.setOnPageChangeListener(mPageChangeListener);
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(mPageChangeListener);
        indicator.bringToFront();
		
		vpAdapter.notifyDataSetChanged();
		
		btnEnter=(Button) view4.findViewById(R.id.button1);
		btnEnter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				EnterApp();
			}
			
		});
		//ʵ������¼��ť
		//startBt = (Button) view4.findViewById(R.id.startBtn);
		//ʵ����ע�ᰴť
		//regisBt = (Button) view4.findViewById(R.id.regisBtn);
		
		//checkBoxCh=(CheckBox)view4.findViewById(R.id.checkBox1);
		
		//checkBoxEn=(CheckBox)view4.findViewById(R.id.checkBox2);
		
//		checkBoxCh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if(isChecked)
//				{
//					checkBoxEn.setChecked(false);
//					ChineseSet(view4);
//					
//				}
//				
//			}
//		
//		});
//		
//		checkBoxEn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if(isChecked)
//				{
//					checkBoxCh.setChecked(false);
//					EnglishSet(view4);
//					
//				}
//			}
//		
//		});
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
			
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		
	}
	
	
	private void EnterApp()
	{
		Intent intent = new Intent();
		intent.setClass(this,Login_Activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	/**
	 * ��Ӧ��ť����¼�
	 */
	private void startbutton() {  
      	Intent intent = new Intent();
		intent.setClass(guide_activity.this,Login_Activity.class);//�����¼������棬�����Ի���
		startActivity(intent);
		this.finish();
      }
	
	private void regisButton()
	{
      	Intent intent = new Intent();
		intent.setClass(guide_activity.this,Register_Activity.class);//����ע�������棬���Կ���ʹ�û����¼��¼�û���Ϣ
		startActivity(intent);
		this.finish();
	}
	
	/**
	 * ����������ʾ,���ǲ�Ʒ��ȥ�������
	 * @param view
	 */
	public void ChineseSet(View view)
	{
		Locale.setDefault(Locale.CHINESE); 
		Configuration config = getBaseContext().getResources().getConfiguration(); 
        config.locale = Locale.CHINESE; 
        getBaseContext().getResources().updateConfiguration(config
        		, getBaseContext().getResources().getDisplayMetrics());
		Intent intent = new Intent();
		intent.setClass(this,Login_Activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	/**
	 * ����Ӣ���л�
	 * @param view
	 */
	public void EnglishSet(View view)
	{
		Locale.setDefault(Locale.ENGLISH); 
		Configuration config = getBaseContext().getResources().getConfiguration(); 
        config.locale = Locale.ENGLISH; 
        getBaseContext().getResources().updateConfiguration(config
        		, getBaseContext().getResources().getDisplayMetrics());
		Intent intent = new Intent();
		intent.setClass(this,Login_Activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
    private ViewPager.OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            //Log.d(TAG, "\nonPageSelected(" + position + ")");
            // notifyViewPagerDataSetChanged();
        }
        
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        
        @Override
        public void onPageScrollStateChanged(int state) {}
    };
}
