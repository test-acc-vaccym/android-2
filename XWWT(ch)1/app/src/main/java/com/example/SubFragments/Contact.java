package com.example.SubFragments;

import com.example.xwwt.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//在帮助页面的联系方式页面
public class Contact extends Fragment
{
	private View fragmentView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		fragmentView = inflater.inflate(R.layout.contact_fragment,container,false);

		return fragmentView;
	}
	
}