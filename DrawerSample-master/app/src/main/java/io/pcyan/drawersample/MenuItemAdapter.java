package io.pcyan.drawersample;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by qxs on 2017/8/30.
 */

public class MenuItemAdapter extends BaseAdapter {
    private final int mIconSize;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mOddColor, mEvenColor;
    private List<LvMenuItem> mItems;

    public MenuItemAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        mContext = context;

        mIconSize = context.getResources().getDimensionPixelSize(R.dimen.drawer_icon_size);
        mOddColor = context.getResources().getColor(R.color.menu_item_odd_background);
        mEvenColor = context.getResources().getColor(R.color.menu_item_even_background);

        mItems = new ArrayList<LvMenuItem>(
                Arrays.asList(
                        new LvMenuItem(android.R.drawable.ic_btn_speak_now, mContext.getString(R.string.item1), mOddColor),
                        new LvMenuItem(android.R.drawable.ic_delete, mContext.getString(R.string.item2), mEvenColor),
                        new LvMenuItem(android.R.drawable.ic_dialog_alert, mContext.getString(R.string.item3), mOddColor),
                        new LvMenuItem(android.R.drawable.ic_dialog_dialer, mContext.getString(R.string.item4), mEvenColor),
                        new LvMenuItem(android.R.drawable.ic_dialog_email, mContext.getString(R.string.item5), mOddColor)
                ));
    }



    @Override
    public int getCount(){
        return mItems.size();
    }


    @Override
    public Object getItem(int position){
        return mItems.get(position);
    }


    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getViewTypeCount(){
        return 3;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mItems.get(position).type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LvMenuItem item = mItems.get(position);
        switch (item.type){
            case LvMenuItem.TYPE_NORMAL:
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.design_drawer_item, parent,
                            false);
                }
                TextView itemView = (TextView) convertView;
                itemView.setText(item.name);
                itemView.setBackgroundColor(item.background);
                Drawable icon = mContext.getResources().getDrawable(item.icon);
                setIconColor(icon);
                if (icon != null){
                    icon.setBounds(0, 0, mIconSize, mIconSize);
                    // TextViewCompat.setCompoundDrawablesRelative(itemView, icon, null, null, null);
                    TextViewCompat.setCompoundDrawablesRelative(itemView, null, null, icon, null);
                }

                break;
            case LvMenuItem.TYPE_NO_ICON:
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.design_drawer_item_subheader,
                            parent, false);
                }
                TextView subHeader = (TextView) convertView;
                subHeader.setText(item.name);
                break;
            case LvMenuItem.TYPE_SEPARATOR:
                if (convertView == null){
                    convertView = mInflater.inflate(R.layout.design_drawer_item_separator,
                            parent, false);
                }
                break;
        }

        return convertView;
    }

    public void setIconColor(Drawable icon){
        int textColorSecondary = android.R.attr.textColorSecondary;
        TypedValue value = new TypedValue();
        if (!mContext.getTheme().resolveAttribute(textColorSecondary, value, true)){
            return;
        }
        int baseColor = mContext.getResources().getColor(value.resourceId);
        icon.setColorFilter(baseColor, PorterDuff.Mode.MULTIPLY);
    }
}
