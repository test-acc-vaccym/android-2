package com.example.fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.example.CommonFunction.Data;
import com.example.CommonFunction.SerialCom;
import com.example.CommonFunction.ServerIP;
import com.example.fragment.MonitorActivity.BackHandlerInterface;
import com.example.xwwt.Login_Activity;
import com.example.xwwt.MainUI;
import com.example.xwwt.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
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
 * �Է������������õȲ���
 */
public class SetFragment extends Fragment
{

	private View fragmentView;
	private EditText editText1,editText2,editText3,editText4,editText5;
	
	private EditText etip1,etip2,etip3,etip4;
	
	private EditText etms1,etms2,etms3,etms4;
	
	private EditText etgw1,etgw2,etgw3,etgw4;
	
	private EditText etll1,etll2;
	
	private Spinner spMode;
	private ArrayAdapter<String> adapter;
	private int control;//������Ʒ�ʽ
	
	
	private Button btnSet;//����IP��ַ
	private Button btnSetNet;//��������
	private Button btnSetCtl;//���ÿ��Ʒ�ʽ
	private Button btnSetLL;//���þ�γ��
	
	
	private String firstIP;
	private String secondIP;
	private String thirdIP;
	private String fourthIP;
	
	
	private String firstNetIP;
	private String secondNetIP;
	private String thirdNetIP;
	private String fourthNetIP;
	
	private String firstNetMs;
	private String secondNetMs;
	private String thirdNetMs;
	private String fourthNetMs;
	
	private String firstNetGw;
	private String secondNetGw;
	private String thirdNetGw;
	private String fourthNetGw;

    private boolean mHandledPress = false;
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(SetFragment setFragment);
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
		
		fragmentView = inflater.inflate(R.layout.set_fragment,container,false);
	
