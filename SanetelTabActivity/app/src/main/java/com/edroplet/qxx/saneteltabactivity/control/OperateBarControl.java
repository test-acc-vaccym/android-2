package com.edroplet.qxx.saneteltabactivity.control;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
 * 快捷键
 */

public class OperateBarControl {
    private static RandomDialog randomDialog;
    public static void setupOperatorBar(final AppCompatActivity activity){

        final StatusButton sbExploded = (StatusButton)  activity.findViewById(R.id.button_operate_explode);
        final StatusButton sbFold = (StatusButton) activity.findViewById(R.id.button_operate_fold);

        final String buttonOkText = activity.getString(R.string.operate_confirm_ok);
        boolean explodeClickable = true; // 展开
        boolean manualClickable = true; // 手动
        boolean searchClickable = true; // 寻星
        boolean resetClickable = true; // 复位
        boolean foldClickable = true; // 收藏

        randomDialog = new RandomDialog(activity);
        // 当天线处于已收藏状态时，手动、寻星、复位、收藏快捷键为灰色，不能操作。只有展开快捷键能够操作。
        if(SystemServices.getAntennaState() == AntennaInfo.AntennaStatus.FOLDED){
            manualClickable = false;
            searchClickable = false;
            resetClickable = false;
            foldClickable = false;
        }else if (SystemServices.getAntennaState() == AntennaInfo.AntennaStatus.EXPLODED ){
            //  当天线处于已展开状态时，展开和复位快捷键为灰色，不能操作。只有手动、寻星、收藏快捷键能够操作。
            explodeClickable = false;
            resetClickable = false;
        }else if (SystemServices.getAntennaState() == AntennaInfo.AntennaStatus.EXPLODING
                || SystemServices.getAntennaState() == AntennaInfo.AntennaStatus.INIT
                || SystemServices.getAntennaState() == AntennaInfo.AntennaStatus.FOLDING){
            // 当天线处于展开中、初始、收藏中，所有快捷键为灰色，不能操作。
            explodeClickable = false;
            manualClickable = false;
            searchClickable = false;
            resetClickable = false;
            foldClickable = false;
        }
        // 当锁紧机构处于锁紧时，快捷键为灰色，不能操作。
        if (SystemServices.getLockerState() == LockerInfo.LOCKER_STATE_LOCKED){
            explodeClickable = false;
            manualClickable = false;
            searchClickable = false;
            resetClickable = false;
            foldClickable = false;
        }
        // 当节能处于开启时，快捷键为灰色，不能操作。
        if (SystemServices.getSavingState() == SavingInfo.SAVING_STATE_OPEN){
            explodeClickable = false;
            manualClickable = false;
            searchClickable = false;
            resetClickable = false;
            foldClickable = false;
        }

        if (sbExploded != null) {
            if (explodeClickable){
                sbExploded.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            }else {
                sbExploded.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbExploded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbExploded)) {
                        randomDialog.onConfirm(activity.getString(R.string.explode_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO 处理确定事件
                                sbExploded.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                                if (sbFold != null) {
                                    sbFold.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                                }
                                randomDialog.getDialogBuilder().dismiss();
                            }
                        }, buttonOkText);
                    }
                    return;
                }
            });
        }
        if (sbFold!=null){
            if (foldClickable){
                sbFold.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            }else {
                sbFold.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbFold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbFold)) {
                        randomDialog.onConfirm(activity.getString(R.string.fold_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 设置为不可点击状态
                                sbFold.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                                if (sbExploded != null) {
                                    sbExploded.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                                }
                                randomDialog.getDialogBuilder().dismiss();
                            }
                        }, buttonOkText);
                    }
                }
            });
        }
        final StatusButton sbPause = (StatusButton) activity.findViewById(R.id.button_operate_pause);
        if (sbPause != null) {
            if (manualClickable) {
                sbPause.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            } else {
                sbPause.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbPause)) {
                        // todo 直接停止，不需要确认
                    }
                }
            });
        }
        final StatusButton sbReset = (StatusButton) activity.findViewById(R.id.button_operate_reset);
        if (sbReset != null) {
            if (resetClickable) {
                sbReset.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
            } else {
                sbReset.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            }
            sbReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canOperate(activity, sbReset)) {
                        randomDialog.onConfirm(activity.getString(R.string.reset_confirm), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                randomDialog.getDialogBuilder().dismiss();
                            }
                        }, buttonOkText);
                    }
                }
            });
        }
        final DialogInterface.OnClickListener mCancelClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(activity, activity.getString(R.string.cancel_button_prompt), Toast.LENGTH_SHORT).show();
            }
        };

        final StatusButton sbSearch = (StatusButton) activity.findViewById(R.id.button_operate_search);
        if (sbSearch != null) {
            if (searchClickable){
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

        @IdRes int resId = statusButton.getId();
        switch (satelliteStatus){
            case AntennaInfo.AntennaStatus.EXPLODED:
                if (resId == R.id.button_operate_explode || resId == R.id.button_operate_reset) {
                    statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                    randomDialog.onConfirm(context.getString(R.string.antenna_exploded_bind_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            randomDialog.getDialogBuilder().dismiss();
                        }
                    }, buttonOkText);
                    return false;
                }
                break;
            case AntennaInfo.AntennaStatus.INIT:
                statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                randomDialog.onConfirm(context.getString(R.string.antenna_init_bind_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        randomDialog.getDialogBuilder().dismiss();
                    }
                }, buttonOkText);
                return false;
            case AntennaInfo.AntennaStatus.EXPLODING:
                statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                randomDialog.onConfirm(context.getString(R.string.antenna_exploding_bind_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        randomDialog.getDialogBuilder().dismiss();
                    }
                }, buttonOkText);
                return false;
            case AntennaInfo.AntennaStatus.FOLDING:
                statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                randomDialog.onConfirm(context.getString(R.string.antenna_folding_bind_confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        randomDialog.getDialogBuilder().dismiss();
                    }
                }, buttonOkText);
                return false;
            case AntennaInfo.AntennaStatus.FOLDED:
                if (resId != R.id.button_operate_explode) {
                    statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                    randomDialog.onConfirm(context.getString(R.string.antenna_folded_bind_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            randomDialog.getDialogBuilder().dismiss();
                        }
                    }, buttonOkText);
                    return false;
                }
                break;
        }

        int lockerStatus = SystemServices.getLockerState();
        // 锁紧机构状态判断
        if (lockerStatus == LockerInfo.LOCKER_STATE_LOCKED){
            statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            randomDialog.onConfirm(context.getString(R.string.locker_bind_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    randomDialog.getDialogBuilder().dismiss();
                }
            }, buttonOkText);
            return false;
        }

        // 节能状态判断
        int savingStatus = SystemServices.getSavingState();
        if (savingStatus == SavingInfo.SAVING_STATE_OPEN){
            buttonOkText = context.getString(R.string.saving_bind_quit);
            statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            randomDialog.onConfirm(context.getString(R.string.saving_bind_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/11/5 发送退出节能的命令
                    randomDialog.getDialogBuilder().dismiss();
                }
            }, buttonOkText);
            return false;
        }

        statusButton.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
        return true;
    }
}
