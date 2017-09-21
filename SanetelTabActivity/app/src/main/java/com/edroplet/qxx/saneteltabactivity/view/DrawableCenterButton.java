package com.edroplet.qxx.saneteltabactivity.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatButton;
import android.text.method.CharacterPickerDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.ChangeTypeFace;
import com.edroplet.qxx.saneteltabactivity.utils.ImageUtil;

/**
 * Created by qxs on 2017/8/27.
 * drawableLeft\drawableRight与文本一起居中显示
 */

public class DrawableCenterButton extends RelativeLayout {
    private ImageView imgView;
    private TextView textView;

    public DrawableCenterButton(Context context) {
        super(context,null);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.drawable_text_button, this,true);
        this.imgView = (ImageView)findViewById(R.id.drawable_text_image_view);
        this.textView = (TextView)findViewById(R.id.drawable_text_text_view);

        this.textView.setTypeface(ChangeTypeFace.getSimHei(this.getContext()));
        //获取所需的控件参数
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.DrawableCenterButton);
        CharSequence cs = typedArray.getText(R.styleable.DrawableCenterButton_text);
        if (cs != null)
            setText(cs.toString());
        /*
        @IdRes int resId;
        resId = typedArray.getResourceId(R.styleable.DrawableCenterButton_drawableLeft, R.drawable.ic_menu_history_lt);
        if (resId != -1)
            setImgResource(resId);
        */

        int size = typedArray.getInt(R.styleable.DrawableCenterButton_textSize, 16);
        setTextSize(size);

        Drawable drawable = typedArray.getDrawable(R.styleable.DrawableCenterButton_drawableLeft);
        if (drawable == null)
            drawable = typedArray.getDrawable(R.styleable.DrawableCenterButton_drawableStart);
        if (drawable!= null) {
            int px = ImageUtil.sp2px(context, size);
            px = px * 2;
            setImage(ImageUtil.zoomImage(drawable,px,px));
        }
        setTextColor(typedArray.getColor(R.styleable.DrawableCenterButton_textColor, Color.BLACK));
        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImage(Bitmap drawable) {
        imgView.setImageBitmap(drawable);
    }

    public void setImgResource(int resourceID) {
        this.imgView.setImageResource(resourceID);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public void setTextSize(float size) {
        this.textView.setTextSize(size);
    }
}
