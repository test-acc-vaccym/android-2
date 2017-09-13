/*
 * 
 */

package com.example.fragment;


import com.example.xwwt.Login_Activity;
import com.example.xwwt.MainUI;
import com.example.xwwt.R;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.example.CommonFunction.Data;
import com.example.CommonFunction.SerialCom;
import com.example.SubFragments.AngleFragment;
import com.example.SubFragments.InertialFragment;
import com.example.SubFragments.OtherView;
import com.example.SubFragments.PositionFragment;
//import com.example.TDView;//��ά��ͼpackage
import com.example.TDView.Main3DView;
import com.example.TDViewer.AntennaInfo;
import com.example.TDViewer.CircleForDraw;
import com.example.TDViewer.CylinderTextureByVertex;
import com.example.TDViewer.AntennaSurfaceView;


import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



//������Activity����ʾ��fragment,ͬʱʵ�ֶ��߳̽ӿ�
public class MonitorActivity extends Fragment implements OnClickListener,Callback
{
	//MySurfaceView mGLSurfaceView;
    private static final String TAG = "Adapter";
    private static final int PAGE_COUNT = 4;//����4��ҳ��
    public static Object boatInitLock=new Object();
    AntennaSurfaceView xcV=null;//�������߶�����
	
    private boolean mHandledPress = false;
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(MonitorActivity backHandledFragment);
    }
    
    //���������ʱ���Ľӿ�
    
    
    private ViewPager mViewPager;    
	private View fragmentView;
	private Fragment conFragment;
	private String faultInfo;
	private EditText editText1;
	private ViewPager vp;
	private MainUI mainui;
	private ImageView mTabLine;
    private int currIndex = 0;
    private int bottomLineWidth;
    private int position_one;
    private int offset = 0;
    
    private int freshFlag=1;
    
    public String pitch="0.0";
    public String azimuth="0.0";
	
	private String actionName="readInfo";  
	Bundle bundle;
	Resources resources;
	
	FragmentTabHost mTabHost = null;
	private EditText editFault;
	
	
	//20151119
	private static Handler handler=new Handler();
	
	public boolean renderFlag=false;
	
	private AngleFragment angleFragment;
	private InertialFragment inertialFragment;
	private PositionFragment positionFragment;
	private OtherView otherView;
	JobPagerAdapter fpa;
	//Bundle bundle = new Bundle();

	
	//2016
	  public Timer mTimer=new Timer();
	  public TimerTask mTimerTask;
	  public final Handler mHandler=new Handler();
	
	
	private Button btn3D;//3D��ʾ��ť
	
	private Button btnRefresh;//���°�ť
	
	private TextView textView1,textView2,textView3,textView4;
	
	
	//����������ťButton�¼��ӿ�
    private ButtonClick buttonClick;

    public interface ButtonClick {
        public abstract void buttonClicked();
    }
    
    public void setClicked(ButtonClick click){
    	this.buttonClick=click;
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
  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//T.showShort(getActivity(), "FragmentMessage==onCreateView");
		View view = inflater.inflate(R.layout.monitor_fragment, null);
		//View view = inflater.inflate(R.layout.satellite_fragment, null);
		resources = getResources();
        
		InitWidth(view);
        initView(view);
        InitViewPager(view);
        //Tab�Ļ�����ǩ
        TranslateAnimation animation = new TranslateAnimation(position_one, offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        mTabLine.startAnimation(animation);
        
        DataUpdateTimer();
        mTimer.schedule(mTimerTask, 1000, 3000); 
        //DataUpdate();
		
		return view;
	}

    private void InitWidth(View view) 
    {
    	mTabLine=(ImageView)view.findViewById(R.id.tab_line);//����Tab�Ļ���ͼ��
        bottomLineWidth = mTabLine.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / PAGE_COUNT - bottomLineWidth) / 2);
        int avg = (int) (screenW / PAGE_COUNT);
        position_one = avg + offset;
    }
	
	
    @SuppressLint("NewApi")
    private void InitViewPager(View view) {
        vp = (ViewPager) view.findViewById(R.id.id_pager);
        vp.setOffscreenPageLimit(4);//Ԥ����4������
        fpa = new JobPagerAdapter(getActivity().getSupportFragmentManager());
        vp.setAdapter(fpa);
        vp.setOnPageChangeListener(new MyOnPageChangeListener());
        vp.setCurrentItem(0);
}
    
    
    public class JobPagerAdapter extends FragmentStatePagerAdapter {

        public JobPagerAdapter(android.support.v4.app.FragmentManager fm) 
        {
                super(fm);
                // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), "JobPagerAdapter getItem", Toast.LENGTH_SHORT).show();
                switch (position) {
                case 0:
                        return angleFragment;
                case 1:
                        return inertialFragment;
                case 2:
                        return positionFragment;
                case 3:
                        return otherView;
                }
                throw new IllegalStateException("No fragment at position "
                                + position);
        }
        
        @Override
        public int getCount() {
                // TODO Auto-generated method stub
                return PAGE_COUNT;
        }
}
    
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
                index = i;
        }

        @Override
        public void onClick(View v) {
                vp.setCurrentItem(index);
        }
};
  
