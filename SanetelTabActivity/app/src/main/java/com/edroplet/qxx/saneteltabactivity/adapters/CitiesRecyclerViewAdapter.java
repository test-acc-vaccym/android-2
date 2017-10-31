package com.edroplet.qxx.saneteltabactivity.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.settings.CityLocationDetailActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.CityLocationDetailFragment;
import com.edroplet.qxx.saneteltabactivity.activities.settings.CityLocationListActivity;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;
import com.edroplet.qxx.saneteltabactivity.view.custom.SmoothCheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qxx on 2017/10/26.
 */
public class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private List<LocationInfo> mValues = new ArrayList<>();
    private FragmentActivity activity;

    //是否显示单选框,默认false
    private boolean isShowBox = false;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    //接口实例
    private RecyclerViewOnItemClickListener onItemClickListener;


    public CitiesRecyclerViewAdapter(List<LocationInfo> items, FragmentActivity activity) {
        this.mValues.addAll(items);
        this.activity = activity;
        initMap();
    }

    public void setmValues(List<LocationInfo> mValues) {
        this.mValues.clear();
        this.mValues.addAll(mValues);
    }

    //初始化map集合,默认为不选中
    public void initMap() {
        map.clear();
        for (int i = 0; i < getItemCount(); i++) {
            map.put(i, false);
        }
    }
    public void fillMap(){
        map.clear();
        for (int i = 0; i < getItemCount(); i++) {
            map.put(i, true);
        }
    }

    public void deleteItem(LocationInfo locationInfo){
        map.remove(mValues.indexOf(locationInfo));
        mValues.remove(locationInfo);
    }

    //绑定视图管理者
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mProvinceView.setText(mValues.get(position).getProvince());
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mLatitudeView.setText(String.valueOf(mValues.get(position).getLatitude()));
        holder.mLongitudeView.setText(String.valueOf(mValues.get(position).getLongitude()));

        //长按显示/隐藏
        if (isShowBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }


        //设置Tag
        holder.mView.setTag(position);

        //设置checkBox改变监听
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //用map集合保存
                map.put(position, isChecked);
            }
        });

        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        //设置checkBox显示的动画
        /*
        if (isShowBox)
            holder.checkBox.setChecked(map.get(position), true);
        else
            holder.checkBox.setChecked(map.get(position), false);
        */

         Animation animation = AnimationUtils.loadAnimation(activity, R.anim.list_anim);
         //设置checkBox显示的动画
         if (isShowBox) {
             holder.checkBox.startAnimation(animation);
             holder.checkBox.setChecked(map.get(position));
         }


        /** 这部分内容在activity中实现
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(CityLocationDetailFragment.CITY_ARG_ITEM_ID, holder.mItem.getName());
                    CityLocationDetailFragment fragment = new CityLocationDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.city_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CityLocationDetailActivity.class);
                    intent.putExtra(CityLocationDetailFragment.CITY_ARG_ITEM_ID, holder.mItem.getName());
                    activity.startActivityForResult(intent, CityLocationListActivity.CITY_DETAIL_REQUEST_CODE);
                }
            }
        });
        */
        /*
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setShowBox();
                notifyDataSetChanged();
                Toast.makeText(view.getContext(), "长按了" + holder.mItem.getName(), Toast.LENGTH_SHORT).show();

                String confirmDelete = String.format(view.getContext().getString(R.string.confirm_delete_message),holder.mItem.getName());
                final RandomDialog dialogBuilder = new RandomDialog(view.getContext());
                dialogBuilder.onConfirm(confirmDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.mView.setVisibility(View.GONE);
                        mValues.remove(holder.mItem);
                        // 修改文件
                        JsonLoad js = new JsonLoad(view.getContext(), LocationInfo.citiesJsonFile);
                        ArrayList<LocationInfo> al = new ArrayList<LocationInfo>();
                        for (LocationInfo l:  mValues){
                            al.add(l);
                        }
                        try {
                            js.saveCities(al);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        notifyItemChanged(holder.getAdapterPosition());
                        dialogBuilder.getDialogBuilder().dismiss();
                    }
                });
                return true;
            }
        });
         */
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //视图管理
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mProvinceView;
        public final TextView mNameView;
        public final TextView mLatitudeView;
        public final TextView mLongitudeView;
        //private SmoothCheckBox checkBox;
        private CheckBox checkBox;
        public LocationInfo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProvinceView = (CustomTextView) view.findViewById(R.id.city_list_province);
            mNameView = (CustomTextView) view.findViewById(R.id.city_list_name);
            mLatitudeView = (CustomTextView) view.findViewById(R.id.city_list_latitude);
            mLongitudeView = (CustomTextView) view.findViewById(R.id.city_list_longitude);
            checkBox = view.findViewById(R.id.city_select_checkbox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return viewHolder;
    }

    //点击事件
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
        }
    }

    //长按事件
    @Override
    public boolean onLongClick(View v) {
        //不管显示隐藏，清空状态
        initMap();
        return onItemClickListener != null && onItemClickListener.onItemLongClickListener(v, (Integer) v.getTag());
    }

    //设置点击事件
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //设置是否显示CheckBox
    public void setShowBox() {
        //取反
        isShowBox = !isShowBox;
    }

    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (map.get(position)) {
            map.put(position, false);
        } else {
            map.put(position, true);
        }
        notifyItemChanged(position);
    }

    //返回集合给MainActivity
    public Map<Integer, Boolean> getMap() {
        return map;
    }

    //接口回调设置点击事件

    public interface RecyclerViewOnItemClickListener {
        //点击事件
        void onItemClickListener(View view, int position);

        //长按事件
        boolean onItemLongClickListener(View view, int position);
    }
}
