package cn.hugeterry.coordinatortablayout.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hugeterry.coordinatortablayout.R;
import cn.hugeterry.coordinatortablayout.bean.LvMenuItem;

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
                        new LvMenuItem(R.mipmap.monitor, mContext.getString(R.string.right_nav_monitor), mOddColor),
                        new LvMenuItem(R.mipmap.communicate, mContext.getString(R.string.right_nav_communication), mEvenColor),
                        new LvMenuItem(R.mipmap.settings, mContext.getString(R.string.right_nav_system_settings), mOddColor),
                        new LvMenuItem(R.mipmap.satellite_settings, mContext.getString(R.string.right_nav_satellite), mEvenColor),
                        new LvMenuItem(R.mipmap.scales, mContext.getString(R.string.right_nav_calibration), mOddColor),
                        new LvMenuItem(R.mipmap.manual, mContext.getString(R.string.right_nav_manual), mEvenColor),
                        new LvMenuItem(R.mipmap.power, mContext.getString(R.string.right_nav_power), mOddColor),
                        new LvMenuItem(R.mipmap.info, mContext.getString(R.string.right_nav_system_info), mEvenColor),
                        new LvMenuItem(),
                        new LvMenuItem("Sub Items"),
                        new LvMenuItem(R.mipmap.contacts, mContext.getString(R.string.right_nav_contacts), mOddColor),
                        new LvMenuItem(R.mipmap.update, mContext.getString(R.string.right_nav_update), mEvenColor),
                        new LvMenuItem(R.mipmap.log, mContext.getString(R.string.right_nav_logs), mOddColor),
                        new LvMenuItem(R.mipmap.help, mContext.getString(R.string.right_nav_help), mEvenColor)
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
