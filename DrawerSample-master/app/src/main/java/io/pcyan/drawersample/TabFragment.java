package io.pcyan.drawersample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2015/10/30.
 */
public class TabFragment extends Fragment {

    private List<String> mData;
    private String title;
    public TabFragment(){

    }

    @SuppressLint("ValidFragment")
    public TabFragment(String title){
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        RecyclerView rlData = (RecyclerView) view.findViewById(R.id.rl_data);
        mData = addData();
//        llData.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,mData));
        Log.i("tab fragment","data size"+mData.size());
        rlData.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rlData.setLayoutManager(layoutManager);
        rlData.setAdapter(new RVArrayAdapter(mData));
        return view;
    }

    private List<String> addData(){
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add(title+" Item "+i);
        }

        return dataList;
    }
}
