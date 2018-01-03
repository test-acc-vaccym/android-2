package com.edroplet.sanetel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by qxx on 2017/11/1.
 */

public class SpinnerAdapter1 extends ArrayAdapter {
    private LayoutInflater inflater;
    private String[] start;
    private int resource;
    private int textViewResourceId;
    private Context context;

    public SpinnerAdapter1(Context context, int resource,
                           int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);

        this.start = objects;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(resource,null);
        TextView text = (TextView) convertView
                .findViewById(textViewResourceId);
        text.setText("pos:"+position);
        text.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        return convertView;
    }

    @Override
    public int getCount() {
        return start.length;
    }
}
