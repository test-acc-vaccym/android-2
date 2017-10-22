package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.GalleryOnTime;

import java.util.Timer;

/**
 * An activity representing a single CityLocation detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CityLocationListActivity}.
 */
public class CityLocationDetailActivity extends AppCompatActivity {

    private static int[] cityImages = {R.mipmap.city1, R.mipmap.city2, R.mipmap.city3};
    public CollapsingToolbarLayout collap;
    public AppBarLayout appBarLayout;
    //因为setExpanded会调用事件监听，所以通过标志过滤掉
    public static int expendedtag=2;
    FrameLayout frameLayout;
    Timer timer;
    GalleryOnTime galleryOnTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.city_detail_toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.city_detail_frame);
        galleryOnTime = new GalleryOnTime(this);
        galleryOnTime.setFrameLayout(frameLayout);
        galleryOnTime.setImages(cityImages);
        galleryOnTime.setImageView();
        timer = galleryOnTime.getTimer();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.city_detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.city_detail_app_bar);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (getSupportActionBar().getHeight()-appBarLayout.getHeight() == verticalOffset){
                    // TODO 折叠监听
                    // timer.cancel();
                    toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.state_bar_background));
                }
                if (expendedtag == 2 && verticalOffset == 0){
                    //展开监听
                    toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(),R.color.button_blink));
                    galleryOnTime.setImageView();
                }else if (expendedtag != 2 && verticalOffset == 0){
                    expendedtag++;
                }
            }
        });

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
            arguments.putString(CityLocationDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(CityLocationDetailFragment.ARG_ITEM_ID));
            CityLocationDetailFragment fragment = new CityLocationDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.city_detail_container, fragment)
                    .commit();
        }
    }
    @Override
    protected void onStart() {
        galleryOnTime.setImageView();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        galleryOnTime.setImageView();
    }

    @Override
    protected void onPause() {
        // timer.cancel();
        super.onPause();
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
            NavUtils.navigateUpTo(this, new Intent(this, CityLocationListActivity.class));
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
