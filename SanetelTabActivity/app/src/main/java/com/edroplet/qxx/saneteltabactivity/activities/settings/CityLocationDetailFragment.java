package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.CityElements;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

/**
 * A fragment representing a single CityLocation detail screen.
 * This fragment is either contained in a {@link CityLocationListActivity}
 * in two-pane mode (on tablets) or a {@link CityLocationDetailActivity}
 * on handsets.
 */
public class CityLocationDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

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

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = CityElements.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

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

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((CustomTextView) rootView.findViewById(R.id.city_name_detail)).setText(mItem.getName());
            ((CustomEditText) rootView.findViewById(R.id.city_detail_latitude)).setText(String.valueOf(mItem.getLatitude()));
            ((CustomEditText) rootView.findViewById(R.id.city_detail_longitude)).setText(String.valueOf(mItem.getLongitude()));
        }
        return rootView;
    }
}
