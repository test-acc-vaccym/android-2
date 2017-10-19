package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.ImageUtil;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentLocation extends Fragment {
    public static GuideFragmentLocation newInstance(boolean showFirst, String firstLine, boolean showSecond, String secondLine, boolean showThird, String thirdLineStart, int icon, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentLocation fragment = new GuideFragmentLocation();
        args.putBoolean("showFirst",showFirst);
        args.putString("first", firstLine);
        args.putBoolean("showSecond",showSecond);
        args.putString("second", secondLine);
        args.putBoolean("showThird",showThird);
        args.putString("start", thirdLineStart);
        args.putInt("icon", icon);
        args.putString("end", thirdLineEnd);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_me_location, null);
        if (view == null){
            return null;
        }
        Context context = getContext();

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        // 位置输入
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);
            int icon = bundle.getInt("icon", -1);
            if (icon >= 0) {
                popDialog.setDrawable(ImageUtil.bitmapToDrawable(
                        ImageUtil.textAsBitmap(context,context.getString(
                                R.string.setting_string_button),
                                ImageUtil.sp2px(context,24)))) ;
            }
        }
        return popDialog.show();
    }
}
