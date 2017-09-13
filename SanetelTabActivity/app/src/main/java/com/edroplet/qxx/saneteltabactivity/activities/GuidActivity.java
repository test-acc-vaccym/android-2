package com.edroplet.qxx.saneteltabactivity.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.edroplet.qxx.saneteltabactivity.R;

public class GuidActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mExplode;
    private Button mLocation;
    private Button mDestination;
    private Button mSearchMode;
    private Button mSearching;
    private Button mLock;
    private Button mSaving;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
            }
            return false;
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        mExplode = (Button) findViewById(R.id.guide_main_button_explode);
        mExplode.setOnClickListener(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.guide_main_button_explode:
                break;
        }
    }
}
