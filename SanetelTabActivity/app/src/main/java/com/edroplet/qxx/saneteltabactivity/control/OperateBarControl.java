package com.edroplet.qxx.saneteltabactivity.control;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.guide.FollowMeActivity;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.view.StatusButton;

/**
 * Created by qxs on 2017/9/19.
 */

public class OperateBarControl {
    public static void setupOperatorBar(final AppCompatActivity activity){

        final StatusButton sbExploded = (StatusButton)  activity.findViewById(R.id.button_operate_explode);
        final StatusButton sbFold = (StatusButton) activity.findViewById(R.id.button_operate_fold);

        if (sbExploded != null)
            sbExploded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbExploded.onConfirm(activity.getString(R.string.explode_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO 处理确定事件
                            sbExploded.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                            if (sbFold!=null) {
                                sbFold.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                            }
                            sbExploded.getDialogBuilder().dismiss();
                        }
                    });
                    return;
                }
            });
        if (sbFold!=null){
            sbFold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbFold.onConfirm(activity.getString(R.string.fold_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 设置为不可点击状态
                            sbFold.setButtonState(StatusButton.BUTTON_STATE_DISABLE);
                            if (sbExploded!=null) {
                                sbExploded.setButtonState(StatusButton.BUTTON_STATE_OPERATE);
                            }
                            sbFold.getDialogBuilder().dismiss();
                        }
                    });
                }
            });
        }
        final StatusButton sbPause = (StatusButton) activity.findViewById(R.id.button_operate_pause);
        if (sbPause != null)
            sbPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //sbPause.onConfirm("暂停？", new View.OnClickListener() {
                    //    @Override
                    //    public void onClick(View v) {
                    //        sbPause.getDialogBuilder().dismiss();
                    //    }
                    //});
                    // todo 直接停止，不需要确认
                }
            });

        final StatusButton sbReset = (StatusButton) activity.findViewById(R.id.button_operate_reset);
        if (sbReset != null)
            sbReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sbReset.onConfirm(activity.getString(R.string.reset_confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sbReset.getDialogBuilder().dismiss();
                        }
                    });
                }
            });

        final StatusButton sbSearch = (StatusButton) activity.findViewById(R.id.button_operate_search);
        if (sbSearch != null)
            sbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final RandomDialog rd = new RandomDialog(activity);
                    rd.onConfirmEDropletDialogBuilder(activity.getString(R.string.searching_confirm),
                            activity.getString(R.string.switch_destination),
                            new DialogInterface.OnClickListener(){
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
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(activity, FollowMeActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FollowMeActivity.POSITION, FollowMeActivity.FOLLOWME_PAGES_INDEX.INDEX_DESTINATION.ordinal());
                                    intent.putExtras(bundle);
                                    activity.startActivity(intent);
                                }
                            });
                }
            });
    }
}
