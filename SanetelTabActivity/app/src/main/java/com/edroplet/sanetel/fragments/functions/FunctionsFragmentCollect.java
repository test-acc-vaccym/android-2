package com.edroplet.sanetel.fragments.functions;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.functions.FunctionsCollectHistoryFileListActivity;
import com.edroplet.sanetel.beans.CollectHistoryFileInfo;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.services.communicate.CommunicateService;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.DateTime;
import com.edroplet.sanetel.utils.FileUtils;
import com.edroplet.sanetel.utils.TimerUtil;
import com.edroplet.sanetel.view.NDialog;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static com.edroplet.sanetel.activities.functions.FunctionsCollectHistoryFileListActivity.KEY_IS_SELECT;
import static com.edroplet.sanetel.beans.CollectHistoryFileInfo.KEY_NEWEST_COLLECT_FILE;
import static com.edroplet.sanetel.services.communicate.CommunicateDataReceiver.ACTION_STOP_SAVE;

/**
 * Created by qxs on 2017/9/14.
 * 日志UI
 */

public class FunctionsFragmentCollect extends Fragment implements OnClickListener{
    public static final String AntennaAzimuthInfo = "AntennaAzimuthInfo";

    private Timer timer  = new Timer();

    private static int schedule;
    public static FunctionsFragmentCollect newInstance(@Nullable MainMonitorFragmentHolder mfh){
        Bundle args = new Bundle();
        FunctionsFragmentCollect fragment = new FunctionsFragmentCollect();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_functions_logs, null);
        context = getContext();
        schedule = context.getResources().getInteger(R.integer.collect_data_schedule_timer);
        view.findViewById(R.id.main_collect_data_history).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_new).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_clear).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_save).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_start).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_stop).setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 回收资源
        // 停止服务
        if (null != intentCommunicateService){
            context.stopService(intentCommunicateService);
            intentCommunicateService = null;
        }
        // 停止定时器
        if(timer!= null) {
            TimerUtil.close(TimerUtil.SysState);
            timer = null;
        }
    }

    static CollectHistoryFileInfo collectHistoryFileInfo;
    static Context context;
    Intent intentCommunicateService;
    @Override
    public void onClick(View view) {
        String newestFile = CustomSP.getString(context,KEY_NEWEST_COLLECT_FILE,"");
        switch (view.getId()){
            case R.id.main_collect_data_history:
                final Intent intent = new Intent(getActivity(), FunctionsCollectHistoryFileListActivity.class);
                intent.putExtra(KEY_IS_SELECT, false);
                startActivity(intent);
                break;
            case R.id.main_collect_data_new:

                // 6.0在权限管理方面更加全面，在读写外置存储的时候不仅要在manifest中静态授权，还需要在代码中动态授权。
                // 在Activity中发起权限请求：
                //ActivityCompat.requestPermissions(getActivity(), new String[]{android
                //        .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                onConfirm();
                break;
            case R.id.main_collect_data_start:
                if (newestFile.isEmpty()){
                    Toast.makeText(context,getText(R.string.main_collect_data_no_file_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!FileUtils.isFileExist(newestFile)){
                    Toast.makeText(context,getText(R.string.main_collect_data_file_not_exist_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                if(FileUtils.getFileSize(newestFile) >= FileUtils.FILE_LIMIT) {
                    Toast.makeText(context,getText(R.string.main_collect_data_file_full_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                if (null == timer){
                    timer = new TimerUtil(TimerUtil.SysState, true).getTimer();
                }
                timer.schedule(new TimerTask(){
                    @Override
                    public void run(){
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("message",DateTime.getCurrentDateTime());
                        message.setData(bundle);

                        // 通信, 启动服务接收
                        try {
                            if (collectHistoryFileInfo == null){
                                collectHistoryFileInfo = new CollectHistoryFileInfo(context);
                            }
                            String newestFile = collectHistoryFileInfo.getNewestCollectFile();
                            if (FileUtils.isFileExist(newestFile) && FileUtils.getFileSize(newestFile) < FileUtils.FILE_LIMIT) {
                                if (null == intentCommunicateService) {
                                    // 接收数据, 只启用一次服务
                                    intentCommunicateService = new Intent(context, CommunicateService.class);
                                    context.startService(intentCommunicateService);
                                }

                                // 发送监视信息命令
                                Protocol.sendMessage(context, Protocol.cmdGetSystemState);

                                // 在广播中保存数据
                                // FileUtils.saveFile(newestFile, DateTime.getCurrentDateTime() + SAMPLEDATA, true);
                            } else {
                                Toast.makeText(context, context.getString(R.string.main_collect_data_file_full_prompt), Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }
                        handler.sendMessage(message);
                        try {
                            Thread.sleep(schedule);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, schedule);

                break;
            case R.id.main_collect_data_stop:
                if (newestFile.isEmpty()){
                    Toast.makeText(context,getText(R.string.main_collect_data_no_file_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                // 停止服务
                if (null != intentCommunicateService){
                    // 不停止服务，只发送停止保存的广播
                    // context.stopService(intentCommunicateService);
                    Intent intentStopSave = new Intent();
                    intentStopSave.setAction(ACTION_STOP_SAVE);
                    context.sendBroadcast(intentStopSave);
                }
                if(timer!= null) {
                    TimerUtil.close(TimerUtil.SysState);
                    timer = null;
                }
                break;
            case R.id.main_collect_data_save:
                if (newestFile.isEmpty()){
                    Toast.makeText(context,getText(R.string.main_collect_data_no_file_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                break;
            case R.id.main_collect_data_clear:
                if (newestFile.isEmpty()){
                    Toast.makeText(context,getText(R.string.main_collect_data_no_file_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                if (collectHistoryFileInfo == null){
                    collectHistoryFileInfo = new CollectHistoryFileInfo(context);
                }
                try {
                    collectHistoryFileInfo.clearFile();
                }catch (IOException e){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }

    private final Handler handler = new CollectHandler(getActivity());

    // 解决 The handler class should be static or leaks might occur.
    // 在一个新的文件中继承Handler类，或者使用一个静态内部类。
    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class CollectHandler extends Handler {
        private final WeakReference<FragmentActivity> mActivity;

        public CollectHandler(FragmentActivity activity) {
            mActivity = new WeakReference<FragmentActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = mActivity.get();
            if (activity != null) {
            }
            super.handleMessage(msg);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建文件夹
                    /**
                     if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                     File file = new File(Environment.getExternalStorageDirectory() + "/aa/bb/");
                     if (!file.exists()) {
                     Log.d("jim", "path1 create:" + file.mkdirs());
                     }
                     }
                     */
                    onConfirm();
                    break;
                }
            }
        }

        private void onConfirm(){
            if (collectHistoryFileInfo == null) {
                collectHistoryFileInfo = new CollectHistoryFileInfo(context);
            }
            new NDialog(context).inputDialog(new NDialog.OnInputListener() {
                @Override
                public void onClick(String inputText, int which) {
                    //which,0代表NegativeButton，1代表PositiveButton
                    if (which == 1) {
                        if (inputText.length() > 0) {
                            try {
                                collectHistoryFileInfo.setDateTime(DateTime.getCurrentDateTime()).setFileName(inputText).save();
                                Toast.makeText(context, context.getString(R.string.create_new_file_complete) + inputText, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.create_new_file_canceled), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.create_new_file_canceled), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
}
