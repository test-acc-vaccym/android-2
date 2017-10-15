package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edroplet.qxx.saneteltabactivity.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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

    // 定时器
    Timer timer = new Timer();
    private static int i = 0;
    // ImageView iv_satellite;
    FrameLayout frameLayout;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            Log.e("@@@", i + "");
            //index=msg.what;
            if (i > 3){
                i = 0;
            }else{
                switch (i){
                    case 1:
                        frameLayout.setBackgroundResource(R.mipmap.s1);
                        break;
                    case 2:
                        frameLayout.setBackgroundResource(R.mipmap.s2);
                        break;
                    default:
                    case 3:
                        frameLayout.setBackgroundResource(R.mipmap.s3);
                        break;
                }
                frameLayout.invalidate();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_detail);
        setImageView();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
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

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout01);

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
                    .add(R.id.satellite_detail_container, fragment)
                    .commit();
        }

        // iv_satellite = (ImageView) findViewById(R.id.iv_satellite);
    }

    @Override
    protected void onStart() {
        setImageView();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
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
        setImageView();
    }

    @Override
    protected void onPause() {
        // timer.cancel();
        super.onPause();
    }

    private void setImageView() {
        // Random random = new Random();
        // int seed=100;
        // int rInt = (random.nextInt(100))%(ivImages.length);
        // Glide.with( this ).load( R.mipmap.s1 ).placeholder( R.mipmap.s2 ).error( R.mipmap.s3 ).into( iv_satellite ) ;
        // iv_satellite.setImageResource(ivImages[rInt]);

//        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        try {
//            Date startDate = dateFormatter.parse("2017/09/27 01:06:00");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        final int schedule = getResources().getInteger(R.integer.detail_schedule_timer);
        // timer.scheduleAtFixedRate(new TimerTask(){
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                i++;
                Message message = new Message();
                message.what = i;
                handler.sendMessage(message);
                try {
                    Thread.sleep(schedule);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, schedule);
    }
}

