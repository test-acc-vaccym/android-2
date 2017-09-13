package com.example.SubFragments;

import com.example.CommonFunction.SerialCom;
import com.example.xwwt.R;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
public class IMUCalibSubFragment extends Fragment
{

		private View fragmentView;
		private Button btnIMU;//设置惯导标定
		private EditText editText1,editText2,editText3;
		
		private String ed1,ed2,ed3;
		
		Bundle bundle =new Bundle();
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{  
			super.onCreate(savedInstanceState);
			bundle=this.getArguments();
		}
		
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.imucalib_layout,container,false);
		    //惯导标定页面
			initialView(fragmentView);
			return fragmentView;
		}
		//初始页面视图
		public void initialView(View view)
		{
			ed1=bundle.getString("heading");
			ed2=bundle.getString("pitch");
			ed3=bundle.getString("roll");
			
			
			editText1=(EditText)view.findViewById(R.id.editText01);
			editText1.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String numberSpan; 
					if(s!=null&&!s.equals(""))
					{
						if(-5.0!=-1&&5.0!=-1)
						{
//							Double toWrite=0.0;
//							try
//							{
//								toWrite=Double.parseDouble(s.toString());	
//							}
//							catch(NumberFormatException e)
//							{
//								toWrite=0.0;
//							}
							Double toWrite=Double.parseDouble(s.toString());
							if(toWrite>5.0)
							{
								editText1.setText("5.0");
							}
							else if(toWrite<-5.0)
							{
								editText1.setText("-5.0");
							}
						}
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
			});  
			editText2=(EditText)view.findViewById(R.id.editText02);
			editText2.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String numberSpan; 
					if(s!=null&&!s.equals(""))
					{
						if(-5.0!=-1&&5.0!=-1)
						{
//							Double toWrite=0.0;
//							try
//							{
//								toWrite=Double.parseDouble(s.toString());	
//							}
//							catch(NumberFormatException e)
//							{
//								toWrite=0.0;
//							}
							Double toWrite=Double.parseDouble(s.toString());
							if(toWrite>5.0)
							{
								editText2.setText("5.0");
							}
							else if(toWrite<-5.0)
							{
								editText2.setText("-5.0");
							}
						}
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
			});  
			editText3=(EditText)view.findViewById(R.id.editText04);
			editText3.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String numberSpan; 
					if(s!=null&&!s.equals(""))
					{
						if(-5.0!=-1&&5.0!=-1)
						{
//							Double toWrite=0.0;
//							try
//							{
								//toWrite=Double.parseDouble(s.toString());
								Double toWrite=Double.parseDouble(s.toString());
//							}
//							catch(NumberFormatException e)
//							{
//								toWrite=0.0;
//							}
							if(toWrite>5.0)
							{
								editText3.setText("5.0");
							}
							else if(toWrite<-5.0)
							{
								editText3.setText("-5.0");
							}
						}
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
			}); 
			
			editText1.setText(ed1);
			editText2.setText(ed2);
			editText3.setText(ed3);
		
			
			btnIMU=(Button)view.findViewById(R.id.button01);
			btnIMU.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//判断所有的输入是否均有值
					if("".equals(editText1.getText().toString())||"".equals(editText2.getText().toString())||"".equals(editText3.getText().toString()))
					{
						//弹出对话框，输入不能为空
						AlertDialog dialog =new AlertDialog.Builder(getActivity())
								.setTitle("Alert")
								.setMessage("输入不能为空")
								.setPositiveButton("确定",null)
								.show();
					}
					else
					{
						//向下位机发送位置控制数据
						//JSONObject json=new JSONObject();
						//String actionName="referenceStar";					
						SerialCom newSer=new SerialCom();
						newSer.IMUCalib(editText1+"", editText2+"", editText3+"");
					}
				}
			});
		}

}