package com.example.xwwt;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;  
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;  
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.example.CommonFunction.Data;
import com.example.CommonFunction.SerialCom;
import com.example.fragment.MenuFragment;
import com.example.fragment.ContentFragment;
import com.example.fragment.MonitorActivity;
import com.example.fragment.RefStarFragment;
import com.example.fragment.SetFragment;
import com.example.fragment.MonitorActivity.BackHandlerInterface;
import com.example.fragment.MonitorActivity.ButtonClick;
import com.example.fragment.HelpFragment;
import com.example.fragment.DatabaseFragment;
import com.example.SubFragments.AntCalibFragment;
import com.example.fragment.AntennaCalibFragment;
import com.example.fragment.ReadThreshold;
import com.example.fragment.ManualControl;
import com.example.fragment.IMUCalibFragment;


/**
 * 
 * 主界面运行程序
 * @author lei
 *
 */
public class MainUI extends FragmentActivity implements MonitorActivity.BackHandlerInterface, RefStarFragment.BackHandlerInterface,
SetFragment.BackHandlerInterface, HelpFragment.BackHandlerInterface,DatabaseFragment.BackHandlerInterface,
ReadThreshold.BackHandlerInterface, ManualControl.BackHandlerInterface,AntennaCalibFragment.BackHandlerInterface,
IMUCalibFragment.BackHandlerInterface, MenuFragment.BackHandlerInterface, MonitorActivity.ButtonClick
{
	private SlidingMenu menu;
	private Fragment conFragment;
	private Fragment MonitorPanel;
	private Fragment RefStar;
	private Fragment setFragment;
	private Fragment helpFragment;
	private Fragment dbFragment;
	private Fragment acFragment;
	private Fragment thFragment;
	private Fragment manualFragment;
	private Fragment imuFragment;
	private FragmentActivity conFragment1;
	private MenuFragment menuFragment;
	private Button button;
	SlidingFragmentActivity newSliding; 

	private FragmentManager fragmentManager;
	private FragmentTransaction transaction;
	
	private MonitorActivity ma ;

	private EditText ed_1,ed_2,ed_3,ed_4,ed_5,ed_6,ed_7,ed_8,ed_9;

	static boolean exitFlag;//back两次退出标志位
	boolean renderFlag;
	private String fragmentFlag="";//选择的Fragment标志位
	private String currentFlag="";//主页面标志位


	
	Handler mHandler=new Handler()
	{
		@Override  
		public void handleMessage(Message msg) {  
			// TODO Auto-generated method stub  
			super.handleMessage(msg);  
			exitFlag = false;  
			Log.i("exitFlag", "false");
		}  
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_page);

		conFragment = new ContentFragment();//新建主页
		menuFragment = new MenuFragment();//新建菜单
		MonitorPanel=new MonitorActivity();

		fragmentManager = getSupportFragmentManager();
		//transaction = fragmentManager.beginTransaction();

		menu = new SlidingMenu(this,SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.menu_main);
		menu.setSecondaryMenu(R.layout.second_menu);

		//		menu.setSecondaryShadowDrawable(R.drawable.shadow);
		menu.setShadowWidth(20);//设置阴影有立体效果
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffset(50);
		menu.setBehindScrollScale(1);
		menu.setBehindWidth((int) (getWindowManager().getDefaultDisplay().getWidth()*0.9));
		menu.setFadeDegree(1.0f);
		//如果希望使用侧滑，将下段注释去掉
		//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setMode(SlidingMenu.RIGHT);
		initView();

	}

	//初始化视图
	private void initView() {
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, MonitorPanel);//conFragment
		fragmentTransaction.replace(R.id.menu_frame, menuFragment);
		fragmentTransaction.commitAllowingStateLoss();//ft.commit();
	}


