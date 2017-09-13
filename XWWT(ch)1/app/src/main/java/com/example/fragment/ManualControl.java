package com.example.fragment;

import com.example.xwwt.MainUI;
import com.example.xwwt.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.Dao;
import com.example.CommonFunction.EditTextLocker;
import com.example.CommonFunction.PHPOperator;
import com.example.CommonFunction.SerialCom;
import com.example.fragment.ReadThreshold.BackHandlerInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/*
 * ������ҳѡ���࣬��Ҫ�����ǽ����ֶ����ƣ����Զ���ӿ�
 */
public class ManualControl extends Fragment
{
	private View fragmentView;
	private static final int DECIMAL_DIGITS = 1;//���������λ��
	private EditText editText1,editText2,editText3,editText4,editText5;
	private Button btnVel,btnPos;//�ֶ�λ�����ֶ��ٶȿ��ư�ť
	private static final String[] spinnerValue={"Azimuth Increase","Azimuth Decrease","Pitch Increase","Pitch Decrease","Transmit Increase","Transmit Decrease","Receive Increase","Receive Decrease"};
	private Spinner spinnerAlt;//���õ�����ʽspinner
	private ArrayAdapter<String> adapter;
	private int angleVelocity;//���ٶ�ѡ��ֵ
	
    private boolean mHandledPress = false;
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(ManualControl manualFragment);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	   super.onCreate(savedInstanceState);
	}
    
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		fragmentView = inflater.inflate(R.layout.maunal_layout,container,false);
	
		//��ʼ����ͼ����
		initalView(fragmentView);
		
		return fragmentView;
	}
	
	/*
	 * ��ʼ����ͼ����
	 */
	public void initalView(View view)
	{
		////////////////////EditText5���ٶȿ���ָ��ķ�Χ
		editText5=(EditText)view.findViewById(R.id.editText04);//�ֶ��ٶȵĽ��ٶ�
		editText5.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String numberSpan; 
				if(s!=null&&!s.equals(""))
				{
					if(0!=-1&&19.9!=-1)
					{
						Double toWrite=0.0;
						try
						{
							toWrite=Double.parseDouble(s.toString());			
						}
						catch(NumberFormatException e)
						{
							toWrite=0.0;
						}
						if(toWrite>19.9)
						{
							editText5.setText("19.9");
						}
						else if(toWrite<0)
						{
							editText5.setText("0.0");
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
		
		editText5.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10)
	});
		/////////////////////////////////���ٶȿ��Ʒ�Χ
		//��list��adapter������
		String[] mItems=getResources().getStringArray(R.array.alter);
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mItems);
		//������ʾ��ʽ
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAlt=(Spinner)view.findViewById(R.id.spinner02);
		spinnerAlt.setAdapter(adapter);
        //����¼�Spinner�¼�����  
		spinnerAlt.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub    
                //��ȡѡ���ֵ 
				switch (arg2) {
				case 0:
					angleVelocity = 1;
					break;
				case 1:
					angleVelocity = 2;
					break;
				case 2:
					angleVelocity = 3;
					break;
				case 3:
					angleVelocity = 4;
					break;
				case 4:
					angleVelocity = 5;
					break;
				case 5:
					angleVelocity = 6;
					break;
				case 6:
					angleVelocity = 7;
					break;
				case 7:
					angleVelocity = 8;
					break;
				default:
					break;
				}      	
            	adapter.getItem(arg2);    
                /* ��mySpinner ��ʾ*/    
                arg0.setVisibility(View.VISIBLE);    
            }    
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub    
                
                arg0.setVisibility(View.VISIBLE);    
            }    
        }); 
        //����Ĭ��ֵ
		spinnerAlt.setVisibility(View.VISIBLE);
		
		btnVel=(Button)view.findViewById(R.id.button01);
		btnVel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ж����е������Ƿ����ֵ
				if("".equals(editText5.getText().toString()))
				{
					//�����Ի������벻��Ϊ��
					AlertDialog dialog =new AlertDialog.Builder(getActivity())
							.setTitle(R.string.alert)
							.setMessage(R.string.assert06)
							.setPositiveButton("OK",null)
							.show();
				}
				else
				{
					//����λ����������
					//JSONObject json=new JSONObject();
					//String actionName="referenceStar";	
					new Thread()
					{
						@Override
						public void run()
						{
							SerialCom newSer=new SerialCom();
							JSONObject json=newSer.ManualSearch(angleVelocity+"", editText5.getText().toString());
							
							Looper.prepare();
							CheckSuccess(json);
							Looper.loop();	
						}
					}.start();
				}
			}
		});
		
		///////////////////////////////
		editText1=(EditText)view.findViewById(R.id.editText01);//��λ��
		EditTextLocker decimalEditTextLockerAzimuth = new EditTextLocker(editText1,0.0,360.0);
		decimalEditTextLockerAzimuth.limitFractionDigitsinDecimal(4);
		
		editText2=(EditText)view.findViewById(R.id.editText02);//������
		EditTextLocker decimalEditTextLockerPitch = new EditTextLocker(editText2,-90.0,180.0);
		decimalEditTextLockerPitch.limitFractionDigitsinDecimal(4);

		editText3=(EditText)view.findViewById(R.id.editText03);//���伫��
		EditTextLocker decimalEditTextLockerEmit = new EditTextLocker(editText3,0.0,360.0);
		decimalEditTextLockerEmit.limitFractionDigitsinDecimal(4);

		editText4=(EditText)view.findViewById(R.id.editText4);//���ռ���
		EditTextLocker decimalEditTextLockerReceive = new EditTextLocker(editText4,0.0,360.0);
		decimalEditTextLockerReceive.limitFractionDigitsinDecimal(4);

		btnPos=(Button)view.findViewById(R.id.button02);
		btnPos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ж����е������Ƿ����ֵ
				if("".equals(editText1.getText().toString())||"".equals(editText2.getText().toString())||"".equals(editText3.getText().toString())||"".equals(editText4.getText().toString()))
				{
					//�����Ի������벻��Ϊ��
					AlertDialog dialog =new AlertDialog.Builder(getActivity())
							.setTitle("Alert")
							.setMessage(R.string.assert06)
							.setPositiveButton("OK",null)
							.show();;
				}
				else
				{
					//����λ������λ�ÿ�������
					//JSONObject json=new JSONObject();
					//String actionName="referenceStar";	
					new Thread()
					{
						@Override
						public void run()
						{
							SerialCom newSer=new SerialCom();
							JSONObject json=newSer.ManualControl(editText1.getText().toString(), editText2.getText().toString(), editText3.getText().toString(), editText4.getText().toString());
							Looper.prepare();
							CheckSuccess(json);
							Looper.loop();	
						
						}
					}.start();
				}
			}
		});
	}
	
	
	//
	/**  
     *  ����С��λ�������뷶Χ�Ŀ���     
     */  
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