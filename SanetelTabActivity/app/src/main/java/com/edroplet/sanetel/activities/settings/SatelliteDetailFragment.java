package com.edroplet.sanetel.activities.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.SpinnerAdapter2;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.beans.Satellites;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomTextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    CustomButton satelliteDetailSave;
    CustomButton satelliteDetailReturn;

    @BindView(R.id.satellite_detail_uuid)
    CustomTextView satelliteDetailUuid;
    @BindView(R.id.id_detail)
    CustomTextView satelliteDetailId;
    @BindView(R.id.name_detail)
    CustomEditText satelliteDetailName;
    @BindView(R.id.polarization_detail)
    Spinner satelliteDetailPolarization;
    @BindView(R.id.beacon_detail)
    CustomEditText satelliteDetailBeacon;
    @BindView(R.id.longitude_detail)
    CustomEditText satelliteDetailLongitude;
    @BindView(R.id.threshold_detail)
    CustomEditText satelliteDetailThreshold;
    @BindView(R.id.symbol_rate_detail)
    CustomEditText satelliteDetailSymbolRate;
    @BindView(R.id.comment_detail)
    CustomEditText satelliteDetailComment;
    @BindView(R.id.carrier_detail)
    CustomEditText satelliteDetailCarrier;

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

    /**
     *  在Fragment里的onAttach()方法中获得Activity的Textview控件的引用,
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SatelliteDetailActivity){
            SatelliteDetailActivity activity = (SatelliteDetailActivity) context;
            satelliteDetailSave = (CustomButton) activity.findViewById(R.id.satellite_detail_save);
            satelliteDetailReturn = (CustomButton) activity.findViewById(R.id.satellite_detail_return);

            assert satelliteDetailReturn != null;
            assert satelliteDetailSave != null;

            satelliteDetailSave.setOnClickListener(this);
            satelliteDetailReturn.setOnClickListener(this);

        }
    }

    Satellites satellites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_satellite_detail, container, false);

        ButterKnife.bind(this, rootView);

        /*
        satelliteDetailSave = rootView.findViewById(R.id.satellite_detail_save);
        satelliteDetailReturn = rootView.findViewById(R.id.satellite_detail_return);

        satelliteDetailSave.setOnClickListener(this);
        satelliteDetailReturn.setOnClickListener(this);
        */

        satelliteDetailSymbolRate.setFilters(new InputFilter[]{new InputFilterFloat(6000,30000)});
        
        satelliteDetailComment = rootView.findViewById(R.id.comment_detail);

        // 不让被定焦
        satelliteDetailName.setFocusable(false);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            satelliteDetailUuid.setText(mItem.getmId().toString());
            satelliteDetailId.setText(mItem.getId());
            satelliteDetailName.setText(mItem.toString());
            satelliteDetailBeacon.setText(mItem.beacon);
            satelliteDetailLongitude.setText(mItem.longitude);
            satelliteDetailThreshold.setText(mItem.threshold);
            satelliteDetailSymbolRate.setText(mItem.symbolRate);
            satelliteDetailComment.setText(mItem.comment);
            satelliteDetailCarrier.setText(mItem.carrier);

            try {
                satellites = new Satellites(getContext());
                String[] polarizationArray = satellites.getSatellitePolarizationArray(mItem.name);
                satelliteDetailPolarization.setAdapter(new SpinnerAdapter2(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, polarizationArray));

                for(int i=0; i<polarizationArray.length; i++){
                    if(mItem.polarization.equals(polarizationArray[i])){
                        satelliteDetailPolarization.setSelection(i,true);
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
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
                                satelliteDetailPolarization.getSelectedItem().toString(),
                                satelliteDetailLongitude.getText().toString(),
                                satelliteDetailBeacon.getText().toString(),
                                satelliteDetailThreshold.getText().toString(),
                                satelliteDetailSymbolRate.getText().toString(),
                                satelliteDetailComment.getText().toString(),
                                satelliteDetailCarrier.getText().toString(),
                                null
                        ));
                bundle.putString(SatelliteInfo.uuidKey, satelliteDetailUuid.getText().toString());

                intent.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
                break;
            case R.id.satellite_detail_return:
                getActivity().finish();
                break;
        }
    }

}