public class MyOnPageChangeListener implements OnPageChangeListener {

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	//
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
        Animation animation = null;
       switch (arg0) {
       case 0:
               if(currIndex==1)//��ҳ��2��Ҫ��ʾҳ��1
               {
            	   animation = new TranslateAnimation(position_one,offset, 0, 0);
            	   textView2.setTextColor(Color.BLACK);
               }
               else if(currIndex==2)//��ҳ��3��Ҫ��ʾҳ��1
               {
            	   animation = new TranslateAnimation(2*position_one-offset, offset, 0, 0);
            	   textView3.setTextColor(Color.BLACK);
               }
               else if(currIndex==3)//��ҳ��4��Ҫ��ʾҳ��1
               {
            	   animation = new TranslateAnimation(3*position_one-2*offset, offset, 0, 0);
            	   textView4.setTextColor(Color.BLACK);
               }
               textView1.setTextColor(Color.BLUE);
               break;
       case 1:
           if(currIndex==0)//1->2
           {
        	   animation = new TranslateAnimation(offset, position_one, 0, 0);
        	   textView1.setTextColor(Color.BLACK);
           }
           else if(currIndex==2)//3->2
           {
        	   animation = new TranslateAnimation(2*position_one-offset, position_one, 0, 0);
        	   textView3.setTextColor(Color.BLACK);
           }
           else if (currIndex==3)//4->2
           {
        	   animation = new TranslateAnimation(3*position_one-2*offset, position_one, 0, 0);
        	   textView4.setTextColor(Color.BLACK);
           }
           textView2.setTextColor(Color.BLUE);
               break;
       case 2:
           if(currIndex==0)//1->3
           {
        	   animation = new TranslateAnimation(offset,2*position_one-offset,  0, 0);
        	   textView1.setTextColor(Color.BLACK);
           }
           else if(currIndex==1)//2->3
           {
        	   animation = new TranslateAnimation(position_one,2*position_one-offset, 0, 0);
        	   textView2.setTextColor(Color.BLACK);
           }
           else if (currIndex==3)//4->3
           {
        	   animation = new TranslateAnimation(3*position_one-2*offset,2*position_one-offset, 0, 0);
        	   textView4.setTextColor(Color.BLACK);
           }
           textView3.setTextColor(Color.BLUE);
               break;
       case 3:
           if(currIndex==0)//1->4
           {
        	   animation = new TranslateAnimation(offset, 3*position_one-2*offset, 0, 0);
        	   textView1.setTextColor(Color.BLACK);
           }
           else if(currIndex==1)//2->4
           {
        	   animation = new TranslateAnimation(position_one, 3*position_one-2*offset, 0, 0);
        	   textView2.setTextColor(Color.BLACK);
           }
           else if (currIndex==2)//3->4
           {
        	   animation = new TranslateAnimation(2*position_one-offset, 3*position_one-2*offset, 0, 0);
        	   textView3.setTextColor(Color.BLACK);
           }
           textView4.setTextColor(Color.BLUE);
               break;
       }
       currIndex = arg0;
       animation.setFillAfter(true);
       animation.setDuration(300);
       mTabLine.startAnimation(animation);
	}

}

    
//	 @Override
//	public void onResume() {
//	     super.onResume();
//	 }
	 @Override
	 public void onStop() {
	     super.onStop();
	 }
	
	
