package com.edroplet.qxx.saneteltabactivity.activities.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.functions.ApplicationActivity;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

public class GuideEntryActivity extends AppCompatActivity implements View.OnClickListener{
    private CustomButton mLocation;
    private CustomButton mDestination;
    private CustomButton mSearchMode;
    private CustomButton mSearching;
    private CustomButton mLock;
    private CustomButton mSaving;

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(GuideEntryActivity.this, FollowMeActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.guide_main_button_explode:
                bundle.putInt("position", 0 );
                break;
            case R.id.guide_main_button_location:
                bundle.putInt("position", 1 );
                break;
            case R.id.guide_main_button_destination:
                bundle.putInt("position", 2 );
                break;
            case R.id.guide_main_button_search_mode:
                bundle.putInt("position", 3 );
                break;
            case R.id.guide_main_button_search:
                bundle.putInt("position", 4 );
                break;
            case R.id.guide_main_button_lock:
                bundle.putInt("position", 5 );
                break;
            case R.id.guide_main_button_saving:
                bundle.putInt("position", 6);
                break;
            default:
                bundle.putInt("position", 0 );
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid_entry);

        // StatusBarControl.setupToolbar(this, R.id.guide_tool_bar);
        setToolbar();

        findViewById(R.id.guide_main_button_explode).setOnClickListener(this);
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
                startActivity(new Intent(GuideEntryActivity.this, ApplicationActivity.class));
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
