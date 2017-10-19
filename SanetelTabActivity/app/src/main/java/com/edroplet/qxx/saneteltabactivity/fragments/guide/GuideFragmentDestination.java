package com.edroplet.qxx.saneteltabactivity.fragments.guide;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.GalleryOnTime;
import com.edroplet.qxx.saneteltabactivity.utils.ImageUtil;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.Timer;

import static com.edroplet.qxx.saneteltabactivity.fragments.guide.GuideFragmentLocation.mOnCheckedChangeListener;

/**
 * Created by qxs on 2017/9/19.
 */

public class GuideFragmentDestination extends Fragment {
    private static int[] satellitesImages = {R.mipmap.satellite1, R.mipmap.satellite2, R.mipmap.satellite3};
    // 定时器
    Timer timer = new Timer();

    public static GuideFragmentDestination newInstance(boolean showFirst, String firstLine,
                                                       boolean showSecond, String secondLine,
                                                       boolean showThird, String thirdLineStart,
                                                       int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        GuideFragmentDestination fragment = new GuideFragmentDestination();
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
        final View view = inflater.inflate(R.layout.fragment_follow_me_destination_satellite, null);
        if (view == null){
            return null;
        }

        FrameLayout frameLayout = view.findViewById(R.id.destination_frameLayout_satellite);

        GalleryOnTime galleryOnTime = new GalleryOnTime(getContext());
        galleryOnTime.setFrameLayout(frameLayout);
        galleryOnTime.setImages(satellitesImages);
        galleryOnTime.setImageView();
        timer = galleryOnTime.getTimer();

        CustomRadioButton crbDbSatellite = view.findViewById(R.id.angle_calculate_satellite_select);
        CustomRadioButton crbNewSatellite = view.findViewById(R.id.follow_me_destination_satellite_new);
        if (crbDbSatellite != null){
            crbDbSatellite.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
        if (crbNewSatellite != null){
            crbNewSatellite.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        Context context = getContext();

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

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
