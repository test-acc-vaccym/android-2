package com.edroplet.sanetel.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edroplet.sanetel.R;

/**
 * Created by qxx on 2017/10/20.
 */

public class EDropletDialogBuilder {

    private Context mContext;
    private EditText mEditText;
    private BaseAdapter adapter;
    private View inputLine;
    private View customView;

    public static final int CONFIRM = 100;//确认提示框
    public static final int INPUT = 200;//输入框
    public static final int CHOICE = 300;//选择

    private String neutralButtonText = "?";
    private String positiveButtonText = "确定";
    private String negativeButtonText = "取消";

    private int positiveTextColor = Color.parseColor("#333333");
    private int negativeTextColor = Color.parseColor("#333333");
    private int neutralTextColor = Color.parseColor("#333333");

    private int buttonSize = 14;//button字体大小
    private boolean isButtonCenter;//button居中

    private String message;
    private int messageSize = 15;
    private int messageColor = Color.BLACK;
    private boolean isMessageCenter;//信息居中
    private boolean isMessageBold = false;

    private String title;
    private int titleSize = 16;
    private int titleColor = Color.BLACK;
    private boolean isTitleCenter;//标题是否居中
    private boolean isTitleBold = true;

    private int titleBackgroundColor = Color.WHITE;
    private int messageBackgroundColor = Color.WHITE;
    private int dialogBackgroundColor = Color.WHITE;
    private int buttonBackgroundColor = Color.WHITE;

    private String inputText;
    private int inputTextSize = 15;
    private int inputTextColor = Color.parseColor("#333333");
    private String inputHintText;
    private int inputHintTextColor = Color.parseColor("#c1c1c1");
    private int inputType;
    private int inputLineColor = Color.parseColor("#003333");

    private boolean hasDivider=true;//选择时，是否有分割线
    private int dividerHeight = 1;
    private int dividerColor = Color.parseColor("#c1c1c1");

    private String[] items;
    private int itemColor = Color.parseColor("#333333");
    private int itemSize = 14;
    private int itemGravity = Gravity.LEFT;
    private int itemHeight = 50;
    private boolean cancelable = true;


    public EDropletDialogBuilder(Context context) {
        this.mContext = context;
        titleBackgroundColor = ContextCompat.getColor(mContext, R.color.operate_confirm_divider_color);
        messageColor = ContextCompat.getColor(mContext, R.color.operate_confirm_message_color);
        dialogBackgroundColor = ContextCompat.getColor(mContext, R.color.operate_confirm_divider_color);
        dividerColor = ContextCompat.getColor(mContext, R.color.defDividerColor);
        buttonBackgroundColor  = ContextCompat.getColor(mContext, R.color.defTextColor);
    }

    private OnConfirmListener onConfirmListener;
    private OnInputListener onInputListener;

    private OnChoiceListener onChoiceListener;

    public AlertDialog create(final int type) {
        if (type == CONFIRM) {
            if (mNeutralButtonListener == null ){
                mNeutralButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setButtonClick(type, 2);
                    }
                };
            }

