package com.edroplet.sanetel.activities.settings;

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

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.adapters.SpinnerAdapter2;
import com.edroplet.sanetel.beans.Cities;
import com.edroplet.sanetel.beans.LocationInfo;
import com.edroplet.sanetel.utils.ConvertUtil;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomTextView;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a single CityLocation detail screen.
 * This fragment is either contained in a {@link CityLocationListActivity}
 * in two-pane mode (on tablets) or a {@link CityLocationDetailActivity}
 * on handsets.
 */
public class CityLocationDetailFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.city_detail_latitude)
    CustomEditText cityDetailLatitude;

    @BindView(R.id.city_detail_longitude)
    CustomEditText cityDetailLongitude;

    @BindView(R.id.city_detail_name)
    CustomEditText cityName;

    @BindView(R.id.city_detail_province)
    CustomEditText province;

    @BindView(R.id.city_detail_id)
    CustomTextView cityId;

    private CustomButton cityDetailSave;
    private CustomButton cityDetailReturn;

    @BindView(R.id.longitude_unit)
    Spinner longitudeUnit;
    @BindView(R.id.latitude_unit)
    Spinner latitudeUnit;

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
                mItem.setLatitudeUnitPosition(latitudeUnit.getSelectedItemPosition());
                mItem.setLongitudeUnitPosition(longitudeUnit.getSelectedItemPosition());

                bundle.putParcelable(LocationInfo.objectKey, mItem);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK, intent);
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

    Unbinder unbinder;
    Context context;
    @BindArray(R.array.longitude_unit)
    String[] longitudeArray;
    @BindArray(R.array.latitude_unit)
    String[] latitudeArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_city_detail, container, false);
        unbinder = ButterKnife.bind(this,rootView);
        context = getContext();

        cityName.setFocusable(false);

        longitudeUnit.setAdapter(new SpinnerAdapter2(context, android.R.layout.simple_list_item_1, android.R.id.text1, longitudeArray));
        latitudeUnit.setAdapter(new SpinnerAdapter2(context, android.R.layout.simple_list_item_1, android.R.id.text1, latitudeArray));

        if (mItem != null) {
            cityId.setText(mItem.getmId());
            province.setText(mItem.getProvince());
            cityName.setText(mItem.getName());
            cityDetailLatitude.setText(String.valueOf(mItem.getLatitude()));
            cityDetailLongitude.setText(String.valueOf(mItem.getLongitude()));

            longitudeUnit.setSelection(mItem.getLongitudeUnitPosition());
            latitudeUnit.setSelection(mItem.getLatitudeUnitPosition());

        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
