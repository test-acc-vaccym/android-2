package com.jianchi.fsp.buddhismnetworkradio;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jianchi.fsp.buddhismnetworkradio.api.Server;

/**
 * Created by fsp on 16-7-6.
 */
public class ServerListAdapter extends BaseAdapter {
    DataCenter data;
    private LayoutInflater mInflater;
    Context context;
    public ServerListAdapter(Context context, DataCenter data){
        this.context=context;
        this.data=data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.getServers().servers.size();
    }

    @Override
    public Object getItem(int i) {
        return data.getServers().servers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Server holder = data.getServers().servers.get(i);
        Server selectedServer = data.getServers().getSelectedServer();
        //观察convertView随ListView滚动情况

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_servers, null);
        }
        convertView.setTag(holder);
        TextView txt = (TextView) convertView.findViewById(R.id.txt);
        txt.setText(TW2CN.getInstance(context).toLocalString(holder.title));
        if(holder.title.equals(selectedServer.title)){
            txt.setTextColor(Color.parseColor("#ffff8800"));
        } else {
            txt.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}
