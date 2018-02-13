package com.edroplet.sanetel.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.edroplet.sanetel.utils.ChangeTypeFace;

/**
 * Created by qxs on 2017/11/9.
 * 调整字符宽度
 * 设置
 */

public class JustifyTextView extends AppCompatTextView {
    private int mLineY;
    private int mViewWidth;
    private int mViewHeight;
    public static final String TWO_CHINESE_BLANK = "  ";

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void init() {
        if (!isInEditMode()) {
            setTypeface(ChangeTypeFace.getSimHei(this.getContext()));
        }
        // 3dp or 1.5倍
        setLineSpacing(3,1.2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        float textSize = getTextSize();
        String text = getText().toString();
        // 居中
        mLineY = (int)(mViewHeight-textSize)/2;
        mLineY += textSize;
        Layout layout = getLayout();

        // layout.getLayout()在4.4.3出现NullPointerException
        if (layout == null) {
            return;
        }

        Paint.FontMetrics fm = paint.getFontMetrics();

        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout
                .getSpacingAdd());

        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float width = StaticLayout.getDesiredWidth(text, lineStart,
                    lineEnd, getPaint());
            String line = text.substring(lineStart, lineEnd);
            if (needScale(line)) {
                drawScaledText(canvas, lineStart, line, width);
            } else {
                canvas.drawText(line, 0, mLineY, paint);
            }
            mLineY += textHeight;
        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line,
                                float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());
            float bw = StaticLayout.getDesiredWidth(blanks, getPaint());
            x += bw;

            line = line.substring(3);
        }

        int gapCount = line.length() - 1;
        int i = 0;
        if (line.length() > 2 && line.charAt(0) == 12288
                && line.charAt(1) == 12288) {
            String substring = line.substring(0, 2);
            float cw = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mLineY, getPaint());
            x += cw;
            i += 2;
        }

        float d = (mViewWidth - lineWidth) / gapCount;
        for (; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' '
                && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        if (line == null || line.length() == 0) {
            return false;
        } else {
            return line.charAt(line.length() - 1) != '\n';
        }
    }
}
