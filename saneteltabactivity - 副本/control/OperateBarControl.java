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
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;

import java.util.Timer;
import java.util.TimerTask;

import static com.edroplet.qxx.saneteltabactivity.beans.SavingInfo.*;

/**
 * Created by qxs on 2017/9/19.
 * 快捷键
 */

public class OperateBarControl {
    private static RandomDialog randomDialog;
    private static StatusButton energyStateButton; // 节能
    private static StatusButton lockerStateButton; // 锁紧
    private static StatusButton antennaStateButton; // 天线

    private static Timer timer = new Timer();
    private static AppCompatActivity mActivity;
    public static void setupOperatorBar(final AppCompatActivity activity){
        if (null == activity)
            return;

        if (mActivity == null){
            mActivity = activity;
        }

        final StatusButton sbExploded = (StatusButton)  activity.findViewById(R.id.button_operate_explode);
        final StatusButton sbFold = (StatusButton) activity.findViewById(R.id.button_operate_fold);

        final String buttonOkText = activity.getString(R.string.operate_confirm_ok);
        boolean explodeClickable = true; // 展开
        boolean manualClickable = true; // 手动
        boolean searchClickable = true; // 寻星
        boolean resetClickable = true; // 复位
        boolean foldClickable = true; // 收藏
        energyStateButton = (StatusButton) activity.findViewById(R.id.status_bar_button_power_state);
        lockerStateButton = (StatusButton) activity.findViewById(R.id.status_bar_button_locker_state);
        antennaStateButton = (StatusButton) activity.findViewById(R.id.status_bar_button_antenna_state);

        randomDialog = new RandomDialog(activity);
        // 当天线处于已收藏状态时，手动、寻星、复位、收藏快捷键为灰色，不能操作。只有展开快捷键能够操作。
        if(AntennaInfo.getAntennaState(activity) == AntennaInfo.AntennaStatus.FOLDED){
            manualClickable = false;
            searchClickable = false;
            resetClickable = false;
            foldClickable = false;
        }else if (AntennaInfo.getAntennaState(activity) == AntennaInfo.AntennaStatus.EXPLODED ){
            //  当天线处于已展开状态时，展开和复位快捷键为灰色，不能操作。只有手动、寻星、收藏快捷键能够操作。
            explodeClickable = false;
            resetClickable = false;
        }else if (AntennaInfo.getAntennaState(activity) == AntennaInfo.AntennaStatus.EXPLODING
                || AntennaInfo.getAntennaState(activity) == AntennaInfo.AntennaStatus.INIT
                || AntennaInfo.getAntennaState(activity) == AntennaInfo.AntennaStatus.FOLDING){
            // 当天线处于展开中、初始、收藏中，所有快捷键为灰色，不能操作。
            explodeClickable = false;
            manualClickable = false;
            searchClickable = false;
            resetClickable = false;
            foldClickable = false;
        }
        // 当锁紧机构处于锁紧时，快捷键为灰色，不能操作。
        if (LockerInfo.getLockerState(activity) == LockerInfo.LOCKER_STATE_LOCKED){
            explodeClickable = false;
            manualClickable = false;
            searchClickable = false;
            resetClickable = false;
            foldClickable = false;
        }
        // 当节能处于开启时，快捷键为灰色，不能操作。
        if (SavingInfo.getSavingState(activity) == SAVING_STATE_OPEN){
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
                                sbExploded.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                                /** 展开中的状态不能操作
                                if (sbFold != null) {
                                    sbFold.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                                }*/
                                // 保存天线状态
                                AntennaInfo.setAntennaState(activity, AntennaInfo.AntennaStatus.EXPLODING);
                                if (null != antennaStateButton){
                                    antennaStateButton.setButtonState(StatusButton.BUTTON_STATE_ABNORMAL);
                                    antennaStateButton.setText(R.string.antenna_state_exploding);
                                }
                                // TODO: 2017/11/13 发送展开命令
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
                                // 保存天线状态
                                AntennaInfo.setAntennaState(activity, AntennaInfo.AntennaStatus.FOLDING);
                                if (null != antennaStateButton){
                                    antennaStateButton.setButtonState(StatusButton.BUTTON_STATE_SPECIAL);
                                    antennaStateButton.setText(R.string.antenna_state_folding);
                                }
                                // TODO: 2017/11/13 发送收藏命令
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
                        // 直接停止，不需要确认
                        AntennaInfo.setAntennaState(activity, AntennaInfo.AntennaStatus.PAUSE);
                        if (null != antennaStateButton){
                            antennaStateButton.setButtonState(StatusButton.BUTTON_STATE_ABNORMAL);
                            antennaStateButton.setText(R.string.antenna_state_paused);
                        }
                        // todo 发送停止命令
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
                                AntennaInfo.setAntennaState(activity, AntennaInfo.AntennaStatus.RECYCLED);
                                if (null != antennaStateButton){
                                    antennaStateButton.setButtonState(StatusButton.BUTTON_STATE_SPECIAL);
                                    antennaStateButton.setText(R.string.antenna_state_reset);
                                }
                                // todo 发送复位命令
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
                                        // 设置状态为寻星中
                                        AntennaInfo.setAntennaState(activity,AntennaInfo.AntennaStatus.SEARCHING);
                                        if (null != antennaStateButton){
                                            antennaStateButton.setButtonState(StatusButton.BUTTON_STATE_SPECIAL);
                                            antennaStateButton.setText(R.string.antenna_state_searching);
                                        }
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

        /*
        if (null == timer){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setupOperatorBar(mActivity);
            }
        },0,1000);
        */
    }

    private static boolean canOperate(final Context context, final StatusButton statusButton){
        String buttonOkText = context.getString(R.string.operate_confirm_ok);

        // 优先判断节能状态
        // 节能状态判断
        int savingStatus = SavingInfo.getSavingState(context);
        if (savingStatus == SAVING_STATE_OPEN){
            buttonOkText = context.getString(R.string.saving_bind_quit);
            statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            randomDialog.onConfirm(context.getString(R.string.saving_bind_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != energyStateButton) {
                        energyStateButton.setButtonState(StatusButton.BUTTON_STATE_ABNORMAL);
                        energyStateButton.setText(R.string.power_state_charged);
                    }
                    SavingInfo.setSavingState(context,SAVING_STATE_CLOSE);

                    // TODO: 2017/11/5 发送退出节能的命令
                    randomDialog.getDialogBuilder().dismiss();
                }
            }, buttonOkText);
            return false;
        }

        // 然后锁紧机构状态
        int lockerStatus = LockerInfo.getLockerState(context);
        // 锁紧机构状态判断
        if (lockerStatus == LockerInfo.LOCKER_STATE_LOCKED){
            statusButton.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
            randomDialog.onConfirm(context.getString(R.string.locker_bind_confirm), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != lockerStateButton) {
                        lockerStateButton.setButtonState(StatusButton.BUTTON_STATE_NORMAL);
                        lockerStateButton.setText(R.string.locker_state_released);
                    }
                    LockerInfo.setLockerState(context,LockerInfo.LOCKER_STATE_UNLOCK);
                    randomDialog.getDialogBuilder().dismiss();
                }
            }, buttonOkText);
            return false;
        }

        // 最后天线状态
        int satelliteStatus = AntennaInfo.getAntennaState(context);

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

        statusButton.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
        return true;
    }
}
