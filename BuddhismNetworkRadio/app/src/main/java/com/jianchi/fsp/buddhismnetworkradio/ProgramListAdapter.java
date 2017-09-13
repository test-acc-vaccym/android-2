package com.jianchi.fsp.buddhismnetworkradio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by fsp on 16-7-6.
 */
public class ProgramListAdapter extends BaseAdapter {
    DataCenter data;
    private LayoutInflater mInflater;
    Context context;
    public ProgramListAdapter(Context context, DataCenter data){
        this.context=context;
        this.data=data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.getPrograms().programs.size();
    }

    @Override
    public Object getItem(int i) {
        return data.getPrograms().programs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_programs, null);
        }
        TextView txt = (TextView) convertView.findViewById(R.id.txt);
        txt.setText(TW2CN.getInstance(context).toLocalString(data.getPrograms().programs.get(i)));
        return convertView;
    }
}
