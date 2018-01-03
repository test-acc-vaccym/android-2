package com.edroplet.qxx.saneteltabactivity.adapters.recycler;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.adapters.CitiesRecyclerViewAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qxs on 2017/11/9.
 * 外层RecyclerView的Adapter
 */

public class SatelliteListOuterRecyclerViewAdapter extends RecyclerView.Adapter<BaseHolder> implements View.OnClickListener,View.OnLongClickListener{
    //条目样式
    private final int HORIZONTAL_VIEW = 1000;
    private final int VERTICAL_VIEW = 1001;
    private final int GRID_VIEW = 1002;
    private static List<SatelliteInfo> data;
    private static Activity mActivity;
    private boolean isShowBox;

    private Map<Integer, Boolean> map = new HashMap<>();

    public SatelliteListOuterRecyclerViewAdapter(Activity activity, List<SatelliteInfo> datas) {
        mActivity = activity;
        data = datas;
    }

    public static void setData(List<SatelliteInfo> data) {
        SatelliteListOuterRecyclerViewAdapter.data = data;
    }


    public void initMap(){
        map.clear();
        for (int i = 0; i < getItemCount(); i++){
            map.put(i, false);
        }
    }

    public void fillMap(){
        map.clear();
        for (int i = 0; i < getItemCount(); i++){
            map.put(i, true);
        }
    }

    public Map<Integer, Boolean> getMap() {
        return map;
    }


    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HORIZONTAL_VIEW:
                HorizontalViewHolder horizontalScrollView = new HorizontalViewHolder(R.layout.satellite_list_item_recyclerview, parent, viewType);
                horizontalScrollView.setActivity(mActivity);
                return horizontalScrollView;
            case GRID_VIEW:
                return new GridViewHolder(R.layout.satellite_list_item_recyclerview, parent, viewType, mActivity);
            case VERTICAL_VIEW:
                return new ItemViewHolder(R.layout.satellite_list_content, parent, viewType, mActivity);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
            if (holder instanceof HorizontalViewHolder) {
                holder.refreshData(data, position);
            } else if (holder instanceof GridViewHolder) {
                holder.refreshData(data, position);
            } else if (holder instanceof ItemViewHolder) {
                holder.refreshData(data.get(position - 2), position);
            }

    }

    @Override
    /**
     * 当Item出现时调用此方法
     */
    public void onViewAttachedToWindow(BaseHolder holder) {
            Log.i("mengyuan", "onViewAttachedToWindow:" + holder.getClass().toString());
    }

    @Override
    /**
     * 当Item被回收时调用此方法
     */
    public void onViewDetachedFromWindow(BaseHolder holder) {
    Log.i("mengyuan", "onViewDetachedFromWindow:" + holder.getClass().toString());
        if (holder instanceof HorizontalViewHolder) {
            ((HorizontalViewHolder) holder).saveStateWhenDestroy();
        }
    }


    @Override
    public int getItemCount() {
            return data.size();
    }

    @Override
    public int getItemViewType(int position) {
            if (position == 0) return HORIZONTAL_VIEW;
            if (position == 1) return GRID_VIEW;
            return VERTICAL_VIEW;
    }


    public void deleteItem(SatelliteInfo item){
        map.remove(data.indexOf(item));
        data.remove(item);
    }

    //设置是否显示CheckBox
    public void setShowBox() {
        //取反
        isShowBox = !isShowBox;
    }

    private CitiesRecyclerViewAdapter.RecyclerViewOnItemClickListener onItemClickListener;
    //设置点击事件
    public void setRecyclerViewOnItemClickListener(CitiesRecyclerViewAdapter.RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

}
