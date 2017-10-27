package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;

import com.edroplet.qxx.saneteltabactivity.adapters.SatelliteItemRecyclerViewAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Satellites;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Cities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SatelliteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SatelliteListActivity extends AppCompatActivity {
    public static final int NEW_SATELLITES_REQUEST_CODE = 11000;
    public static final int SATELLITE_DETAIL_REQUEST_CODE = 11001;

    private SatelliteItemRecyclerViewAdapter satelliteItemRecyclerViewAdapter;
    private Satellites sp;

    @BindId((R.id.satellite_list))
    private RecyclerView recyclerView;

    @BindId(R.id.satellite_list_fab)
    private FloatingActionButton fab;

    @BindId(R.id.recover_satellite)

    private CustomButton recoverySatellites;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position = 0;
        String id = "";
        SatelliteInfo satelliteInfo = null;
        if (null != data) {
            if (data.hasExtra(SatelliteInfo.objectKey)) {
                Serializable serializable = data.getSerializableExtra(SatelliteInfo.objectKey);
                satelliteInfo = (SatelliteInfo) serializable;
            }
            if (data.hasExtra(SatelliteInfo.positionKey)) {
                position = data.getIntExtra(SatelliteInfo.positionKey, 0);
            }
            if (data.hasExtra(SatelliteInfo.uuidKey)){
                id = data.getStringExtra(SatelliteInfo.uuidKey);
            }
        }

        switch (requestCode){
            case SATELLITE_DETAIL_REQUEST_CODE:
                if (id != null && id.length() > 0) {
                    sp.update(id, satelliteInfo);
                    satelliteItemRecyclerViewAdapter.notifyItemChanged(position);
                }
                break;
            case NEW_SATELLITES_REQUEST_CODE:
                // if(resultCode== Activity.RESULT_OK){
                if (satelliteInfo != null) {
                    //  刷新当前activity界面数据
                    sp.addItem(satelliteInfo);
                    //RecyclerView列表进行UI数据更新
                    satelliteItemRecyclerViewAdapter.notifyItemInserted(position);
                    //如果在第一项添加模拟数据需要调用 scrollToPosition（0）把列表移动到顶端（可选）
                    recyclerView.scrollToPosition(position);
                }
                // }
                break;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satellite_list);

        ViewInject.inject(this, this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.back);
            // ab.setTitle(R.string.satellite_toolbar_title);
            // 设置居中的时候不能含有原标题
            ab.setDisplayShowTitleEnabled(false);
        }

        if (fab !=null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

        findViewById(R.id.add_satellite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SatelliteListActivity.this, NewSatelliteActivity.class), NEW_SATELLITES_REQUEST_CODE);
            }
        });
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.satellite_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recoverySatellites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomSP.putBoolean(getApplicationContext(), CustomSP.firstReadSatellites, true);
                sp.clear();
                finish();
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            sp = new Satellites(this);
            satelliteItemRecyclerViewAdapter = new SatelliteItemRecyclerViewAdapter(this, sp.ITEMS, mTwoPane);
            /*
            satelliteItemRecyclerViewAdapter.setOnItemClickListener(new SatelliteItemRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int data) {
                    Toast.makeText(SatelliteListActivity.this, "点击了"+data, Toast.LENGTH_SHORT).show();
                }
            });
            satelliteItemRecyclerViewAdapter.setOnItemLongClickListener(new SatelliteItemRecyclerViewAdapter.OnRecyclerItemLongListener() {
                @Override
                public boolean onItemLongClick(View view, int position) {
                    Toast.makeText(SatelliteListActivity.this, "长按了"+position, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            */
            recyclerView.setAdapter(satelliteItemRecyclerViewAdapter);
        }catch (JSONException je){
            je.printStackTrace();
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }


}
