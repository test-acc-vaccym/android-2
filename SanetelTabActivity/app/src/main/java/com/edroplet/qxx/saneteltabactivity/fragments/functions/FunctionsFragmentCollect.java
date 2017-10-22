package com.edroplet.qxx.saneteltabactivity.fragments.functions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.FunctionsCollectHistoryFileListActivity;
import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameterItem;
import com.edroplet.qxx.saneteltabactivity.utils.DateTime;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;

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
                RandomDialog randomDialog = new RandomDialog(getContext());
                randomDialog.onInputBuilder(getString(R.string.main_collect_data_new_input_message),getString(R.string.main_collect_data_new_input_hint));
                String input =  randomDialog.getmInputText();

                break;
        }
    }

    public static class MainMonitorFragmentHolder {
        private SatelliteParameterItem spi;
    }
}
