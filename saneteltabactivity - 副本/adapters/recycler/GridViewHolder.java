package com.edroplet.qxx.saneteltabactivity.adapters.recycler;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;

import java.util.List;

/**
 * Created by qxs on 2017/11/9.
 */

public class GridViewHolder  extends BaseHolder<List<Integer>>  {private RecyclerView item_recyclerview;

    private final int ONE_LINE_SHOW_NUMBER = 3;
    private Activity activity;
    private List<Integer> data;

    public GridViewHolder(int viewId, ViewGroup parent, int viewType, Activity activity) {
        super(viewId, parent, viewType);
        this.activity = activity;
        item_recyclerview = (RecyclerView) itemView.findViewById(R.id.satellite_list_item_recycler_view);

    }

    @Override
    public void refreshData(List<Integer> data, int position) {
        super.refreshData(data, position);
        this.data = data;
        //每行显示3个，水平显示
        item_recyclerview.setLayoutManager(new GridLayoutManager(activity, ONE_LINE_SHOW_NUMBER, LinearLayoutManager.VERTICAL, false));
        //设置个背景色
        item_recyclerview.setBackgroundResource(R.color.colorPrimary);
        //设置Adapter
        item_recyclerview.setAdapter(new GridAdapter());
    }


    private class GridAdapter extends RecyclerView.Adapter<BaseHolder> {

        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(R.layout.satellite_list_content, parent, viewType, activity);
        }

        @Override
        public void onBindViewHolder(BaseHolder holder, int position) {
            holder.refreshData(data.get(position), position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
