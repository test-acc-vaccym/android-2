package com.edroplet.sanetel.adapters.recycler;

import android.app.Activity;
import android.app.SearchableInfo;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.view.custom.CustomTextView;
import com.edroplet.sanetel.view.custom.list.ListHScrollView;

import java.util.List;

/**
 * Created by qxs on 2017/11/9.
 * 通用子条目hodler
 */

public class ItemViewHolder  extends BaseHolder<SatelliteInfo>  {

    private final CustomTextView mNameView;
    private final CustomTextView mPolarizationView;
    private final CustomTextView mLongitudeView;
    private final CustomTextView mBeaconView;
    private final CustomTextView mThresholdView;
    private final CustomTextView mSymbolRateView;
    private CheckBox mCheckbox;

    // public final TextView mComentView;
    private SatelliteInfo mItem;

    ListHScrollView scrollView;

    private Activity activity;
    /**
     * 计算屏幕的宽度
     */
    private void basicParamInit() {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);

        // screenWidth = metric.widthPixels;

    }

    public ItemViewHolder(int viewId, ViewGroup parent, int viewType, Activity activity) {
        super(viewId, parent, viewType);
        this.activity = activity;

        basicParamInit();

        // mIdView = view.findViewById(R.id.id);
        // assert mIdView != null;
        mNameView = itemView.findViewById(R.id.name);
        assert mNameView != null;
        mPolarizationView = itemView.findViewById(R.id.polarization);
        assert mPolarizationView == null;
        mLongitudeView = itemView.findViewById(R.id.longitude);
        assert mLongitudeView == null;
        mBeaconView = itemView.findViewById(R.id.beacon);
        assert mBeaconView != null;
        mThresholdView = itemView.findViewById(R.id.threshold);
        assert mThresholdView != null;
        mSymbolRateView = itemView.findViewById(R.id.symbolRate);
        assert mSymbolRateView != null;
        mCheckbox = itemView.findViewById(R.id.satellite_select_checkbox);
        assert mCheckbox != null;
        // scrollView = itemView.findViewById(R.id.satelliteListHorizontalScrollView);

        /*
        imageview_item = (ImageView) itemView.findViewById(R.id.imageview_item);
        ViewGroup.LayoutParams layoutParams = imageview_item.getLayoutParams();
        layoutParams.width = layoutParams.height = screenWidth / 3;
        imageview_item.setLayoutParams(layoutParams);
        */
    }

    private boolean isShowBox;

    public void setShowBox() {
        isShowBox = !isShowBox;
    }

    public boolean getIsChecked() {
        return mCheckbox.isChecked();
    }

    public void setCheckbox(boolean checked){
        mCheckbox.setChecked(checked);
    }

    @Override
    public void refreshData(SatelliteInfo data, final int position) {
        // imageview_item.setBackgroundResource(data);

        mItem = data;

        mNameView.setText(mItem.name);
        mPolarizationView.setText(mItem.polarization);
        mLongitudeView.setText(mItem.longitude);
        mBeaconView.setText(mItem.beacon);
        mThresholdView.setText(mItem.threshold);
        mSymbolRateView.setText(mItem.symbolRate);
        // mComentView.setText(mItem.comment);

        //长按显示/隐藏
        if (isShowBox) {
            mCheckbox.setVisibility(View.VISIBLE);
        } else {
            mCheckbox.setVisibility(View.GONE);
        }

        //设置Tag
        itemView.setTag(position);
        // 隔行变色
        // mView.setBackgroundColor(colors[position % 2]);
        //设置checkBox改变监听
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            }
        });

        // 设置CheckBox的状态

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.list_anim);
        //设置checkBox显示的动画
        if (isShowBox) {
            mCheckbox.startAnimation(animation);
            // mCheckbox.setChecked(map.get(position));
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
