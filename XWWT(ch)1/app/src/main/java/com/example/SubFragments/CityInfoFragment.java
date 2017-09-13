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
	private Spinner spinnerCity;//���òο������Զ�Ѱ�ǵ������˵���ʾ
	private ListView listView;
	private ListView lvTitle;
	private ArrayAdapter<String> adapter;//�������ݿ��г�����Ϣ
	private boolean scrolling = false;
	public String cities[];//�����������
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
	
	//��ȡ�������ݿ��д洢����Ϣ
	public void initialView(View view)
	{
		String[] spinnerValue;
		
//		final WheelView city = (WheelView) view.findViewById(R.id.city);
//		final WheelView longitude = (WheelView) view.findViewById(R.id.longitude);
//		final WheelView latitude = (WheelView) view.findViewById(R.id.latitude);
//		
//		//����
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
				//�ж����е������Ƿ����ֵ				
				ReadDB();	
				//Dao db =new Dao();
					//json=db.CheckCity();	
					//û�л������
			}
		});
		//spinnerCity=(Spinner)view.findViewById(R.id.spinner01);
		
		
		btnCreate=(Button) view.findViewById(R.id.button1);
		btnCreate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ʵ����SelectPicPopupWindow
				popCity = new PopupCityAdd(getActivity());//, itemsOnClick
				//��ʾ����
				popCity.showAtLocation(getActivity().findViewById(R.id.city_main), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
			
			}
		});
	}
	
    //Ϊ��������ʵ�ּ�����
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			popCity.dismiss();
			switch (v.getId()) {
			case R.id.button02://���г������ݿ���Ӳ���
				Toast.makeText(getActivity(), "���ݿ�д�����", Toast.LENGTH_SHORT).show();
				Dao dao=new Dao();
				dao.AddCity("����", "11", "22");
				break;
			default:
				break;
			}
		}
    	
    };

    //��ȡ���ݿ��߳�
    public void ReadDB()
    {
		handler = new Handler(this);
		// �½�һ���߳�
		ReadDBThread thread = new ReadDBThread(handler,actionName);
		// ��ʼ�߳�
		thread.start();
   	
    }
    
    //ˢ�½���

    
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

		private Handler handler; // �����handler
        String actionName;
		public ReadDBThread(Handler handler, String actionName) {
			// TODO Auto-generated constructor stub
			this.handler=handler;
			this.actionName=actionName;
		}
		public void run() // �̴߳��������
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
			
			// ����һ����Ϣ�����ڷ���UI�̵߳�handler����
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			
			bundle.putStringArray("cityName", cityName);
			bundle.putStringArray("longitude", longitude);
			bundle.putStringArray("latitude", latitude);

			msg.setData(bundle);
			// �������msg�ı�ʶ������UI�е�handler���ܸ���������Ķ�Ӧ��UI
			msg.what = 0;
			// ����Ϣ���͸�UI�е�handler����
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
	 * ��listView������ʾ����
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