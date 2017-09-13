package com.example.SubFragments;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.SerialCom;
import com.example.xwwt.R;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * 在TabHost中显示的天线角度信息
 * @author lei
 *
 */
public class ReadIMUCalibFragment extends Fragment
{

		private View fragmentView;
		private Button btnRead;//读取惯导标定指令
		private EditText editText1,editText2,editText3;
		JSONObject json;
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.readimucalib_layout,container,false);
		    initialView(fragmentView);
			return fragmentView;
		}

	public void initialView(View view) {
		
		editText1=(EditText)view.findViewById(R.id.editText01);//发射角
		editText2=(EditText)view.findViewById(R.id.editText02);//俯仰角
		editText3=(EditText)view.findViewById(R.id.editText04);//发射极化
		
		//读取惯导标定数据
		btnRead=(Button)view.findViewById(R.id.button01);
		btnRead.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					//向下位机发送读取惯导标定指令
				
					SerialCom newSer=new SerialCom();
					json=newSer.ReadIMUCalib();
					try
					{
						editText1.setText(json.getString("heading").toString());
						editText2.setText(json.getString("pitch").toString());
						editText3.setText(json.getString("roll").toString());
					}
					catch(JSONException e)
					{
						
					}
			}
		});
	}
}