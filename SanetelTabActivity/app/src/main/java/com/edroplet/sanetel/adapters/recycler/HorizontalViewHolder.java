package com.edroplet.sanetel.adapters.recycler;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.SatelliteInfo;

import java.util.List;

/**
 * Created by qxs on 2017/11/9.
 * 嵌套的水平RecyclerView
 * 当条目被回收时，下次加载会重新回到之前的x轴
 */

public class HorizontalViewHolder extends BaseHolder<List<SatelliteInfo>>  {

    private int HORIZONTAL_VIEW_X = 0;//水平RecyclerView滑动的距离

    private RecyclerView hor_recyclerview;

    private List<SatelliteInfo> data;

    private int scrollX;//纪录X移动的距离

    private boolean isLoadLastState = false;//是否加载了之前的状态
    private Activity activity;

    public HorizontalViewHolder(int viewId, ViewGroup parent, int viewType) {
        super(viewId, parent, viewType);
        this.activity = activity;

        hor_recyclerview = (RecyclerView) itemView.findViewById(R.id.satellite_list_item_recycler_view);
        //为了保存移动距离，所以添加滑动监听
        hor_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //每次条目重新加载时，都会滑动到上次的距离
                if (!isLoadLastState) {
                    isLoadLastState = true;
                    hor_recyclerview.scrollBy(HORIZONTAL_VIEW_X, 0);
                }
                //dx为每一次移动的距离，所以我们需要做累加操作
                scrollX += dx;
            }
        });
    }

    @Override
    public void refreshData(List<SatelliteInfo> data, int position) {
        this.data = data;
        //设置水平RecyclerView水平显示
        hor_recyclerview.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        //设置背景
        hor_recyclerview.setBackgroundResource(R.color.button_blink);
        //设置Adapter
        hor_recyclerview.setAdapter(new HorizontalAdapter());

    }

    /**
     * 在条目回收时调用，保存X轴滑动的距离
     */
    public void saveStateWhenDestroy() {
        HORIZONTAL_VIEW_X = scrollX;
        isLoadLastState = false;
        scrollX = 0;
    }

    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private class HorizontalAdapter extends RecyclerView.Adapter<BaseHolder> {

        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(R.layout.satellite_list_content, parent, viewType,activity);
        }

        @Override
        public void onBindViewHolder(BaseHolder holder, int position) {
            holder.setShowBox();
            holder.refreshData(data.get(position), position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

}
