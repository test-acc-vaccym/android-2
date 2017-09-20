package com.edroplet.qxx.saneteltabactivity.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.control.StatusBarControl;

public class GuideEntryActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_guid_entry);

        // StatusBarControl.setupToolbar(this, R.id.guide_tool_bar);
        setToolbar();

        mExplode = (Button) findViewById(R.id.guide_main_button_explode);
        mExplode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
        // BottomNavigationView manual_navigation = (BottomNavigationView) findViewById(R.id.guide_navigation);
        // manual_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.guide_tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.guide_enter_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"直接进入吧！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }else if (id == R.id.guide_enter_main){
            Toast.makeText(this,"直接进入吧！", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    */

}
