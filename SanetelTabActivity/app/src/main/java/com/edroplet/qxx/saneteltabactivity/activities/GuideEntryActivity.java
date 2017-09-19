package com.edroplet.qxx.saneteltabactivity.activities;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;

public class GuideEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mExplode;
    private Button mLocation;
    private Button mDestination;
    private Button mSearchMode;
    private Button mSearching;
    private Button mLock;
    private Button mSaving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);

        StatusBarControl.setupToolbar(this, R.id.guide_tool_bar);

        mExplode = (Button) findViewById(R.id.guide_main_button_explode);
        mExplode.setOnClickListener(this);
        // BottomNavigationView manual_navigation = (BottomNavigationView) findViewById(R.id.guide_navigation);
        // manual_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.guide_main_button_explode:
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
