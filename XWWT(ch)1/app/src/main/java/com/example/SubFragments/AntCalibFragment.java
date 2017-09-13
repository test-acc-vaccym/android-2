package com.example.SubFragments;

import com.example.CommonFunction.SerialCom;
import com.example.fragment.HelpFragment;
import com.example.fragment.HelpFragment.BackHandlerInterface;
import com.example.xwwt.MainUI;
import com.example.xwwt.R;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * ��TabHost����ʾ�����߽Ƕ���Ϣ,����SUBFRAGMENT
 * @author lei
 *
 */
public class AntCalibFragment extends Fragment
{

		private View fragmentView;
		private Button btnCalib;
		private EditText editText1,editText2,editText3,editText4;
		
		private String ed1,ed2,ed3,ed4;
		
		Bundle bundle =new Bundle();
		
	    private boolean mHandledPress = false;
	    private static final int DECIMAL_DIGITS = 1;//���������λ��
	    protected BackHandlerInterface backHandlerInterface;

	    public interface BackHandlerInterface {
	        public void setSelectedFragment(AntCalibFragment acFragment);
	    }
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{  
			super.onCreate(savedInstanceState);
			bundle=this.getArguments();
		}
		
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.antcalib_layout,container,false);
		    //initial view
			initialView(fragmentView);
			return fragmentView;
		}

		//�Խ�����в����Ļ�������
		public void initialView(View view)
		{
			ed1=bundle.getString("azimuth");
			ed2=bundle.getString("pitch");
			ed3=bundle.getString("rollEmit");
			ed4=bundle.getString("rollReceive");
			
			editText1=(EditText)view.findViewById(R.id.editText01);//�����
			editText1.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String numberSpan; 
					if(s!=null&&!s.equals(""))
					{
						if(0!=-1&&360!=-1)
						{
							Double toWrite=0.0;
							try
							{
								toWrite=Double.parseDouble(s.toString());	
								editText1.setText(toWrite+"");
							}
							catch(NumberFormatException e)
							{
								toWrite=0.0;
							}
							if(toWrite>360)
							{
								editText1.setText("360");
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
			editText2=(EditText)view.findViewById(R.id.editText02);//������
			editText2.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String numberSpan; 
					if(s!=null&&!s.equals(""))
					{
						if(-90!=-1&&180!=-1)
						{
							Double toWrite=0.0;
							try
							{
								toWrite=Double.parseDouble(s.toString());	
								editText2.setText(toWrite+"");
							}
							catch(NumberFormatException e)
							{
								toWrite=0.0;
							}
							if(toWrite>180)
							{
								editText2.setText("180");
							}
							else if(toWrite<-90)
							{
								editText2.setText("-90.0");
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
			editText2.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10)
		});
			editText3=(EditText)view.findViewById(R.id.editText04);//���伫��
			editText3.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String numberSpan; 
					if(s!=null&&!s.equals(""))
					{
						if(0!=-1&&360!=-1)
						{
							Double toWrite=0.0;
							try
							{
								toWrite=Double.parseDouble(s.toString());	
								editText3.setText(toWrite+"");
							}
							catch(NumberFormatException e)
							{
								toWrite=0.0;
							}
							if(toWrite>360)
							{
								editText3.setText("360");
							}
							else if(toWrite<0)
							{
								editText3.setText("0.0");
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
			editText3.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10)
			});
			
			editText4=(EditText)view.findViewById(R.id.editText05);//���ռ���
			editText4.addTextChangedListener(new TextWatcher()
			{
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					String numberSpan; 
					if(s!=null&&!s.equals(""))
					{
						if(0!=-1&&360!=-1)
						{
							Double toWrite=0.0;
							try
							{
								toWrite=Double.parseDouble(s.toString());	
								editText4.setText(toWrite+"");
							}
							catch(NumberFormatException e)
							{
								toWrite=0.0;
							}
							if(toWrite>360)
							{
								editText4.setText("360");
							}
							else if(toWrite<0)
							{
								editText4.setText("0.0");
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
		    
			editText4.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10)
			});
			
			editText1.setText(ed1);
			editText2.setText(ed2);
			editText3.setText(ed3);
			editText4.setText(ed4);
			
			btnCalib=(Button)view.findViewById(R.id.button01);
			btnCalib.setOnClickListener(new View.OnClickListener() {
				
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
								.show();
					}
					else
					{
						//����λ������λ�ÿ�������
						//JSONObject json=new JSONObject();
						//String actionName="referenceStar";					
						SerialCom newSer=new SerialCom();
						newSer.ManualControl(editText1+"", editText2+"", editText3+"", editText4+"");
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
		
}