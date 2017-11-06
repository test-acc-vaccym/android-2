package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsCollectHistoryFileListActivity;
import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.DateTime;
import com.edroplet.qxx.saneteltabactivity.utils.FileUtils;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.view.EDropletDialogBuilder;
import com.edroplet.qxx.saneteltabactivity.view.NDialog;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_APPEND;
import static com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsCollectHistoryFileListActivity.KEY_IS_SELECT;
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

        schedule = getContext() .getResources().getInteger(R.integer.collect_data_schedule_timer);
        view.findViewById(R.id.main_collect_data_history).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_new).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_clear).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_save).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_start).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_stop).setOnClickListener(this);

        return view;
    }

    CollectHistoryFileInfo collectHistoryFileInfo;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_collect_data_history:
                Intent intent = new Intent(getActivity(), FunctionsCollectHistoryFileListActivity.class);
                intent.putExtra(KEY_IS_SELECT, false);
                startActivity(intent);
                break;
            case R.id.main_collect_data_new:
//                RandomDialog randomDialog = new RandomDialog(getContext());
//                randomDialog.onInputBuilder(getString(R.string.main_collect_data_new_input_message),
//                        getString(R.string.main_collect_data_new_input_hint),
//
//                        new EDropletDialogBuilder.OnInputListener() {
//                            @Override
//                            public void onClick(String inputText, int which) {
//                                //which,0代表NegativeButton，1代表PositiveButton
//                                Toast.makeText(getContext(), "输入了: " + inputText, Toast.LENGTH_SHORT).show();
//                                if (which == 0){
//                                    CollectHistoryFileInfo collectHistoryFileInfo = new CollectHistoryFileInfo(getContext());
//                                    collectHistoryFileInfo.setDateTime(DateTime.getCurrentDateTime()).setFileName(inputText).save();
//                                }
//                            }
//                        });
                new NDialog(getContext()).inputDialog();
                break;
            case R.id.main_collect_data_start:
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
                if(timer!= null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
                break;
            case R.id.main_collect_data_save:
                break;
            case R.id.main_collect_data_clear:

                if (collectHistoryFileInfo == null){
                    collectHistoryFileInfo = new CollectHistoryFileInfo(getContext());
                }
                try {
                    collectHistoryFileInfo.clearFile();
                }catch (IOException e){
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            // TODO: 2017/11/3  通信
            try {
                if (collectHistoryFileInfo == null){
                    collectHistoryFileInfo = new CollectHistoryFileInfo(getContext());
                }
                FileUtils.saveFile(getContext(),collectHistoryFileInfo.getNewestCollectFile(), MODE_APPEND, msg.getData().getString("message")+ SAMPLEDATA);
            }catch (IOException e){
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

}
