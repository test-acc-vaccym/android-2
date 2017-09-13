package com.example.fragment;

/*
 * 
 */

import com.example.xwwt.MainUI;
import com.example.xwwt.R;
import com.example.xwwt.R.id;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.EditTextLocker;
import com.example.CommonFunction.SerialCom;
import com.example.SubFragments.AngleFragment;
import com.example.SubFragments.AntCalibFragment;
import com.example.SubFragments.InertialFragment;
import com.example.SubFragments.OtherView;
import com.example.SubFragments.PositionFragment;
import com.example.SubFragments.ReadAntCalibFragment;
import com.example.fragment.IMUCalibFragment.BackHandlerInterface;
import com.example.fragment.MonitorActivity.GetSerialThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
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
public class AntennaCalibFragment extends Fragment implements Callback
{
	private View fragmentView;
	private static Handler handler=new Handler();
	private EditText etAzimuth,etPitch,etEmit,etReceive;
	private Button btnCalib;
    private static final int DECIMAL_DIGITS = 1;//设置输入的位数
	private String actionName="antannaCalib";
	
	private boolean mHandledPress = false;
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(AntennaCalibFragment acFragment);
    }
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	   super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//T.showShort(getActivity(), "FragmentMessage==onCreateView");
		View view = inflater.inflate(R.layout.antenna_fragment, null);

		initView(view);
		ReadData();
		return view;
	}

	private void ReadData() 
	{
		// TODO Auto-generated method stub
		handler = new Handler(this);
		// 新建一个线程
		GetCalibData thread = new GetCalibData(handler);
		// 开始线程
		thread.start();
	}

	public class GetCalibData extends Thread
	{

		private Handler handler; // 传入的handler
        JSONObject json=new JSONObject();
		
		public GetCalibData(Handler handler)
		{
			this.handler = handler;
		}

		@Override
		public void run() // 线程处理的内容
		{
			//查询串口数据
			Looper.prepare();
			SerialCom newSer=new SerialCom();
			//定义时间，超出时间没有数据则显示页面？或者服务器连接成功则不需要此操作
			json=newSer.ReadAntennaCalib();
			// 定义一个消息，用于发给UI线程的handler处理
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			try {
				if (json.length()==0||"0".equals(json.getString("success")))//没有读取到数据
				{
					bundle.putString("azimuth", "5.0");
					bundle.putString("pitch", "5.0");
					bundle.putString("rollEmit", "5.0");
					bundle.putString("rollReceive", "5.0");
					try
					{
					Toast.makeText(getActivity(), R.string.assert11, Toast.LENGTH_SHORT).show();
					}
					catch(Resources.NotFoundException e)
					{
						
					}
				}
				else
				{
					try {
						bundle.putString("azimuth", json.getString("azimuth"));
						bundle.putString("pitch", json.getString("pitch"));
						bundle.putString("rollEmit", json.getString("rollEmit"));
						bundle.putString("rollReceive", json.getString("rollReceive"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// 将查询的结果放进msg中
			//bundle.putString("answer", a);
			msg.setData(bundle);
			// 设置这个msg的标识，这样UI中的handler才能根据这个更改对应的UI
			msg.what = 0;
			// 将消息发送给UI中的handler处理
			handler.sendMessage(msg);
			//handler.sendEmptyMessage(0);
			super.run();
			Looper.loop();
		}
	}
	
	//安卓控件初始化
	private void initView(View view) 
	{

		etAzimuth=(EditText)view.findViewById(R.id.editText01);
		EditTextLocker decimalEditTextLockerAzimuth = new EditTextLocker(etAzimuth,-180.0,180.0);
		decimalEditTextLockerAzimuth.limitFractionDigitsinDecimal(1);

		
		etPitch=(EditText)view.findViewById(R.id.editText02);
		EditTextLocker decimalEditTextLockerPitch = new EditTextLocker(etPitch,-10.0,10.0);
		decimalEditTextLockerPitch.limitFractionDigitsinDecimal(1);

		
		etEmit=(EditText)view.findViewById(R.id.editText04);
		EditTextLocker decimalEditTextLockerEmit = new EditTextLocker(etEmit,-10.0,10.0);
		decimalEditTextLockerEmit.limitFractionDigitsinDecimal(1);

		
		etReceive=(EditText)view.findViewById(R.id.editText05);
		EditTextLocker decimalEditTextLockerReceive = new EditTextLocker(etReceive,-10.0,10.0);
		decimalEditTextLockerReceive.limitFractionDigitsinDecimal(1);

		
		
		btnCalib=(Button)view.findViewById(R.id.button_calib);
		btnCalib.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if("".equals(etAzimuth.getText().toString())||"".equals(etPitch.getText().toString())
								||"".equals(etEmit.getText().toString())||"".equals(etReceive.getText().toString()))
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
								JSONObject json=newSer.AntennaCalib(etAzimuth.getText().toString(), etPitch.getText().toString(),
										etEmit.getText().toString(), etReceive.getText().toString());
								Looper.prepare();
								CheckSuccess(json);
								Looper.loop();
							}
						}.start();
						}
					}
			
				});
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
	
	/**
	 * 对edittext控件进行设置
	 * @param bundle
	 */
	public void SetEditText(Bundle bundle)
	{
		etAzimuth.setText(bundle.getString("azimuth"));
		etPitch.setText(bundle.getString("pitch"));
		etEmit.setText(bundle.getString("rollEmit"));
		etReceive.setText(bundle.getString("rollReceive"));
	}

	

	
	 @Override
	public boolean handleMessage(Message msg)
	{
		Bundle bundle =new Bundle();
		switch (msg.what)
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
	
	 
    
    /**
     * 读取天线标定数据
     * @return
     */
    public Bundle GetACValue()
    {
    	Bundle retBundle=new Bundle();
    	JSONObject json=new JSONObject();
		SerialCom newSer=new SerialCom();
		json=newSer.ReadAntennaCalib();
		try
		{
			retBundle.putString("azimuth", json.getString("azimuth"));//将返回的值存储在Bundle中
			retBundle.putString("pitch", json.getString("pitch"));
			retBundle.putString("rollEmit",json.getString("rollEmit"));
			retBundle.putString("rollReceive",json.getString("rollReceive"));
		}
		catch(JSONException e)
		{
			
		}
    	
    	return retBundle;
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