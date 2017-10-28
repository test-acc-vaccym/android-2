package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Satellites;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.util.Map;

/**
 * A fragment representing a single City detail screen.
 * This fragment is either contained in a {@link SatelliteListActivity}
 * in two-pane mode (on tablets) or a {@link SatelliteDetailActivity}
 * on handsets.
 */
public class SatelliteDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String SATELLITE_ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private SatelliteInfo mItem;

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

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((CustomTextView) rootView.findViewById(R.id.satellite_detail_uuid)).setText(mItem.mId.toString());
            ((CustomTextView) rootView.findViewById(R.id.id_detail)).setText(mItem.id);
            ((CustomTextView) rootView.findViewById(R.id.name_detail)).setText(mItem.toString());
            ((CustomEditText) rootView.findViewById(R.id.polarization_detail)).setText(mItem.polarization);
            ((CustomEditText) rootView.findViewById(R.id.beacon_detail)).setText(mItem.beacon);
            ((CustomEditText) rootView.findViewById(R.id.longitude_detail)).setText(mItem.longitude);
            ((CustomEditText) rootView.findViewById(R.id.threshold_detail)).setText(mItem.threshold);
            ((CustomEditText) rootView.findViewById(R.id.symbol_rate_detail)).setText(mItem.symbolRate);
            ((CustomEditText) rootView.findViewById(R.id.comment_detail)).setText(mItem.comment);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        // TODO: 2017/10/24 修改保存
        super.onDestroy();
    }
}
