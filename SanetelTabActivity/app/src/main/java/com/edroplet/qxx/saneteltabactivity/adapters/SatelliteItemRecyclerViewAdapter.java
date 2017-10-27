package com.edroplet.qxx.saneteltabactivity.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.settings.SatelliteDetailActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.SatelliteDetailFragment;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qxs on 2017/10/26.
 */

public class SatelliteItemRecyclerViewAdapter  extends RecyclerView.Adapter<SatelliteItemRecyclerViewAdapter.ViewHolder>  implements View.OnClickListener,View.OnLongClickListener{

    // TODO: 2017/10/25 然并卵

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerItemLongListener mOnItemLongClickListener = null;

    private final List<SatelliteInfo> mValues;
    private boolean mTwoPane;
    private FragmentActivity activity;
    private Map<Integer, Boolean> map = new HashMap<>();
    //接口实例
    private CitiesRecyclerViewAdapter.RecyclerViewOnItemClickListener onItemClickListener;
    // TODO: 2017/10/25 然并卵

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnItemLongClickListener != null && mOnItemLongClickListener.onItemLongClick(v, (Integer) v.getTag());
    }


    public SatelliteItemRecyclerViewAdapter(FragmentActivity activity, List<SatelliteInfo> items, boolean mTwoPane) {
        mValues = items;
        this.mTwoPane = mTwoPane;
        this.activity = activity;
        initMap();
    }

    private void initMap(){
        for (int i = 0; i < getItemCount(); i++){
            map.put(i, false);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        // holder.mIdView.setText(holder.mItem.id);
        holder.mNameView.setText(holder.mItem.name);
        holder.mPolarizationView.setText(holder.mItem.polarization);
        holder.mLongitudeView.setText(holder.mItem.longitude);
        holder.mBeaconView.setText(holder.mItem.beacon);
        holder.mThresholdView.setText(holder.mItem.threshold);
        holder.mSymbolRateView.setText(holder.mItem.symbolRate);
        // holder.mComentView.setText(holder.mItem.comment);

        // TODO: 2017/10/25 然并卵 只有这个靠谱
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID, holder.mItem.mId.toString());
                    SatelliteDetailFragment fragment = new SatelliteDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.satellite_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SatelliteDetailActivity.class);
                    intent.putExtra(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID, holder.mItem.mId.toString());

                    Toast.makeText(context, "点击了" + holder.mItem.mId.toString(), Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "长按了" + holder.mItem.mId.toString(), Toast.LENGTH_SHORT).show();

                String confirmDelete = String.format(view.getContext().getString(R.string.confirm_delete_message), holder.mItem.name);
                final RandomDialog dialogBuilder = new RandomDialog(view.getContext());
                dialogBuilder.onConfirm(confirmDelete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.mView.setVisibility(View.GONE);
                        mValues.remove(holder.mItem);
                        // 修改文件
                        JsonLoad js = new JsonLoad(view.getContext(), SatelliteInfo.satelliteJsonFile);
                        ArrayList<SatelliteInfo> al = new ArrayList<SatelliteInfo>();
                        for (SatelliteInfo l:  mValues){
                            al.add(l);
                        }
                        try {
                            js.saveSatellites(al);
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

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.satellite_list_content, parent, false);
        ViewHolder vh =
                new ViewHolder(view , mOnItemClickListener,mOnItemLongClickListener);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        // TODO: 2017/10/25 然并卵

        private OnRecyclerViewItemClickListener mOnItemClickListener = null;
        private OnRecyclerItemLongListener mOnItemLong = null;


        public final View mView;
        // public final TextView mIdView;
        public final CustomTextView mNameView;
        public final CustomTextView mPolarizationView;
        public final CustomTextView mLongitudeView;
        public final CustomTextView mBeaconView;
        public final CustomTextView mThresholdView;
        public final CustomTextView mSymbolRateView;
        // public final TextView mComentView;
        public SatelliteInfo mItem;

        public ViewHolder(View view, OnRecyclerViewItemClickListener mListener, OnRecyclerItemLongListener longListener) {
            super(view);
            /*
            this.mOnItemClickListener = mListener;
            this.mOnItemLong = longListener;
            */
            mView = view;
            // mIdView = view.findViewById(R.id.id);
            // assert mIdView != null;
            mNameView = view.findViewById(R.id.name);
            assert mNameView != null;
            mPolarizationView = view.findViewById(R.id.polarization);
            assert mPolarizationView == null;
            mLongitudeView = view.findViewById(R.id.longitude);
            assert mLongitudeView == null;
            mBeaconView = view.findViewById(R.id.beacon);
            assert mBeaconView != null;
            mThresholdView = view.findViewById(R.id.threshold);
            assert mThresholdView != null;
            mSymbolRateView = view.findViewById(R.id.symbolRate);
            assert mSymbolRateView != null;
            // mComentView = view.findViewById(R.id.comment);
            // assert mComentView != null;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mOnItemLong != null){
                mOnItemLong.onItemLongClick(v,getAdapterPosition());
            }
            return true;
        }

    }
    // TODO: 2017/10/25 然并卵

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int data);

    }
    public interface OnRecyclerItemLongListener{
        boolean onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnRecyclerItemLongListener listener){
        this.mOnItemLongClickListener =  listener;
    }

}
