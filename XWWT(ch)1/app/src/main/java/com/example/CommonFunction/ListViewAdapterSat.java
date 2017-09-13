package com.example.CommonFunction;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.xwwt.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import static com.example.CommonFunction.Constants.FIRST_COLUMN;
import static com.example.CommonFunction.Constants.SECOND_COLUMN;
import static com.example.CommonFunction.Constants.THIRD_COLUMN;
import static com.example.CommonFunction.Constants.FOURTH_COLUMN;
import static com.example.CommonFunction.Constants.FIFTH_COLUMN;
import static com.example.CommonFunction.Constants.SIXTH_COLUMN;

public class ListViewAdapterSat extends BaseAdapter{
	 
	public ArrayList<HashMap<String, String>> list;
	Activity activity;
	TextView txtFirst;
	TextView txtSecond;
	TextView txtThird;
	TextView txtFourth;
	TextView txtFifth;
	TextView txtSixth;
	public ListViewAdapterSat(Activity activity,ArrayList<HashMap<String, String>> list){
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
		return 0;
	}
 	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	
		
		
		LayoutInflater inflater=activity.getLayoutInflater();
		
		if(convertView == null){
			
			convertView=inflater.inflate(R.layout.columnsat, null);			
		}
			txtFirst=(TextView) convertView.findViewById(R.id.t1);
			txtSecond=(TextView) convertView.findViewById(R.id.t2);
			txtThird=(TextView) convertView.findViewById(R.id.t3);
			txtFourth=(TextView) convertView.findViewById(R.id.t4);
			txtFifth=(TextView) convertView.findViewById(R.id.t5);
			txtSixth=(TextView) convertView.findViewById(R.id.t6);
		HashMap<String, String> map=list.get(position);
		txtFirst.setText(map.get(FIRST_COLUMN));
		txtSecond.setText(map.get(SECOND_COLUMN));
		txtThird.setText(map.get(THIRD_COLUMN));
		txtFourth.setText(map.get(FOURTH_COLUMN));
		txtFifth.setText(map.get(FIFTH_COLUMN));
		txtSixth.setText(map.get(SIXTH_COLUMN));
		
		return convertView;
	} 
}