		//����Ѱ�����޳�ʼ����ͼ
		initialView(fragmentView);
		return fragmentView;
	}
	
	/*
	 * ��ʼ����ͼ
	 */
	public void initialView(View view)
	{
		editText1=(EditText)view.findViewById(R.id.editText01);//�˿ں�

		editText2=(EditText)view.findViewById(R.id.editText02);//IP1
		editText3=(EditText)view.findViewById(R.id.editText03);//IP2
		editText4=(EditText)view.findViewById(R.id.editText04);//IP3
		editText5=(EditText)view.findViewById(R.id.editText05);//IP4
		
		editText2.addTextChangedListener(new TextWatcher() 
		{
			              @Override
			              public void onTextChanged(CharSequence s, int start, int before, int count) 
			              {
			                  // TODO Auto-generated method stub
			                  if(null!=s && s.length()>0)
			                  {
			                      if(s.length() > 2 || s.toString().trim().contains("."))
			                      {
			                          if(s.toString().trim().contains("."))
			                          {
			                              firstIP = s.toString().trim().substring(0,s.length()-1);
			                          }
			                          else
			                          {
			                        	  firstIP = s.toString().trim();
			                          }
			                          editText3.setFocusable(true);
			                          editText3.requestFocus();
			                      }
			                      else
			                      {
			                          firstIP = s.toString().trim();
			                      }
			                  }
			             }
			              @Override
			              public void beforeTextChanged(CharSequence s, int start, int count,
			                      int after) {
			                  // TODO Auto-generated method stub
			              }
			              @Override
			              public void afterTextChanged(Editable s) {
			                  // TODO Auto-generated method stub
			           
			  				if(s.toString()!=null&&!s.toString().equals(""))
							{
								Double toWrite=Double.parseDouble(s.toString());
								if(toWrite>255)
								{
									firstIP="255";
								}
								else if(toWrite<=0)
								{
									firstIP="0";
								}
							}
			  				else
			  				{
			  					firstIP="";
			  				}
			  				
			  				if(firstIP.indexOf('0')==0)//����ĵ�һλ����Ϊ0
			  				{
			  					firstIP="0";
			  				}
			            	  editText2.removeTextChangedListener(this);
			            	  editText2.setText(firstIP);
			            	  editText2.setSelection(firstIP.length());
			            	  editText2.addTextChangedListener(this);
			              }
			          });
		  
		editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                            secondIP = s.toString().trim().substring(0,s.length()-1);
                        }else{
                        	secondIP = s.toString().trim();
                        }
                        editText4.setFocusable(true);
                        editText4.requestFocus();
                    }
                    else
                    {
                    	secondIP = s.toString().trim();
                    }
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
  				if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						secondIP="255";
					}
					else if(toWrite<=0)
					{
						secondIP="0";
					}
				}
  				else
  				{
  					secondIP="";
  				}
  				if(secondIP.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					secondIP="0";
  				}
					editText3.removeTextChangedListener(this);
					editText3.setText(secondIP);
					editText3.setSelection(secondIP.length());
					editText3.addTextChangedListener(this);
            }
        });
		
		editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                        	thirdIP = s.toString().trim().substring(0,s.length()-1);
                        }else{
                        	thirdIP = s.toString().trim();
                        }
                        editText5.setFocusable(true);
                        editText5.requestFocus();
                    }
                    else
                    {
                    	thirdIP = s.toString().trim();
                    }
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            	if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						thirdIP="255";
					}
					else if(toWrite<=0)
					{
						thirdIP="0";
					}
				}
            	else
            	{
            		thirdIP="";
            	}
  				if(thirdIP.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					thirdIP="0";
  				}
					editText4.removeTextChangedListener(this);
					editText4.setText(thirdIP);
					editText4.setSelection(thirdIP.length());
					editText4.addTextChangedListener(this);
            }
        }); 

		editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                	fourthIP = s.toString().trim();
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            	if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						fourthIP="255";
					}
					else if(toWrite<=0)
					{
						fourthIP="0";
					}
				}
            	else
            	{
            		fourthIP="";
            	}
  				if(fourthIP.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					fourthIP="0";
  				}
					editText5.removeTextChangedListener(this);
					editText5.setText(fourthIP);
					editText5.setSelection(fourthIP.length());
					editText5.addTextChangedListener(this);
            }
        }); 
		
				
		btnSet=(Button)view.findViewById(R.id.Button01);
		btnSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if("".equals(editText2.getText().toString())||"".equals(editText3.getText().toString())
						||"".equals(editText4.getText().toString())||"".equals(editText5.getText().toString()))
				{
					AlertDialog dialog =new AlertDialog.Builder(getActivity())
							.setTitle("Alert")
							.setMessage(R.string.assert06)
							.setPositiveButton("OK",null)
							.show();
				}
				else
				{
		    		ServerIP newIP=new ServerIP(getActivity());
		    		try {
						newIP.SetIP(editText2.getText().toString()+"."+editText3.getText().toString()+"."+editText4.getText().toString()
								+"."+editText5.getText().toString(), editText1.getText().toString());//��IP����ƴ��
			    		Data.SetIP(editText2.getText().toString()+"."+editText3.getText().toString()+"."+editText4.getText().toString()
								+"."+editText5.getText().toString());  
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						Toast.makeText(getActivity(), R.string.assert09, Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					};	    		
				}
			}
		});
		
		//����Ϊ���������������
		
		etip1=(EditText)view.findViewById(R.id.etip1);
		etip2=(EditText)view.findViewById(R.id.etip2);
		etip3=(EditText)view.findViewById(R.id.etip3);
		etip4=(EditText)view.findViewById(R.id.etip4);
		
		etip1.addTextChangedListener(new TextWatcher() 
		{
			              @Override
			              public void onTextChanged(CharSequence s, int start, int before, int count) 
			              {
			                  // TODO Auto-generated method stub
			                  if(null!=s && s.length()>0)
			                  {
			                      if(s.length() > 2 || s.toString().trim().contains("."))
			                      {
			                          if(s.toString().trim().contains("."))
			                          {
			                        	  firstNetIP = s.toString().trim().substring(0,s.length()-1);
			                          }
			                          else
			                          {
			                        	  firstNetIP = s.toString().trim();
			                          }
			                          etip2.setFocusable(true);
			                          etip2.requestFocus();
			                      }
			                      else
			                      {
			                    	  firstNetIP = s.toString().trim();
			                      }
			                  }
			             }
			              @Override
			              public void beforeTextChanged(CharSequence s, int start, int count,
			                      int after) {
			                  // TODO Auto-generated method stub
			              }
			              @Override
			              public void afterTextChanged(Editable s) {
			                  // TODO Auto-generated method stub
			           
			  				if(s.toString()!=null&&!s.toString().equals(""))
							{
								Double toWrite=Double.parseDouble(s.toString());
								if(toWrite>255)
								{
									firstNetIP="255";
								}
								else if(toWrite<=0)
								{
									firstNetIP="0";
								}
							}
			  				else
			  				{
			  					firstNetIP="";
			  				}
			  				
			  				if(firstNetIP.indexOf('0')==0)//����ĵ�һλ����Ϊ0
			  				{
			  					firstNetIP="0";
			  				}
			  				etip1.removeTextChangedListener(this);
			  				etip1.setText(firstNetIP);
			  				etip1.setSelection(firstNetIP.length());
			  				etip1.addTextChangedListener(this);
			              }
			          });
		
		etip2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                        	secondNetIP = s.toString().trim().substring(0,s.length()-1);
                        }else{
                        	secondNetIP = s.toString().trim();
                        }
                        etip3.setFocusable(true);
                        etip3.requestFocus();
                    }
                    else
                    {
                    	secondNetIP = s.toString().trim();
                    }
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
  				if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						secondNetIP="255";
					}
					else if(toWrite<=0)
					{
						secondNetIP="0";
					}
				}
  				else
  				{
  					secondNetIP="";
  				}
  				if(secondNetIP.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					secondNetIP="0";
  				}
  				    etip2.removeTextChangedListener(this);
  				    etip2.setText(secondNetIP);
  				    etip2.setSelection(secondNetIP.length());
  				    etip2.addTextChangedListener(this);
            }
        });
		
		etip3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                        	thirdNetIP = s.toString().trim().substring(0,s.length()-1);
                        }else{
                        	thirdNetIP = s.toString().trim();
                        }
                        etip4.setFocusable(true);
                        etip4.requestFocus();
                    }
                    else
                    {
                    	thirdNetIP = s.toString().trim();
                    }
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            	if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						thirdNetIP="255";
					}
					else if(toWrite<=0)
					{
						thirdNetIP="0";
					}
				}
            	else
            	{
            		thirdNetIP="";
            	}
  				if(thirdNetIP.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					thirdNetIP="0";
  				}
  				etip3.removeTextChangedListener(this);
  				etip3.setText(thirdNetIP);
  				etip3.setSelection(thirdNetIP.length());
  				etip3.addTextChangedListener(this);
            }
        }); 

		etip4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                	fourthNetIP = s.toString().trim();
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            	if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						fourthNetIP="255";
					}
					else if(toWrite<=0)
					{
						fourthNetIP="0";
					}
				}
            	else
            	{
            		fourthNetIP="";
            	}
  				if(fourthNetIP.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					fourthNetIP="0";
  				}
  				etip4.removeTextChangedListener(this);
  				etip4.setText(fourthNetIP);
  				etip4.setSelection(fourthNetIP.length());
  				etip4.addTextChangedListener(this);
            }
        }); 
		
		
		etms1=(EditText)view.findViewById(R.id.etms1);
		etms2=(EditText)view.findViewById(R.id.etms2);
		etms3=(EditText)view.findViewById(R.id.etms3);
		etms4=(EditText)view.findViewById(R.id.etms4);
		
		etms1.addTextChangedListener(new TextWatcher() 
		{
			              @Override
			              public void onTextChanged(CharSequence s, int start, int before, int count) 
			              {
			                  // TODO Auto-generated method stub
			                  if(null!=s && s.length()>0)
			                  {
			                      if(s.length() > 2 || s.toString().trim().contains("."))
			                      {
			                          if(s.toString().trim().contains("."))
			                          {
			                        	  firstNetMs = s.toString().trim().substring(0,s.length()-1);
			                          }
			                          else
			                          {
			                        	  firstNetMs = s.toString().trim();
			                          }
			                          etms2.setFocusable(true);
			                          etms2.requestFocus();
			                      }
			                      else
			                      {
			                    	  firstNetMs = s.toString().trim();
			                      }
			                  }
			             }
			              @Override
			              public void beforeTextChanged(CharSequence s, int start, int count,
			                      int after) {
			                  // TODO Auto-generated method stub
			              }
			              @Override
			              public void afterTextChanged(Editable s) {
			                  // TODO Auto-generated method stub
			           
			  				if(s.toString()!=null&&!s.toString().equals(""))
							{
								Double toWrite=Double.parseDouble(s.toString());
								if(toWrite>255)
								{
									firstNetMs="255";
								}
								else if(toWrite<=0)
								{
									firstNetMs="0";
								}
							}
			  				else
			  				{
			  					firstNetMs="";
			  				}
			  				
			  				if(firstNetMs.indexOf('0')==0)//����ĵ�һλ����Ϊ0
			  				{
			  					firstNetMs="0";
			  				}
			  				etms1.removeTextChangedListener(this);
			  				etms1.setText(firstNetMs);
			  				etms1.setSelection(firstNetMs.length());
			  				etms1.addTextChangedListener(this);
			              }
			          });
		
		etms2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                        	secondNetMs = s.toString().trim().substring(0,s.length()-1);
                        }else{
                        	secondNetMs = s.toString().trim();
                        }
                        etms3.setFocusable(true);
                        etms3.requestFocus();
                    }
                    else
                    {
                    	secondNetMs = s.toString().trim();
                    }
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
  				if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						secondNetMs="255";
					}
					else if(toWrite<=0)
					{
						secondNetMs="0";
					}
				}
  				else
  				{
  					secondNetMs="";
  				}
  				if(secondNetMs.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					secondNetMs="0";
  				}
  				etms2.removeTextChangedListener(this);
  				etms2.setText(secondNetMs);
  				etms2.setSelection(secondNetMs.length());
  				etms2.addTextChangedListener(this);
            }
        });
		
		etms3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                        	thirdNetMs = s.toString().trim().substring(0,s.length()-1);
                        }else{
                        	thirdNetMs = s.toString().trim();
                        }
                        etms4.setFocusable(true);
                        etms4.requestFocus();
                    }
                    else
                    {
                    	thirdNetMs = s.toString().trim();
                    }
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            	if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						thirdNetMs="255";
					}
					else if(toWrite<=0)
					{
						thirdNetMs="0";
					}
				}
            	else
            	{
            		thirdNetMs="";
            	}
  				if(thirdNetMs.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					thirdNetMs="0";
  				}
  				etms3.removeTextChangedListener(this);
  				etms3.setText(thirdNetMs);
  				etms3.setSelection(thirdNetMs.length());
  				etms3.addTextChangedListener(this);
            }
        }); 

		etms4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                	fourthNetMs = s.toString().trim();
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            	if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						fourthNetMs="255";
					}
					else if(toWrite<=0)
					{
						fourthNetMs="0";
					}
				}
            	else
            	{
            		fourthNetMs="";
            	}
  				if(fourthNetMs.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					fourthNetMs="0";
  				}
  				etms4.removeTextChangedListener(this);
  				etms4.setText(fourthNetMs);
  				etms4.setSelection(fourthNetMs.length());
  				etms4.addTextChangedListener(this);
            }
        }); 
		
		etgw1=(EditText)view.findViewById(R.id.etgw1);
		etgw2=(EditText)view.findViewById(R.id.etgw2);
		etgw3=(EditText)view.findViewById(R.id.etgw3);
		etgw4=(EditText)view.findViewById(R.id.etgw4);
		
		etgw1.addTextChangedListener(new TextWatcher() 
		{
			              @Override
			              public void onTextChanged(CharSequence s, int start, int before, int count) 
			              {
			                  // TODO Auto-generated method stub
			                  if(null!=s && s.length()>0)
			                  {
			                      if(s.length() > 2 || s.toString().trim().contains("."))
			                      {
			                          if(s.toString().trim().contains("."))
			                          {
			                        	  firstNetGw = s.toString().trim().substring(0,s.length()-1);
			                          }
			                          else
			                          {
			                        	  firstNetGw = s.toString().trim();
			                          }
			                          etgw2.setFocusable(true);
			                          etgw2.requestFocus();
			                      }
			                      else
			                      {
			                    	  firstNetGw = s.toString().trim();
			                      }
			                  }
			             }
			              @Override
			              public void beforeTextChanged(CharSequence s, int start, int count,
			                      int after) {
			                  // TODO Auto-generated method stub
			              }
			              @Override
			              public void afterTextChanged(Editable s) {
			                  // TODO Auto-generated method stub
			           
			  				if(s.toString()!=null&&!s.toString().equals(""))
							{
								Double toWrite=Double.parseDouble(s.toString());
								if(toWrite>255)
								{
									firstNetGw="255";
								}
								else if(toWrite<=0)
								{
									firstNetGw="0";
								}
							}
			  				else
			  				{
			  					firstNetGw="";
			  				}
			  				
			  				if(firstNetGw.indexOf('0')==0)//����ĵ�һλ����Ϊ0
			  				{
			  					firstNetGw="0";
			  				}
			  				etgw1.removeTextChangedListener(this);
			  				etgw1.setText(firstNetGw);
			  				etgw1.setSelection(firstNetGw.length());
			  				etgw1.addTextChangedListener(this);
			              }
			          });
		
		etgw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                        	secondNetGw = s.toString().trim().substring(0,s.length()-1);
                        }else{
                        	secondNetGw = s.toString().trim();
                        }
                        etgw3.setFocusable(true);
                        etgw3.requestFocus();
                    }
                    else
                    {
                    	secondNetGw = s.toString().trim();
                    }
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
  				if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						secondNetGw="255";
					}
					else if(toWrite<=0)
					{
						secondNetGw="0";
					}
				}
  				else
  				{
  					secondNetGw="";
  				}
  				if(secondNetGw.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					secondNetGw="0";
  				}
  				etgw2.removeTextChangedListener(this);
  				etgw2.setText(secondNetGw);
  				etgw2.setSelection(secondNetGw.length());
  				etgw2.addTextChangedListener(this);
            }
        });
		
		etgw3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                        	thirdNetGw = s.toString().trim().substring(0,s.length()-1);
                        }else{
                        	thirdNetGw = s.toString().trim();
                        }
                        etgw4.setFocusable(true);
                        etgw4.requestFocus();
                    }
                    else
                    {
                    	thirdNetGw = s.toString().trim();
                    }
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            	if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						thirdNetGw="255";
					}
					else if(toWrite<=0)
					{
						thirdNetGw="0";
					}
				}
            	else
            	{
            		thirdNetGw="";
            	}
  				if(thirdNetGw.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					thirdNetGw="0";
  				}
  				etgw3.removeTextChangedListener(this);
  				etgw3.setText(thirdNetGw);
  				etgw3.setSelection(thirdNetGw.length());
  				etgw3.addTextChangedListener(this);
            }
        }); 

		etgw4.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                	fourthNetGw = s.toString().trim();
                }
           }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            	if(s.toString()!=null&&!s.toString().equals(""))
				{
					Double toWrite=Double.parseDouble(s.toString());
					if(toWrite>255)
					{
						fourthNetGw="255";
					}
					else if(toWrite<=0)
					{
						fourthNetGw="0";
					}
				}
            	else
            	{
            		fourthNetGw="";
            	}
  				if(fourthNetGw.indexOf('0')==0)//����ĵ�һλ����Ϊ0
  				{
  					fourthNetGw="0";
  				}
  				etgw4.removeTextChangedListener(this);
  				etgw4.setText(fourthNetGw);
  				etgw4.setSelection(fourthNetGw.length());
  				etgw4.addTextChangedListener(this);
            }
        }); 
		
		//�����������ã���Ҫ����µ�����ͨ��ָ��		
		btnSetNet=(Button)view.findViewById(R.id.BtnNet);
		btnSetNet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if("".equals(etip1.getText().toString())||"".equals(etip2.getText().toString())
						||"".equals(etip3.getText().toString())||"".equals(etip4.getText().toString())
						||"".equals(etms1.getText().toString())||"".equals(etms2.getText().toString())
						||"".equals(etms3.getText().toString())||"".equals(etms4.getText().toString())
						||"".equals(etgw1.getText().toString())||"".equals(etgw2.getText().toString())
						||"".equals(etgw3.getText().toString())||"".equals(etgw4.getText().toString()))
				{
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
							JSONObject json=newSer.SetNetwork(etip1.getText().toString()+"."+etip2.getText().toString()+"."+etip3.getText().toString()
									+"."+etip4.getText().toString(), etms1.getText().toString()+"."+etms2.getText().toString()+"."+etms3.getText().toString()
									+"."+etms4.getText().toString(), etgw1.getText().toString()+"."+etgw2.getText().toString()+"."+etgw3.getText().toString()
									+"."+etgw4.getText().toString());
							Looper.prepare();
							CheckSuccess(json);
							Looper.loop();
						}
					}.start();
					
				}
			}
		});
		
	//����Ϊ���Ʒ�ʽ�л�ָ��
		spMode=(Spinner)view.findViewById(R.id.spinner01);
		String[] mItems=getResources().getStringArray(R.array.mode);
		//��list��adapter������
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mItems);
		//������ʾ��ʽ
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spMode.setAdapter(adapter);
        //����¼�Spinner�¼�����  
		spMode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub    
                //��ȡѡ���ֵ 
            	if(arg2==0)//����
            	{
            		control=1;
            	}
            	else if(arg2==1)//����
            	{
            		control=2;
            	}
            	else if(arg2==2)//TCP
            	{
            		control=3;
            	}
            	else
            	{
            		control=4;//UDP
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
		spMode.setVisibility(View.VISIBLE);
		btnSetCtl=(Button)view.findViewById(R.id.BtnCtl);
		btnSetCtl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
					new Thread()
					{
						@Override
						public void run()
						{

							SerialCom newSer=new SerialCom();
							JSONObject json=newSer.ControlMode(control+"");
							Looper.prepare();
							CheckSuccess(json);
							Looper.loop();
						}
					}.start();

			}
		});
		
		//����ָ��Ϊ���þ�γ��ָ��
		etll1=(EditText)view.findViewById(R.id.etlogi);
		etll2=(EditText)view.findViewById(R.id.etlati);
		btnSetLL=(Button)view.findViewById(R.id.btnll);
		btnSetLL.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�ж����е������Ƿ����ֵ
				if("".equals(etll1.getText().toString())||"".equals(etll2.getText().toString()))
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
							JSONObject json=newSer.Position(etll1.getText().toString(), etll2.getText().toString());
							Looper.prepare();
							CheckSuccess(json);
							Looper.loop();
							//newSer.SetReferenceStar(etFrequancy.getText().toString(), etLongitude.getText().toString(), polarization+"");
                            //Toast.makeText(getActivity(), "���òο��ǿ�ʼ������", 1);
						}
					}.start();
				}
			}
		});
		
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