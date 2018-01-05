package com.edroplet.sanetel.activities.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.functions.FunctionsActivity;
import com.edroplet.sanetel.view.custom.CustomButton;

public class GuideEntryActivity extends AppCompatActivity implements View.OnClickListener{
    private CustomButton mLocation;
    private CustomButton mDestination;
    private CustomButton mSearchMode;
    private CustomButton mSearching;
    private CustomButton mLock;
    private CustomButton mSaving;

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(GuideEntryActivity.this, GuideActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.guide_main_button_explode:
                bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_EXPLODE.ordinal() );
                break;
            case R.id.guide_main_button_location:
                bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_LOCATION.ordinal() );
                break;
            case R.id.guide_main_button_destination:
                bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_DESTINATION.ordinal() );
                break;
            case R.id.guide_main_button_search_mode:
                bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_SEARCH_MODE.ordinal() );
                break;
            case R.id.guide_main_button_search:
                bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_SEARCHING.ordinal() );
                break;
            case R.id.guide_main_button_lock:
                bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_LOCKER.ordinal() );
                break;
            case R.id.guide_main_button_saving:
                bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_SAVING.ordinal());
                break;
            default:
                bundle.putInt(GuideActivity.POSITION, GuideActivity.GUIDE_PAGES_INDEX.INDEX_EXPLODE.ordinal() );
                break;
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid_entry);

        // StatusBarControl.setupToolbar(this, R.id.guide_tool_bar);
        setToolbar();

        findViewById(R.id.guide_main_button_explode).setOnClickListener(this);
        findViewById(R.id.guide_main_button_location).setOnClickListener(this);
        findViewById(R.id.guide_main_button_destination).setOnClickListener(this);
        findViewById(R.id.guide_main_button_search_mode).setOnClickListener(this);
        findViewById(R.id.guide_main_button_search).setOnClickListener(this);
        findViewById(R.id.guide_main_button_lock).setOnClickListener(this);
        findViewById(R.id.guide_main_button_saving).setOnClickListener(this);
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
                startActivity(new Intent(GuideEntryActivity.this, FunctionsActivity.class));
            }
        });
    }

}
