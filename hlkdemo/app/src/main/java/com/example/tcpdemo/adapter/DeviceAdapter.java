package com.example.tcpdemo.adapter;

import java.util.ArrayList;

import com.example.tcpdemo.EditM30Paramter;
import com.example.tcpdemo.smartlink.DeviceInfoCache;
import com.example.udp_tcp_demo.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter {
	
	ArrayList<DeviceInfoCache> list ;
	DeviceInfoCache device;
	Context con;
	ViewHolder holder;
	public DeviceAdapter(ArrayList<DeviceInfoCache> li,Context con) {
		this.list = li;
		this.con = con;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void updateList(ArrayList<DeviceInfoCache> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(con).inflate(R.layout.item_device_adapter, null);
			holder = new ViewHolder();
			holder.m_tvName = (TextView) convertView.findViewById(R.id.tvDeviceName);
			holder.m_tvMac = (TextView) convertView.findViewById(R.id.tvDeviceMac);
			holder.m_tvIp = (TextView) convertView.findViewById(R.id.tvDeviceIp);
			holder.m_tvVer = (TextView) convertView.findViewById(R.id.tvVer);
			holder.m_tvInfo = (TextView) convertView.findViewById(R.id.tvInfo);
			holder.m_btnSet = (Button) convertView.findViewById(R.id.btnSet);
			holder.m_progressBar1 = (ProgressBar) convertView.findViewById(R.id.progressBar1);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		device = list.get(position);
		holder.m_tvName.setText(device.getName());
		holder.m_tvMac.setText("Mac："+device.getMac());
		holder.m_tvIp.setText("Ip："+device.getIp());
		holder.m_tvVer.setText("Ver："+device.getVerType());
		
		if (list.get(position).getDeviceInfo() == "") 
			holder.m_tvInfo.setVisibility(View.GONE);
		else {
			holder.m_tvInfo.setVisibility(View.VISIBLE);
		}
		
		holder.m_tvInfo.setText("VerInfo："+device.getDeviceInfo());
		if (device.isConnectiong()) 
			holder.m_progressBar1.setVisibility(View.VISIBLE);
		else
			holder.m_progressBar1.setVisibility(View.GONE);
		
		holder.m_btnSet.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent2 = new Intent(con,EditM30Paramter.class);
				String addr=list.get(position).getIp();
				intent2.putExtra("addr", addr);
				con.startActivity(intent2);
			}
		});
		
		
		
		
		return convertView;
	}
	
	/**
	 * 控件缓存
	 * @author Denny
	 *
	 */
	private class ViewHolder {
		
		private TextView m_tvName;
		private TextView m_tvMac;
		private TextView m_tvIp;
		private TextView m_tvInfo;
		private TextView m_tvVer;
		private Button m_btnSet;
		private ProgressBar m_progressBar1;
		
	} 

}
