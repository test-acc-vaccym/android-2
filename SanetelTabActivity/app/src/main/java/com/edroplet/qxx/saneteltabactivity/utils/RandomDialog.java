package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.view.EDropletDialogBuilder;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.IOException;

/**
 * Created by qxs on 2017/10/15.
 */

public class RandomDialog {
    private NiftyDialogBuilder dialogBuilder;
    private AlertDialog.Builder alertDialogBuilder;
    private Context context;
    public RandomDialog(Context context){
        this.context = context;
    }
    public boolean onConfirm(String message, View.OnClickListener listener, String buttonOkText){
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
        dialogBuilder = NiftyDialogBuilder.getInstance(context);
        dialogBuilder
                // 重点设置
                .withEffect(effect)        //设置对话框弹出样式
                //.setCustomView(R.layout.custom, MainActivity.this) // 设置自定义对话框的布局
                .withDuration(context.getResources().getInteger(R.integer.operate_confirm_animate_time))              //动画显现的时间（时间长就类似放慢动作）
                // 基本设置
                .withTitle(context.getResources().getString(R.string.operate_confirm_title))         //设置对话框标题
                .withTitleColor(ContextCompat.getColor(context, R.color.operate_confirm_title_color))          //设置标题字体颜色
                .withDividerColor(ContextCompat.getColor(context, R.color.operate_confirm_divider_color))      //设置分隔线的颜色
                .withMessage(message)//设置对话框显示内容
                .withMessageColor(ContextCompat.getColor(context, R.color.operate_confirm_message_color))       //设置消息字体的颜色
                .withDialogColor(ContextCompat.getColor(context, R.color.operate_confirm_dialog_color))        //设置对话框背景的颜色
                //.withIcon(getResources().getDrawable(R.drawable.logo)) //设置标题的图标
                // 设置是否模态，默认false，表示模态，
                //要求必须采取行动才能继续进行剩下的操作 | isCancelable(true)
                .isCancelableOnTouchOutside(true)
                .withButton1Text(buttonOkText)             //设置按钮1的文本
                .withButton2Text(context.getResources().getString(R.string.operate_confirm_cancel))         //设置按钮2的文本
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


    public void onConfirmThreeButton(String message,
                                        DialogInterface.OnClickListener btnOkListener,
                                        DialogInterface.OnClickListener btnHelpListener){
        alertDialogBuilder = new AlertDialog.Builder(context);
//        builder.setIcon(R.drawable.ic_launcher);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.operate_confirm_title))
                .setMessage(message)
                //  第一个按钮
                .setNeutralButton(context.getResources().getString(R.string.operate_confirm_ok), btnOkListener)
                //  中间的按钮
                .setNegativeButton(context.getResources().getString(R.string.operate_confirm_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //  提示信息
                        Toast toast = Toast.makeText(context, "你选择了取消", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                //  第二个按钮
                .setPositiveButton("？", btnHelpListener)
                //  Dialog的显示
                .create().show();
    }
    public  NiftyDialogBuilder getDialogBuilder() {
        return dialogBuilder;
    }

    public AlertDialog.Builder getAlertDialog(){return alertDialogBuilder; }

    public void onConfirmEDropletDialogBuilder(String message,
                                               String secondButtonText,
                                               final DialogInterface.OnClickListener btnOkListener,
                                               final DialogInterface.OnClickListener btnHelpListener,
                                               final DialogInterface.OnClickListener btnCancelClickListener){
        new EDropletDialogBuilder(context)
                .setTitle(context.getResources().getString(R.string.operate_confirm_title))
                .setTitleSize(context.getResources().getInteger(R.integer.title_size))
                //.setTitleColor(Color.parseColor("#000000"))
                .setTitleBold(true)
                .setTitleCenter(true)
                .setMessageCenter(true)
                .setMessage(message)
                .setMessageSize(context.getResources().getInteger(R.integer.message_size))
                .setMessageBold(true)
                //.setMessageColor(Color.parseColor("#000000"))
                //  第一个按钮
                .setNegativeTextColor(Color.BLUE)
                //  第二个按钮 最后一个
                .setPositiveTextColor(Color.BLUE)
                //  第3个按钮 中间
                .setNeutralTextColor(Color.BLUE)
                .setButtonCenter(false)
                .setButtonSize(context.getResources().getInteger(R.integer.button_size))
                .setCancelable(false)
                //  第一个按钮
                .setNeutralButton(context.getResources().getString(R.string.operate_confirm_ok), btnOkListener)
                //  第二个按钮
                .setPositiveButton(secondButtonText, btnHelpListener)
                //  第3个按钮
                .setNegativeButton(context.getResources().getString(R.string.operate_confirm_cancel), btnCancelClickListener)
                /*
                .setOnConfirmListener(new EDropletDialogBuilder.OnConfirmListener() {
                    @Override
                    public void onClick(int which) {
                        //which,0代表NegativeButton，1代表PositiveButton
                        Toast.makeText(context, "点击了：：" + which, Toast.LENGTH_SHORT).show();
                        switch (which) {
                            // 最左边的
                            // positive 1 最后一个
                            case 1:
                                btnHelpListener.onClick(null, which);
                                break;
                            // 0 确定
                            // negtive 第一个
                            case 0:
                                btnOkListener.onClick(null, which);
                                break;
                            // 取消
                            // 中间的
                            default:
                                //  提示信息
                                Toast.makeText(context, "你选择了取消", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })*/
                .create(EDropletDialogBuilder.CONFIRM).show();
    }


    public void inputDialog() {
        new EDropletDialogBuilder(context).setTitle(context.getString(R.string.main_collect_data_new_input_message))
                .setInputHintText(context.getString(R.string.main_collect_data_new_input_hint))
                .setInputHintTextColor(Color.parseColor("#c1c1c1"))
                .setInputText("")
                .setInputTextColor(Color.parseColor("#333333"))
                .setInputTextSize(14)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .setInputLineColor(Color.parseColor("#00ff00"))
                .setPositiveButtonText(context.getString(R.string.operate_confirm_ok))
                .setNegativeButtonText(context.getString(R.string.operate_confirm_cancel))
                .setNegativeTextColor(Color.parseColor("#c1c1c1"))
                .setOnInputListener(new EDropletDialogBuilder.OnInputListener() {
                    @Override
                    public void onClick(String inputText, int which) {
                        //which,0代表NegativeButton，1代表PositiveButton
                        if (which == 1) {
                            if (inputText.length()>0) {
                                CollectHistoryFileInfo collectHistoryFileInfo = new CollectHistoryFileInfo(context);
                                try {
                                    collectHistoryFileInfo.setDateTime(DateTime.getCurrentDateTime()).setFileName(inputText).save();
                                    Toast.makeText(context, context.getString(R.string.create_new_file_complete) + inputText, Toast.LENGTH_SHORT).show();
                                }catch (IOException ioe){
                                    Toast.makeText(context, ioe.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(context, context.getString(R.string.create_new_file_canceled), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, context.getString(R.string.create_new_file_canceled), Toast.LENGTH_SHORT).show();
                        }

                    }
                }).create(EDropletDialogBuilder.INPUT).show();

    }


    public void onSelectBuilder (String message,
                                 EDropletDialogBuilder.OnConfirmListener btnOkListener,
                                 EDropletDialogBuilder.OnConfirmListener btnHelpListener){
        new EDropletDialogBuilder(context)
                .setItems(new String[]{"aaa", "bbb", "ccc", "ddd"})
                .setItemGravity(Gravity.LEFT)
                .setItemColor(Color.parseColor("#000000"))
                .setItemHeight(50)
                .setItemSize(16)
                .setDividerHeight(1)
                .setAdapter(null)
                .setDividerColor(Color.parseColor("#c1c1c1"))
                .setHasDivider(true)
                .setOnChoiceListener(new EDropletDialogBuilder.OnChoiceListener() {
                    @Override
                    public void onClick(String item, int which) {
                        Toast.makeText(context, "选择了：：" + item, Toast.LENGTH_SHORT).show();
                    }
                }).create(EDropletDialogBuilder.CHOICE).show();
    }
}
