package com.edroplet.qxx.saneteltabactivity.activities;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameterItem;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameters;

import java.util.Map;

/**
 * A fragment representing a single City detail screen.
 * This fragment is either contained in a {@link CityListActivity}
 * in two-pane mode (on tablets) or a {@link CityDetailActivity}
 * on handsets.
 */
public class CityDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private SatelliteParameterItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String itemId = getArguments().getString(ARG_ITEM_ID);
            Map<String, SatelliteParameterItem> map = SatelliteParameters.ITEM_MAP;
            mItem = map.get(itemId);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && mItem != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.city_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.uuid_detail)).setText(mItem.mId.toString());
            ((TextView) rootView.findViewById(R.id.id_detail)).setText(mItem.id);
            ((TextView) rootView.findViewById(R.id.name_detail)).setText(mItem.toString());
            ((EditText) rootView.findViewById(R.id.polarization_detail)).setText(mItem.polarization);
            ((EditText) rootView.findViewById(R.id.beacon_detail)).setText(mItem.beacon);
            ((EditText) rootView.findViewById(R.id.longitude_detail)).setText(mItem.longitude);
            ((EditText) rootView.findViewById(R.id.threshold_detail)).setText(mItem.threshold);
            ((EditText) rootView.findViewById(R.id.symbol_rate_detail)).setText(mItem.symbolRate);
            ((EditText) rootView.findViewById(R.id.comment_detail)).setText(mItem.comment);
        }

        return rootView;
    }
}
