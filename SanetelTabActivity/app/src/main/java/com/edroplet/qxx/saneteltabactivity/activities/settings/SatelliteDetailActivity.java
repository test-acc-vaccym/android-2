package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.edroplet.qxx.saneteltabactivity.R;

import java.util.Random;

/**
 * An activity representing a single City detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SatelliteListActivity}.
 */
public class SatelliteDetailActivity extends AppCompatActivity {
    final int[] ivImages = {R.mipmap.s3, R.mipmap.s1, R.mipmap.s2};
    public CollapsingToolbarLayout collap;
    public AppBarLayout appBarLayout;
    //因为setExpanded会调用事件监听，所以通过标志过滤掉
    public static int expendedtag=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_detail);
        setImageView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        appBarLayout = (AppBarLayout) findViewById(R.id.satellite_detail_app_bar);
        appBarLayout.setExpanded(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (getSupportActionBar().getHeight()-appBarLayout.getHeight() == verticalOffset){
                    // TODO 折叠监听
                }
                if (expendedtag == 2 && verticalOffset == 0){
                    //展开监听
                    setImageView();
                }else if (expendedtag != 2 && verticalOffset == 0){
                    expendedtag++;
                }
            }
        });
        collap = (CollapsingToolbarLayout) findViewById(R.id.satellite_detail_toolbar_layout);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(SatelliteDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(SatelliteDetailFragment.ARG_ITEM_ID));
            SatelliteDetailFragment fragment = new SatelliteDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.city_detail_container, fragment)
                    .commit();
        }
    }

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
            // NavUtils.navigateUpTo(this, new Intent(this, SatelliteListActivity.class));
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImageView();
    }

    private void setImageView() {
        ImageView iv_satellite = (ImageView) findViewById(R.id.iv_satellite);
        Random random = new Random();
        //for (int iv : ivImages){
        //    if (iv_satellite != null){
        int seed=100;
        int rInt = (random.nextInt(100))%(ivImages.length);
        iv_satellite.setImageResource(ivImages[rInt]);
        //    }
        //}
    }
}

