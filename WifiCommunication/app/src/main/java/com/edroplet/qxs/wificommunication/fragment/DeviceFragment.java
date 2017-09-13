package com.edroplet.qxs.wificommunication.fragment;

import android.widget.Toast;

import com.edroplet.qxs.wificommunication.R;

/**
 * Created by qxs on 2017/7/25.
 */

public class DeviceFragment extends BaseFragment {
    @Override
    protected void initView() {

    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_device;
    }
    @Override
    protected void getDataFromServer() {
        Toast.makeText(mContext, "DeviceFragment 页面请求数据了", Toast.LENGTH_SHORT).show();
    }
}
