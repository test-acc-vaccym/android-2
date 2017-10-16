package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

/**
 * Created by qxs on 2017/10/15.
 */

public class RandomDialog {
    private NiftyDialogBuilder dialogBuilder;
    private Context context;
    RandomDialog(Context context){
        this.context = context;
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
                .withButton1Text(context.getResources().getString(R.string.operate_confirm_ok))             //设置按钮1的文本
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

    public  NiftyDialogBuilder getDialogBuilder() {
        return dialogBuilder;
    }
}
