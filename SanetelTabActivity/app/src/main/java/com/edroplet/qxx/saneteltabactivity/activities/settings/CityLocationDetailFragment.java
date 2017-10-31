package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Cities;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.Arrays;

/**
 * A fragment representing a single CityLocation detail screen.
 * This fragment is either contained in a {@link CityLocationListActivity}
 * in two-pane mode (on tablets) or a {@link CityLocationDetailActivity}
 * on handsets.
 */
public class CityLocationDetailFragment extends Fragment implements View.OnClickListener{

    @BindId(R.id.city_detail_latitude)
    private CustomEditText cityDetailLatitude;

    @BindId(R.id.city_detail_longitude)
    private CustomEditText cityDetailLongitude;

    @BindId(R.id.city_detail_name)
    private CustomTextView cityName;

    @BindId(R.id.city_detail_province)
    private CustomEditText province;

    @BindId(R.id.city_detail_id)
    private CustomTextView cityId;

    private CustomButton cityDetailSave;
    private CustomButton cityDetailReturn;

    private Spinner longitudeUnit;
    private Spinner latitudeUnit;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String CITY_ARG_ITEM_ID = "city_item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private LocationInfo mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityLocationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(CITY_ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Cities.ITEM_MAP.get(getArguments().getString(CITY_ARG_ITEM_ID));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.city_detail_toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city_detail_save:
                Bundle bundle = new Bundle();
                if (cityId !=null) {
                    bundle.putString(LocationInfo.JSON_ID_KEY, cityId.getText().toString());
                } else {
                    bundle.putString(LocationInfo.JSON_ID_KEY, mItem.getmId());
                }
                mItem.setLatitude(ConvertUtil.convertToFloat(cityDetailLatitude.getText().toString(), 0f));
                mItem.setLongitude(ConvertUtil.convertToFloat(cityDetailLongitude.getText().toString(), 0f));
                mItem.setProvince(province.getText().toString());
                mItem.setName(cityName.getText().toString());
                mItem.setLatitudeUnit(latitudeUnit.getSelectedItem().toString());
                mItem.setLongitudeUnit(longitudeUnit.getSelectedItem().toString());

                bundle.putParcelable(LocationInfo.objectKey, mItem);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                getActivity().setResult(CityLocationListActivity.CITY_DETAIL_REQUEST_CODE, intent);
                getActivity().finish();
                break;
            case R.id.city_detail_return:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CityLocationDetailActivity){
            CityLocationDetailActivity activity = (CityLocationDetailActivity)context;
            cityDetailSave = (CustomButton) activity.findViewById(R.id.city_detail_save);
            cityDetailReturn = (CustomButton) activity.findViewById(R.id.city_detail_return);
            assert cityDetailReturn != null;
            assert cityDetailSave != null;

            cityDetailSave.setOnClickListener(this);
            cityDetailReturn.setOnClickListener(this);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment_city_detail, container, false);
        ViewInject.inject(getActivity(), this);

        // Show the dummy content as text in a TextView.
        cityId =  ((CustomTextView) rootView.findViewById(R.id.city_detail_id));
        province = ((CustomEditText) rootView.findViewById(R.id.city_detail_province));
        cityName = ((CustomTextView) rootView.findViewById(R.id.city_detail_name));
        cityDetailLatitude = ((CustomEditText) rootView.findViewById(R.id.city_detail_latitude));
        cityDetailLongitude = ((CustomEditText) rootView.findViewById(R.id.city_detail_longitude));
        longitudeUnit = rootView.findViewById(R.id.longitude_unit);
        latitudeUnit = rootView.findViewById(R.id.latitude_unit);

        if (mItem != null) {
            cityId.setText(mItem.getmId());
            province.setText(mItem.getProvince());
            cityName.setText(mItem.getName());
            cityDetailLatitude.setText(String.valueOf(mItem.getLatitude()));
            cityDetailLongitude.setText(String.valueOf(mItem.getLongitude()));

            String[]  longitudeUnits = getContext().getResources().getStringArray(R.array.longitude_unit);
            String longitudeUnitString = mItem.getLongitudeUnit();

            if (longitudeUnitString != null && longitudeUnitString.equals(longitudeUnits[1]))
                longitudeUnit.setSelection(1);
            else
                longitudeUnit.setSelection(0);

            String[]  latitudeUnits = getContext().getResources().getStringArray(R.array.latitude_unit);
            String latitudeUnitString = mItem.getLongitudeUnit();
            if (latitudeUnitString != null && latitudeUnitString.equals(latitudeUnits[1]))
                latitudeUnit.setSelection(1);
            else
                latitudeUnit.setSelection(0);

        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
