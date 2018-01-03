package com.edroplet.qxx.saneteltabactivity.view.appintro;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.view.appintro.*;

public final class AppIntro2Fragment extends AppIntroBaseFragment {
    public static com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntroFragment newInstance(CharSequence title, CharSequence description,
                                                                               @DrawableRes int imageDrawable,
                                                                               @ColorInt int bgColor) {
        return newInstance(title, description, imageDrawable, bgColor, 0, 0);
    }

    public static com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntroFragment newInstance(CharSequence title, CharSequence description,
                                                                               @DrawableRes int imageDrawable, @ColorInt int bgColor,
                                                                               @ColorInt int titleColor, @ColorInt int descColor) {
        com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntroFragment slide = new com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title.toString());
        args.putString(ARG_TITLE_TYPEFACE, null);
        args.putString(ARG_DESC, description.toString());
        args.putString(ARG_DESC_TYPEFACE, null);
        args.putInt(ARG_DRAWABLE, imageDrawable);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        slide.setArguments(args);

        return slide;
    }

    public static com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntroFragment newInstance(CharSequence title, String titleTypeface,
                                                                               CharSequence description, String descTypeface,
                                                                               @DrawableRes int imageDrawable,
                                                                               @ColorInt int bgColor) {
        return newInstance(title, titleTypeface, description, descTypeface, imageDrawable, bgColor,
                0, 0);
    }

    public static com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntroFragment newInstance(CharSequence title, String titleTypeface,
                                                                               CharSequence description, String descTypeface,
                                                                               @DrawableRes int imageDrawable, @ColorInt int bgColor,
                                                                               @ColorInt int titleColor, @ColorInt int descColor) {
        com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntroFragment slide = new com.edroplet.qxx.saneteltabactivity.view.appintro.AppIntroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title.toString());
        args.putString(ARG_TITLE_TYPEFACE, titleTypeface);
        args.putString(ARG_DESC, description.toString());
        args.putString(ARG_DESC_TYPEFACE, descTypeface);
        args.putInt(ARG_DRAWABLE, imageDrawable);
        args.putInt(ARG_BG_COLOR, bgColor);
        args.putInt(ARG_TITLE_COLOR, titleColor);
        args.putInt(ARG_DESC_COLOR, descColor);
        slide.setArguments(args);

        return slide;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.app_intro_fragment_intro2;
    }
}
