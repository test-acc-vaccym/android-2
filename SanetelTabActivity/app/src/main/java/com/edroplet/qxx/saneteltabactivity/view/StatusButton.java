package com.edroplet.qxx.saneteltabactivity.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
/**
 * Created by qxs on 2017/8/27.
 * drawableLeft\drawableRight与文本一起居中显示
 * http://ask.csdn.net/questions/197
 *
 */

public class StatusButton extends AppCompatButton {
    private int operator_color = Color.BLUE;
    public static final int BUTTON_STATE_UNKNOWN = -1;
    public static final int BUTTON_STATE_NORMAL = 0;
    public static final int BUTTON_STATE_SPECIAL = 1;
    public static final int BUTTON_STATE_ABNORMAL = 2;
    public static final int BUTTON_STATE_OPERATE = 3;
    public static final int BUTTON_STATE_DISABLE = 4;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BUTTON_STATE_UNKNOWN, BUTTON_STATE_NORMAL, BUTTON_STATE_SPECIAL, BUTTON_STATE_ABNORMAL, BUTTON_STATE_OPERATE,BUTTON_STATE_DISABLE})
    public @interface ButtonState {
    }

    @ButtonState
    private int mButtonState = BUTTON_STATE_UNKNOWN;

    private static int []ext_attr_normal = { R.attr.state_color_normal };
    private static int []ext_attr_abnormal = { R.attr.state_color_abnormal };
    private static int []ext_attr_special = { R.attr.state_color_special };
    private static int []ext_attr_operate = { R.attr.state_color_operate };
    private static int []ext_attr_disable = { R.attr.state_color_disable };

    private float mDrawableWidth;
    private float mDrawableHight;
    private float mDrawableSpace;
    private Bitmap mBitmap;
    private float mTxtSize;
    private Drawable[] mDrawables;
    Drawable mDrawableLeft, mDrawableTop, mDrawableRight, mDrawableBottom;

    @Nullable
    private StateListener mListener;

    // 对话框
    private NiftyDialogBuilder dialogBuilder;

    public StatusButton(Context context) {
        super(context);
    }

    public StatusButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    public StatusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public void setButtonState(@ButtonState int state) {
        mButtonState = state;
        refreshDrawableState();
        // if (mListener != null) mListener.onStateChanged(mButtonState);
    }

    @Override
    public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        super.addOnAttachStateChangeListener(listener);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public void init(Context context, AttributeSet attributeSet) {
        //获取所需的控件参数
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet,R.styleable.StatusButton);
        // int imgId = typedArray.getResourceId(R.styleable.StatusButton_drawable_space, 0);
        //如果没指定img直接返回
        // if (imgId == 0)
        //     return;
        //获得位图
        // mBitmap = BitmapFactory.decodeResource(getResources(), imgId);
        mTxtSize = this.getTextSize();
        //将默认值设置到配置文件中
        float dimenHeight = context.getResources().getDimension(R.dimen.status_button_height);
        float dimenSpace = context.getResources().getDimension(R.dimen.status_button_space);
        mDrawableSpace = typedArray.getDimension( R.styleable.StatusButton_drawable_space, dimenSpace);
        mDrawableHight = typedArray.getDimension( R.styleable.StatusButton_drawable_height, dimenHeight);
        mDrawableWidth = typedArray.getDimension( R.styleable.StatusButton_drawable_width, dimenHeight);
        // 获取button状态
        int buttonState = typedArray.getInt(R.styleable.StatusButton_state_color, BUTTON_STATE_UNKNOWN);
        switch (buttonState) {
            case BUTTON_STATE_NORMAL:
                this.mButtonState = BUTTON_STATE_NORMAL;
                break;

            case BUTTON_STATE_SPECIAL:
                this.mButtonState = BUTTON_STATE_SPECIAL;
                break;

            case BUTTON_STATE_ABNORMAL:
                this.mButtonState = BUTTON_STATE_ABNORMAL;
                break;

            case BUTTON_STATE_OPERATE:
                this.mButtonState = BUTTON_STATE_OPERATE;
                break;

            case BUTTON_STATE_DISABLE:
                this.mButtonState = BUTTON_STATE_DISABLE;
                break;

            default:
                this.mButtonState = BUTTON_STATE_UNKNOWN;
                break;
        }

        mDrawables = getCompoundDrawables();
        if (mDrawables != null) {
            mDrawableLeft = mDrawables[0];
            mDrawableTop = mDrawables[1];
            mDrawableRight = mDrawables[2];
            mDrawableBottom = mDrawables[3];
            /**
             * 没卵用
            if(mDrawableLeft != null){
                // bd = (BitmapDrawable) mDrawableLeft;
                mBitmap = drawableToBitmap(mDrawableLeft);
            }else if(mDrawableRight != null){
                // bd = (BitmapDrawable) mDrawableRight;
                mBitmap = drawableToBitmap(mDrawableRight);
            }
            if (bd != null) {
                mBitmap = bd.getBitmap();
            }
            */
        }
        //if (mBitmap == null){
        //    mBitmap = drawableToBitmap(ResourcesCompat.getDrawable(this.getResources(),R.drawable.triangle, null));
        //}

        // 下面两行可以考虑删除，然后手动设置android:layout_height
        // 重新定义View的高度,图片高度+3倍的文本高度，这样设置个人感觉很不错，mDrawableSpace是额外指定的高度
        // this.setHeight((int) (mTxtSize * 3 + mDrawableSpace * 2 + mDrawableHight));
        // 虽然知道这个时候getMeasuredWidth()的值是0，还是设置一下
        this.setWidth(getMeasuredWidth());
        // 释放资源
        typedArray.recycle();
        // 关键的一步， 在这儿才能修改状态啊， 被搞死了。
        setButtonState(mButtonState);

        if (mDrawables != null) {
            //if (mDrawableLeft != null) {
            // int [] s = getDrawableState();
            // mDrawableLeft.setState(s);
            //float textWidth = getPaint().measureText(getText().toString());
            // drawableWidth = drawableLeft.getIntrinsicWidth();
            //float bodyWidth = textWidth + mDrawableWidth + drawablePadding;
            //canvas.translate((getWidth() - bodyWidth) / 2, 0);
            //}
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        // 这种方式不行！ mergeDrawableStates(drawableState, STATE_COLOR);
        switch (this.mButtonState){
            case BUTTON_STATE_NORMAL:
                mergeDrawableStates(drawableState,ext_attr_normal);
                break;
            case BUTTON_STATE_SPECIAL:
                mergeDrawableStates(drawableState,ext_attr_special);
                break;
            case BUTTON_STATE_ABNORMAL:
                mergeDrawableStates(drawableState,ext_attr_abnormal);
                break;
            case BUTTON_STATE_OPERATE:
                mergeDrawableStates(drawableState,ext_attr_operate);
                break;
            case BUTTON_STATE_DISABLE:
                mergeDrawableStates(drawableState, ext_attr_disable);
            default:
                break;
        }
        return drawableState;
    }

    /**
     * Returns the current {@link com.edroplet.qxx.saneteltabactivity.view.StatusButton}
     *
     * @return
     */
    @ButtonState
    public int getButtonState() {
        return mButtonState;
    }


    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        int boundRight = (int)mDrawableWidth;
        int boundBottom = (int) mDrawableHight;

        int []s = getDrawableState();
        int color = Color.BLUE;
        // 设置为不可点击, 没卵用
        // setClickable(false);
        setFocusable(false);
        // setEnabled(false);

        if (s[s.length-1] == ext_attr_normal[0]){
            color = ContextCompat.getColor(getContext(), R.color.status_normal);
        }else if (s[s.length-1] == ext_attr_special[0]){
            color = ContextCompat.getColor(getContext(), R.color.status_special);
        }else if (s[s.length-1] == ext_attr_abnormal[0]){
            color = ContextCompat.getColor(getContext(), R.color.status_abnormal);
        }else if (s[s.length-1] == ext_attr_operate[0]){
            // 只有操作按钮可以点击

            color = ContextCompat.getColor(getContext(), R.color.status_operate);
            setClickable(true);
            setFocusable(true);
            // setEnabled(true);
        }else {
            color = ContextCompat.getColor(getContext(), R.color.operate_disabled_color);
            setClickable(false);
        }
        // 设置字体颜色
        setTextColor(color);
        // 设置字体大小
        setTextSize(getResources().getDimension(R.dimen.status_bar_text_size));
        // 设置文字
        switch (getId()) {
            case R.id.status_bar_button_communication_state:
                if (mButtonState == BUTTON_STATE_NORMAL) setText(R.string.communication_state_connected);
                else setText(R.string.communication_state_disconnected);
            break;
            case R.id.status_bar_button_antenna_state:
                if (mButtonState == BUTTON_STATE_NORMAL) setText(R.string.antenna_state_exploded);
                else if (mButtonState == BUTTON_STATE_ABNORMAL) setText(R.string.antenna_state_abnormal);
                break;
            case R.id.status_bar_button_bd_state:
                if (mButtonState == BUTTON_STATE_NORMAL) setText(R.string.bd_state_enabled);
                else setText(R.string.bd_state_disabled);
                break;
            case R.id.status_bar_button_locker_state:
                if (mButtonState == BUTTON_STATE_NORMAL) setText(R.string.locker_state_released);
                else setText(R.string.locker_state_locked);
                break;
            case R.id.status_bar_button_power_state:
                if (mButtonState == BUTTON_STATE_NORMAL) setText(R.string.power_state_saved);
                else setText(R.string.power_state_charged);
                break;
        }
        // 自适应尺寸
        if (left!=null){
            // http://blog.sina.com.cn/s/blog_5da93c8f01012pkj.html
            // 取两层绘制交集。显示上层。
            left.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            left.setBounds(0,0,boundRight,boundBottom);
        }
        if (top!=null){
            top.setBounds(0,0,boundRight,boundBottom);
            // top.setColorFilter(color, PorterDuff.Mode.DST_IN);
        }
        if (right!=null){
            right.setBounds(0,0,boundRight,boundBottom);
            // right.setColorFilter(color, PorterDuff.Mode.DST_IN);
        }
        if (bottom!=null){
            bottom.setBounds(0,0,boundRight,boundBottom);
            // bottom.setColorFilter(color, PorterDuff.Mode.DST_IN);
        }
        setCompoundDrawables(left,top,right,bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 先画图，然后再设置位置
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft, mDrawableTop, mDrawableRight, mDrawableBottom);

        int drawablePadding = getCompoundDrawablePadding();
        //if (mDrawableRight != null) {
        float textWidth = getPaint().measureText(getText().toString());
        float bodyWidth = textWidth + mDrawableWidth + drawablePadding;
        // 居中
        int paddingVertical = (int)(getHeight()-mDrawableWidth)/2;
        int paddingHorizontal = (int)(getWidth()-bodyWidth)/2;
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
        canvas.translate((getWidth() - bodyWidth) / 2, 0);
        //}
        super.onDraw(canvas);
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * Sets the {@link StateListener} for the view
     *
     * @param listener The {@link StateListener} that will receive callbacks
     */
    public void setStateListener(StateListener listener) {
        mListener = listener;
    }

    public interface StateListener {
        /**
         * Callback for when the {@link ButtonState} has changed
         *
         * @param buttonState The {@link ButtonState} that was switched to
         */
        void onStateChanged(@ButtonState int buttonState);
    }

