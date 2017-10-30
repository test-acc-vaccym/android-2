package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Satellites;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.Map;

/**
 * A fragment representing a single City detail screen.
 * This fragment is either contained in a {@link SatelliteListActivity}
 * in two-pane mode (on tablets) or a {@link SatelliteDetailActivity}
 * on handsets.
 */
public class SatelliteDetailFragment extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String SATELLITE_ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private SatelliteInfo mItem;

    @BindId(R.id.satellite_detail_save)
    private CustomButton satelliteDetailSave;

    @BindId(R.id.satellite_detail_return)
    private CustomButton satelliteDetailReturn;

    @BindId(R.id.satellite_detail_uuid)
    private CustomTextView satelliteDetailUuid;
    @BindId(R.id.id_detail)
    private CustomTextView satelliteDetailId;
    @BindId(R.id.name_detail)
    private CustomTextView satelliteDetailName;
    @BindId(R.id.polarization_detail)
    private CustomEditText satelliteDetailPlarization;
    @BindId(R.id.beacon_detail)
    private CustomEditText satelliteDetailBeacon;
    @BindId(R.id.longitude_detail)
    private CustomEditText satelliteDetailLongitude;
    @BindId(R.id.threshold_detail)
    private CustomEditText satelliteDetailThreshold;
    @BindId(R.id.symbol_rate_detail)
    private CustomEditText satelliteDetailSymbolRate;
    @BindId(R.id.comment_detail)
    private CustomEditText satelliteDetailComment;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SatelliteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(SATELLITE_ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String itemId = getArguments().getString(SATELLITE_ARG_ITEM_ID);
            Map<String, SatelliteInfo> map = Satellites.ITEM_MAP;
            mItem = map.get(itemId);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.satellite_detail_toolbar_layout);
            if (appBarLayout != null && mItem != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment_satellite_detail, container, false);
        satelliteDetailSave = rootView.findViewById(R.id.satellite_detail_save);
        satelliteDetailReturn = rootView.findViewById(R.id.satellite_detail_return);

        satelliteDetailSave.setOnClickListener(this);
        satelliteDetailReturn.setOnClickListener(this);

        satelliteDetailUuid = rootView.findViewById(R.id.satellite_detail_uuid);
        satelliteDetailId = rootView.findViewById(R.id.id_detail);
        satelliteDetailName = rootView.findViewById(R.id.name_detail);
        satelliteDetailPlarization = rootView.findViewById(R.id.polarization_detail);
        satelliteDetailBeacon = rootView.findViewById(R.id.beacon_detail);
        satelliteDetailLongitude = rootView.findViewById(R.id.longitude_detail);
        satelliteDetailThreshold = rootView.findViewById(R.id.threshold_detail);
        satelliteDetailSymbolRate = rootView.findViewById(R.id.symbol_rate_detail);
        satelliteDetailComment = rootView.findViewById(R.id.comment_detail);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            satelliteDetailUuid.setText(mItem.mId.toString());
            satelliteDetailId.setText(mItem.id);
            satelliteDetailName.setText(mItem.toString());
            satelliteDetailPlarization.setText(mItem.polarization);
            satelliteDetailBeacon.setText(mItem.beacon);
            satelliteDetailLongitude.setText(mItem.longitude);
            satelliteDetailThreshold.setText(mItem.threshold);
            satelliteDetailSymbolRate.setText(mItem.symbolRate);
            satelliteDetailComment.setText(mItem.comment);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.satellite_detail_save:
                // 修改保存
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(SatelliteInfo.objectKey,
                        new SatelliteInfo(
                                satelliteDetailUuid.getText().toString(),
                                satelliteDetailId.getText().toString(),
                                satelliteDetailName.getText().toString(),
                                satelliteDetailPlarization.getText().toString(),
                                satelliteDetailLongitude.getText().toString(),
                                satelliteDetailBeacon.getText().toString(),
                                satelliteDetailThreshold.getText().toString(),
                                satelliteDetailSymbolRate.getText().toString(),
                                satelliteDetailComment.getText().toString()
                        ));
                bundle.putString(SatelliteInfo.uuidKey, satelliteDetailUuid.getText().toString());

                intent.putExtras(bundle);
                getActivity().setResult(SatelliteListActivity.SATELLITE_DETAIL_REQUEST_CODE,intent);
                getActivity().finish();
                break;
            case R.id.satellite_detail_return:
                getActivity().finish();
                break;
        }
    }

}
