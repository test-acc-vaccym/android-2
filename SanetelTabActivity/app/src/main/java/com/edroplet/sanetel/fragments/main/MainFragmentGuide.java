package com.edroplet.sanetel.fragments.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.functions.FunctionsActivity;
import com.edroplet.sanetel.activities.guide.GuideActivity;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.services.network.SystemServices;
import com.edroplet.sanetel.utils.NetworkUtils;

import static com.edroplet.sanetel.activities.main.MainActivity.defaultDeviceName;
import static com.edroplet.sanetel.utils.CustomSP.WifiSettingsNameKey;
import static com.edroplet.sanetel.services.network.SystemServices.REQUEST_WIFI_CONNECT_MANAGER;

/**
 * Created by qxs on 2017/9/19.
 * 主页面 点击
 */

public class MainFragmentGuide extends Fragment implements View.OnClickListener{

    private DialogInterface.OnClickListener mCancelClickListener;
    private Context context;

    public static MainFragmentGuide newInstance(String info) {
        Bundle args = new Bundle();
        MainFragmentGuide fragment = new MainFragmentGuide();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_guide, null);
        /*
        view.findViewById(R.id.main_guide_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GuideEntryActivity.class));
            }
        });
        */
        context = getContext();
        boolean skipCancel= false;

        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            skipCancel = appInfo.metaData.getBoolean("skip_cancel");
        } catch (Exception e){
            e.printStackTrace();
        }

        if (!skipCancel) {
            mCancelClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(getContext(), getString(R.string.cancel_button_prompt), Toast.LENGTH_SHORT).show();
                    jumpToFollowMe();
                }
            };
        } else {
            // 否则跳到监视页面
            mCancelClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    startActivity(new Intent(getContext(), FunctionsActivity.class));
                }
            };
        }
        view.findViewById(R.id.guide_main_button_explode).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_location).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_destination).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_search_mode).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_search).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_lock).setOnClickListener(this);
        view.findViewById(R.id.guide_main_button_saving).setOnClickListener(this);
        return view;
    }

    private @IdRes int clickedId = -1;
    @Override
    public void onClick(View view) {
        boolean skip = false;

        Context context = getContext();
        skip = true;
        SystemServices.checkConnectedSsid(context, CustomSP.getString(context,
                WifiSettingsNameKey, defaultDeviceName), getActivity(), mCancelClickListener,
                SystemServices.REQUEST_WIFI_CONNECT_MANAGER);
        clickedId = view.getId();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WIFI_CONNECT_MANAGER){
            String ssid = NetworkUtils.getConnectWifiSsid(getContext());
            Toast.makeText(getContext().getApplicationContext(), getString(R.string.main_connected_ssid_prompt) + ssid, Toast.LENGTH_SHORT).show();
            if (ssid.toUpperCase().startsWith(SystemServices.XWWT_PREFIX)){
                jumpToFollowMe();
            }else {
                SystemServices.checkConnectedSsid(getContext(),
                        CustomSP.getString(getContext(), WifiSettingsNameKey, defaultDeviceName),
                        getActivity(), mCancelClickListener,
                        SystemServices.REQUEST_WIFI_CONNECT_MANAGER);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int getClickedId() {
        return clickedId;
    }

    private boolean jumpToFollowMe(){

        Intent intent = new Intent(getActivity(), GuideActivity.class);
        Bundle bundle = new Bundle();
        boolean skip = false;

        switch (clickedId){
            case R.id.guide_main_button_explode:
                bundle.putInt(GuideActivity.POSITION, 0 );
                break;
            case R.id.guide_main_button_location:
                bundle.putInt(GuideActivity.POSITION, 1 );
                break;
            case R.id.guide_main_button_destination:
                bundle.putInt(GuideActivity.POSITION, 2 );
                break;
            case R.id.guide_main_button_search_mode:
                bundle.putInt(GuideActivity.POSITION, 3 );
                break;
            case R.id.guide_main_button_search:
                bundle.putInt(GuideActivity.POSITION, 4 );
                break;
            case R.id.guide_main_button_lock:
                bundle.putInt(GuideActivity.POSITION, 5 );
                break;
            case R.id.guide_main_button_saving:
                bundle.putInt(GuideActivity.POSITION, 6);
                break;
            default:
                skip = true;
                break;
        }
        if (!skip) {
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return true;
    }
}