//	//��TabHost�������Ҫ��ʾ����Fragment
	private void initView(View view) {
		
		angleFragment=new AngleFragment();
		inertialFragment=new InertialFragment();
		positionFragment=new PositionFragment();
		otherView=new OtherView();
	
		
		textView1=(TextView)view.findViewById(R.id.textView01);
		textView1.setOnClickListener(new MyOnClickListener(0));
		textView2=(TextView)view.findViewById(R.id.textView02);
		textView2.setOnClickListener(new MyOnClickListener(1));
		textView3=(TextView)view.findViewById(R.id.textView03);
		textView3.setOnClickListener(new MyOnClickListener(2));
		textView4=(TextView)view.findViewById(R.id.textView04);
		textView4.setOnClickListener(new MyOnClickListener(3));
		
//		editFault=(EditText)view.findViewById(R.id.editText01);
//		editFault.setText("0");
		//btn3D=(Button)view.findViewById(R.id.button02);
		//btn3D.setOnClickListener(this);
		btnRefresh=(Button)view.findViewById(R.id.button01);
		btnRefresh.setOnClickListener(this);


	}

	//��ȡ�Ĵ�������
    public void getJobData(Bundle bundle) {
    	
    	//List<Map<String, String>> tmp = new ArrayList<Map<String, String>>();
          Bundle bundle_1= new Bundle();  
          bundle_1 = listData(bundle, 1);
          try
          {
            angleFragment.setList(bundle_1);

            bundle_1 = listData(bundle, 2);
            inertialFragment.setList(bundle_1);
            
            bundle_1 = listData(bundle, 3);
            positionFragment.setList(bundle_1);
            
            bundle_1 = listData(bundle, 4);
            otherView.setList(bundle_1);
          }catch (Exception e)
          {}
    }
	
    
    public Bundle listData(Bundle bundle, int k) {
        if (bundle != null) {
                //List<Map<String, String>> tmpList = new ArrayList<Map<String, String>>();
                //Map<String, String> map = null;
                Bundle map=new Bundle();
                if(k==1)
                {
                	pitch=bundle.getString("pitch");
                	azimuth=bundle.getString("azimuth");
                	map.putString("azimuth", bundle.getString("azimuth"));
                	map.putString("pitch", bundle.getString("pitch"));
                	map.putString("rollEmit", bundle.getString("rollEmit"));
                	map.putString("rollReceive", bundle.getString("rollReceive"));
                	map.putString("currentAzimuth", bundle.getString("currentAzimuth"));
                	map.putString("currentPitch", bundle.getString("currentPitch"));
                	map.putString("currentRollEmit", bundle.getString("currentRollEmit"));
                	map.putString("currentrollReceive", bundle.getString("currentrollReceive"));
                }
                else if(k==2)
                {
                	map.putString("INSHeading", bundle.getString("INSHeading"));
                	map.putString("INSPitch", bundle.getString("INSPitch"));
                	map.putString("INSRoll", bundle.getString("INSRoll"));
                	map.putString("INSStatus", bundle.getString("INSStatus"));
                }
                else if(k==3)
                {
                	map.putString("longitude", bundle.getString("longitude"));
                	map.putString("latitude", bundle.getString("latitude"));
                	map.putString("altitude", bundle.getString("altitude"));
                }
                else if(k==4)
                {
                	map.putString("searchState", bundle.getString("searchState"));
                	map.putString("beaconFrequency", bundle.getString("beaconFrequency"));
                	map.putString("AGCLevel", bundle.getString("AGCLevel"));
                	map.putString("pitchOffset", bundle.getString("pitchOffset"));
                }
                //tmpList.add(map);
                return map;
        } else
        {
                return null;
        }
}
    
	//��ȡ���ڼ������
	public Bundle GetMonitorValue()
	{
		Bundle bundle_1=new Bundle();
		JSONObject json=new JSONObject();
		String actionName="readInfo";
		SerialCom newSer=new SerialCom();
		//����ʱ�䣬����ʱ��û����������ʾҳ�棿���߷��������ӳɹ�����Ҫ�˲���
		//��PHPOperator�ж�Httpclient��������
		json=newSer.CheckMonitor(actionName);
		try 
		{
			bundle_1.putString("azimuth", json.getString("azimuth"));
			bundle_1.putString("pitch", json.getString("pitch"));
			bundle_1.putString("rollEmit", json.getString("rollEmit"));
			bundle_1.putString("rollReceive", json.getString("rollReceive"));
			bundle_1.putString("currentAzimuth", json.getString("currentAzimuth"));
			bundle_1.putString("currentPitch", json.getString("currentPitch"));
			bundle_1.putString("currentRollEmit", json.getString("currentRollEmit"));
			bundle_1.putString("currentrollReceive", json.getString("currentrollReceive"));
			bundle_1.putString("searchState", json.getString("searchState"));
			bundle_1.putString("INSHeading", json.getString("INSHeading"));
			bundle_1.putString("INSPitch", json.getString("INSPitch"));
			bundle_1.putString("INSRoll", json.getString("INSRoll"));
			bundle_1.putString("longitude", json.getString("longitude"));
			bundle_1.putString("latitude", json.getString("latitude"));
			bundle_1.putString("altitude", json.getString("altitude"));
			bundle_1.putString("INSStatus", json.getString("INSStatus"));
			bundle_1.putString("beaconFrequency", json.getString("beaconFrequency"));
			bundle_1.putString("AGCLevel", json.getString("AGCLevel"));
			bundle_1.putString("pitchOffset", json.getString("pitchOffset"));
			faultInfo=json.getString("faultStatus");
			
		}catch(JSONException e)
		{
			
		}
		return bundle_1;
	}
	

	//�����ѯ���о�γ����Ϣ�����ݿ��ѯ����
