package com.example.fragment;

import com.example.xwwt.R;

import com.example.CommonFunction.Dao;
import com.example.CommonFunction.PHPOperator;
import com.example.SubFragments.CityModifyFragment;
import com.example.SubFragments.CityQueryFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/*
 * 定义主页选项类，主要作用是进行经纬度查询/插入等，可以定义接口
 */
/*
 * 
 */



public class LaLongFragment extends Fragment
{
	private View fragmentView;
	FragmentTabHost mTabHost = null;
	
//	OnClickListener mCallback;//声明接口
//	
//	//定义接口,Fragment与activity进行通信
//	public interface  OnClickListener
//	{
//		 public void onMonitor(int position);
//	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		Bundle bundle = getArguments();
		if (null != bundle) {
			//
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//T.showShort(getActivity(), "FragmentMessage==onCreateView");
		View view = inflater.inflate(R.layout.lalong_fragment, null);

		initView(view);

		return view;
	}

	//在TabHost中添加需要显示的子Fragment
	private void initView(View view) {

		mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				android.R.id.tabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("sub1").setIndicator("城市查询"),
				CityQueryFragment.class, null);//对数据库中存储的信息进行查询

	
		mTabHost.addTab(mTabHost.newTabSpec("sub4").setIndicator("城市编辑"),
				CityModifyFragment.class, null);//对城市等的信息进行编辑
		
		mTabHost.getTabWidget().setDividerDrawable(R.color.white);

	}

	
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState)
//	{
//		fragmentView = inflater.inflate(R.layout.monitor_layout,container,false);
//	
//		return fragmentView;
//	}
	
   
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnClickListener) activity;
//        } catch (ClassCastException e) {
//           throw new ClassCastException(activity.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
//    }
	
    //定义Fragment中的按钮操作
    public void OnClick()
    {
    	
    }
    
    @Override
    public void onDestroyView() {
    super.onDestroyView();
    mTabHost = null;
    }
	//定义查询城市经纬度信息的数据库查询方法
//	public void ReadMonitor(View view)
//	{
//		String actionName="readInfo";
//		SerialCom newSer=new SerialCom();
//		newSer.CheckMonitor(actionName);
//	}
	
}