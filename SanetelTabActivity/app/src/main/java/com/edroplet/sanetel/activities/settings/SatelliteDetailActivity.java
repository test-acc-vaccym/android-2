package com.edroplet.sanetel.activities.settings;

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
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.SatelliteInfo;
import com.edroplet.sanetel.beans.Satellites;
import com.edroplet.sanetel.utils.GalleryOnTime;
import com.edroplet.sanetel.view.ViewInject;
import com.edroplet.sanetel.view.annotation.BindId;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;
import com.edroplet.sanetel.view.custom.CustomTextView;

import java.util.Timer;

/**
 * An activity representing a single City detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SatelliteListActivity}.
 */
public class SatelliteDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static int[] satellitesImages = {R.mipmap.satellite1, R.mipmap.satellite2, R.mipmap.satellite3};
    public CollapsingToolbarLayout collap;
    public AppBarLayout appBarLayout;
    //因为setExpanded会调用事件监听，所以通过标志过滤掉
    public static int expendedtag=2;

    @BindId(R.id.satellite_detail_toolbar)
    private Toolbar toolbar;

    @BindId(R.id.frameLayout_satellite)
    private
    FrameLayout frameLayout;
    private SatelliteInfo satelliteInfo;
	
	private View fragmentView;

	/*
    @BindId(R.id.satellite_detail_save)
    private CustomButton satelliteDetailSave;

    @BindId(R.id.satellite_detail_return)
    private CustomButton satelliteDetailReturn;

    @BindId(R.id.satellite_detail_uuid)
    private CustomTextView satelliteDetailUuid;
    @BindId(R.id.id_detail)
    private CustomTextView satelliteDetailId;
    @BindId(R.id.name_detail)
    private CustomTextView satelliteDetailName;
    @BindId(R.id.polarization_detail)
    private CustomEditText satelliteDetailPlarization;
    @BindId(R.id.beacon_detail)
    private CustomEditText satelliteDetailBeacon;
    @BindId(R.id.longitude_detail)
    private CustomEditText satelliteDetailLongitude;
    @BindId(R.id.threshold_detail)
    private CustomEditText satelliteDetailThreshold;
    @BindId(R.id.symbol_rate_detail)
    private CustomEditText satelliteDetailSymbolRate;
    @BindId(R.id.comment_detail)
    private CustomEditText satelliteDetailComment;
    */

    Timer timer;
    GalleryOnTime galleryOnTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_detail);

        ViewInject.inject(this, this);

        /** 在fragment中监听
        satelliteDetailSave.setOnClickListener(this);
        satelliteDetailReturn.setOnClickListener(this);
        */
        galleryOnTime = new GalleryOnTime(this);
        galleryOnTime.setFrameLayout(frameLayout);
        galleryOnTime.setImages(satellitesImages);
        galleryOnTime.setImageView();
        timer = galleryOnTime.getTimer();

        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.satellite_detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        appBarLayout = (AppBarLayout) findViewById(R.id.satellite_detail_app_bar);
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
            satelliteInfo = Satellites.ITEM_MAP.get(getIntent().getStringExtra(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID));
            // 在Activity里动态加载Fragment
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID,
                    getIntent().getStringExtra(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID));
            SatelliteDetailFragment fragment = new SatelliteDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.satellite_detail_container, fragment)
                    .commit();
            /** 此方法不可行
            fragmentView = getSupportFragmentManager().findFragmentById(R.id.satellite_detail_container).getView();
            if (fragmentView != null) {
                satelliteDetailUuid = fragmentView.findViewById(R.id.satellite_detail_uuid);
                satelliteDetailId = fragmentView.findViewById(R.id.id_detail);
                satelliteDetailName = fragmentView.findViewById(R.id.name_detail);
                satelliteDetailPlarization = fragmentView.findViewById(R.id.polarization_detail);
                satelliteDetailBeacon = fragmentView.findViewById(R.id.beacon_detail);
                satelliteDetailLongitude = fragmentView.findViewById(R.id.longitude_detail);
                satelliteDetailThreshold = fragmentView.findViewById(R.id.threshold_detail);
                satelliteDetailSymbolRate = fragmentView.findViewById(R.id.symbol_rate_detail);
                satelliteDetailComment = fragmentView.findViewById(R.id.comment_detail);
            }*/
        }
        // iv_satellite = (ImageView) findViewById(R.id.iv_satellite);
    }

    @Override
    public void onClick(View v) {
        /** 在activity中引入fragment的内容这里不可行
        switch (v.getId()){
            case R.id.satellite_detail_save:
                if (satelliteDetailName != null) {
                    // 修改保存
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(SatelliteInfo.objectKey,
                            new SatelliteInfo(
                                    satelliteDetailUuid.getText().toString(),
                                    satelliteDetailId.getText().toString(),
                                    satelliteDetailName.getText().toString(),
                                    satelliteDetailPlarization.getText().toString(),
                                    satelliteDetailLongitude.getText().toString(),
                                    satelliteDetailBeacon.getText().toString(),
                                    satelliteDetailThreshold.getText().toString(),
                                    satelliteDetailSymbolRate.getText().toString(),
                                    satelliteDetailComment.getText().toString()
                            ));
                    bundle.putString(SatelliteInfo.uuidKey, satelliteDetailUuid.getText().toString());

                    intent.putExtras(bundle);
                    setResult(SatelliteListActivity.SATELLITE_DETAIL_REQUEST_CODE, intent);
                }
                finish();
                break;
            case R.id.satellite_detail_return:
                finish();
                break;
        }
        */
    }

    @Override
    protected void onStart() {
        galleryOnTime.setImageView();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onDestroy();
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
        galleryOnTime.setImageView();
    }

    @Override
    protected void onPause() {
        // timer.cancel();
        super.onPause();
    }

}

