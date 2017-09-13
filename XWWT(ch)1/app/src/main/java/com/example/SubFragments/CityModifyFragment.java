package com.example.SubFragments;

import com.example.xwwt.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 
 * 进行数据库城市修改操作
 */
public class CityModifyFragment extends Fragment {
	
	private View fragmentView;
	private Button btn;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		fragmentView = inflater.inflate(R.layout.citymodify_layout,container,false);
	
		return fragmentView;
	}
}
