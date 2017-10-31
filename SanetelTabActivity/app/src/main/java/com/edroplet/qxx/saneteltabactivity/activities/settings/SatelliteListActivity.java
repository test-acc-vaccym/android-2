package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;

import com.edroplet.qxx.saneteltabactivity.adapters.CitiesRecyclerViewAdapter;
import com.edroplet.qxx.saneteltabactivity.adapters.SatelliteItemRecyclerViewAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Satellites;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo.satelliteJsonFile;

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
    private boolean isShowSelect = false;
    private SatelliteItemRecyclerViewAdapter satelliteItemRecyclerViewAdapter;
    private Satellites satellites;

    @BindId((R.id.satellite_list))
    private RecyclerView recyclerView;

    @BindId(R.id.recover_satellite)
    private CustomButton recoverySatellites;

    @BindId(R.id.satellite_select_button)
    private CustomButton satelliteSelectButton;

    @BindId(R.id.add_satellite)
    private CustomButton addSatellite;

    @BindId(R.id.delete_satellite)
    private CustomButton deleteSatellite;

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
                    satellites.update(id, satelliteInfo);
                    satelliteItemRecyclerViewAdapter.setmValues(satellites.getITEMS());
                    satelliteItemRecyclerViewAdapter.notifyDataSetChanged();
                    // 保存到文件
                    JsonLoad js = new JsonLoad(this, SatelliteInfo.satelliteJsonFile);
                    ArrayList<SatelliteInfo> al = new ArrayList<SatelliteInfo>();
                    al.addAll(satellites.getITEMS());
                    try {
                        js.saveSatellites(al);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;
            case NEW_SATELLITES_REQUEST_CODE:
                // if(resultCode== Activity.RESULT_OK){
                if (satelliteInfo != null) {
                    //  刷新当前activity界面数据
                    satellites.addItem(satelliteInfo, true);
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

        assert addSatellite != null;
        addSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SatelliteListActivity.this, NewSatelliteActivity.class), NEW_SATELLITES_REQUEST_CODE);
            }
        });

        assert recyclerView != null;
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
                satellites.clear();
                finish();
            }
        });

        satelliteSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CustomButton)view).getText().equals(getString(R.string.select_all))){
                    satelliteItemRecyclerViewAdapter.fillMap();
                    ((CustomButton)view).setText(R.string.select_none);
                }else {
                    satelliteItemRecyclerViewAdapter.initMap();
                    ((CustomButton)view).setText(R.string.select_all);
                }
                satelliteItemRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        deleteSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<Integer, Boolean> map = satelliteItemRecyclerViewAdapter.getMap();
                int size = map.size();
                boolean hasCheckedAny = false;
                final List<Integer> checkedPosition = new ArrayList<>();
                for (int i = size - 1; i >= 0; i--) {
                    if (map.get(i)) {
                        hasCheckedAny = true;
                        checkedPosition.add(i);
                    }
                }
                if (isShowSelect && hasCheckedAny) {
                    String confirmDelete = String.format(getString(R.string.confirm_delete_message), "所选项？");
                    final RandomDialog dialogBuilder = new RandomDialog(SatelliteListActivity.this);
                    dialogBuilder.onConfirm(confirmDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<SatelliteInfo> items = satellites.getITEMS();
                            for (int p: checkedPosition) {
                                if (map.get(p)) {
                                    SatelliteInfo satelliteInfoToBeDeleted = items.get(p);
                                    satelliteItemRecyclerViewAdapter.deleteItem(satelliteInfoToBeDeleted);
                                    satellites.deleteItem(satelliteInfoToBeDeleted);
                                }
                            }

                            try {
                                // 修改文件
                                JsonLoad js = new JsonLoad(view.getContext(), satelliteJsonFile);
                                ArrayList<SatelliteInfo> al = new ArrayList<SatelliteInfo>();
                                //for (SatelliteInfo l:  satellites.getITEMS()){
                                //    al.add(l);
                                //}
                                al.addAll(satellites.getITEMS());
                                js.saveSatellites(al);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            satelliteItemRecyclerViewAdapter.setmValues(satellites.getITEMS());
                            satelliteItemRecyclerViewAdapter.initMap();
                            toggleState();
                            satelliteItemRecyclerViewAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(0);
                            // satelliteItemRecyclerViewAdapter.notifyAll();
                            dialogBuilder.getDialogBuilder().dismiss();
                        }
                    });
                }else{
                    toggleState();
                    satelliteItemRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void toggleState(){
        satelliteItemRecyclerViewAdapter.setShowBox();
        if (isShowSelect) {
            satelliteSelectButton.setVisibility(View.GONE);
            isShowSelect = false;
        }
        else {
            isShowSelect = true;
            satelliteSelectButton.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            satellites = new Satellites(this);
            satelliteItemRecyclerViewAdapter = new SatelliteItemRecyclerViewAdapter(this, satellites.getITEMS());
            satelliteItemRecyclerViewAdapter.setRecyclerViewOnItemClickListener(new CitiesRecyclerViewAdapter.RecyclerViewOnItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    // Toast.makeText(SatelliteListActivity.this, "点击了"+satellites.getITEMS().get(position).name, Toast.LENGTH_SHORT).show();

                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID, satellites.getITEMS().get(position).mId.toString());
                        SatelliteDetailFragment fragment = new SatelliteDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.satellite_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, SatelliteDetailActivity.class);
                        intent.putExtra(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID, satellites.getITEMS().get(position).mId.toString());
                        startActivityForResult(intent, SatelliteListActivity.SATELLITE_DETAIL_REQUEST_CODE);
                    }
                }

                @Override
                public boolean onItemLongClickListener(View view, int position) {
                    // Toast.makeText(SatelliteListActivity.this, "长按了"+satellites.getITEMS().get(position).name, Toast.LENGTH_SHORT).show();
                    satelliteSelectButton.setVisibility(View.VISIBLE);
                    satelliteItemRecyclerViewAdapter.setShowBox();
                    satelliteItemRecyclerViewAdapter.notifyDataSetChanged();
                    return true;
                }
            });

            //为RecyclerView添加默认动画效果，测试不写也可以
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(satelliteItemRecyclerViewAdapter);

        }catch (JSONException je){
            je.printStackTrace();
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }


}
