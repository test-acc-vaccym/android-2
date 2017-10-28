package com.edroplet.qxx.saneteltabactivity.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by qxs on 2017/9/24.
 */

public class CustomRadioGroupWithCustomRadioButton extends LinearLayout {
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;


    public CustomRadioGroupWithCustomRadioButton(Context context) {
        super(context);
        setOrientation(VERTICAL);
        init();
    }
    public CustomRadioGroupWithCustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof CustomRadioButton) {
            final CustomRadioButton button = (CustomRadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                int id  = button.getId();
                setCheckedId(id);
            }
        } else if (child instanceof ViewGroup) {
            final ArrayList<CustomRadioButton> buttons = findRadioButton((ViewGroup) child);
            for (CustomRadioButton button: buttons){
                if (button != null && button.isChecked()) {
                    mProtectFromCheckedChange = true;
                    if (mCheckedId != -1) {
                        setCheckedStateForView(mCheckedId, false);
                    }
                    mProtectFromCheckedChange = false;
                    int id  = button.getId();
                    setCheckedId(id);
                }
            }
        }
        super.addView(child, index, params);
    }

    /** 查找radioButton控件 */
    public ArrayList<CustomRadioButton> findRadioButton(ViewGroup group) {
        int len = group.getChildCount();
        ArrayList<CustomRadioButton> resBtn = new ArrayList<CustomRadioButton>();
        for (int i = 0; i < len; i++) {
            if (group.getChildAt(i) instanceof CustomRadioButton) {
                resBtn.add( (CustomRadioButton) group.getChildAt(i));
            } else if (group.getChildAt(i) instanceof ViewGroup) {
               for(CustomRadioButton button: findRadioButton((ViewGroup) group.getChildAt(i))) {
                   resBtn.add(button);
               }
            }
        }
        return resBtn;
    }
    /**
     * <p>
     * Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.
     * </p>
     *
     * @param id
     *            the unique id of the radio button to select in this group
     *
     * @see #getCheckedRadioButtonId()
     * @see #clearCheck()
     */
    public void check(int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }
        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }
        if (id != -1) {
            setCheckedStateForView(id, true);
        }
        setCheckedId(id);
    }
    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }
    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof CustomRadioButton) {
            ((CustomRadioButton) checkedView).setChecked(checked);
        }
    }
    /**
     * <p>
     * Returns the identifier of the selected radio button in this group. Upon
     * empty selection, the returned value is -1.
     * </p>
     *
     * @return the unique id of the selected radio button in this group
     *
     * @see #check(int)
     * @see #clearCheck()
     */
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }
    /**
     * <p>
     * Clears the selection. When the selection is cleared, no radio button in
     * this group is selected and {@link #getCheckedRadioButtonId()} returns
     * null.
     * </p>
     *
     * @see #check(int)
     * @see #getCheckedRadioButtonId()
     */
    public void clearCheck() {
        check(-1);
    }
    /**
     * <p>
     * Register a callback to be invoked when the checked radio button changes
     * in this group.
     * </p>
     *
     * @param listener
     *            the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomRadioGroupWithCustomRadioButton.LayoutParams(getContext(), attrs);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CustomRadioGroupWithCustomRadioButton.LayoutParams;
    }
    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }
    
    /**
     * <p>
     * This set of layout parameters defaults the width and the height of the
     * children to {@link #WRAP_CONTENT} when they are not specified in the XML
     * file. Otherwise, this class ussed the value read from the XML file.
     * </p>
     *
     * <p>
     * See {@link  LinearLayout
     * Attributes} for a list of all child view attributes that this class
     * supports.
     * </p>
     *
     */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h) {
            super(w, h);
        }
        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }
        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }
        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
        /**
         * <p>
         * Fixes the child's width to
         * {@link ViewGroup.LayoutParams#WRAP_CONTENT} and the
         * child's height to
         * {@link ViewGroup.LayoutParams#WRAP_CONTENT} when not
         * specified in the XML file.
         * </p>
         *
         * @param a
         *            the styled attributes set
         * @param widthAttr
         *            the width attribute to fetch
         * @param heightAttr
         *            the height attribute to fetch
         */
        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr,
                                         int heightAttr) {
            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }
            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }
    /**
     * <p>
     * Interface definition for a callback to be invoked when the checked radio
     * button changed in this group.
     * </p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>
         * Called when the checked radio button has changed. When the selection
         * is cleared, checkedId is -1.
         * </p>
         *
         * @param group
         *            the group in which the checked radio button has changed
         * @param checkedId
         *            the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(CustomRadioGroupWithCustomRadioButton group, int checkedId);
    }
    private class CheckedStateTracker implements
            CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }
            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;
            int id = buttonView.getId();
            setCheckedId(id);
        }
    }
    /**
     * <p>
     * A pass-through listener acts upon the events and dispatches them to
     * another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.
     * </p>
     */
    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;
        public void onChildViewAdded(View parent, View child) {
            if (parent == CustomRadioGroupWithCustomRadioButton.this && child instanceof CustomRadioButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = child.hashCode();
                    child.setId(id);
                }
                ((CustomRadioButton) child).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
            } else if (parent == CustomRadioGroupWithCustomRadioButton.this
                    && child instanceof ViewGroup) {
                ArrayList<CustomRadioButton> btns = findRadioButton((ViewGroup) child);
                for (CustomRadioButton btn: btns) {
                    int id = btn.getId();
                    // generates an id if it's missing
                    if (id == View.NO_ID) {
                        id = btn.hashCode();
                        btn.setId(id);
                    }
                    btn.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
                }
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            if (parent == CustomRadioGroupWithCustomRadioButton.this && child instanceof CustomRadioButton) {
                ((CustomRadioButton) child).setOnCheckedChangeListener(null);
            } else if (parent == CustomRadioGroupWithCustomRadioButton.this
                    && child instanceof ViewGroup) {
                ArrayList<CustomRadioButton> rdBtns = findRadioButton((ViewGroup) child);
                for (CustomRadioButton crb: rdBtns){
                    crb.setOnCheckedChangeListener(
                            null);
                }
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
