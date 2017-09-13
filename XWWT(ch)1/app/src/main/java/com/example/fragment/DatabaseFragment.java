package com.example.fragment;

import com.example.SubFragments.AntCalibFragment;
import com.example.SubFragments.CityInfoFragment;
import com.example.SubFragments.ReadAntCalibFragment;
import com.example.SubFragments.SatelliteInfoFragment;
import com.example.fragment.HelpFragment.BackHandlerInterface;
import com.example.xwwt.MainUI;
import com.example.xwwt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DatabaseFragment extends Fragment
{
	private View fragmentView;
	FragmentTabHost mTabHost = null;
	
    private boolean mHandledPress = false;
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(DatabaseFragment dbFragment);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		
		super.onCreate(savedInstanceState);
		
//		Bundle bundle = getArguments();
//		if (null != bundle) {
//			//
//		}
		
        if (!(getActivity() instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//T.showShort(getActivity(), "FragmentMessage==onCreateView");
		View view = inflater.inflate(R.layout.database_fragment, null);

		initView(view);

		return view;
	}

	//在TabHost中添加需要显示的子Fragment
	private void initView(View view) {

		mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				android.R.id.tabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("sub1").setIndicator("城市信息"),
				CityInfoFragment.class, null);//City Infromation

		mTabHost.addTab(mTabHost.newTabSpec("sub2").setIndicator("卫星信息"),
				SatelliteInfoFragment.class, null);//读Satellite Information
		
		mTabHost.getTabWidget().setDividerDrawable(R.color.white);

	}


	
    //定义Fragment中的按钮操作
    public void OnClick()
    {
    	
    }
    
    @Override
    public void onDestroyView() {
    super.onDestroyView();
    mTabHost = null;
    }
    @Override
    public void onStart() {
        super.onStart();
        backHandlerInterface.setSelectedFragment(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        }
    }
	
    public boolean onBackPressed() {
        if (!mHandledPress) {
            mHandledPress = true;
    		Intent aIntent = new Intent();
    		aIntent.setClass(getActivity(),MainUI.class);
    		startActivity(aIntent);
            return true;
        }
        return false;
    }
}