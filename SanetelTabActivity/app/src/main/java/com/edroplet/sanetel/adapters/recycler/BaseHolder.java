package com.edroplet.sanetel.adapters.recycler;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by qxs on 2017/11/9.
 */

public class BaseHolder <T> extends RecyclerView.ViewHolder  {

    public BaseHolder(int viewId, ViewGroup parent, int viewType) {
        super(((LayoutInflater) parent.getContext().getSystemService(parent.getContext().LAYOUT_INFLATER_SERVICE)).inflate(viewId, parent,false));
    }

    public void refreshData(T data, int position) {

    }
    public void setActivity(Activity activity){
    }
    public void setShowBox(){}
}
