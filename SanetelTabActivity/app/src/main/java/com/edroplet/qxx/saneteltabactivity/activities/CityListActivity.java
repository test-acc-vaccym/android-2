package com.edroplet.qxx.saneteltabactivity.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;

import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameterItem;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameters;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.R.attr.path;

/**
 * An activity representing a list of Cities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CityDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CityListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // NavUtils.navigateUpTo(this, new Intent(this, CityListActivity.class));
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        /**
         * 没必要复制先观察
        try {
            String fileNames[] =getAssets().list(".");
            for (String s:fileNames) {
                copyFilesFassets(this, s, );
            }
        }catch (IOException ie){
            ie.printStackTrace();
        }
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
            ab.setTitle(R.string.satellite_toolbar_title);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab !=null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

        View recyclerView = findViewById(R.id.city_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.city_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            SatelliteParameters sp = new SatelliteParameters(this);
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(sp.ITEMS));
        }catch (JSONException je){
            je.printStackTrace();
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<SatelliteParameterItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<SatelliteParameterItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.city_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            // holder.mIdView.setText(holder.mItem.id);
            holder.mNameView.setText(holder.mItem.name);
            holder.mPolarizationView.setText(holder.mItem.polarization);
            holder.mLongitudeView.setText(holder.mItem.longitude);
            holder.mBeaconView.setText(holder.mItem.beacon);
            holder.mThresholdView.setText(holder.mItem.threshold);
            holder.mSymbolRateView.setText(holder.mItem.symbolRate);
            // holder.mComentView.setText(holder.mItem.comment);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CityDetailFragment.ARG_ITEM_ID, holder.mItem.mId.toString());
                        CityDetailFragment fragment = new CityDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.city_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CityDetailActivity.class);
                        intent.putExtra(CityDetailFragment.ARG_ITEM_ID, holder.mItem.mId.toString());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            // public final TextView mIdView;
            public final TextView mNameView;
            public final TextView mPolarizationView;
            public final TextView mLongitudeView;
            public final TextView mBeaconView;
            public final TextView mThresholdView;
            public final TextView mSymbolRateView;
            // public final TextView mComentView;
            public SatelliteParameterItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                // mIdView = (TextView) view.findViewById(R.id.id);
                // assert mIdView != null;
                mNameView = (TextView) view.findViewById(R.id.name);
                assert mNameView != null;
                mPolarizationView = (TextView) view.findViewById(R.id.polarization);
                assert mPolarizationView == null;
                mLongitudeView = (TextView) view.findViewById(R.id.longitude);
                assert mLongitudeView == null;
                mBeaconView = (TextView) view.findViewById(R.id.beacon);
                assert mBeaconView != null;
                mThresholdView = (TextView) view.findViewById(R.id.threshold);
                assert mThresholdView != null;
                mSymbolRateView = (TextView) view.findViewById(R.id.symbolRate);
                assert mSymbolRateView != null;
                // mComentView = (TextView) view.findViewById(R.id.comment);
                // assert mComentView != null;
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }
}