//	//切换主页对应菜单的内容
//	public void switchContentActivity(Fragment fragment) {
//		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
//		MonitorPanel= fragment;//conFragment
//		ft.replace(R.id.content_frame, MonitorPanel);//conFragment
//		//ft.addToBackStack(null);
//		ft.commitAllowingStateLoss();//ft.commit();
//		menu.showContent();
//	}

	//切换主页对应菜单的内容
	public void switchContent(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		MonitorPanel = fragment;//conFragment
		ft.replace(R.id.content_frame, MonitorPanel);//conFragment
		//ft.addToBackStack(null);
		ft.commitAllowingStateLoss();//ft.commit();
		menu.showContent();
	}
	//显示右侧菜单按钮
	public void ShowRightMenu(View view){
		if(currentFlag.equals("1"))
		{
		   ((MonitorActivity) MonitorPanel).cancelTimer();
		}
		menu.showSecondaryMenu();
	}

	//左侧的主页显示菜单项
	public void ShowHomeMenu(View view){
		
		if(currentFlag.equals("1"))
		{
		    ((MonitorActivity) MonitorPanel).cancelTimer();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Fragment fragment=new MonitorActivity();
		switchContent(fragment);
	}

//	//定义查询城市经纬度信息的数据库查询方法
//	public void ReadMonitor(View view){
//		JSONObject json=new JSONObject();
//		String actionName="readInfo";
//		SerialCom newSer=new SerialCom();//this.getApplication()
//		json=newSer.CheckMonitor(actionName);
//
//		MenuFragment mf=new MenuFragment();
//		ed_1=(EditText)conFragment.getActivity().findViewById(R.id.editText01);
//		ed_2=(EditText)conFragment.getActivity().findViewById(R.id.editText02);
//
//		try
//		{
//			ed_1.setText(json.getString("azimuth"));
//			ed_2.setText(json.getString("pitch"));
//			ed_3.setText(json.getString("rollEmit"));
//			ed_4.setText(json.getString("rollReceive"));
//			ed_5.setText(json.getString("currentAzimuth"));
//			ed_6.setText(json.getString("currentPitch"));
//			ed_7.setText(json.getString("currentRollEmit"));
//			ed_8.setText(json.getString("currentrollReceive"));
//			ed_9.setText(json.getString("searchState"));
//		}
//		catch(JSONException e)
//		{
//			e.printStackTrace();
//		}
//	}

//	//设置参考星
//	public void SetRefStar(View view)
//	{
//
//		JSONObject json=new JSONObject();
//		String actionName="referenceStar";//设置参考星的行为标识
//		SerialCom newSer=new SerialCom();
//		json=newSer.CheckMonitor(actionName);
//
//		ed_1=(EditText)conFragment.getActivity().findViewById(R.id.editText01);
//		ed_2=(EditText)conFragment.getActivity().findViewById(R.id.editText02);
//		//ed_3=(EditText)conFragment.getActivity().findViewById(R.id.editText03);
//
//		try
//		{
//			ed_1.setText(json.getString("azimuth"));
//			ed_2.setText(json.getString("azimuth"));
//			ed_3.setText(json.getString("azimuth"));
//		}
//		catch(JSONException e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	//读取寻星门限操作
//	public void ReadThreshold()
//	{
//		JSONObject json=new JSONObject();
//		String actionName="readThreshhold";//设置参考星的行为标识
//		SerialCom newSer=new SerialCom();
//		json=newSer.CheckMonitor(actionName);
//
//		ed_1=(EditText)conFragment.getActivity().findViewById(R.id.editText01);
//
//		try
//		{
//			ed_1.setText(json.getString("azimuth"));
//		}
//		catch(JSONException e)
//		{
//			e.printStackTrace();
//		}
//	}
//	//设置寻星门限
//	public void SetThreshold()
//	{
//
//		JSONObject json=new JSONObject();
//		String actionName="setThreshhold";//设置参考星的行为标识
//		SerialCom newSer=new SerialCom();//this.getApplication()
//		json=newSer.CheckMonitor(actionName);
//
//		ed_1=(EditText)conFragment.getActivity().findViewById(R.id.editText01);
//
//		try
//		{
//			ed_1.setText(json.getString("azimuth"));
//		}
//		catch(JSONException e)
//		{
//			e.printStackTrace();
//		}
//	}

	//撤销返回键对程序的影响
	//	@Override   
	//	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	//	if(keyCode == KeyEvent.KEYCODE_BACK){     
	//	return  true;
	//	} 
	//	return  super.onKeyDown(keyCode, event);     
	//
	//	} 

	//按下返回键不退出程序
	//    @Override
	//    public boolean dispatchKeyEvent(KeyEvent event) {
	//            // menuUtils.createTwoDispatcher(event);
	//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	//                    Intent intent = new Intent();
	//                    intent.setAction("android.intent.action.MAIN");
	//                    intent.addCategory("android.intent.category.HOME");
	//                    Log.i("33", "后退事件");
	//                    startActivity(intent);
	//            }
	//            return false;
	//    }

	//定义3D渲染按钮操作
	//    public void Render3Dimension(View view)
	//    {
	//    	String xx="";
	//    }

	@Override
	public void setSelectedFragment(MonitorActivity backHandledFragment) {
		// TODO Auto-generated method stub
		Log.i("monitor", "被选中");
		fragmentFlag="1";
		currentFlag="1";
		Log.i("fragmentFlag", fragmentFlag);
		this.MonitorPanel=backHandledFragment;
	}
	@Override
	public void onBackPressed() {

		if(Data.GetFlag()){
			renderFlag=false;
			Data.SetFlag(renderFlag);
			Log.i("flag","no 3d render");
		}
		else{

			Intent intent = new Intent();
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			startActivity(intent);    	
			//Thread.interrupted(); 
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}

		//    	if(exitFlag)
		//    	{
		//    		Log.i("33", "后退事件");
		//    		Intent intent = new Intent();
		//    		intent.setAction("android.intent.action.MAIN");
		//    		intent.addCategory("android.intent.category.HOME");
		//    		startActivity(intent);    	
		//    		//Thread.interrupted(); 
		//    		android.os.Process.killProcess(android.os.Process.myPid());
		//    		System.exit(0);
		//    	}
		//    	else
		//    	{    		
		//            StackTraceElement[] stackTraceElements = Thread.currentThread()
		//                    .getStackTrace();
		//            
		//    		exitFlag=true;
		//    		Log.i("exitFlag", "true");	
		//     		Toast.makeText(this, "再按一次退出程序", 2000).show();
		//    		mHandler.sendEmptyMessageDelayed(0, 2000);
		//
		//    	}

		//		Log.i("33", "后退事件");
		//		Intent intent = new Intent();
		//		intent.setAction("android.intent.action.MAIN");
		//		intent.addCategory("android.intent.category.HOME");
		//		startActivity(intent);    	
		//		//Thread.interrupted(); 
		//		android.os.Process.killProcess(android.os.Process.myPid());
		//		System.exit(0);

		switch(fragmentFlag){
		case "1":
			if (MonitorPanel == null || !((MonitorActivity) MonitorPanel).onBackPressed()) {
				// Selected fragment did not consume the back press event.
				Log.i("MonitorActivity", "pressed");
				super.onBackPressed();
			}
			Log.i("MonitorActivity", "pressed");
			break;
		case "2":
			if(RefStar == null || !((RefStarFragment) RefStar).onBackPressed()){
				Log.i("RefStarFragment", "pressed");
				super.onBackPressed();
			}
			Log.i("RefStarFragment", "pressed");
			break;
		case "3":
			if(setFragment == null || !((SetFragment) setFragment).onBackPressed()){
				Log.i("SetFragment", "pressed");
				super.onBackPressed();
			}
			break;
		case "4":
			if(helpFragment == null || !((HelpFragment) helpFragment).onBackPressed()){
				Log.i("HelpFragment", "pressed");
				super.onBackPressed();
			}
			break;
		case "5":
			if(dbFragment == null || !((DatabaseFragment) dbFragment).onBackPressed()){
				Log.i("DatabaseFragment", "pressed");
				super.onBackPressed();
			}
			break;
		case "6":
			if(acFragment == null || !((AntCalibFragment) acFragment).onBackPressed()){
				Log.i("AntCalibFragment", "pressed");
				super.onBackPressed();
			}
			break;
		case "7":
			if(thFragment == null || !((ReadThreshold) thFragment).onBackPressed()){
				Log.i("ReadThreshold", "pressed");
				super.onBackPressed();
			}
			break;
		case "8":
			if(manualFragment == null || !((ManualControl) manualFragment).onBackPressed()){
				Log.i("ManualControl", "pressed");
				super.onBackPressed();
			}
			break;
		case "9":
			if(imuFragment == null || !((IMUCalibFragment) imuFragment).onBackPressed()){
				Log.i("ManualControl", "pressed");
				super.onBackPressed();
			}
			break;
		case "10":
			if(menuFragment == null || !((MenuFragment) menuFragment).onBackPressed()){
				Log.i("MenuFragment", "pressed");
				super.onBackPressed();
			}
			Log.i("MenuFragment", "pressed");
			super.onBackPressed();
			break;
		default:
			break;
		}
		//if (MonitorPanel == null || !((MonitorActivity) MonitorPanel).onBackPressed()) {
		// Selected fragment did not consume the back press event.
		//super.onBackPressed();
		//}
		//        else if(RefStar == null || !((RefStarFragment) RefStar).onBackPressed())
		//        {
		//        	super.onBackPressed();
		//        }

	}

	@Override
	public void setSelectedFragment(RefStarFragment refStarFragment) {
		// TODO Auto-generated method stub
		Log.i("refstar", "被选中");
		fragmentFlag="2";
		currentFlag="0";
		Log.i("fragmentFlag", fragmentFlag);
		this.RefStar=refStarFragment;
	}

	@Override
	public void setSelectedFragment(SetFragment setFragment) {
		// TODO Auto-generated method stub
		Log.i("setFragment", "被选中");
		fragmentFlag="3";
		currentFlag="0";
		Log.i("fragmentFlag", fragmentFlag);
		this.setFragment=setFragment;
	}

	@Override
	public void setSelectedFragment(HelpFragment helpFragment) {
		// TODO Auto-generated method stub
		Log.i("helpFragment", "被选中");
		fragmentFlag="4";
		currentFlag="0";
		Log.i("helpgmentFlag", fragmentFlag);
		this.helpFragment=helpFragment;
	}

	@Override
	public void setSelectedFragment(DatabaseFragment dbFragment) {
		// TODO Auto-generated method stub
		Log.i("DBFragment", "被选中");
		fragmentFlag="5";
		currentFlag="0";
		Log.i("DBgmentFlag", fragmentFlag);
		this.dbFragment=dbFragment;
	}

	@Override
	public void setSelectedFragment(AntennaCalibFragment acFragment) {
		// TODO Auto-generated method stub
		Log.i("acFragment", "被选中");
		fragmentFlag="6";
		currentFlag="0";
		Log.i("acgmentFlag", fragmentFlag);
		this.acFragment=acFragment;
	}

	@Override
	public void setSelectedFragment(com.example.fragment.ReadThreshold threadFragment) {
		// TODO Auto-generated method stub
		Log.i("thFragment", "被选中");
		fragmentFlag="7";
		currentFlag="0";
		Log.i("thgmentFlag", fragmentFlag);
		this.thFragment=threadFragment;
	}

	@Override
	public void setSelectedFragment(ManualControl manualFragment) {
		// TODO Auto-generated method stub
		Log.i("manualFragment", "被选中");
		fragmentFlag="8";
		currentFlag="0";
		Log.i("manualFragment", fragmentFlag);
		this.manualFragment=manualFragment;
	}

	@Override
	public void setSelectedFragment(IMUCalibFragment imuFragment) {
		// TODO Auto-generated method stub
		Log.i("imuFragment", "被选中");
		fragmentFlag="9";
		currentFlag="0";
		Log.i("imuFragment", fragmentFlag);
		this.imuFragment=imuFragment;
	}

	@Override
	public void setSelectedFragment(MenuFragment menuFragment) {
		// TODO Auto-generated method stub
		Log.i("menuFragment", "被选中");
		fragmentFlag="10";
		Log.i("menuFragment", fragmentFlag);
		this.menuFragment=menuFragment;
	}

	@Override
	public void buttonClicked() {
		// TODO Auto-generated method stub

		((MonitorActivity) MonitorPanel).setClicked(new MonitorActivity.ButtonClick() {
			
			@Override
			public void buttonClicked() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	//定义Button事件
	
	
	

}