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


//������Activity����ʾ��fragment
public class AntennaCalibFragment extends Fragment implements Callback
{
	private View fragmentView;
	private static Handler handler=new Handler();
	private EditText etAzimuth,etPitch,etEmit,etReceive;
	private Button btnCalib;
    private static final int DECIMAL_DIGITS = 1;//���������λ��
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
		// �½�һ���߳�
		GetCalibData thread = new GetCalibData(handler);
		// ��ʼ�߳�
		thread.start();
	}

	public class GetCalibData extends Thread
	{

		private Handler handler; // �����handler
        JSONObject json=new JSONObject();
		
		public GetCalibData(Handler handler)
		{
			this.handler = handler;
		}

		@Override
		public void run() // �̴߳��������
		{
			//��ѯ��������
			Looper.prepare();
			SerialCom newSer=new SerialCom();
			//����ʱ�䣬����ʱ��û����������ʾҳ�棿���߷��������ӳɹ�����Ҫ�˲���
			json=newSer.ReadAntennaCalib();
			// ����һ����Ϣ�����ڷ���UI�̵߳�handler����
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			try {
				if (json.length()==0||"0".equals(json.getString("success")))//û�ж�ȡ������
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
			
			// ����ѯ�Ľ���Ž�msg��
			//bundle.putString("answer", a);
			msg.setData(bundle);
			// �������msg�ı�ʶ������UI�е�handler���ܸ���������Ķ�Ӧ��UI
			msg.what = 0;
			// ����Ϣ���͸�UI�е�handler����
			handler.sendMessage(msg);
			//handler.sendEmptyMessage(0);
			super.run();
			Looper.loop();
		}
	}
	
	//��׿�ؼ���ʼ��
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
							//�����Ի������벻��Ϊ��
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
            // ɾ���������ַ���ֱ�ӷ���     
            if ("".equals(source.toString())) {     
                return null;     
            }     
            String dValue = dest.toString();  
            //��String����ת��ΪDouble����һ����Χ��

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
	 * ��edittext�ؼ���������
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
     * ��ȡ���߱궨����
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
			retBundle.putString("azimuth", json.getString("azimuth"));//�����ص�ֵ�洢��Bundle��
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