package com.edroplet.sanetel.view.appintro;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.ChangeTypeFace;
import com.edroplet.sanetel.view.appintro.util.LogHelper;

public abstract class AppIntro extends AppIntroBase {
    private static final String TAG = LogHelper.makeLogTag(AppIntro.class);

    @Override
    protected int getLayoutId() {
        return R.layout.app_intro_intro_layout;
    }

    /**
     * Override viewpager bar color
     *
     * @param color your color resource
     */
    public void setBarColor(@ColorInt final int color) {
        LinearLayout bottomBar = (LinearLayout) findViewById(R.id.bottom);
        bottomBar.setBackgroundColor(color);
    }

    /**
     * Override next button arrow color
     *
     * @param color your color
     */
    public void setNextArrowColor(@ColorInt final int color) {
        ImageButton nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setColorFilter(color);
    }

    /**
     * Override separator color
     *
     * @param color your color resource
     */
    public void setSeparatorColor(@ColorInt final int color) {
        TextView separator = (TextView) findViewById(R.id.bottom_separator);
        separator.setBackgroundColor(color);
    }

    /**
     * Override skip text
     *
     * @param text your text
     */
    public void setSkipText(@Nullable final CharSequence text) {
        TextView skipText = (TextView) findViewById(R.id.skip);
        skipText.setText(text);
        skipText.setTextAppearance(this, android.R.style.TextAppearance_Medium);
    }


    /**
     * Override skip text typeface
     *
     * @param typeURL URL of font file located in Assets folder
     */
    public void setSkipTextTypeface(@Nullable final String typeURL) {
        TextView skipText = (TextView) findViewById(R.id.skip);
        skipText.setTypeface(ChangeTypeFace.getSimHei(this));
    }

    /**
     * Override done text
     *
     * @param text your text
     */
    public void setDoneText(@Nullable final CharSequence text) {
        TextView doneText = (TextView) findViewById(R.id.done);
        doneText.setText(text);
        Context context = getBaseContext();
        doneText.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
    }

    /**
     * Override done text typeface
     *
     * @param typeURL your text
     */
    public void setDoneTextTypeface(@Nullable final String typeURL) {
        TextView doneText = (TextView) findViewById(R.id.done);
        doneText.setTypeface(ChangeTypeFace.getSimHei(this));
    }

    /**
     * Override done button text color
     *
     * @param colorDoneText your color resource
     */
    public void setColorDoneText(@ColorInt final int colorDoneText) {
        TextView doneText = (TextView) findViewById(R.id.done);
        doneText.setTextColor(colorDoneText);
    }

    /**
     * Override skip button color
     *
     * @param colorSkipButton your color resource
     */
    public void setColorSkipButton(@ColorInt final int colorSkipButton) {
        TextView skip = (TextView) findViewById(R.id.skip);
        skip.setTextColor(colorSkipButton);
    }

    /**
     * Override Next button
     *
     * @param imageNextButton your drawable resource
     */
    public void setImageNextButton(final Drawable imageNextButton) {
        final ImageView nextButton = (ImageView) findViewById(R.id.next);
        nextButton.setImageDrawable(imageNextButton);
    }

    /**
     * Shows or hides Done button, replaced with setProgressButtonEnabled
     *
     * @deprecated use {@link #setProgressButtonEnabled(boolean)} instead.
     */
    @Deprecated
    public void showDoneButton(boolean showDone) {
        setProgressButtonEnabled(showDone);
    }

    /**
     * Show or hide the Separator line.
     * This is a static setting and Separator state is maintained across slides
     * until explicitly changed.
     *
     * @param showSeparator Set : true to display. false to hide.
     */
    public void showSeparator(boolean showSeparator) {
        TextView bottomSeparator = (TextView) findViewById(R.id.bottom_separator);
        if(showSeparator) {
            bottomSeparator.setVisibility(View.VISIBLE);
        } else {
            bottomSeparator.setVisibility(View.INVISIBLE);
        }
    }
}
