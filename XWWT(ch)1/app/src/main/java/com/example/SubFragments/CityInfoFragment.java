package com.example.SubFragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.Dao;
import com.example.CommonFunction.ListViewAdapter;
import com.example.CommonFunction.SerialCom;
import com.example.fragment.MonitorActivity.GetSerialThread;
import com.example.xwwt.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.CommonFunction.Constants.FIRST_COLUMN;
import static com.example.CommonFunction.Constants.SECOND_COLUMN;
import static com.example.CommonFunction.Constants.THIRD_COLUMN;

public class CityInfoFragment extends Fragment implements Callback
{
	private View fragmentView;
	PopupCityAdd popCity;
	JSONObject json;
	private Button btnCity,btnCreate;
	private Spinner spinnerCity;//设置参考星与自动寻星的下拉菜单显示
	private ListView listView;
	private ListView lvTitle;
	private ArrayAdapter<String> adapter;//定义数据库中城市信息
	private boolean scrolling = false;
	public String cities[];//定义城市数组
	public String logitude[];
	public String latitude[];
	private static Handler handler=new Handler();
	private String actionName="readCity";
	
	ArrayList<HashMap<String, String>> list;
	ArrayList<HashMap<String, String>> listTitle;
	
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		fragmentView = inflater.inflate(R.layout.cityinfo_fragment,container,false);
		listView=(ListView)fragmentView.findViewById(R.id.lv);
		lvTitle=(ListView)fragmentView.findViewById(R.id.lv_title);
	    initialView(fragmentView);
	    //spinnerCity=(Spinner)fragmentView.findViewById(R.id.spinner01);
		return fragmentView;
	}
	
	//读取城市数据库中存储的信息
	public void initialView(View view)
	{
		String[] spinnerValue;
		
//		final WheelView city = (WheelView) view.findViewById(R.id.city);
//		final WheelView longitude = (WheelView) view.findViewById(R.id.longitude);
//		final WheelView latitude = (WheelView) view.findViewById(R.id.latitude);
//		
//		//增加
//		city.setViewAdapter((WheelViewAdapter) getActivity());
		
//		city.addChangingListener(new OnWheelChangedListener() {
//			@Override
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				if (!scrolling) {
//					updateCities(city, cities, newValue);
//				}
//			}
//		});
		
		btnCity=(Button)view.findViewById(R.id.button01);
		
		btnCity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//判断所有的输入是否均有值				
				ReadDB();	
				//Dao db =new Dao();
					//json=db.CheckCity();	
					//没有获得数据
			}
		});
		//spinnerCity=(Spinner)view.findViewById(R.id.spinner01);
		
		
		btnCreate=(Button) view.findViewById(R.id.button1);
		btnCreate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//实例化SelectPicPopupWindow
				popCity = new PopupCityAdd(getActivity());//, itemsOnClick
				//显示窗口
				popCity.showAtLocation(getActivity().findViewById(R.id.city_main), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
			
			}
		});
	}
	
    //为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			popCity.dismiss();
			switch (v.getId()) {
			case R.id.button02://进行城市数据库添加操作
				Toast.makeText(getActivity(), "数据库写入操作", Toast.LENGTH_SHORT).show();
				Dao dao=new Dao();
				dao.AddCity("北京", "11", "22");
				break;
			default:
				break;
			}
		}
    	
    };

    //读取数据库线程
    public void ReadDB()
    {
		handler = new Handler(this);
		// 新建一个线程
		ReadDBThread thread = new ReadDBThread(handler,actionName);
		// 开始线程
		thread.start();
   	
    }
    
    //刷新界面

    
    private void SpinnerSetting(Bundle data) {
		// TODO Auto-generated method stub
    	adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,data.getStringArray("cityName"));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerCity.setAdapter(adapter);
		spinnerCity.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		
		});
		
	}

	public class ReadDBThread extends Thread
	{
		JSONArray json=new JSONArray();

		private Handler handler; // 传入的handler
        String actionName;
		public ReadDBThread(Handler handler, String actionName) {
			// TODO Auto-generated constructor stub
			this.handler=handler;
			this.actionName=actionName;
		}
		public void run() // 线程处理的内容
		{

			Dao dao=new Dao();
			json=dao.CheckCity();
			int jaLength=json.length();
			String cityName[]=new String[jaLength];
			String latitude[]= new String[jaLength];
			String longitude[]= new String[jaLength];
			for (int i=0;i<json.length();i++)
			{
				
				try {				
					JSONObject names=json.getJSONObject(i);
					//JSONObject json1=new JSONObject();
					cityName[i]=names.getString("cityName");
					latitude[i]=names.getString("latitude");
					longitude[i]=names.getString("longitude");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			// 定义一个消息，用于发给UI线程的handler处理
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			
			bundle.putStringArray("cityName", cityName);
			bundle.putStringArray("longitude", longitude);
			bundle.putStringArray("latitude", latitude);

			msg.setData(bundle);
			// 设置这个msg的标识，这样UI中的handler才能根据这个更改对应的UI
			msg.what = 0;
			// 将消息发送给UI中的handler处理
			handler.sendMessage(msg);
			//handler.sendEmptyMessage(0);
			super.run();
		}
	
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what)
		{
			case 0:
				//SpinnerSetting(msg.getData());
				SetListView(msg.getData());
				break;
			default:
				break;
		}
		return false;
	}
   
	/**
	 * 对listView进行显示设置
	 * @param bundle
	 */
	public void SetListView(Bundle bundle)
	{
		list=new ArrayList<HashMap<String,String>>();
		listTitle=new ArrayList<HashMap<String,String>>();
		
		String cityName[]=bundle.getStringArray("cityName");
		String longitude[]=bundle.getStringArray("longitude");
		String latitude[]=bundle.getStringArray("latitude");
		//int num=Math.min(cityName.length, Math.min(longitude.length, latitude.length));
		int num=cityName.length;
		for (int i=0;i<num;i++)
		{
			HashMap<String,String> temp=new HashMap<String, String>();
			temp.put(FIRST_COLUMN, cityName[i]);
			temp.put(SECOND_COLUMN, longitude[i]);
			temp.put(THIRD_COLUMN, latitude[i]);
			list.add(temp);
		}
		HashMap<String,String> tempTitle=new HashMap<String, String>();
		tempTitle.put(FIRST_COLUMN, "City Name");
		tempTitle.put(SECOND_COLUMN, "Longitude");
		tempTitle.put(THIRD_COLUMN, "Latitude");
		listTitle.add(tempTitle);
		
		
		ListViewAdapter adapter=new ListViewAdapter(getActivity(), list);
		listView.setAdapter(adapter);
		
		
		ListViewAdapter adapterTitle=new ListViewAdapter(getActivity(), listTitle);
		lvTitle.setAdapter(adapterTitle);

	}
	
    
}