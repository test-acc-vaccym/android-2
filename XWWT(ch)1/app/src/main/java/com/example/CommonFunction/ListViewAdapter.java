package com.example.CommonFunction;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.xwwt.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import static com.example.CommonFunction.Constants.FIRST_COLUMN;
import static com.example.CommonFunction.Constants.SECOND_COLUMN;
import static com.example.CommonFunction.Constants.THIRD_COLUMN;

public class ListViewAdapter extends BaseAdapter{
	 
	public ArrayList<HashMap<String, String>> list;
	Activity activity;
	TextView txtFirst;
	TextView txtSecond;
	TextView txtThird;
	public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
		super();
		this.activity=activity;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
 
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
 
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			
			convertView=inflater.inflate(R.layout.columnrow, null);
					
		}
		
		txtFirst=(TextView) convertView.findViewById(R.id.t1);
		txtSecond=(TextView) convertView.findViewById(R.id.t2);
		txtThird=(TextView) convertView.findViewById(R.id.t3);
		
		HashMap<String, String> map=list.get(position);
		txtFirst.setText(map.get(FIRST_COLUMN));
		txtSecond.setText(map.get(SECOND_COLUMN));
		txtThird.setText(map.get(THIRD_COLUMN));
		
//		if(position%2==0)
//		{
//			convertView.setBackgroundColor(Color.WHITE);
//		}
//		else{
//			convertView.setBackgroundColor(Color.GRAY);
//		}
		
		return convertView;
	}


 
}