            if (mNegativeButtonListener == null ){
                mNegativeButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setButtonClick(type, 0);
                    }
                };
            }

            if (mPositiveButtonListener == null){
                mPositiveButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setButtonClick(type, 1);
                    }
                };
            }
        }else if (type == INPUT) {
            customView = LayoutInflater.from(mContext).inflate(R.layout.input_layout, null);
        } else if (type == CHOICE) {
            positiveButtonText = null;
            negativeButtonText = null;
            customView = LayoutInflater.from(mContext).inflate(R.layout.choice_layout, null);
        } else {
            throw new RuntimeException("目前只支持CONFIRM，INPUT和CHOICE三种弹窗！");
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setTitle(title)
                .setView(customView)
                .setCancelable(cancelable)
                .setNeutralButton(neutralButtonText,mNeutralButtonListener)
                .setNegativeButton(negativeButtonText, mNegativeButtonListener)
                .setPositiveButton(positiveButtonText, mPositiveButtonListener).create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                setDetails(alertDialog, type);
            }
        });

        return alertDialog;
    }

    private void setButtonClick(int type, int which) {
        if (type == INPUT && onInputListener != null) {
            String inputString = mEditText.getText().toString();
            onInputListener.onClick(inputString, which);
        }

        if (type == CONFIRM && onConfirmListener != null) {
            onConfirmListener.onClick(which);
        }

    }


    private void setDetails(AlertDialog alertDialog, int type) {
        switch (type) {
            case CONFIRM:
                setButtonStyle(alertDialog);
                setTitleStyle(alertDialog);
                setMessageStyle(alertDialog);
                break;
            case INPUT:
                setButtonStyle(alertDialog);
                setTitleStyle(alertDialog);
                setInputStyle();
                popupSoftInput(mContext, mEditText);
                break;
            case CHOICE:
                setListViewStyle(alertDialog);
                break;
        }

    }

    private void setListViewStyle(final AlertDialog alertDialog) {
        // R.layout.choice_layout
        ListView listView = (ListView) customView.findViewById(R.id.listview);
        listView.setAdapter(adapter == null ? new AAdapter() : adapter);
        if (hasDivider) {
            listView.setDivider(new ColorDrawable(dividerColor));
            listView.setDividerHeight(dividerHeight);
        } else {
            listView.setDividerHeight(0);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onChoiceListener != null) {
                    onChoiceListener.onClick(items == null ? null : items[position], position);
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void setInputStyle() {
        // R.layout.input_layout
        mEditText = (EditText) customView.findViewById(R.id.input);
        inputLine = customView.findViewById(R.id.input_line);

        mEditText.setText(inputText);
        mEditText.setTextColor(inputTextColor);
        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, inputTextSize);
        mEditText.setHint(inputHintText);
        mEditText.setHintTextColor(inputHintTextColor);
        if (inputType != 0) {
            mEditText.setInputType(inputType);
        }
        mEditText.setSelection(inputText == null ? 0 : inputText.length());
        inputLine.setBackgroundColor(inputLineColor);

    }

    private Button positiveButton;
    private Button negativeButton;
    private Button neutralButton;

    private void setButtonStyle(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        // abc_alert_dialog_button_bar_material.xml
        // C:\Users\yonghuming\.gradle\caches\transforms-1\files-1.1\appcompat-v7-26.0.0-alpha1.aar\845d5e8af19752f0a4aea565ccfe1f5d\res\layout\abc_alert_dialog_material.xml
        ScrollView buttonPanel = (ScrollView) window.findViewById(R.id.buttonPanel);
        if (buttonPanel != null){
            buttonPanel.setBackgroundColor(dialogBackgroundColor);
        }
        positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        neutralButton.setText(neutralButtonText);
        neutralButton.setBackgroundColor(buttonBackgroundColor);
        negativeButton.setText(negativeButtonText);
        negativeButton.setBackgroundColor(buttonBackgroundColor);
        positiveButton.setText(positiveButtonText);
        positiveButton.setBackgroundColor(buttonBackgroundColor);

        negativeButton.setTextColor(negativeTextColor);
        positiveButton.setTextColor(positiveTextColor);
        neutralButton.setTextColor(neutralTextColor);

        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
        neutralButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, buttonSize);
        if (mNeutralButtonListener == null ){
            neutralButton.setVisibility(View.VISIBLE);
        }
        if (isButtonCenter) {
            Button button3 = (Button) window.findViewById(android.R.id.button3);
            Space space = (Space) window.findViewById(R.id.spacer);

            button3.setVisibility(View.GONE);
            space.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            negativeButton.setLayoutParams(lp);
            positiveButton.setLayoutParams(lp);
        }
    }

    private void setTitleStyle(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        TextView titleView = (TextView) window.findViewById(R.id.alertTitle);
        LinearLayout titleTemplate = window.findViewById(R.id.title_template);

        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);

        // 标题背景
        titleView.setTextColor(titleColor);
        if (titleTemplate != null)
            titleTemplate.setBackgroundColor(titleBackgroundColor);
        // set bold
        TextPaint textPaint = titleView.getPaint();
        textPaint.setFakeBoldText(true);

        if (isTitleCenter) {
            ImageView imageView = (ImageView) window.findViewById(android.R.id.icon);
            imageView.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    private void setMessageStyle(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        TextView messageView = (TextView) window.findViewById(android.R.id.message);
        AlertDialogLayout parentPanel = window.findViewById(R.id.parentPanel);
        if (parentPanel != null){
            parentPanel.setBackgroundColor(messageBackgroundColor);
        }

        messageView.setTextSize(TypedValue.COMPLEX_UNIT_SP, messageSize);
        messageView.setTextColor(messageColor);

        if (isMessageCenter) {
            messageView.setGravity(Gravity.CENTER);
        }
    }

    private void popupSoftInput(final Context context, final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, 0);
            }
        }, 10);
    }


    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnConfirmListener {
        void onClick(int which);//whichButton:0,1

    }

    public interface OnInputListener {
        void onClick(String inputText, int which);
    }

    public interface OnChoiceListener {
        void onClick(String item, int which);
    }


    class AAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items == null ? 0 : items.length;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.ll);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(mContext, itemHeight));
            linearLayout.setLayoutParams(layoutParams);
            TextView itemView = (TextView) convertView.findViewById(R.id.text1);
            itemView.setText(items[position]);
            itemView.setTextColor(itemColor);
            itemView.setGravity(itemGravity | Gravity.CENTER_VERTICAL);
            itemView.setTextSize(TypedValue.COMPLEX_UNIT_SP, itemSize);
            return convertView;
        }
    }


    /**
     * Set a listener to be invoked when the neutral button of the dialog is pressed.
     * @param text The text to display in the neutral button
     * @param listener The {@link DialogInterface.OnClickListener} to use.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    private DialogInterface.OnClickListener mNeutralButtonListener = null;
    public EDropletDialogBuilder setNeutralButton(CharSequence text, final DialogInterface.OnClickListener listener) {
        neutralButtonText = text.toString();
        mNeutralButtonListener = listener;
        return this;
    }

    private DialogInterface.OnClickListener mPositiveButtonListener = null;
    public EDropletDialogBuilder setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
        positiveButtonText = text.toString();
        mPositiveButtonListener = listener;
        return this;
    }

    private DialogInterface.OnClickListener mNegativeButtonListener = null;
    public EDropletDialogBuilder setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
        negativeButtonText = text.toString();
        mNegativeButtonListener = listener;
        return this;
    }

    /****************参数statr*****************/


    public EDropletDialogBuilder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }


    public EDropletDialogBuilder setButtonCenter(boolean buttonCenter) {
        isButtonCenter = buttonCenter;
        return this;
    }


    public EDropletDialogBuilder setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    public EDropletDialogBuilder setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
        return this;
    }

    public EDropletDialogBuilder setOnChoiceListener(OnChoiceListener onChoiceListener) {
        this.onChoiceListener = onChoiceListener;
        return this;
    }

    public EDropletDialogBuilder setInputLineColor(int inputLineColor) {
        this.inputLineColor = inputLineColor;
        return this;
    }

    public EDropletDialogBuilder setInputHintText(String inputHintText) {
        this.inputHintText = inputHintText;
        return this;
    }

    public EDropletDialogBuilder setInputHintTextColor(int inputHintTextColor) {
        this.inputHintTextColor = inputHintTextColor;
        return this;
    }

    public EDropletDialogBuilder setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }

    public EDropletDialogBuilder setMessageCenter(boolean messageCenter) {
        isMessageCenter = messageCenter;
        return this;
    }

    public EDropletDialogBuilder setItems(String[] items) {
        this.items = items;
        return this;
    }

    public EDropletDialogBuilder setItemColor(int itemColor) {
        this.itemColor = itemColor;
        return this;
    }

    public EDropletDialogBuilder setItemSize(int itemSize) {
        this.itemSize = itemSize;
        return this;
    }

    public EDropletDialogBuilder setItemGravity(int itemGravity) {
        this.itemGravity = itemGravity;
        return this;
    }

    public EDropletDialogBuilder setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public EDropletDialogBuilder setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public EDropletDialogBuilder setNeutralButtonText(String neutralButtonText) {
        this.neutralButtonText = neutralButtonText;
        return this;
    }

    public EDropletDialogBuilder setPositiveTextColor(int positiveTextColor) {
        this.positiveTextColor = positiveTextColor;
        return this;
    }

    public EDropletDialogBuilder setNegativeTextColor(int negativeTextColor) {
        this.negativeTextColor = negativeTextColor;
        return this;
    }

    public EDropletDialogBuilder setNeutralTextColor(int neutralTextColor) {
        this.neutralTextColor = neutralTextColor;
        return this;
    }

    public EDropletDialogBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public EDropletDialogBuilder setMessageSize(int sp) {
        this.messageSize = sp;
        return this;
    }

    public EDropletDialogBuilder setMessageColor(int messageColor) {
        this.messageColor = messageColor;
        return this;
    }

    public EDropletDialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EDropletDialogBuilder setTitleSize(int sp) {
        this.titleSize = sp;
        return this;
    }

    public EDropletDialogBuilder setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public EDropletDialogBuilder setTitleCenter(boolean titleCenter) {
        isTitleCenter = titleCenter;
        return this;
    }

    public EDropletDialogBuilder setButtonSize(int buttonSize) {
        this.buttonSize = buttonSize;
        return this;
    }

    public EDropletDialogBuilder setInputText(String inputText) {
        this.inputText = inputText;
        return this;
    }

    public EDropletDialogBuilder setInputTextColor(int inputTextColor) {
        this.inputTextColor = inputTextColor;
        return this;
    }

    public EDropletDialogBuilder setInputTextSize(int sp) {
        this.inputTextSize = sp;
        return this;
    }

    public EDropletDialogBuilder setHasDivider(boolean hasDivider) {
        this.hasDivider = hasDivider;
        return this;
    }

    public EDropletDialogBuilder setDividerHeight(int px) {
        this.dividerHeight = px;
        return this;
    }

    public EDropletDialogBuilder setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        return this;
    }

    public EDropletDialogBuilder setItemHeight(int dp) {
        this.itemHeight = dp;
        return this;
    }

    public EDropletDialogBuilder setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public EDropletDialogBuilder setTitleBold(boolean titleBold) {
        isTitleBold = titleBold;
        return this;
    }

    public EDropletDialogBuilder setMessageBold(boolean messageBold) {
        isMessageBold = messageBold;
        return this;
    }

    /****************参数end*****************/





}
