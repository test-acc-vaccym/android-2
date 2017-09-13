package com.example.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.EditTextLocker;
import com.example.CommonFunction.SerialCom;
import com.example.SubFragments.AntCalibFragment;
import com.example.SubFragments.AntCalibFragment.BackHandlerInterface;
import com.example.fragment.AntennaCalibFragment.GetCalibData;
import com.example.xwwt.MainUI;
import com.example.xwwt.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



	//定义在Activity中显示的fragment，首先将门限值显示出来，而后可以在此基础上更改
public class ReadThreshold extends Fragment implements OnFocusChangeListener, TextWatcher, Callback
	{
		private View fragmentView;
		private static Handler handler=new Handler();
		private Button btnSet;
		private EditText editText1;
		private EditText etFreq,etLongi;
		private Button btnAuto,btnStop;
		private Spinner sPolarization;
		private static final int DECIMAL_DIGITS = 1;//设置输入的位数
		private static final int DECIMAL_DIGITS_FRE=4;//设置卫星频率值
		private Spinner spinnerAuto;
		private String threshold;
		private JSONObject json;
		private ArrayAdapter<String> adapter;
		
		private int polarization;//定义极化方式选项
		
	    private boolean mHandledPress = false;
	    protected BackHandlerInterface backHandlerInterface;

	    public interface BackHandlerInterface {
	        public void setSelectedFragment(ReadThreshold threadFragment);
	    }
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{  
			super.onCreate(savedInstanceState);
			
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.threshold_activity,container,false);
		
			//设置寻星门限初始化视图
			initialView(fragmentView);
			
			//创建读取串口线程
			ReadThreadHold();
			
			return fragmentView;
		}
		
		/*
		 * 初始化视图
		 */
		public void initialView(View view)
		{
			
			//卫星经度
			etLongi=(EditText)view.findViewById(R.id.et01);
			etLongi.addTextChangedListener(this);  
			etLongi.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10) });  
			
			
			//设置参考星信标频率
			etFreq=(EditText)view.findViewById(R.id.editText02);
			etFreq.addTextChangedListener(this);
			etFreq.setFilters(new InputFilter[] { lengthfilterFre, new InputFilter.LengthFilter(10) });  
			
			
			editText1=(EditText)view.findViewById(R.id.editText01);			
			editText1.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String numberSpan; 
					if(s.toString()!=null&&!s.toString().equals(""))
					{
						if(0!=-1&&9.9!=-1)
						{
							Double toWrite=Double.parseDouble(s.toString());
							if(toWrite>9.9)
							{
								editText1.setText("9.9");
							}
							else if(toWrite<0)
							{
								editText1.setText("0.0");
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
			
			editText1.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10)
		});
			

			btnSet=(Button)view.findViewById(R.id.button01);
			btnSet.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//判断所有的输入是否均有值
					if("".equals(editText1.getText().toString()))
					{
						//弹出对话框，输入不能为空
						AlertDialog dialog =new AlertDialog.Builder(getActivity())
								.setTitle("Alert")
								.setMessage(R.string.assert06)
								.setPositiveButton("OK",null)
								.show();
					}
					else
					{
						//向下位机发送设置数据
						//JSONObject json=new JSONObject();
						final String setThreshold="setThreshold";	
						new Thread()
						{
							@Override
							public void run()
							{
								SerialCom newSer=new SerialCom();
								JSONObject json=newSer.SetThreshhold(setThreshold,editText1.getText().toString());
							Looper.prepare();
							CheckSuccess(json);
							Looper.loop();
							}
						}.start();
					}
				}
			});
			
			spinnerAuto=(Spinner)view.findViewById(R.id.spinner01);
			String[] mItems=getResources().getStringArray(R.array.polarizations2);
			//将list与adapter连起来
			adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mItems);
			//设置显示形式
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spinnerAuto.setAdapter(adapter);
	        //添加事件Spinner事件监听  
			spinnerAuto.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
	            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
	                // TODO Auto-generated method stub    
	                //获取选择的值 
	            	if(arg2==0)//水平极化
	            	{
	            		polarization=0;
	            	}
	            	else if(arg2==1)//垂直极化
	            	{
	            		polarization=1;
	            	}
	            	else if(arg2==2)//圆极化
	            	{
	            		polarization=4;
	            	}
	            	
	            	adapter.getItem(arg2);    
	                /* 将mySpinner 显示*/    
	                arg0.setVisibility(View.VISIBLE);    
	            }    
	            public void onNothingSelected(AdapterView<?> arg0) {    
	                // TODO Auto-generated method stub    
	                
	                arg0.setVisibility(View.VISIBLE);    
	            }    
	        }); 
	        //设置默认值
			spinnerAuto.setVisibility(View.VISIBLE);
			
			//自动寻星按钮操作指令
			btnAuto=(Button)view.findViewById(R.id.Button01);
			btnAuto.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//判断所有的输入是否均有值
					if("".equals(etLongi.getText().toString())||"".equals(etFreq.getText().toString()))
					{
						//弹出对话框，输入不能为空
						AlertDialog dialog =new AlertDialog.Builder(getActivity())
								.setTitle("Alert")
								.setMessage(R.string.assert06)
								.setPositiveButton("OK",null)
								.show();
					}
					else
					{
						//向下位机发送数据
						//JSONObject json=new JSONObject();
						//String actionName="referenceStar";
						new Thread()
						{
							@Override
							public void run()
							{
								SerialCom newSer=new SerialCom();
								JSONObject json=newSer.AutoSearch(etFreq.getText().toString(),etLongi.getText().toString() ,polarization+"");	
								Looper.prepare();
								CheckSuccess(json);
								Looper.loop();
							}			
						}.start();
					}
				}
			});
			
			//停止寻星指令
			btnStop=(Button)view.findViewById(R.id.button03);
			btnStop.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

						//向下位机发送数据
						//JSONObject json=new JSONObject();
						//String actionName="referenceStar";	
					
