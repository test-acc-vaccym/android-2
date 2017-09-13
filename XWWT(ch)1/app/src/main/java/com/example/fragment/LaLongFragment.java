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
 * ������ҳѡ���࣬��Ҫ�����ǽ��о�γ�Ȳ�ѯ/����ȣ����Զ���ӿ�
 */
/*
 * 
 */



public class LaLongFragment extends Fragment
{
	private View fragmentView;
	FragmentTabHost mTabHost = null;
	
//	OnClickListener mCallback;//�����ӿ�
//	
//	//����ӿ�,Fragment��activity����ͨ��
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

	//��TabHost�������Ҫ��ʾ����Fragment
	private void initView(View view) {

		mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				android.R.id.tabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("sub1").setIndicator("���в�ѯ"),
				CityQueryFragment.class, null);//�����ݿ��д洢����Ϣ���в�ѯ

	
		mTabHost.addTab(mTabHost.newTabSpec("sub4").setIndicator("���б༭"),
				CityModifyFragment.class, null);//�Գ��еȵ���Ϣ���б༭
		
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
	
    //����Fragment�еİ�ť����
    public void OnClick()
    {
    	
    }
    
    @Override
    public void onDestroyView() {
    super.onDestroyView();
    mTabHost = null;
    }
	//�����ѯ���о�γ����Ϣ�����ݿ��ѯ����
//	public void ReadMonitor(View view)
//	{
//		String actionName="readInfo";
//		SerialCom newSer=new SerialCom();
//		newSer.CheckMonitor(actionName);
//	}
	
}