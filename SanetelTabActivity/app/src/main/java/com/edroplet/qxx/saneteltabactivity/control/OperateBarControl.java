package com.edroplet.qxx.saneteltabactivity.control;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.guide.FollowMeActivity;
import com.edroplet.qxx.saneteltabactivity.beans.AntennaInfo;
import com.edroplet.qxx.saneteltabactivity.beans.LockerInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SavingInfo;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;

/**
 * Created by qxs on 2017/9/19.
 */

public class OperateBarControl {
    public static void setupOperatorBar(final AppCompatActivity activity){

        final StatusButton sbExploded = (StatusButton)  activity.findViewById(R.id.button_operate_explode);
        final StatusButton sbFold = (StatusButton) activity.findViewById(R.id.button_operate_fold);

        final String buttonOkText = activity.getString(R.string.operate_confirm_ok);
        boolean clickable = true;
        if( SystemServices.getAntennaState() == AntennaInfo.AntennaStatus.EXPLODED ||
                SystemServices.getAntennaState() == AntennaInfo.AntennaStatus.FOLDED){
            clickable = false;
        }
        if (SystemServices.getLockerState() == LockerInfo.LOCKER_STATE_LOCKED){
            clickable = false;
        }
        if (SystemServices.getSavingState() == SavingInfo.SAVING_STATE_OPEN){
            clickable = false;
        }
        if (sbExploded != null) {
            if (clickable){
                sbExploded.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            }else {
                sbExploded.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbExploded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbExploded)) {
                        sbExploded.onConfirm(activity.getString(R.string.explode_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO 处理确定事件
                                sbExploded.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                                if (sbFold != null) {
                                    sbFold.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                                }
                                sbExploded.getDialogBuilder().dismiss();
                            }
                        }, buttonOkText);
                    }
                    return;
                }
            });
        }
        if (sbFold!=null){
            if (clickable){
                sbFold.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            }else {
                sbFold.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbFold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbFold)) {
                        sbFold.onConfirm(activity.getString(R.string.fold_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 设置为不可点击状态
                                sbFold.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                                if (sbExploded != null) {
                                    sbExploded.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                                }
                                sbFold.getDialogBuilder().dismiss();
                            }
                        }, buttonOkText);
                    }
                }
            });
        }
        final StatusButton sbPause = (StatusButton) activity.findViewById(R.id.button_operate_pause);
        if (sbPause != null)
            if (clickable){
                sbPause.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            }else {
                sbPause.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbPause)) {
                        //sbPause.onConfirm("暂停？", new View.OnClickListener() {
                        //    @Override
                        //    public void onClick(View v) {
                        //        sbPause.getDialogBuilder().dismiss();
                        //    }
                        //});
                        // todo 直接停止，不需要确认
                    }
                }
            });

        final StatusButton sbReset = (StatusButton) activity.findViewById(R.id.button_operate_reset);
        if (sbReset != null)
            if (clickable){
                sbReset.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            }else {
                sbReset.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbReset)) {
                        sbReset.onConfirm(activity.getString(R.string.reset_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sbReset.getDialogBuilder().dismiss();
                            }
                        }, buttonOkText);
                    }
                }
            });

        final DialogInterface.OnClickListener mCancelClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(activity, activity.getString(R.string.cancel_button_prompt), Toast.LENGTH_SHORT).show();
            }
        };

        final StatusButton sbSearch = (StatusButton) activity.findViewById(R.id.button_operate_search);
        if (sbSearch != null) {
            if (clickable){
                sbSearch.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            }else {
                sbSearch.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbSearch)) {
                        final RandomDialog randomDialog = new RandomDialog(activity);
                        randomDialog.onConfirmEDropletDialogBuilder(activity.getString(R.string.searching_confirm),
                                activity.getString(R.string.switch_destination),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(activity, FollowMeActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FollowMeActivity.POSITION, FollowMeActivity.FOLLOWME_PAGES_INDEX.INDEX_SEARCHING.ordinal());
                                        intent.putExtras(bundle);
                                        activity.startActivity(intent);
                                    }
                                },
                                // helpListener
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(activity, FollowMeActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FollowMeActivity.POSITION, FollowMeActivity.FOLLOWME_PAGES_INDEX.INDEX_DESTINATION.ordinal());
                                        intent.putExtras(bundle);
                                        activity.startActivity(intent);
                                    }
                                }, mCancelClickListener);
                    }
                }
            });
        }
    }

    private static boolean canOperate(Context context, final StatusButton statusButton){
        String buttonOkText = context.getString(R.string.operate_confirm_ok);
        int satelliteStatus = SystemServices.getAntennaState();
        // 天线状态判断
        if (satelliteStatus == AntennaInfo.AntennaStatus.EXPLODED || satelliteStatus == AntennaInfo.AntennaStatus.FOLDED){

            statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            statusButton.onConfirm(context.getString(R.string.explode_bind_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statusButton.getDialogBuilder().dismiss();
                }
            }, buttonOkText);
            return false;
        }else{
            statusButton.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
        }
        int lockerStatus = SystemServices.getLockerState();
        // 锁紧机构状态判断
        if (lockerStatus == LockerInfo.LOCKER_STATE_LOCKED){
            statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            statusButton.onConfirm(context.getString(R.string.locker_bind_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statusButton.getDialogBuilder().dismiss();
                }
            }, buttonOkText);
            return false;
        }else{
            statusButton.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
        }

        // 节能状态判断
        int savingStatus = SystemServices.getSavingState();
        if (savingStatus == SavingInfo.SAVING_STATE_OPEN){
            buttonOkText = context.getString(R.string.saving_bind_quit);
            statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            statusButton.onConfirm(context.getString(R.string.saving_bind_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/11/5 发送退出节能的命令
                    statusButton.getDialogBuilder().dismiss();
                }
            }, buttonOkText);
            return false;
        }else {
            statusButton.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
        }
        return true;
    }
}
