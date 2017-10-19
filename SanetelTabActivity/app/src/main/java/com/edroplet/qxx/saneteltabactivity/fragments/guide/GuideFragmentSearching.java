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

public class GuideFragmentSearching extends Fragment {
    public static GuideFragmentSearching newInstance(boolean showInfo,boolean showFirst, String firstLine,
                                                     boolean showSecond, String secondLine, boolean showThird,
                                                     String thirdLineStart, int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentSearching fragment = new GuideFragmentSearching();
        args.putBoolean("showInfo",showInfo);
        args.putBoolean("showFirst",showFirst);
        args.putString("first", firstLine);
        args.putBoolean("showSecond",showSecond);
        args.putString("second", secondLine);
        args.putBoolean("showThird",showThird);
        args.putString("start", thirdLineStart);
        args.putInt("icon", icon);
        args.putString("buttonText", buttonText);
        args.putString("end", thirdLineEnd);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_follow_me_searching, null);
        if (view == null){
            return null;
        }
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);
            Context context = getContext();
            int icon = bundle.getInt("icon", -1);
            if (icon == 1) {
//                popDialog.setDrawable(ImageUtil.bitmapToDrawable(
//                        ImageUtil.textAsBitmap(context,context.getString(
//                                R.string.triangle_string),
//                                ImageUtil.sp2px(context,24))));
                popDialog.setButtonText(context, getString(R.string.follow_me_searching_third_button_start));
            } else if (icon == 2){
//                popDialog.setDrawable(ImageUtil.bitmapToDrawable(
//                        ImageUtil.textAsBitmap(context,context.getString(
//                                R.string.revert_triangle_string),
//                                ImageUtil.sp2px(context,24))));
                popDialog.setButtonText(context, getString(R.string.follow_me_searching_third_button_stop));
            }else if (icon == 3) {
//                popDialog.setDrawable(ImageUtil.bitmapToDrawable(
//                        ImageUtil.textAsBitmap(context,context.getString(
//                                R.string.triangle_string),
//                                ImageUtil.sp2px(context,24)))) ;
                popDialog.setButtonText(context, getString(R.string.return_string_button));
            }else if (icon == 4){
                // 故障
                popDialog.setSetThirdColor(true);
            }

        }
        return popDialog.show();
    }
}
