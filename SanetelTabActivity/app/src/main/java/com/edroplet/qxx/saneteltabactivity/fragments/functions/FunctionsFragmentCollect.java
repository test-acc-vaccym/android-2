package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsActivity;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsCollectHistoryFileListActivity;
import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.DateTime;
import com.edroplet.qxx.saneteltabactivity.utils.FileUtils;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.view.EDropletDialogBuilder;
import com.edroplet.qxx.saneteltabactivity.view.NDialog;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_APPEND;
import static com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsCollectHistoryFileListActivity.KEY_IS_SELECT;
import static com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo.KEY_NEWEST_COLLECT_FILE;
import static com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo.SAMPLEDATA;

/**
 * Created by qxs on 2017/9/14.
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
        View view = inflater.inflate(R.layout.functions_fragment_collect, null);
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

    static CollectHistoryFileInfo collectHistoryFileInfo;
    static Context context;
    
    @Override
    public void onClick(View view) {
        String newestFile = CustomSP.getString(context,KEY_NEWEST_COLLECT_FILE,"");
        switch (view.getId()){
            case R.id.main_collect_data_history:
                Intent intent = new Intent(getActivity(), FunctionsCollectHistoryFileListActivity.class);
                intent.putExtra(KEY_IS_SELECT, false);
                startActivity(intent);
                break;
            case R.id.main_collect_data_new:
                if (collectHistoryFileInfo == null){
                    collectHistoryFileInfo = new CollectHistoryFileInfo(context);
                }
                new NDialog(context).inputDialog();
                break;
            case R.id.main_collect_data_start:
                if (newestFile.isEmpty()){
                    Toast.makeText(context,getText(R.string.main_collect_data_no_file_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                if (FileUtils.isFileExist(newestFile)){
                    Toast.makeText(context,getText(R.string.main_collect_data_file_not_exist_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                if(FileUtils.getFileSize(newestFile) >= FileUtils.FILE_LIMIT) {
                    Toast.makeText(context,getText(R.string.main_collect_data_file_full_prompt),Toast.LENGTH_SHORT).show();
                    break;
                }
                if (null == timer){
                    timer = new Timer();
                }
                timer.schedule(new TimerTask(){
                    @Override
                    public void run(){

                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("message",DateTime.getCurrentDateTime());
                        message.setData(bundle);
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
                if(timer!= null) {
                    timer.cancel();
                    timer.purge();
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
                // TODO: 2017/11/3  通信
                try {
                    if (collectHistoryFileInfo == null){
                        collectHistoryFileInfo = new CollectHistoryFileInfo(context);
                    }
                    String newestFile = collectHistoryFileInfo.getNewestCollectFile();
                    if (FileUtils.isFileExist(newestFile) &&FileUtils.getFileSize(newestFile) < FileUtils.FILE_LIMIT) {
                        // TODO: 2017/11/12 接收数据
                        FileUtils.saveFile(context, newestFile, MODE_APPEND, msg.getData().getString("message") + SAMPLEDATA);
                    } else {
                        Toast.makeText(context, context.getString(R.string.main_collect_data_file_full_prompt), Toast.LENGTH_LONG).show();
                    }
                }catch (IOException e){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            super.handleMessage(msg);
        }
    }

}
