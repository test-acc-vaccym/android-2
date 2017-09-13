/*
 * ���òο���
 */

package com.example.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.SerialCom;
import com.example.fragment.AntennaCalibFragment.GetCalibData;
import com.example.fragment.MonitorActivity.BackHandlerInterface;
import com.example.xwwt.MainUI;
import com.example.xwwt.R;

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
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


//������Activity����ʾ��fragment
public class RefStarFragment extends Fragment implements OnFocusChangeListener, TextWatcher, Callback
{
	private View fragmentView;
	private static final int DECIMAL_DIGITS = 1;//���������λ��
	private static final int DECIMAL_DIGITS_FRE = 4;//���������λ��
	private EditText etLongitude,editText2,etFrequancy,editText4,editText5,editText6;
	private static Handler handler=new Handler();
	private Button btnSet,btnAuto,btnStop;
	private static final String[] spinnerValue={"ˮƽ����","��ֱ����","����Բ����","������Բ����"};
	private Spinner spinnerAuto,spinnerRef;//���òο������Զ�Ѱ�ǵ������˵���ʾ
	private ArrayAdapter<String> adapter;
	
	private int polarization;//���弫����ʽѡ��
	
    private boolean mHandledPress = false;
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(RefStarFragment refStarFragment);
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandlerInterface)) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        } else {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        }
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		fragmentView = inflater.inflate(R.layout.refstar_layout,container,false);
	
		//��ʼ������
		initialSet(fragmentView);
		
		ReadData();
		
		return fragmentView;
	}
	
	private void ReadData() 
	{
		// TODO Auto-generated method stub
		handler = new Handler(this);
		// �½�һ���߳�
		GetRefData thread = new GetRefData(handler);
		// ��ʼ�߳�
		thread.start();
	}
	
	public class GetRefData extends Thread
	{
		private Handler handler; // �����handler
        JSONObject json=new JSONObject();
		
		public GetRefData(Handler handler)
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
			json=newSer.ReadReferenceStar();
			// ����һ����Ϣ�����ڷ���UI�̵߳�handler����
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			try {
				if (json.length()==0||"0".equals(json.getString("success")))//û�ж�ȡ������
				{
					bundle.putString("refSatFre", "10.000");
					bundle.putString("refSatLati", "100");
					bundle.putString("polarMode", "0");
					
					Toast.makeText(getActivity(), R.string.assert11, Toast.LENGTH_SHORT).show();
				}
				else
				{
					try {
						bundle.putString("refSatFre", json.getString("refSatFre"));
						bundle.putString("refSatLati", json.getString("refSatLati"));
						bundle.putString("polarMode", json.getString("polarMode"));
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
	
	//�����ʼ�����ã�����������
	public void initialSet(View view)
	{
		//���Ǿ�������С�����1λ����
		etLongitude=(EditText)view.findViewById(R.id.editText01);
		etLongitude.addTextChangedListener(this);  
		etLongitude.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10) });  
		
		//���Ǿ�������С�����1λ����
		//editText2=(EditText)view.findViewById(R.id.editText08);
		//editText2.addTextChangedListener(this);  
		//editText2.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10) });  
		
		//���òο����ű�Ƶ��
		etFrequancy=(EditText)view.findViewById(R.id.editText02);
		etFrequancy.addTextChangedListener(this);
		etFrequancy.setFilters(new InputFilter[] { lengthfilterFre, new InputFilter.LengthFilter(10) });  
		
		//���òο�����ؼ�����ʽ
		//editText4=(EditText)view.findViewById(R.id.editText05);
		//editText4.addTextChangedListener(this);
		spinnerRef=(Spinner)view.findViewById(R.id.spinner01);
		String[] mItems=getResources().getStringArray(R.array.polarizations);
		//��list��adapter������
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mItems);
		//������ʾ��ʽ
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerRef.setAdapter(adapter);
        //����¼�Spinner�¼�����  
		spinnerRef.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub    
                //��ȡѡ���ֵ 
            	if(arg2==0)//ˮƽ����
            	{
            		polarization=0;
            	}
            	else if(arg2==1)//��ֱ����
            	{
            		polarization=1;
            	}
            	else if(arg2==2)//Բ����
            	{
            		polarization=4;
            	}
            	else
            	{
            		polarization=8;//δ֪ACU����
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
		spinnerRef.setVisibility(View.VISIBLE);
		
		//�Զ�Ѱ������ű�Ƶ��
		//editText5=(EditText)view.findViewById(R.id.editText07);
		//editText5.addTextChangedListener(this);
		//�Զ�Ѱ����ؼ�����ʽ
		//editText6=(EditText)view.findViewById(R.id.editText09);
		//editText6.addTextChangedListener(this);
		//spinnerAuto=(Spinner)view.findViewById(R.id.spinner02);
		//spinnerAuto.setAdapter(adapter);
        //����¼�Spinner�¼�����  
//		spinnerAuto.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
//                // TODO Auto-generated method stub    
//                //��ȡѡ���ֵ 
//            	if(arg2==0)//ˮƽ����
//            	{
//            		polarization=0;
//            	}
//            	else if(arg2==1)//��ֱ����
//            	{
//            		polarization=1;
//            	}
//            	else//Բ����
//            	{
//            		polarization=4;
//            	}
//            	
//            	adapter.getItem(arg2);    
//                /* ��mySpinner ��ʾ*/    
//                arg0.setVisibility(View.VISIBLE);    
//            }    
//            public void onNothingSelected(AdapterView<?> arg0) {    
//                // TODO Auto-generated method stub    
//                
//                arg0.setVisibility(View.VISIBLE);    
//            }    
//        }); 
        //����Ĭ��ֵ
		//spinnerAuto.setVisibility(View.VISIBLE);
		
		
		//���òο��ǰ�ť���ָ��
		btnSet=(Button)view.findViewById(R.id.button01);
		btnSet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ж����е������Ƿ����ֵ
				if("".equals(etLongitude.getText().toString())||"".equals(etFrequancy.getText().toString()))
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
					//����λ����������
					//JSONObject json=new JSONObject();
					//String actionName="referenceStar";		
					new Thread()
					{
						@Override
						public void run()
						{

							SerialCom newSer=new SerialCom();
							JSONObject json=newSer.SetReferenceStar(etFrequancy.getText().toString(), etLongitude.getText().toString(), polarization+"");
                            //Toast.makeText(getActivity(), "���òο��ǿ�ʼ������", 1);
							Looper.prepare();
							CheckSuccess(json);
							Looper.loop();
						}
					}.start();
					
					
					//SerialCom newSer=new SerialCom();
					//newSer.SetReferenceStar(editText3.getText().toString(), editText1.getText().toString(), polarization+"");
				}
			}
		});
		
