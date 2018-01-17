package com.edroplet.sanetel.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edroplet.sanetel.R;

/**
 * Created by qxs on 2017/8/31.
 */

public class EDropletToolbar extends Toolbar {
    private Context mContext;
    //添加布局必不可少的工具
    private LayoutInflater mInflater;
    //搜索框
    // private com.edroplet.sanetel.view.custom.CustomEditText mEditSearchView;
    //标题
    private com.edroplet.sanetel.view.custom.CustomTextView mTextTitle;
    //右边按钮
    private com.edroplet.sanetel.view.custom.CustomButton mRightButton;
    //左边按钮
    private com.edroplet.sanetel.view.custom.CustomButton mLeftButton;
    private View mView;

    public  EDropletToolbar(Context context){
        this(context, null);
    }

    public EDropletToolbar(Context context, @Nullable AttributeSet attes){
        this(context, attes, 0);
    }

    public EDropletToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        // 设置边距
        setContentInsetsRelative(0, 0);
        if (attrs != null) {
            // 读取自定义attrs文件
            final TintTypedArray tta = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.EdropletToolbar, defStyleAttr, 0);
            final int leftButtonText = tta.getResourceId(R.styleable.EdropletToolbar_leftButtonText, R.string.toolbar_button_default_text);
            final int leftButtonGravity = tta.getInt(R.styleable.EdropletToolbar_leftButtonGravity, 1);
            final Drawable leftIcon = tta.getDrawable(R.styleable.EdropletToolbar_leftButtonIcon);
            leftIcon.setBounds(0, 0, leftIcon.getMinimumWidth(), leftIcon.getMinimumHeight());
            setButtonIcon(mLeftButton, leftIcon,leftButtonText,leftButtonGravity);

            final Drawable rightIcon = tta.getDrawable(R.styleable.EdropletToolbar_rightButtonIcon);
            rightIcon.setBounds(0, 0, rightIcon.getMinimumWidth(), rightIcon.getMinimumHeight());
            final int rightButtonText = tta.getResourceId(R.styleable.EdropletToolbar_rightButtonText, R.string.toolbar_button_default_text);
            final int rightButtonGravity = tta.getInt(R.styleable.EdropletToolbar_rightButtonGravity, 3);
            setButtonIcon(mRightButton,rightIcon,rightButtonText, rightButtonGravity);


            boolean isShowSearchView = tta.getBoolean(R.styleable.EdropletToolbar_isShowSearchView, false);

            //如果要显示searchView的时候
            if (isShowSearchView) {
                showSearchView();
                hideTitleView();
            }
            //资源的回收
            tta.recycle();
        }
    }

    private void initView(){
        if (mView == null) {
            //初始化
            mInflater = LayoutInflater.from(getContext());
            //添加布局文件
            mView = mInflater.inflate(R.layout.status_bar, null);

            //绑定控件
            //mEditSearchView = (com.edroplet.sanetel.view.custom.CustomEditText) mView.findViewById(R.id.edroplet_toolbar_search);
            mTextTitle = (com.edroplet.sanetel.view.custom.CustomTextView) mView.findViewById(R.id.edroplet_toolbar_title);
            mLeftButton = (com.edroplet.sanetel.view.custom.CustomButton) mView.findViewById(R.id.status_bar_button_communication_state);
            mRightButton = (com.edroplet.sanetel.view.custom.CustomButton) mView.findViewById(R.id.edroplet_toolbar_right_button);


            //然后使用LayoutParams把控件添加到子view中
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);

        }
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            mTextTitle.setBackgroundColor(ContextCompat.getColor(mContext,R.color.button_blink));
            mTextTitle.setCompoundDrawables(ContextCompat.getDrawable(mContext, R.drawable.triangle) ,null,null,null);
            showTitleView();
        }
    }

    //隐藏标题
    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);
    }

    //显示标题
    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }

    //显示搜索框
    public void showSearchView() {
        /*if (mEditSearchView != null)
            mEditSearchView.setVisibility(VISIBLE);*/
    }

    //隐藏搜索框
    public void hideSearchView() {
        /*if (mEditSearchView != null) {
            mEditSearchView.setVisibility(GONE);
        }*/
    }

    //给右侧按钮设置图片，也可以在布局文件中直接引入
    // 如：app:leftButtonIcon="@drawable/icon_back_32px"
    //    public void setRightButtonIcon(Drawable icon) {
    //        if (mRightButton != null) {
    //            mRightButton.setImageDrawable(icon);
    //            mRightButton.setVisibility(VISIBLE);
    //        }
    //    }

    //给左侧按钮设置图片，也可以在布局文件中直接引入
    private void setButtonIcon(com.edroplet.sanetel.view.custom.CustomButton button, Drawable icon, int textResourceId, int drawGravity) {

        if (button != null){
            if (drawGravity == 1) {
                button.setCompoundDrawables(icon, null, null, null);
            }else if (drawGravity == 2) {
                button.setCompoundDrawables(null, icon, null, null);
            }else if (drawGravity == 3) {
                button.setCompoundDrawables(null, null, icon, null);
            }else if (drawGravity == 4) {
                button.setCompoundDrawables(null, null, null, icon);
            }
            button.setText(textResourceId);
            button.setVisibility(VISIBLE);
        }
    }

    //设置右侧按钮监听事件
    public void setRightButtonOnClickLinster(OnClickListener linster) {
        mRightButton.setOnClickListener(linster);
    }

    //设置左侧按钮监听事件
    public void setLeftButtonOnClickLinster(OnClickListener linster) {
        mLeftButton.setOnClickListener(linster);
    }

    public enum STATES{
        NORMAL,
        SPECIAL,
        ABNORMAL
    };

    public  void setButtonState(@IdRes int resId, STATES state, @IdRes int textRes){
        com.edroplet.sanetel.view.custom.CustomButton btn = (com.edroplet.sanetel.view.custom.CustomButton) findViewById(resId);
        int color = ContextCompat.getColor(mContext,(R.color.status_normal));
        switch (state){
            case SPECIAL:
                color = ContextCompat.getColor(mContext,(R.color.status_special));
                break;
            case ABNORMAL:
                color = ContextCompat.getColor(mContext,(R.color.status_abnormal));
                break;
        }
        final TriangleView triangle = new TriangleView(color);
        setButtonIcon(btn, triangle, textRes,1);

    }
}
