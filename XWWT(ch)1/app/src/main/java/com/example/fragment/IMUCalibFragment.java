package com.example.fragment;

/*
 * 
 */



import com.example.xwwt.MainUI;
import com.example.xwwt.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.EditTextLocker;
import com.example.CommonFunction.SerialCom;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


//定义在Activity中显示的fragment
public class IMUCalibFragment extends Fragment implements Callback
{
	private static Context mContext; 
	private View fragmentView;
	private static Handler handler=new Handler();
	private EditText etPitch,etYaw,etRoll;
	private Button btnCalib;
	
	private String pitch;
	private String yaw;
	private String roll;
	
	private boolean pitchFlag=false;
	private boolean yawFlag=false;
	private boolean rollFlag=false;
	
    private boolean mHandledPress = false;
    private static final int DECIMAL_DIGITS = 2;//设置输入的位数
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(IMUCalibFragment imuFragment);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.imucalib_fragment, null);

		initView(view);
		
		GetIMUCalibData();
		return view;
	}

	//在TabHost中添加需要显示的子Fragment
	private void initView(View view) 
	{
		etPitch=(EditText)view.findViewById(R.id.editText01);
		
		EditTextLocker decimalEditTextLockerPitch = new EditTextLocker(etPitch,-5.0,5.0);
		decimalEditTextLockerPitch.limitFractionDigitsinDecimal(2);
//		etPitch.addTextChangedListener(new TextWatcher()
//		{
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub 
//				Double toWrite=0.0;
//				if(s.toString()!=null&&!"".equals(s.toString()))
//				{
//					if(-5!=-1&&5!=-1)
//					{		
//						if(pitchFlag)
//						{
//							pitchFlag=false;
//						    toWrite=Double.parseDouble(s.toString().substring(0,s.length()-1));
//						    pitch=s.toString().substring(0,s.length()-1);
//						}
//						else
//						{
//							toWrite=Double.parseDouble(s.toString());
//							pitch=s.toString().trim();
//						}
//						if(toWrite>5)
//						{
//							//etPitch.setText("5.00");
//							//toWrite=5.00;
//							pitch="5.00";
//						}
//						else if(toWrite<-5)
//						{
//							//etPitch.setText("-5.00");
//							//toWrite=-5.00;
//							pitch="-5.00";
//						}
//					}
//					
//				}
//				else
//				{
//					pitch="";
//				}
//				etPitch.removeTextChangedListener(this);
//				etPitch.setText(pitch);
//				etPitch.setSelection(pitch.length());
//				etPitch.addTextChangedListener(this);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				if(s.toString().indexOf(".")>=0)
//				{
//					if(s.toString().indexOf(".", s.toString().indexOf(".")+1)>0)
//					{
//						//pitch=s.toString().substring(0,s.length()-1);
//						pitchFlag=true;
//					}
//					else
//					{
//						//pitch=s.toString().trim();
//					}
//					
//				}
//			}
//		});
//		//etPitch.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10)});
		
		etYaw=(EditText)view.findViewById(R.id.editText04);
		EditTextLocker decimalEditTextLockerYaw = new EditTextLocker(etYaw,-5.0,5.0);
		decimalEditTextLockerYaw.limitFractionDigitsinDecimal(2);
		
//		etYaw.addTextChangedListener(new TextWatcher()
//		{
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				String numberSpan; 
//				Double toWrite=0.0;
//				if(s.toString()!=null&&!"".equals(s.toString()))
//				{
//					if(-5!=-1&&5!=-1)
//					{						
//						//Double toWrite=Double.parseDouble(s.toString());
//
//						if(yawFlag)
//						{
//							yawFlag=false;
//						    toWrite=Double.parseDouble(s.toString().substring(0,s.length()-1));
//						    yaw=s.toString().substring(0,s.length()-1);
//						}
//						else
//						{
//							toWrite=Double.parseDouble(s.toString());
//							yaw=s.toString().trim();
//						}
//						if(toWrite>5)
//						{
//							//etYaw.setText("5.00");
//							yaw="5.00";
//						}
//						else if(toWrite<-5)
//						{
//							//etYaw.setText("-5.00");
//							yaw="-5.00";
//						}
//				}
//			}
//				else
//				{
//					yaw="";
//				}
//				etYaw.removeTextChangedListener(this);
//				etYaw.setText(yaw);
//				etYaw.setSelection(yaw.length());
//				etYaw.addTextChangedListener(this);
//				
//		}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				if(s.toString().indexOf(".")>=0)
//				{
//					if(s.toString().indexOf(".", s.toString().indexOf(".")+1)>0)
//					{
//						//pitch=s.toString().substring(0,s.length()-1);
//						yawFlag=true;
//					}
//					else
//					{
//						//pitch=s.toString().trim();
//					}
//					
//				}
//			}
//		});
		//etYaw.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10)});
		
		etRoll=(EditText)view.findViewById(R.id.editText02);
		EditTextLocker decimalEditTextLockerRoll = new EditTextLocker(etRoll,-5.0,5.0);
		decimalEditTextLockerRoll.limitFractionDigitsinDecimal(2);
		
//		etRoll.addTextChangedListener(new TextWatcher()
//		{
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				String numberSpan; 
//				Double toWrite=0.0;
//				if(s.toString()!=null&&!"".equals(s.toString()))
//				{
//					if(-5!=-1&&5!=-1)
//					{						
//						//Double toWrite=Double.parseDouble(s.toString());
//
//						if(rollFlag)
//						{
//							rollFlag=false;
//						    toWrite=Double.parseDouble(s.toString().substring(0,s.length()-1));
//						    roll=s.toString().substring(0,s.length()-1);
//						}
//						else
//						{
//							toWrite=Double.parseDouble(s.toString());
//							roll=s.toString().trim();
//						}
//						
//						if(toWrite>5)
//						{
//							//etRoll.setText("5.00");
//							roll="5.00";
//						}
//						else if(toWrite<-5)
//						{
//							//etRoll.setText("-5.00");
//							roll="-5.00";
//						}
//					}
//				}
//				else
//				{
//					roll="";
//				}
//				etRoll.removeTextChangedListener(this);
//				etRoll.setText(roll);
//				etRoll.setSelection(roll.length());
//				etRoll.addTextChangedListener(this);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				if(s.toString().indexOf(".")>=0)
//				{
//					if(s.toString().indexOf(".", s.toString().indexOf(".")+1)>0)
//					{
//						//pitch=s.toString().substring(0,s.length()-1);
//						rollFlag=true;
//					}
//					else
//					{
//						//pitch=s.toString().trim();
//					}
//					
//				}
//				
//			}
//		});
		//etRoll.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10)});
		
		btnCalib=(Button)view.findViewById(R.id.button01);
		btnCalib.setOnClickListener(new OnClickListener()	
		{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if("".equals(etPitch.getText().toString())||"".equals(etYaw.getText().toString())
								||"".equals(etRoll.getText().toString()))
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
						new Thread()
						{
							@Override
							public void run()
							{
								SerialCom newSer=new SerialCom();
								JSONObject json=newSer.IMUCalib(etPitch.getText().toString(), etYaw.getText().toString(),
										etRoll.getText().toString());
								Looper.prepare();
								CheckSuccess(json);
								Looper.loop();
								
							}
						}.start();
						}
					}
			
		});

	}


	//需要更改
	public Bundle GetIMUCalib()
	{
		Bundle retBundle=new Bundle();
		
		JSONObject json=new JSONObject();
		SerialCom sc=new SerialCom();
		json=sc.ReadIMUCalib();
		
		try
		{
			retBundle.putString("heading", json.getString("heading"));
			retBundle.putString("pitch", json.getString("pitch"));
			retBundle.putString("roll", json.getString("roll"));
		}
		catch(JSONException e)
		{
			
		}
		
		return retBundle;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		Bundle bundle =new Bundle();
		switch(msg.what)
		{
		case 0:
			bundle=msg.getData();
			SetEditText(bundle);
			break;
		default:
			break;
		}
		return false;
	}
	
	public void SetEditText(Bundle bundle)
	{
		try{
		etPitch.setText(bundle.getString("heading"));
		etYaw.setText(bundle.getString("pitch"));
		etRoll.setText(bundle.getString("roll"));}
		catch(Exception e)
		{}
	}
	
	//创建读取惯导标定值得线程
	public void GetIMUCalibData()
	{
		handler = new Handler(this);
		IMUCalibThread thread = new IMUCalibThread(handler);
		thread.start();
	}
	public class IMUCalibThread extends Thread
	{

		private Handler handler; 
        JSONObject json=new JSONObject();
		
		public IMUCalibThread(Handler handler)
		{
			this.handler = handler;
		}

		@Override
		public void run() 
		{
			Looper.prepare();
			SerialCom newSer=new SerialCom();
			json=newSer.ReadIMUCalib();
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			try {
				if (json.length()==0||"0".equals(json.getString("success")))//没有读取到数据
				{
					bundle.putString("heading", "0.0");
					bundle.putString("pitch", "0.0");
					bundle.putString("roll", "0.0");
					try
					{
					Toast.makeText(getActivity(), R.string.assert11, Toast.LENGTH_SHORT).show();
					}catch(Resources.NotFoundException e)
					{}
					}
				else
				{
					try {
						bundle.putString("heading", json.getString("heading"));
						bundle.putString("pitch", json.getString("pitch"));
						bundle.putString("roll", json.getString("roll"));
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