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
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.GalleryOnTime;
import com.edroplet.qxx.saneteltabactivity.utils.ImageUtil;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.Timer;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentLocation extends Fragment {

    private static int[] cityImages = {R.mipmap.city1, R.mipmap.city2, R.mipmap.city3};
    // 定时器
    Timer timer = new Timer();

    private CustomRadioButton crbDbCity;
    private CustomRadioButton crbNewCity;
    public static GuideFragmentLocation newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                    String secondLine, boolean showThird, String thirdLineStart,
                                                    int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentLocation fragment = new GuideFragmentLocation();
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
        final View view = inflater.inflate(R.layout.fragment_follow_me_location, null);
        if (view == null){
            return null;
        }

        GalleryOnTime galleryOnTime = new GalleryOnTime(getContext());
        galleryOnTime.setFrameLayout((FrameLayout) view.findViewById(R.id.destination_frameLayout_location));
        galleryOnTime.setImages(cityImages);
        galleryOnTime.setImageView();
        timer = galleryOnTime.getTimer();

        crbDbCity = view.findViewById(R.id.follow_me_location_db_city);
        crbNewCity = view.findViewById(R.id.follow_me_location_new_city);
        if (crbNewCity != null){
            crbNewCity.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
        if (crbDbCity != null){
            crbDbCity.setOnCheckedChangeListener(mOnCheckedChangeListener);
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
//                popDialog.setDrawable(ImageUtil.bitmapToDrawable(
//                        ImageUtil.textAsBitmap(context,context.getString(
//                                R.string.triangle_string),
//                                ImageUtil.sp2px(context,24)))) ;
                popDialog.setButtonText(context, getString(R.string.setting_button_text));
            }
        }
        return popDialog.show();
    }

    public static CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            ViewParent vp = compoundButton.getParent();
            CustomRadioGroupWithCustomRadioButton edGroup = (CustomRadioGroupWithCustomRadioButton) vp;
            int childCount = edGroup.getChildCount();
            for (int i = 0; i < childCount; i++){
                if (edGroup.getChildAt(i).getId() != compoundButton.getId()){
                    CustomRadioButton rdButton =  (CustomRadioButton)edGroup.getChildAt(i);
                    if (b && rdButton.isChecked()){
                        rdButton.setChecked(false);
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
