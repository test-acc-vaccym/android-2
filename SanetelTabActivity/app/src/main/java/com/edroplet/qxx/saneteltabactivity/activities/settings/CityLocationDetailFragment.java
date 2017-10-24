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
import com.edroplet.qxx.saneteltabactivity.beans.Cities;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

/**
 * A fragment representing a single CityLocation detail screen.
 * This fragment is either contained in a {@link CityLocationListActivity}
 * in two-pane mode (on tablets) or a {@link CityLocationDetailActivity}
 * on handsets.
 */
public class CityLocationDetailFragment extends Fragment {

    @BindId(R.id.city_detail_latitude)
    private CustomEditText cityDetailLatitude;

    @BindId(R.id.city_detail_longitude)
    private CustomEditText cityDetailLongitude;

    @BindId(R.id.city_detail_name)
    private CustomTextView cityName;

    @BindId(R.id.city_detail_province)
    private CustomTextView provience;

    @BindId(R.id.city_detail_id)
    private CustomTextView cityId;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String CITY_ARG_ITEM_ID = "item_id";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment_city_detail, container, false);
        ViewInject.inject(getActivity(), this);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            cityId =  ((CustomTextView) rootView.findViewById(R.id.city_detail_id));
            cityId.setText(mItem.getmId());
            provience = ((CustomTextView) rootView.findViewById(R.id.city_detail_province));
            provience.setText(mItem.getProvince());
            cityName = ((CustomTextView) rootView.findViewById(R.id.city_detail_name));
            cityName.setText(mItem.getName());
            cityDetailLatitude = ((CustomEditText) rootView.findViewById(R.id.city_detail_latitude));
            cityDetailLatitude.setText(String.valueOf(mItem.getLatitude()));
            cityDetailLongitude = ((CustomEditText) rootView.findViewById(R.id.city_detail_longitude));
            cityDetailLongitude.setText(String.valueOf(mItem.getLongitude()));
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        Bundle bundle = new Bundle();
        if (cityId !=null) {
            bundle.putString(LocationInfo.JSON_ID_KEY, cityId.getText().toString());
        } else {
            bundle.putString(LocationInfo.JSON_ID_KEY, mItem.getmId());
        }
        bundle.putParcelable(LocationInfo.objectKey, new LocationInfo(cityId.getText().toString(), provience.getText().toString(), cityName.getText().toString(),
                ConvertUtil.convertToFloat(cityDetailLatitude.getText().toString(), 0),
                ConvertUtil.convertToFloat(cityDetailLongitude.getText().toString(), 0)));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getActivity().setResult(CityLocationListActivity.CITY_DETAIL_REQUEST_CODE, intent);
        super.onDestroy();
    }
}
