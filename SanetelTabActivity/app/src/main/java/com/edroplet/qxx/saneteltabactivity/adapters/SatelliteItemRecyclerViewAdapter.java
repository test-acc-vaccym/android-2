package com.edroplet.qxx.saneteltabactivity.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qxs on 2017/10/26.
 */

public class SatelliteItemRecyclerViewAdapter  extends RecyclerView.Adapter<SatelliteItemRecyclerViewAdapter.ViewHolder>  implements View.OnClickListener,View.OnLongClickListener{

    private List<SatelliteInfo> mValues = new ArrayList<>();
    private boolean mTwoPane;
    private FragmentActivity activity;
    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean isShowBox = false;
    private Context context;
    //接口实例
    private CitiesRecyclerViewAdapter.RecyclerViewOnItemClickListener onItemClickListener;


    public SatelliteItemRecyclerViewAdapter(Context context, List<SatelliteInfo> items) {
        this.context = context;
        this.mValues.addAll(items);
        initMap();
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

    public void deleteItem(SatelliteInfo item){
        map.remove(mValues.indexOf(item));
        mValues.remove(item);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setmValues(List<SatelliteInfo> mValues) {
        this.mValues.clear();
        this.mValues.addAll(mValues);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        // holder.mIdView.setText(holder.mItem.id);
        holder.mNameView.setText(holder.mItem.name);
        holder.mPolarizationView.setText(holder.mItem.polarization);
        holder.mLongitudeView.setText(holder.mItem.longitude);
        holder.mBeaconView.setText(holder.mItem.beacon);
        holder.mThresholdView.setText(holder.mItem.threshold);
        holder.mSymbolRateView.setText(holder.mItem.symbolRate);
        // holder.mComentView.setText(holder.mItem.comment);

        //长按显示/隐藏
        if (isShowBox) {
            holder.mCheckbox.setVisibility(View.VISIBLE);
        } else {
            holder.mCheckbox.setVisibility(View.GONE);
        }

        //设置Tag
        holder.mView.setTag(position);

        //设置checkBox改变监听
        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.list_anim);
        //设置checkBox显示的动画
        if (isShowBox) {
            holder.mCheckbox.startAnimation(animation);
            holder.mCheckbox.setChecked(map.get(position));
        }

        // TODO: 2017/10/25 然并卵 只有这个靠谱， 但是不需要在这儿实现，再外面
        /*
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
        */

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final View mView;
        // public final TextView mIdView;
        private final CustomTextView mNameView;
        private final CustomTextView mPolarizationView;
        private final CustomTextView mLongitudeView;
        private final CustomTextView mBeaconView;
        private final CustomTextView mThresholdView;
        private final CustomTextView mSymbolRateView;
        private CheckBox mCheckbox;

        // public final TextView mComentView;
        private SatelliteInfo mItem;

        public ViewHolder(View view) {
            super(view);
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
            mCheckbox = view.findViewById(R.id.satellite_select_checkbox);
            assert mCheckbox != null;

            // mComentView = view.findViewById(R.id.comment);
            // assert mComentView != null;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.satellite_list_content, parent, false);
        ViewHolder vh =
                new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
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
    public void setRecyclerViewOnItemClickListener(CitiesRecyclerViewAdapter.RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //设置是否显示CheckBox
    public void setShowBox() {
        //取反
        isShowBox = !isShowBox;
    }


    //返回集合给MainActivity
    public Map<Integer, Boolean> getMap() {
        return map;
    }
}