//	public void pReadMonitor(View view)
//	{
//		String actionName="readInfo";
//		SerialCom newSer=new SerialCom();
//		newSer.CheckMonitor(actionName);
//	}

    
    //���ݷ����ı䣬���и���
	 @Override
	public boolean handleMessage(Message msg)
	{
		// ͨ���ж�msg.what���жϵ������ĸ�"�¼�"Ҫ���д���
		switch (msg.what)
		{
			case 0:
				// �ó�msg�е����ݲ���ʾ����
				bundle = msg.getData();
				getJobData(bundle);
				//notifyViewPagerDataSetChanged();//֪ͨ���ݷ����仯�����и���
				break;
			default:
				break;
		}
		return false;
	}
	

	
	//���̲߳�ѯ���ڲ���
	public class GetSerialThread extends Thread
	{

		private Handler handler; // �����handler
        String actionName;
        JSONObject json=new JSONObject();
		
		public GetSerialThread(Handler handler, String actionName)
		{
			this.handler = handler;
			this.actionName = actionName;
		}
		
		@Override
		public void run() // �̴߳��������
		{
			//��ѯ��������
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			SerialCom newSer=new SerialCom();
			//����ʱ�䣬����ʱ��û����������ʾҳ�棿���߷��������ӳɹ�����Ҫ�˲���
			json=newSer.CheckMonitor(actionName);
			// ����һ����Ϣ�����ڷ���UI�̵߳�handler����
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			//Looper.prepare();
			//CheckSuccess(json);
			//Looper.loop();
			
			if (json.length()==0)
			{
				bundle.putString("azimuth", "0.0");
				bundle.putString("pitch", "0.0");
				bundle.putString("rollEmit", "0.0");
				bundle.putString("rollReceive", "0.0");
				bundle.putString("currentAzimuth", "0.0");
				bundle.putString("currentPitch", "0.0");
				bundle.putString("currentRollEmit", "0.0");
				bundle.putString("currentrollReceive", "0.0");
				bundle.putString("searchState", "0");
				bundle.putString("INSHeading", "0.0");
				bundle.putString("INSPitch", "0.0");
				bundle.putString("INSRoll", "0.0");
				bundle.putString("longitude", "10.0");
				bundle.putString("latitude", "-10.0");
				bundle.putString("altitude", "0.0");
				bundle.putString("INSStatus", "--");
				bundle.putString("beaconFrequency", "0.0");
				bundle.putString("AGCLevel", "0.0");
				bundle.putString("pitchOffset", "0.0");
				bundle.putString("faultStatus","--");
			}
			else
			{
				try 
				{
					bundle.putString("azimuth", json.getString("azimuth"));
					bundle.putString("pitch", json.getString("pitch"));
					bundle.putString("rollEmit", json.getString("rollEmit"));
					bundle.putString("rollReceive", json.getString("rollReceive"));
					bundle.putString("currentAzimuth", json.getString("currentAzimuth"));
					bundle.putString("currentPitch", json.getString("currentPitch"));
					bundle.putString("currentRollEmit", json.getString("currentRollEmit"));
					bundle.putString("currentrollReceive", json.getString("currentrollReceive"));
					bundle.putString("searchState", json.getString("searchState"));
					bundle.putString("INSHeading", json.getString("INSHeading"));
					bundle.putString("INSPitch", json.getString("INSPitch"));
					bundle.putString("INSRoll", json.getString("INSRoll"));
					bundle.putString("longitude", json.getString("longitude"));
					bundle.putString("latitude", json.getString("latitude"));
					bundle.putString("altitude", json.getString("altitude"));
					bundle.putString("INSStatus", json.getString("INSStatus"));
					bundle.putString("beaconFrequency", json.getString("beaconFrequency"));
					bundle.putString("AGCLevel", json.getString("AGCLevel"));
					bundle.putString("pitchOffset", json.getString("pitchOffset"));
					bundle.putString("faultStatus",json.getString("faultStatus"));
					//faultInfo=json.getString("faultStatus");

				}catch(JSONException e)
				{

				}
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
			}
	}

	//data update timer
	public void DataUpdateTimer()
	{
		mTimer=new Timer();

		mTimerTask=new TimerTask(){
		       public void run () {
		    	   mHandler.post(new Runnable(){
		    		  public void run(){
		    			  DataUpdate();
//		    			  try
//		    			  {
//		    			     Toast.makeText(getActivity(), R.string.assert08, Toast.LENGTH_SHORT).show();
//		    			  }
//		    			  catch(Exception e)
//		    			  {
//		    				  Log.i("not found", "not found");  
//		    			  }
		    			  Log.d("timer", mTimer.toString());
		    		  }
		    		   
		    	   });

		       }
		};
		
		
		
		
	}
	
	
	//���ݸ���
	public void DataUpdate()
	{
		handler = new Handler(this);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		GetSerialThread thread = new GetSerialThread(handler,actionName);
		thread.start();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.button01)//�������ݸ���
		{
			//DataUpdate();
			mTimer.cancel();
			try
			{
			Toast.makeText(getActivity(), R.string.assert07, Toast.LENGTH_SHORT).show();
			}catch(Resources.NotFoundException e)
			{}
		}
		else if(id==R.id.button02)//����3ά��ʾ
		{
			renderFlag=true;
			mTimer.cancel();
			Data.SetFlag(renderFlag);
			new Thread()
			{
			   public void run()
			   {
				   synchronized(boatInitLock)
				   {
					   //��������
					   CylinderTextureByVertex.initVertexData(14, 6, 8, 1);
					   CircleForDraw.initVertexData(8, 14);
					   AntennaSurfaceView.loadWelcomeBitmap(getActivity().getResources());
					   AntennaSurfaceView.loadVertexFromObj(getActivity().getResources());
					   com.example.TDViewer.ShaderManager.loadCodeFromFile(getActivity().getResources());
				  
				   }
			   }
			}.start();			
			gotoXCView();
		}
		
	}
	//����3ά��ʾ������
    public void gotoXCView()
    {
    	//vfd.flag=false;
    	freshFlag=0;
    	float temp_1=Float.parseFloat(pitch);
    	float temp_2=Float.parseFloat(azimuth);
    	xcV = new AntennaSurfaceView(getActivity(),-10,90);//Integer.parseInt(pitch),Integer.parseInt(azimuth)
    	xcV.index_boat=AntennaInfo.cuttBoatIndex;
    	
        getActivity().setContentView(xcV);	
        xcV.requestFocus();//��ȡ����
        xcV.setFocusableInTouchMode(true);//����Ϊ�ɴ���
        //curr=WhichView.XC_VIEW;
    }

//    //���ط��ؼ�����
//    public static boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (keyCode == event.KEYCODE_BACK) {
//            Log.d("GameFragmet�¼�", "OK");
//        }
//        return true;
//    }
    
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
            if(xcV!=null)
            {
   		        xcV.flagForThread=false;
    		    xcV.flag_display=false;
    		    xcV=null;
            }
			Log.i("waiting", "true");
    		Intent aIntent = new Intent();
    		aIntent.setClass(getActivity(),MainUI.class);
    		startActivity(aIntent);
		    
            return true;
        }
        return false;
    }
    
    
    
public void cancelTimer()
{
	//buttonClick.buttonClicked();
	
	Log.i("cancel", "cancel");
	mTimer.cancel();
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