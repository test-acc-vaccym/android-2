package com.example.xwwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.Fragment;  
import android.support.v4.app.FragmentPagerAdapter;  
import android.support.v4.view.ViewPager; 
import android.view.Window;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


/**
 * 
 * ���������г���
 * @author lei
 *
 */
public class MainWindow extends SlidingFragmentActivity
{
    private static final String TAG = "MainActivity";  
    String result= "";  
    TextView tv1 = null;  
    TextView tv2 = null;  
    TextView tv3 = null;  
    
    SlidingMenu slidingMenu;//ʵ����
    
    private ViewPager mViewPager;  
    private FragmentPagerAdapter mAdapter;  
    private List<Fragment> mFragments = new ArrayList<Fragment>(); 
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainwindow);
		
        super.onCreate(savedInstanceState);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_main);  
        // ��ʼ��SlideMenu  
        initRightMenu();  
        // ��ʼ��ViewPager  
        //initViewPager();  
		
	
        //���´˴�����������ʱ������ģ�ͬʱ��Ҫ��AndroidManifest�н�����Ӧ������
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads()  
        .detectDiskWrites()  
        .detectNetwork()   // or .detectAll() for all detectable problems  
        .penaltyLog()  
        .build());  
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects()  
        .detectLeakedClosableObjects()  
        .penaltyLog()  
        .penaltyDeath()  
        .build());
	}
	
	/*
	 * ��ȡ�������ݺ���
	 */
    public void GetSerialValue(View v){
    	
//    	tv1=(TextView)findViewById(R.id.editText1);
//    	tv2=(TextView)findViewById(R.id.editText2);
//    	tv3=(TextView)findViewById(R.id.editText3);
    	
    	StringBuilder str = new StringBuilder();
    	
       List<NameValuePair> params = new ArrayList<NameValuePair>();  //ʹ��JAVA�Ŀ�
        
        params.add(new BasicNameValuePair("action", "readInfo"));  //���������ݿ�д����
    	
    	String url = "http://192.168.1.113:8080/phptest/DIOFinal.php"+"?" + URLEncodedUtils.format(params, HTTP.UTF_8); 
    	
        try{
        	
            
        	HttpGet httpRequest = new HttpGet(url); 
        	HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);

    	if(httpResponse.getStatusLine().getStatusCode() == 200)
    	{
    		BufferedReader buffer = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            for(String s = buffer.readLine(); s != null ; s = buffer.readLine())
            {
            str.append(s);
            }
            try
            {
            JSONObject jsonData = new JSONObject(str.toString());

           String azimuth = jsonData.getString("azimuth");
            
            String pitch = jsonData.getString("pitch");

            String rollEmit = jsonData.getString("rollEmit");
            

            tv1.setText(azimuth);
            tv2.setText(pitch);
            tv3.setText(rollEmit);
            
            
            }
            catch(JSONException e)
            {
            e.printStackTrace();
            }
            //buffer.close();//����ر�
    	}
    	else
    	{
    		httpRequest.abort(); 
    	}
    }
    catch(ClientProtocolException e){
        e.printStackTrace();  
        result = e.getMessage().toString();  
    }
    catch(IOException e){
        e.printStackTrace();  
        result = e.getMessage().toString();  
    }
    	
    }
	
	/*
	 * ��ʼ��ViewPager����
	 */
    
//    private void initViewPager()  
//    {  
//        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);  
//        MainTab01 tab01 = new MainTab01();  
//        MainTab02 tab02 = new MainTab02();  
//        MainTab03 tab03 = new MainTab03();  
//        mFragments.add(tab01);  
//        mFragments.add(tab02);  
//        mFragments.add(tab03);  
//        /** 
//         * ��ʼ��Adapter 
//         */  
//        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())  
//        {  
//            @Override  
//            public int getCount()  
//            {  
//                return mFragments.size();  
//            }  
//  
//            @Override  
//            public Fragment getItem(int arg0)  
//            {  
//                return mFragments.get(arg0);  
//            }  
//        };  
//        mViewPager.setAdapter(mAdapter);  
//    }
    
    /*
     * ��ʼ���Ҳ˵�ҳ��
     */
    private void initRightMenu()  
    {  
          
        //Fragment leftMenuFragment = new RightSetMenu();  
        //setBehindContentView(R.layout.left_menu_frame);  
        //getSupportFragmentManager().beginTransaction()  
        //        .replace(R.id.id_left_menu_frame, leftMenuFragment).commit();  
        SlidingMenu menu = getSlidingMenu();  
        menu.setMode(SlidingMenu.LEFT_RIGHT);  
        // ���ô�����Ļ��ģʽ  
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);  
        menu.setShadowWidthRes(R.dimen.shadow_width);  
        menu.setShadowDrawable(R.drawable.shadow);  
        // ���û����˵���ͼ�Ŀ��  
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);  
        // ���ý��뽥��Ч����ֵ  
        menu.setFadeDegree(0.35f);  
        // menu.setBehindScrollScale(1.0f);  
        menu.setSecondaryShadowDrawable(R.drawable.shadow);  
        //�����ұߣ��������໬�˵�  
        menu.setSecondaryMenu(R.layout.right_menu_frame);  
        Fragment rightMenuFragment = new RightSetMenu();  
        getSupportFragmentManager().beginTransaction()  
                .replace(R.id.id_right_menu_frame, rightMenuFragment).commit();  
    }  
    
    /*
     * ��ʾ�໬�˵�
     */
    public void showRightMenu(View view)  
    {  
        getSlidingMenu().showSecondaryMenu();  
    }  
    
    //�Ӵ˶ο�ʼ����������Ҫ�Ƕ�ʱ�����׷��ĺ���
    
    public void refresh(){   
        String httpUrl = "http://192.168.1.113:8080/Android/httpreq.jsp";   //http�����ַ
        try {   
                    URL url = new URL(httpUrl);   
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();   
                    urlConn.connect();   
                    InputStream input = urlConn.getInputStream();   
                    InputStreamReader inputreader = new InputStreamReader(input);   
                    BufferedReader reader = new BufferedReader(inputreader);   
                    String str = null;   
                    StringBuffer sb = new StringBuffer();   
                    while((str = reader.readLine())!= null){   
                            sb.append(str).append("\n");   
                    }   
                    if(sb != null){   
                            tv1.setText(sb.toString());   
                    }
                    else{   
                            tv1.setText("NULL");   
                    }   
                    reader.close();   
                    inputreader.close();   
                    input.close();   
                    reader = null;   
                    inputreader = null;   
                    input = null;   
            } catch (MalformedURLException e) {   
                    e.printStackTrace();   
            } catch (IOException e) {   
                    // TODO Auto-generated catch block   
                    e.printStackTrace();   
            }   
}   
public Handler handler = new Handler(){   
        public void handleMessage(Message msg){   
                super.handleMessage(msg);   
                refresh();   
        }   
};   
    public void run() {   
            // TODO Auto-generated method stub   
            while(true){   
                    try {   
                            Thread.sleep(1000);   
                            handler.sendMessage(handler.obtainMessage());   
                    } catch (InterruptedException e) {   
                            // TODO Auto-generated catch block   
                            e.printStackTrace();   
                    }   
            }   
    }  
    
    
	
}