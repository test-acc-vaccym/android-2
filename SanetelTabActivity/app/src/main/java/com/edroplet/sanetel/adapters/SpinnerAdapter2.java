package com.edroplet.sanetel.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by qxx on 2017/11/1.
 */

public class SpinnerAdapter2 extends ArrayAdapter {
    private LayoutInflater inflater;
    private String[] target;
    private int resource;
    private int textViewResourceId;
    private Context context;

    public SpinnerAdapter2(Context context, int resource,
                           int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);

        this.target = objects;
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
        if (target != null && target.length > 0) {
            text.setText(target[position]);
            text.setGravity(Gravity.CENTER);
        } else
            text.setText("");
        text.setBackgroundColor(Color.TRANSPARENT);
        text.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        return convertView;
    }

    @Override
    public int getCount() {
        return target.length;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(
                    android.R.layout.simple_list_item_1,null);
        TextView text = (TextView) convertView
                .findViewById(android.R.id.text1);

        if (target != null && target.length > 0) {
            text.setText(target[position]);
            text.setGravity(Gravity.CENTER);
        } else
            text.setText("");
        text.setBackgroundColor(Color.GREEN);
        text.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        return convertView;
    }
}
