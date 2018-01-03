package com.edroplet.qxx.saneteltabactivity.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;

/**
 * Created by qxs on 2017/9/7.
 */

public class TriangleView extends Drawable {
    private Paint mPaint;
    private int paintColor;
    public TriangleView(int color){
        this.paintColor = color;
    }

    @Override
    public  void draw(Canvas canvas) {
        // 创建画笔
        mPaint = new Paint();
        // 设置画笔颜色
        mPaint.setColor(paintColor);
        // 绘制这个三角形,你可以绘制任意多边形
        Path path = new Path();
        path.moveTo(0, 0);// 此点为多边形的起点
        path.lineTo(15, 30);
        path.lineTo(30, 0);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, mPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    // getIntrinsicWidth、getIntrinsicHeight主要是为了在View使用wrap_content的时候，提供一下尺寸
    @Override
    public int getIntrinsicHeight() {
        return 30;
    }
    @Override
    public int getIntrinsicWidth() {
        return 30;
    }
}