//    @Override
//    public void setOnClickListener(@Nullable OnClickListener l) {
//        super.setOnClickListener(l);
//        onConfirm("确定打开？", new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dialogBuilder != null)  dialogBuilder.dismiss();
//            }
//        });
//    }
    public NiftyDialogBuilder getDialogBuilder(){
        return dialogBuilder;
    }
    public boolean onConfirm(String message, View.OnClickListener listener){
        final String []type = {"Fadein", "Slideleft", "Slidetop", "SlideBottom", "Slideright", "Fall", "Newspager", "Fliph", "Flipv", "RotateBottom", "RotateLeft", "Slit", "Shake", "Sidefill"};
        int i= (int) (type.length*Math.random());
        Effectstype effect = null;
        switch (i){
            case 0:effect=Effectstype.Fadein;break;
            case 1:effect=Effectstype.Slideright;break;
            case 2:effect=Effectstype.Slideleft;break;
            case 3:effect=Effectstype.Slidetop;break;
            case 4:effect=Effectstype.SlideBottom;break;
            case 5:effect=Effectstype.Newspager;break;
            case 6:effect=Effectstype.Fall;break;
            case 7:effect=Effectstype.Sidefill;break;
            case 8:effect=Effectstype.Fliph;break;
            case 9:effect=Effectstype.Flipv;break;
            case 10:effect=Effectstype.RotateBottom;break;
            case 11:effect=Effectstype.RotateLeft;break;
            case 12:effect=Effectstype.Slit;break;
            case 13:effect= Effectstype.Shake;break;
        }
        Context mContext = getContext();
        dialogBuilder = NiftyDialogBuilder.getInstance(getContext());
        dialogBuilder
                // 重点设置
                .withEffect(effect)        //设置对话框弹出样式
                //.setCustomView(R.layout.custom, MainActivity.this) // 设置自定义对话框的布局
                .withDuration(getResources().getInteger(R.integer.operate_confirm_animate_time))              //动画显现的时间（时间长就类似放慢动作）
                // 基本设置
                .withTitle(getResources().getString(R.string.operate_confirm_title))         //设置对话框标题
                .withTitleColor(ContextCompat.getColor(mContext, R.color.operate_confirm_title_color))          //设置标题字体颜色
                .withDividerColor(ContextCompat.getColor(mContext, R.color.operate_confirm_divider_color))      //设置分隔线的颜色
                .withMessage(message)//设置对话框显示内容
                .withMessageColor(ContextCompat.getColor(mContext, R.color.operate_confirm_message_color))       //设置消息字体的颜色
                .withDialogColor(ContextCompat.getColor(mContext, R.color.operate_confirm_dialog_color))        //设置对话框背景的颜色
                //.withIcon(getResources().getDrawable(R.drawable.logo)) //设置标题的图标
                // 设置是否模态，默认false，表示模态，
                //要求必须采取行动才能继续进行剩下的操作 | isCancelable(true)
                .isCancelableOnTouchOutside(true)
                .withButton1Text(getResources().getString(R.string.operate_confirm_ok))             //设置按钮1的文本
                .withButton2Text(getResources().getString(R.string.operate_confirm_cancel))         //设置按钮2的文本
                .setButton1Click(listener)
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.operate_confirm_cancel_toast), Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                    }
                })
                .show();
        return true;
    }

    public void toggleClickable(){
        if (mButtonState !=BUTTON_STATE_OPERATE && mButtonState != BUTTON_STATE_DISABLE) return;

        if (mButtonState == BUTTON_STATE_DISABLE) {
            mButtonState = BUTTON_STATE_OPERATE;
        }else{
            mButtonState = BUTTON_STATE_DISABLE;
        }
    }
}
