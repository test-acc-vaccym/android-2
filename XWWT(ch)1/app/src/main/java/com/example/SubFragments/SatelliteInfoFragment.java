package com.example.SubFragments;

import static com.example.CommonFunction.Constants.FIRST_COLUMN;
import static com.example.CommonFunction.Constants.SECOND_COLUMN;
import static com.example.CommonFunction.Constants.THIRD_COLUMN;
import static com.example.CommonFunction.Constants.FOURTH_COLUMN;
import static com.example.CommonFunction.Constants.FIFTH_COLUMN;
import static com.example.CommonFunction.Constants.SIXTH_COLUMN;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.Dao;
import com.example.CommonFunction.ListViewAdapterSat;
import com.example.CommonFunction.SerialCom;
import com.example.SubFragments.CityInfoFragment.ReadDBThread;
import com.example.xwwt.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class SatelliteInfoFragment extends Fragment implements Callback
{
	private View fragmentView;
	JSONArray json;
	private static Handler handler=new Handler();
	private Button btnCreate,btnSat;
	PopupSatelliteAdd popupSat;
	private EditText etName,etOld,etLongitude,rtHorizontal,etVertical,etMark;
	private ListView listView;
	private ListView lvTitle;
	private String actionName="readSatellite";//��ȡ������Ϣ
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		fragmentView = inflater.inflate(R.layout.satellite_fragment,container,false);
	    initialView(fragmentView);
		return fragmentView;
	}
	
	public void initialView(View view)
	{		
		listView=(ListView) view.findViewById(R.id.lv);
		lvTitle=(ListView) view.findViewById(R.id.lv_title);
		btnSat=(Button)view.findViewById(R.id.button01);//��ѯ������Ϣ
		btnSat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Dao dao=new Dao();
//				json=dao.CheckSatllite();
//				
				//��ȡ��������
				ReadDB();
			}
		});
		//����������ӶԻ���
		btnCreate=(Button)view.findViewById(R.id.button02);//�����µ�������Ϣ
		btnCreate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupSat = new PopupSatelliteAdd(getActivity());
				//��ʾ����
				popupSat.showAtLocation(getActivity().findViewById(R.id.sat_main),  Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
			}
		});
	}
	
	//��ȡ��������
    public void ReadDB()
    {
		handler = new Handler(this);
		// �½�һ���߳�
		ReadDBThread thread = new ReadDBThread(handler,actionName);
		// ��ʼ�߳�
		thread.start();
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
			json=dao.CheckSatllite();
			int jaLength=json.length();
			String satName[]=new String[jaLength];
			String satOld[]= new String[jaLength];
			String satLongitude[]= new String[jaLength];
			String satVertical[]=new String[jaLength];
			String satHorizontal[]=new String[jaLength];
			String satRemark[]=new String[jaLength];
			
			for (int i=0;i<json.length();i++)
			{
				
				try {				
					JSONObject names=json.getJSONObject(i);
					//JSONObject json1=new JSONObject();
					satName[i]=names.getString("satelliteName");
					satOld[i]=names.getString("satNameOld");
					satLongitude[i]=names.getString("satLongitude");
					satVertical[i]=names.getString("verticalValue");
					satHorizontal[i]=names.getString("horizontalValue");
					satRemark[i]=names.getString("remark");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			// ����һ����Ϣ�����ڷ���UI�̵߳�handler����
			Message msg = Message.obtain();
			Bundle bundle = new Bundle();
			
			bundle.putStringArray("satelliteName", satName);
			bundle.putStringArray("satNameOld", satOld);
			bundle.putStringArray("satLongitude", satLongitude);
			bundle.putStringArray("verticalValue", satVertical);
			bundle.putStringArray("horizontalValue", satHorizontal);
			bundle.putStringArray("remark", satRemark);

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
		ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
		ArrayList<HashMap<String, String>> listTitle=new ArrayList<HashMap<String,String>>();
	
	
		String satName[]=bundle.getStringArray("satelliteName");
		String satOld[]= bundle.getStringArray("satNameOld");
		String satLongitude[]= bundle.getStringArray("satLongitude");
		String satVertical[]=bundle.getStringArray("verticalValue");
		String satHorizontal[]=bundle.getStringArray("horizontalValue");
		String satRemark[]=bundle.getStringArray("remark");
		//int num=Math.min(satName.length, Math.min(satLongitude.length, satHorizontal.length));
		int num=satName.length;
		
		for (int i=0;i<num;i++)
		{
			HashMap<String,String> temp=new HashMap<String, String>();
			temp.put(FIRST_COLUMN, satName[i]);
			temp.put(SECOND_COLUMN, satOld[i]);
			temp.put(THIRD_COLUMN, satLongitude[i]);
			temp.put(FOURTH_COLUMN, satHorizontal[i]);
			temp.put(FIFTH_COLUMN, satVertical[i]);
			temp.put(SIXTH_COLUMN, satRemark[i]);
			list.add(temp);
		}
		HashMap<String,String> tempTitle=new HashMap<String, String>();
		tempTitle.put(FIRST_COLUMN, "Satellite Name");
		tempTitle.put(SECOND_COLUMN, "Old Name");
		tempTitle.put(THIRD_COLUMN, "Longitude");
		tempTitle.put(FOURTH_COLUMN, "Horizontal");
		tempTitle.put(FIFTH_COLUMN, "Vertical" );
		tempTitle.put(SIXTH_COLUMN, "Remark");
		listTitle.add(tempTitle);
		
		ListViewAdapterSat adapter=new ListViewAdapterSat(getActivity(), list);
		ListViewAdapterSat adapterTitle=new ListViewAdapterSat(getActivity(), listTitle);
		listView.setAdapter(adapter);

//		View listItem = adapterTitle.getView(0, null, listView);
//		
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height=listItem.getMeasuredHeight();
//        lvTitle.setLayoutParams(params);
		lvTitle.setAdapter(adapterTitle);
	}
	
    
    //Ϊ��������ʵ�ּ�����
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			popupSat.dismiss();
			switch (v.getId()) {
			case R.id.button02://�����������ݿ���Ӳ���			

				
//				if("".equals(etName.getText().toString())||"".equals(etLongitude.getText().toString()))
//				{
//					
//				}
//				else
//				{
//					new Thread()
//					{
//						@Override
//						public void run()
//						{
//							Dao dao=new Dao();
//
//							dao.AddSatellite(etName.getText().toString(), etOld.getText().toString(),
//									etLongitude.getText().toString(), rtHorizontal.getText().toString(),
//									etVertical.getText().toString(), etMark.getText().toString());
//						}
//					}.start();
//				}
				break;
			default:
				break;
			}
		}
    	
    };
}