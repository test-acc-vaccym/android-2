package com.jianchi.fsp.buddhismnetworkradio;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jianchi.fsp.buddhismnetworkradio.api.Channel;

/**
 * Created by fsp on 16-7-6.
 */
public class ChannelListAdapter extends BaseAdapter {
    DataCenter data;
    private LayoutInflater mInflater;
    Context context;
    public ChannelListAdapter(Context context, DataCenter data){
        this.context=context;
        this.data=data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.getChannels().channels.size();
    }

    @Override
    public Object getItem(int i) {
        return data.getChannels().channels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Channel holder = data.getChannels().channels.get(i);
        //观察convertView随ListView滚动情况

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_program_type, null);
        }
        convertView.setTag(holder);
        TextView txt = (TextView) convertView.findViewById(R.id.txt);
        txt.setText(TW2CN.getInstance(context).toLocalString(holder.title));
        if(holder.title.equals(data.getChannels().selectedChannelTitle)){
            txt.setTextColor(Color.parseColor("#ffff8800"));
        } else {
            txt.setTextColor(Color.WHITE);
        }

        return convertView;
    }
}
