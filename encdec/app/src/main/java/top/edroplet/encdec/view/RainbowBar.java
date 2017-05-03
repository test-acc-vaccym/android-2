package top.edroplet.encdec.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import top.edroplet.encdec.R;
import top.edroplet.encdec.utils.util.ImageOperator;
import top.edroplet.encdec.utils.util.Utils;

/**
 * Created by xw on 2017/5/3.
 */

public class RainbowBar extends View {
    //progress bar color
    int barColor = Color.parseColor("#1E88E5");
    //every bar segment width
    int hSpace = ImageOperator.dpToPx(80, getContext());
    //every bar segment height
    int vSpace = ImageOperator.dpToPx(4, getContext());
    //space among bars
    int space = ImageOperator.dpToPx(10, getContext());
    float startX = 0;
    float delta = 10f;
    Paint mPaint;

    public RainbowBar(Context context) {
        super(context);
    }

    public RainbowBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainbowBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //read custom attrs
        TypedArray t = context.obtainStyledAttributes(attrs,
                R.styleable.rainbowbar, 0, 0);
        hSpace = t.getDimensionPixelSize(R.styleable.rainbowbar_rainbowbar_hspace, hSpace);
        vSpace = t.getDimensionPixelOffset(R.styleable.rainbowbar_rainbowbar_vspace, vSpace);
        barColor = t.getColor(R.styleable.rainbowbar_rainbowbar_color, barColor);
        t.recycle();   // we should always recycle after used
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(barColor);
        mPaint.setStrokeWidth(vSpace);
    }
}
