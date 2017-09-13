package com.example.fragment;


import com.example.SubFragments.CityInfoFragment;
import com.example.SubFragments.SatelliteInfoFragment;
import com.example.fragment.SetFragment.BackHandlerInterface;
import com.example.xwwt.MainUI;
import com.example.xwwt.R;
import com.example.SubFragments.Introduction;
import com.example.SubFragments.Product;
import com.example.SubFragments.Contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//����ҳ���ļ�
public class HelpFragment extends Fragment
{
	private View fragmentView;
	FragmentTabHost mTabHost = null;
	
	
    private boolean mHandledPress = false;
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(HelpFragment helpFragment);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
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
		View view = inflater.inflate(R.layout.help_fragment, null);

		initView(view);

		return view;
	}
	private void initView(View view) {

		mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				android.R.id.tabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("sub1").setIndicator("ʹ��˵��"),
				Introduction.class, null);//ʹ��˵��Manual

		mTabHost.addTab(mTabHost.newTabSpec("sub2").setIndicator("��Ʒ����"),
				Product.class, null);//��Ʒ����Introduction
		
		mTabHost.addTab(mTabHost.newTabSpec("sub3").setIndicator("��ϵ��ʽ"),
				Contact.class, null);//��ϵ��ʽContact
		
		mTabHost.getTabWidget().setDividerDrawable(R.color.white);

	}


	
    //����Fragment�еİ�ť����
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