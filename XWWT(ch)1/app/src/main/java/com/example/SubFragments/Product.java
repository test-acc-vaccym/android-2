package com.example.SubFragments;

import java.util.ArrayList;

import com.example.xwwt.R;
import com.example.xwwt.ViewPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;

public class Product extends Fragment implements OnPageChangeListener
{
	private View fragmentView;
	private View view1, view2;
	// ����ViewPager����
	private ViewPager viewPager;

	// ����ViewPager������
	private ViewPagerAdapter vpAdapter;
	private ArrayList<View> views;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		fragmentView = inflater.inflate(R.layout.product_viewpager,container,false);
		
		view1=fragmentView.inflate(getActivity(), R.layout.guide_view01, null);
		view2=fragmentView.inflate(getActivity(), R.layout.guide_view02, null);
		viewPager = (ViewPager) fragmentView.findViewById(R.id.id_pager);
		
  
		initView();
		return fragmentView;
	}
	
	public void initView()
	{
		// ʵ����ArrayList����
		views = new ArrayList<View>();

		// ʵ����ViewPager������
		vpAdapter = new ViewPagerAdapter(views);
		
		viewPager.setOnPageChangeListener(this);
		// ��������������
		viewPager.setAdapter(vpAdapter);

		//��Ҫ��ҳ��ʾ��Viewװ��������		
		views.add(view1);
		views.add(view2);		
			
		viewPager.setAdapter(vpAdapter);
		viewPager.setOnPageChangeListener(mPageChangeListener);
		
        CirclePageIndicator indicator = (CirclePageIndicator) fragmentView.findViewById(R.id.main_viewPager_indicator);
        indicator.setViewPager(viewPager);
        indicator.setOnPageChangeListener(mPageChangeListener); 
		
		vpAdapter.notifyDataSetChanged();
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
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