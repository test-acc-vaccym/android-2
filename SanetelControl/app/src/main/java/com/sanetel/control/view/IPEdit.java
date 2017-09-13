package com.sanetel.control.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sanetel.control.R;

/**
 * TODO: document your custom view class.
 */
public class IPEdit extends LinearLayout {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private EditText  mIpAddrEdt1 = null;
    private EditText  mIpAddrEdt2 = null;
    private EditText  mIpAddrEdt3 = null;
    private EditText mIpAddrEdt4 = null;
    private TextView mPointTv1 = null;
    private TextView mPointTv2 = null;
    private TextView mPointTv3 = null;
    private TextWatcher mTextWatcher = null;
    private Context mParentContext = null;


    public IPEdit(Context context) {
        super(context);
        // init(null, 0);
        init();
    }

    public IPEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        // init(attrs, 0);
        init();
    }

    public IPEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.IPEdit, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.IPEdit_exampleString);
        mExampleColor = a.getColor(
                R.styleable.IPEdit_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.IPEdit_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.IPEdit_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.IPEdit_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

    public String getText()
    {
        StringBuilder  strBuild = new StringBuilder();
        String wsIp1 = mIpAddrEdt1.getText().toString();
        String wsIp2 = mIpAddrEdt2.getText().toString();
        String wsIp3 = mIpAddrEdt3.getText().toString();
        String wsIp4 = mIpAddrEdt4.getText().toString();
        String wsPoint = ".";
        strBuild.append(wsIp1);
        strBuild.append(wsPoint);
        strBuild.append(wsIp2);
        strBuild.append(wsPoint);
        strBuild.append(wsIp3);
        strBuild.append(wsPoint);
        strBuild.append(wsIp4);
        return strBuild.toString();
    }

    private void init()
    {
        mParentContext = this.getContext();
        mIpAddrEdt1 = new EditText(mParentContext);
        mIpAddrEdt2 = new EditText(mParentContext);
        mIpAddrEdt3 = new EditText(mParentContext);
        mIpAddrEdt4 = new EditText(mParentContext);
        initEditText(mIpAddrEdt1);
        initEditText(mIpAddrEdt2);
        initEditText(mIpAddrEdt3);
        initEditText(mIpAddrEdt4);
        mPointTv1 = new TextView(mParentContext);
        mPointTv2 = new TextView(mParentContext);
        mPointTv3 = new TextView(mParentContext);
        initTextView(mPointTv1);
        initTextView(mPointTv2);
        initTextView(mPointTv3);
        drawFrame();
        seftLayout();

        mTextWatcher = new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
                String wsText = null;
                EditText focusedEdt = getFocuseEdt();
                if(focusedEdt == null)
                    return;
                if (s != null && s.length() > 0) {
                    if (s.length() > 2 || s.toString().trim().contains(".")) {
                        if (s.toString().trim().contains(".")) {
                            wsText = s.toString().substring(0, s.length() - 1);
                            focusedEdt.setText(wsText);
                        }
                        else {
                            wsText = s.toString().trim();
                        }
                        if (Integer.parseInt(wsText) > 255) {
                            Toast.makeText(mParentContext, "请输入合法的ip地址",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        EditText nextFocuseEdt = getNextFocuseEdt();
                        if(nextFocuseEdt != null)
                        {
                            nextFocuseEdt.setFocusable(true);
                            nextFocuseEdt.requestFocus();
                        }else
                        {
                            return;
                        }
                    }
                    else if(s.length() >= 2 && (s.toString().startsWith("0")) == true)
                    {
                        wsText = s.toString().substring(1, s.length());
                        focusedEdt.setText(wsText);
                        focusedEdt.setSelection(wsText.length());
                    }
                }
                if (start == 0 && s.length() == 0) {
                    EditText prevEdt = getPrevFocuseEdt();
                    if(prevEdt != null)
                    {
                        prevEdt.setFocusable(true);
                        prevEdt.requestFocus();
                    }
                }
            }
        };
        addTextChangeListener();
    }

    private EditText getFocuseEdt()
    {
        if(mIpAddrEdt1.isFocused())
            return mIpAddrEdt1;
        if(mIpAddrEdt2.isFocused())
            return mIpAddrEdt2;
        if(mIpAddrEdt3.isFocused())
            return mIpAddrEdt3;
        if(mIpAddrEdt4.isFocused())
            return mIpAddrEdt4;
        return null;
    }

    private EditText getNextFocuseEdt()
    {
        EditText focusedEdt = getFocuseEdt();
        if(focusedEdt == mIpAddrEdt1)
            return mIpAddrEdt2;
        if(focusedEdt == mIpAddrEdt2)
            return mIpAddrEdt3;
        if(focusedEdt == mIpAddrEdt3)
            return mIpAddrEdt4;
        return null;
    }

    private EditText getPrevFocuseEdt()
    {
        EditText focusedEdt = getFocuseEdt();
        if(focusedEdt == mIpAddrEdt4)
            return mIpAddrEdt3;
        if(focusedEdt == mIpAddrEdt3)
            return mIpAddrEdt2;
        if(focusedEdt == mIpAddrEdt2)
            return mIpAddrEdt1;
        return null;

    }


    private void drawFrame()
    {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(1, 0xFFC8C7C7);  //
        drawable.setColor(0xFFFFFFFF);
        this.setBackground(drawable);
    }

    private void initEditText(EditText edtText)
    {
        edtText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        edtText.setMaxLines(1);
        edtText.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtText.setBackground(null);
        edtText.setGravity(Gravity.CENTER);
    }

    private void initTextView(TextView textView)
    {
        textView.setBackground(null);
        textView.setText(".");
    }

    private void seftLayout()
    {
        this.setOrientation(LinearLayout.HORIZONTAL);
        edtTextLayout(mIpAddrEdt1);
        textViewLayout(mPointTv1);
        edtTextLayout(mIpAddrEdt2);
        textViewLayout(mPointTv2);
        edtTextLayout(mIpAddrEdt3);
        textViewLayout(mPointTv3);
        edtTextLayout(mIpAddrEdt4);
    }

    private void edtTextLayout(EditText edt)
    {
        int lHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
        int lWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams  lp = new LinearLayout.LayoutParams(lHeight, lWidth);
        lp.weight = 1;
        this.addView(edt, lp);
    }

    private void textViewLayout(TextView txtView)
    {
        int lHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
        int lWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams  lp = new LinearLayout.LayoutParams(lHeight, lWidth);
        this.addView(txtView, lp);
    }

    private void addTextChangeListener()
    {
        mIpAddrEdt1.addTextChangedListener(mTextWatcher);
        mIpAddrEdt2.addTextChangedListener(mTextWatcher);
        mIpAddrEdt3.addTextChangedListener(mTextWatcher);
        mIpAddrEdt4.addTextChangedListener(mTextWatcher);
    }
}
