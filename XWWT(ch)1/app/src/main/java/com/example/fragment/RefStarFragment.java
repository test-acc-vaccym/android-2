/*
 * 设置参考星
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


//定义在Activity中显示的fragment
public class RefStarFragment extends Fragment implements OnFocusChangeListener, TextWatcher, Callback
{
	private View fragmentView;
	private static final int DECIMAL_DIGITS = 1;//设置输入的位数
	private static final int DECIMAL_DIGITS_FRE = 4;//设置输入的位数
	private EditText etLongitude,editText2,etFrequancy,editText4,editText5,editText6;
	private static Handler handler=new Handler();
	private Button btnSet,btnAuto,btnStop;
	private static final String[] spinnerValue={"水平极化","垂直极化","左旋圆极化","右旋旋圆极化"};
	private Spinner spinnerAuto,spinnerRef;//设置参考星与自动寻星的下拉菜单显示
	private ArrayAdapter<String> adapter;
	
	private int polarization;//定义极化方式选项
	
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
	
		//初始化设置
		initialSet(fragmentView);
		
		ReadData();
		
		return fragmentView;
	}
	
	private void ReadData() 
	{
		// TODO Auto-generated method stub
		handler = new Handler(this);
		// 新建一个线程
		GetRefData thread = new GetRefData(handler);
		// 开始线程
		thread.start();
	}
	
	public class GetRefData extends Thread
	{
		private Handler handler; // 传入的handler
        JSONObject json=new JSONObject();
		
		public GetRefData(Handler handler)
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
			json=newSer.ReadReferenceStar();
			// 定义一个消息，用于发给UI线程的handler处理
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			try {
				if (json.length()==0||"0".equals(json.getString("success")))//没有读取到数据
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
	
	//进入初始化设置，包括监听等
	public void initialSet(View view)
	{
		//卫星经度设置小数点后1位输入
		etLongitude=(EditText)view.findViewById(R.id.editText01);
		etLongitude.addTextChangedListener(this);  
		etLongitude.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10) });  
		
		//卫星经度设置小数点后1位输入
		//editText2=(EditText)view.findViewById(R.id.editText08);
		//editText2.addTextChangedListener(this);  
		//editText2.setFilters(new InputFilter[] { lengthfilter, new InputFilter.LengthFilter(10) });  
		
		//设置参考星信标频率
		etFrequancy=(EditText)view.findViewById(R.id.editText02);
		etFrequancy.addTextChangedListener(this);
		etFrequancy.setFilters(new InputFilter[] { lengthfilterFre, new InputFilter.LengthFilter(10) });  
		
		//设置参考星相关极化方式
		//editText4=(EditText)view.findViewById(R.id.editText05);
		//editText4.addTextChangedListener(this);
		spinnerRef=(Spinner)view.findViewById(R.id.spinner01);
		String[] mItems=getResources().getStringArray(R.array.polarizations);
		//将list与adapter连起来
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mItems);
		//设置显示形式
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerRef.setAdapter(adapter);
        //添加事件Spinner事件监听  
		spinnerRef.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
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
            	else
            	{
            		polarization=8;//未知ACU参数
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
		spinnerRef.setVisibility(View.VISIBLE);
		
		//自动寻星相关信标频率
		//editText5=(EditText)view.findViewById(R.id.editText07);
		//editText5.addTextChangedListener(this);
		//自动寻星相关极化方式
		//editText6=(EditText)view.findViewById(R.id.editText09);
		//editText6.addTextChangedListener(this);
		//spinnerAuto=(Spinner)view.findViewById(R.id.spinner02);
		//spinnerAuto.setAdapter(adapter);
        //添加事件Spinner事件监听  
//		spinnerAuto.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
//                // TODO Auto-generated method stub    
//                //获取选择的值 
//            	if(arg2==0)//水平极化
//            	{
//            		polarization=0;
//            	}
//            	else if(arg2==1)//垂直极化
//            	{
//            		polarization=1;
//            	}
//            	else//圆极化
//            	{
//            		polarization=4;
//            	}
//            	
//            	adapter.getItem(arg2);    
//                /* 将mySpinner 显示*/    
//                arg0.setVisibility(View.VISIBLE);    
//            }    
//            public void onNothingSelected(AdapterView<?> arg0) {    
//                // TODO Auto-generated method stub    
//                
//                arg0.setVisibility(View.VISIBLE);    
//            }    
//        }); 
        //设置默认值
		//spinnerAuto.setVisibility(View.VISIBLE);
		
		
		//设置参考星按钮监控指令
		btnSet=(Button)view.findViewById(R.id.button01);
		btnSet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断所有的输入是否均有值
				if("".equals(etLongitude.getText().toString())||"".equals(etFrequancy.getText().toString()))
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
							JSONObject json=newSer.SetReferenceStar(etFrequancy.getText().toString(), etLongitude.getText().toString(), polarization+"");
                            //Toast.makeText(getActivity(), "设置参考星开始···", 1);
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
		
//		//自动寻星按钮操作指令
//		btnAuto=(Button)view.findViewById(R.id.Button01);
//		btnAuto.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//判断所有的输入是否均有值
//				if("".equals(etLongitude.getText().toString())||"".equals(etFrequancy.getText().toString()))
//				{
//					//弹出对话框，输入不能为空
//					AlertDialog dialog =new AlertDialog.Builder(getActivity())
//							.setTitle("警告")
//							.setMessage("输入不能为空，请检查输入")
//							.setPositiveButton("确定",null)
//							.show();
//				}
//				else
//				{
//					//向下位机发送数据
//					//JSONObject json=new JSONObject();
//					//String actionName="referenceStar";
//					new Thread()
//					{
//						@Override
//						public void run()
//						{
//							SerialCom newSer=new SerialCom();
//							newSer.AutoSearch(etFrequancy.getText().toString(),etLongitude.getText().toString() ,polarization+"");	
//							//Toast.makeText(getActivity(), "自动寻星开始···", 1);
//						}			
//					}.start();
//				}
//			}
//		});
//		
//		//停止寻星指令
//		btnStop=(Button)view.findViewById(R.id.button03);
//		btnStop.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//					//向下位机发送数据
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
//						//Toast.makeText(getActivity(), "停止寻星", 1);
//					}
//					
//				}.start();
//				//Log.d("RefStar", "begain");
//			}
//		});
	}
	

	//设置卫星经度一位小数位数控制     
    InputFilter lengthfilter = new InputFilter() {     
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
	
	//设置back响应
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
	            Log.i("setFragment", "back 事件");
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
			//再对spinner控件进行赋值操作			
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