//					btnSet.setVisibility(INVISIBLE);
//					btnAuto.setVisibility(INVISIBLE);
					
					new Thread()
					{
						@Override
						public void run()
						{
							SerialCom newSer=new SerialCom();
							JSONObject json=newSer.StopSearch();
							Looper.prepare();
							CheckSuccess(json);
							Looper.loop();
						}
						
					}.start();
					//Log.d("RefStar", "begain");
				}
			});
			
			
		}
			
	
			/**  
		     *  设置小数位数及输入范围的控制     
		     */  
		    InputFilter lengthfilter = new InputFilter() {     
		        public CharSequence filter(CharSequence source, int start, int end,     
		                Spanned dest, int dstart, int dend) {     
		            // 删除等特殊字符，直接返回     
		            if ("".equals(source.toString())) {     
		                return null;     
		            }     
		            String dValue = dest.toString();  
		            //将String类型转化为Double，在一定范围内

		            String[] splitArray = dValue.split("\\.");     
		            if (splitArray.length > 1) {     
		                String dotValue = splitArray[1];     
		                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;     
		                if (diff > 0) {     
		                    return source.subSequence(start, end - diff);     
		                }     
		            }   
		            return null;     
		        }     
		    };
	

			//设置信标频率四位小数位数控制     
		    InputFilter lengthfilterFre = new InputFilter() {     
		        public CharSequence filter(CharSequence source, int start, int end,     
		                Spanned dest, int dstart, int dend) {     
		            // 删除等特殊字符，直接返回     
		            if ("".equals(source.toString())) {     
		                return null;     
		            }     
		            String dValue = dest.toString();     
		            String[] splitArray = dValue.split("\\.");     
		            if (splitArray.length > 1) {     
		                String dotValue = splitArray[1];     
		                int diff = dotValue.length() + 1 - DECIMAL_DIGITS_FRE;     
		                if (diff > 0) {     
		                    return source.subSequence(start, end - diff);     
		                }     
		            }     
		            return null;     
		        }     
		    };
		    
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			
		}
		
		public JSONObject ReturnThreshold()
		{
			JSONObject _value=new JSONObject();

			return _value;
		}
		
		public void ReadThreadHold()
		{
			// TODO Auto-generated method stub
			handler = new Handler(this);
			GetThreadHold thread = new GetThreadHold(handler);
			thread.start();
		}
		
		public class GetThreadHold extends Thread
		{
			private Handler handler;
	        JSONObject json=new JSONObject();
			Bundle bundle = new Bundle();
	        
			public GetThreadHold(Handler handler)
			{
				this.handler = handler;
			}

			@Override
			public void run() 
			{
				Looper.prepare();
				SerialCom newSer=new SerialCom();
				json=newSer.ReadThreshhold();
				Message msg = Message.obtain();
				try {
					if (json.length()==0||"0".equals(json.getString("success")))
					{
						bundle.putString("threshhold", "0.0");	
						
						//Looper.prepare();
						try{
						Toast.makeText(getActivity(), R.string.assert11, Toast.LENGTH_SHORT).show();
						}
						catch(Resources.NotFoundException e)
						{}
						//Looper.loop();					
					}
					else
					{
						try {
							bundle.putString("threshhold", json.getString("threshhold"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg.setData(bundle);
				msg.what = 0;
				handler.sendMessage(msg);
				super.run();
				Looper.loop();
			}
		}
		


		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle bundle=new Bundle();
			switch(msg.what)
			{
			case 0:
				bundle = msg.getData();
				SetEditText(bundle);
				break;
			default:
				break;
			}
			return false;
		}
		
		public void SetEditText(Bundle bundle)
		{
			try
			{
			editText1.setText(bundle.getString("threshhold"));
			}
			catch(Exception e)
			{}
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
	    
	    public void CheckSuccess(JSONObject json)
	    {
			if(json.length()==0)
			{
				try
				{
				Toast.makeText(getActivity(), R.string.assert08, Toast.LENGTH_SHORT).show();
			    }
				catch(Resources.NotFoundException e)
				{}
			}
			else
			{
				try {
					if(json.getString("success").equals("0"))
					{
						try{
						Toast.makeText(getActivity(), R.string.assert09, Toast.LENGTH_SHORT).show();
						}			
						catch(Resources.NotFoundException e)
						{}
					}
					else
					{
						try{
						Toast.makeText(getActivity(), R.string.assert10, Toast.LENGTH_SHORT).show();
						}catch(Resources.NotFoundException e)
						{}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    }
		
	}