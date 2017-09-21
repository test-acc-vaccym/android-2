package com.edroplet.qxx.saneteltabactivity.fragments.manual;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;

/**
 * Created by qxs on 2017/9/19.
 */

public class SpeedControlFragment extends Fragment {
    public static SpeedControlFragment newInstance(AntennaInfo antennaInfo) {
        Bundle args = new Bundle();
        SpeedControlFragment fragment = new SpeedControlFragment();
        args.putParcelable("antennaInfo", antennaInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speed_control, null);
        com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView tvInfo = (com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView) view.findViewById(R.id.speed_info_now);
        AntennaInfo antennaInfo = getArguments().getParcelable("antennaInfo");

        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
            }
        });
        setScrollViewContent(view);
        return view;
    }
    /**
     * 刷新ScrollView的内容
     */
    private void setScrollViewContent(View view) {
        //NestedScrollView下的LinearLayout
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_sc_content);
        layout.removeAllViews();
        // for (int i = 0; i < mData.size(); i++) {
            View view1 = View.inflate(getContext(), R.layout.speed_control_scroll_page, null);
            // ((TextView) view.findViewById(R.id.tv_info)).setText(mData.get(i));
            //动态添加 子View
            layout.addView(view1, 0);
            View view2 = View.inflate(getContext(), R.layout.speed_control_draw_top_scroll_page, null);
            layout.addView(view2, 1);
        // }
    }
}
