package com.example.tcpdemo.adapter;

import java.util.ArrayList;

import com.example.tcpdemo.CommandInfoCache;
import com.example.udp_tcp_demo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author LiuXinYi
 * @Date 2015骞�鏈�0鏃�涓嬪崍4:53:02
 * @Description []
 * @version 1.0.0
 */
public class CmdListAdapter extends BaseAdapter {
    private ArrayList<CommandInfoCache> cmdBeans;
    private Context mContext;

    public CmdListAdapter(Context context, ArrayList<CommandInfoCache> cmdBeans) {
	this.mContext = context;
	this.cmdBeans = cmdBeans;
    }

    public void setCmds(ArrayList<CommandInfoCache> cmdBeans) {
	this.cmdBeans = cmdBeans;
	notifyDataSetChanged();
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return cmdBeans.size();
    }

    @Override
    public Object getItem(int position) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	if (convertView == null) {
	    convertView = LayoutInflater.from(mContext).inflate(
		    R.layout.cmd_tiem, parent, false);
	}
	CommandInfoCache b = cmdBeans.get(position);
	TextView tvName = (TextView) convertView.findViewById(R.id.text_cmd_name);
	tvName.setText(b.getName());
	return convertView;
    }

}