//		//�Զ�Ѱ�ǰ�ť����ָ��
//		btnAuto=(Button)view.findViewById(R.id.Button01);
//		btnAuto.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//�ж����е������Ƿ����ֵ
//				if("".equals(etLongitude.getText().toString())||"".equals(etFrequancy.getText().toString()))
//				{
//					//�����Ի������벻��Ϊ��
//					AlertDialog dialog =new AlertDialog.Builder(getActivity())
//							.setTitle("����")
//							.setMessage("���벻��Ϊ�գ���������")
//							.setPositiveButton("ȷ��",null)
//							.show();
//				}
//				else
//				{
//					//����λ����������
//					//JSONObject json=new JSONObject();
//					//String actionName="referenceStar";
//					new Thread()
//					{
//						@Override
//						public void run()
//						{
//							SerialCom newSer=new SerialCom();
//							newSer.AutoSearch(etFrequancy.getText().toString(),etLongitude.getText().toString() ,polarization+"");	
//							//Toast.makeText(getActivity(), "�Զ�Ѱ�ǿ�ʼ������", 1);
//						}			
//					}.start();
//				}
//			}
//		});
//		
//		//ֹͣѰ��ָ��
//		btnStop=(Button)view.findViewById(R.id.button03);
//		btnStop.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//					//����λ����������
//					//JSONObject json=new JSONObject();
//					//String actionName="referenceStar";	
//				
////				btnSet.setVisibility(INVISIBLE);
////				btnAuto.setVisibility(INVISIBLE);
//				
//				new Thread()
//				{
//					@Override
//					public void run()
//					{
//						SerialCom newSer=new SerialCom();
//						newSer.StopSearch();
//					//Log.d("Stop RefStar", "begain");
//						//Toast.makeText(getActivity(), "ֹͣѰ��", 1);
//					}
//					
//				}.start();
//				//Log.d("RefStar", "begain");
//			}
//		});
	}
	

	//�������Ǿ���һλС��λ������     
    InputFilter lengthfilter = new InputFilter() {     
        public CharSequence filter(CharSequence source, int start, int end,     
                Spanned dest, int dstart, int dend) {     
            // ɾ���������ַ���ֱ�ӷ���     
            if ("".equals(source.toString())) {     
                return null;     
            }     
            String dValue = dest.toString();     
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
    
    
	//�����ű�Ƶ����λС��λ������     
    InputFilter lengthfilterFre = new InputFilter() {     
        public CharSequence filter(CharSequence source, int start, int end,     
                Spanned dest, int dstart, int dend) {     
            // ɾ���������ַ���ֱ�ӷ���     
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
	
	//����back��Ӧ
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
	            Log.i("setFragment", "back �¼�");
	    		Intent aIntent = new Intent();
	    		aIntent.setClass(getActivity(),MainUI.class);
	    		startActivity(aIntent);
			    
	            return true;
	        }
	        return false;
	    }

		public void SetValue(Bundle bundle)
		{
			try{
			etFrequancy.setText(bundle.getString("refSatFre"));
			etLongitude.setText(bundle.getString("refSatLati"));
			//�ٶ�spinner�ؼ����и�ֵ����			
			spinnerRef.setSelection(Integer.parseInt(bundle.getString("polarMode")));//
			}catch (Exception e)
			{}
		}
	    
	    
	    
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle bundle =new Bundle();
			switch (msg.what)
			{
				case 0:
					bundle = msg.getData();
					SetValue(bundle);
				default:
					break;
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