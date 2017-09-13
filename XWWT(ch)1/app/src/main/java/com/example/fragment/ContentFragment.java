package com.example.fragment;

import com.example.xwwt.R;

import com.example.CommonFunction.Dao;
import com.example.CommonFunction.PHPOperator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/*
 * 定义主页选项类
 */
public class ContentFragment extends Fragment
{
	private View fragmentView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		fragmentView = inflater.inflate(R.layout.home_layout,container,false);
	
		return fragmentView;
	}	
}