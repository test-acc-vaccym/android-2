package cn.hugeterry.coordinatortablayout.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.hugeterry.coordinatortablayout.R;


/**
 * Created by qxs on 2017/8/30.
 * Android自定义控件
 * http://www.cnblogs.com/whoislcj/p/5708778.html
 * http://www.cnblogs.com/whoislcj/p/5711001.html
 */

public class PercentView extends View {
    private final static String TAG = PercentView.class.getSimpleName();
    private Paint mPaint;
    private int backgroundColor = Color.GRAY;
    private int progressColor = Color.BLUE;
    private float radius;
    private int progress;
    private float centerX = 0;
    private float centerY = 0;
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int CENTER = 2;
    public static final int RIGHT = 3;
    public static final int BOTTOM = 4;
    private int gravity = CENTER;
    private RectF rectF;  //用于定义的圆弧的形状和大小的界限

    public PercentView(Context context) {
        super(context);
        init();
    }

    public PercentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParams(context, attrs);
    }

    public PercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        rectF = new RectF();
    }

    private void initParams(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        rectF = new RectF();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentView);
        if (typedArray != null) {
            backgroundColor = typedArray.getColor(R.styleable.PercentView_percent_background_color, Color.GRAY);
            progressColor = typedArray.getColor(R.styleable.PercentView_percent_progress_color, Color.BLUE);
            radius = typedArray.getDimension(R.styleable.PercentView_percent_circle_radius, 0);
            progress = typedArray.getInt(R.styleable.PercentView_percent_circle_progress, 0);
            gravity = typedArray.getInt(R.styleable.PercentView_percent_circle_gravity, CENTER);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "onMeasure--widthMode-->" + widthMode);
        switch (widthMode) {
            case MeasureSpec.EXACTLY://
                break;
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        Log.e(TAG, "onMeasure--widthSize-->" + widthSize);
        Log.e(TAG, "onMeasure--heightMode-->" + heightMode);
        Log.e(TAG, "onMeasure--heightSize-->" + heightSize);
        int with = getWidth();
        int height = getHeight();
        Log.e(TAG, "onDraw---->" + with + "*" + height);
        centerX = with / 2;
        centerY = with / 2;
        switch (gravity) {
            case LEFT:
                centerX = radius + getPaddingLeft();
                break;
            case TOP:
                centerY = radius + getPaddingTop();
                break;
            case CENTER:
                break;
            case RIGHT:
                centerX = with - radius - getPaddingRight();
                break;
            case BOTTOM:
                centerY = height - radius - getPaddingBottom();
                break;
        }
        float left = centerX - radius;
        float top = centerY - radius;
        float right = centerX + radius;
        float bottom = centerY + radius;
        rectF.set(left, top, right, bottom);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(backgroundColor);
        // FILL填充, STROKE描边,FILL_AND_STROKE填充和描边
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(centerX, centerY, radius, mPaint);
        mPaint.setColor(progressColor);

        double percent = progress * 1.0 / 100;
        int angle = (int) (percent * 360);
        canvas.drawArc(rectF, 270, angle, true, mPaint);  //根据进度画圆弧
    }
}
