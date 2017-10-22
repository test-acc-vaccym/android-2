package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.view.EDropletDialogBuilder;
import com.edroplet.qxx.saneteltabactivity.view.NDialog;

/**
 * Created by qxs on 2017/9/14.
 */

public class FunctionsFragmentCollect extends Fragment implements OnClickListener{
    public static final String AntennaAzimuthInfo = "AntennaAzimuthInfo";

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
        view.findViewById(R.id.main_collect_data_history).setOnClickListener(this);
        view.findViewById(R.id.main_collect_data_new).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_collect_data_history:
                startActivity(new Intent(getActivity(), FunctionsCollectHistoryFileListActivity.class));
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
                new NDialog(getContext()).intputDialog();
                break;
        }
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteInfo spi;
    }
}
