package com.edroplet.qxx.saneteltabactivity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qxx on 2017/11/2.
 */

public class CollectHistoryRecyclerViewAdapter extends RecyclerView.Adapter<CollectHistoryRecyclerViewAdapter.CollectHistoryViewHolder> {

    private Context mContext;
    private LayoutInflater mInfalter;
    private boolean showCheckbox;

    private final List<CollectHistoryFileInfo> mValues;
    private Map<Integer, Boolean> checkboxMap = new HashMap<>();

    public CollectHistoryRecyclerViewAdapter(Context context, List<CollectHistoryFileInfo> items, boolean showCheckBox) {
        this.mContext = context;
        this. mValues = items;
        this.showCheckbox = showCheckBox;

        initMap();
        mInfalter = LayoutInflater.from(context);
    }

    @Override
    public CollectHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /*
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collect_history_list_content, parent, false);
            return new ViewHolder(view);
            */
        return new CollectHistoryViewHolder(mInfalter.inflate(R.layout.collect_history_list_content, parent, false));
    }

    //返回集合给MainActivity
    public Map<Integer, Boolean> getMap() {
        return checkboxMap;
    }

    @Override
    public void onBindViewHolder(final CollectHistoryViewHolder holder, final int position) {
        ((SwipeMenuLayout) holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);//这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑

        holder.mNameView.setText(mValues.get(position).getFileName());
        holder.mDateView.setText(mValues.get(position).getDateTime());

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.list_anim);

        if (showCheckbox) {
            holder.checkBoxView.setVisibility(View.VISIBLE);
            holder.checkBoxView.startAnimation(animation);
            holder.checkBoxView.setChecked(checkboxMap.get(position));
        } else {
            holder.checkBoxView.setVisibility(View.GONE);
        }

        //设置checkBox改变监听
        holder.checkBoxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //用map集合保存
                checkboxMap.put(position, isChecked);
            }
        });

        // 设置CheckBox的状态
        if (checkboxMap.get(position) == null) {
            checkboxMap.put(position, false);
        }

        //验证长按
        holder.mNameView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "longclig", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onLongClick() called with: v = [" + v + "]");
                return false;
            }
        });

        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        (holder.mNameView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "onClick:" + mValues.get(holder.getAdapterPosition()).getFileName(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });
        // 打开：
        holder.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=mOnSwipeListener){
                    mOnSwipeListener.onOpen(holder.getAdapterPosition());
                }

            }
        });

        // 删除所有
        holder.btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener){
                    mOnSwipeListener.onDelAll();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return null != mValues ? mValues.size() : 0;
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onOpen(int pos);

        void onDelAll();
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnSwipListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    class CollectHistoryViewHolder extends RecyclerView.ViewHolder {
        CustomTextView mNameView;
        CustomTextView mDateView;
        CustomButton btnOpen;
        CustomButton btnDelete;
        CustomButton btnDeleteAll;
        CheckBox checkBoxView;

        public CollectHistoryViewHolder(View itemView) {
            super(itemView);
            mNameView = (CustomTextView) itemView.findViewById(R.id.collect_history_list_file_name);
            mDateView = (CustomTextView) itemView.findViewById(R.id.collect_history_list_save_date);
            btnOpen = (CustomButton) itemView.findViewById(R.id.main_collect_data_history_list_open);
            btnDelete = (CustomButton) itemView.findViewById(R.id.main_collect_data_history_list_delete);
            btnDeleteAll = (CustomButton) itemView.findViewById(R.id.main_collect_data_history_list_delete_all);
            checkBoxView = itemView.findViewById(R.id.collect_history_list_check);
        }

        @Override
        public String toString() {
            return super.toString() + "'" + mNameView.getText() + "'";
        }
    }


    //初始化map集合,默认为不选中
    public void initMap() {
        checkboxMap.clear();
        for (int i = 0; i < getItemCount(); i++) {
            checkboxMap.put(i, false);
        }
    }
    public void fillMap(){
        checkboxMap.clear();
        for (int i = 0; i < getItemCount(); i++) {
            checkboxMap.put(i, true);
        }
    }

}
