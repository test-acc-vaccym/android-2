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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Cities;
import com.edroplet.qxx.saneteltabactivity.beans.LocationInfo;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;
import com.edroplet.qxx.saneteltabactivity.view.custom.SmoothCheckBox;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An activity representing a list of CityLocations. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CityLocationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CityLocationListActivity extends AppCompatActivity {
    public static final int NEW_CITY_REQUEST_CODE = 10010;
    public static final int CITY_DETAIL_REQUEST_CODE = 10011;
    private SimpleItemRecyclerViewAdapter simpleItemRecyclerViewAdapter;
    private Cities ce;
    private RecyclerView recyclerView;

    @BindId(R.id.recover_city)
    private CustomButton recoveryCity;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position = 0;
        String id = "";
        LocationInfo locationInfo = null;
        if (null != data) {
            if (data.hasExtra("city")) {
                locationInfo = data.getParcelableExtra("city");
            }
            if (data.hasExtra("position")) {
                position = data.getIntExtra("position", 0);
            }
            if (data.hasExtra(LocationInfo.JSON_ID_KEY)){
                id = data.getStringExtra(LocationInfo.JSON_ID_KEY);
            }
        }
        switch (requestCode){
            case CITY_DETAIL_REQUEST_CODE:
                if (id != null && id.length() > 0) {
                    ce.update(id, locationInfo);
                    simpleItemRecyclerViewAdapter.notifyItemChanged(position);
                }
                break;
            case NEW_CITY_REQUEST_CODE:
                // if(resultCode== Activity.RESULT_OK){
                if (locationInfo != null) {
                    //  刷新当前activity界面数据
                    ce.addItem(locationInfo);
                    //RecyclerView列表进行UI数据更新
                    simpleItemRecyclerViewAdapter.notifyItemInserted(position);
                    //如果在第一项添加模拟数据需要调用 scrollToPosition（0）把列表移动到顶端（可选）
                    recyclerView.scrollToPosition(position);
                    simpleItemRecyclerViewAdapter.notifyItemChanged(position);
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
        setContentView(R.layout.activity_city_list);

        ViewInject.inject(this, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.city_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.back);
            // ab.setTitle(R.string.satellite_toolbar_title);
            // 设置居中的时候不能含有原标题
            ab.setDisplayShowTitleEnabled(false);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.city_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        findViewById(R.id.add_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CityLocationListActivity.this, NewCityActivity.class), NEW_CITY_REQUEST_CODE);
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.city_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.city_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recoveryCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SystemServices.copyAssetsFiles2FileDir(CityLocationListActivity.this, LocationInfo.citiesJsonFile);

                CustomSP.putBoolean(getApplicationContext(), CustomSP.firstReadCities, true);
//                try {
//                    ce = new Cities(CityLocationListActivity.this,true);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                ce.clear();
                finish();
                // simpleItemRecyclerViewAdapter.notifyDataSetChanged();
                // SystemServices.restartAPP(CityLocationListActivity.this, 1000);
            }
        });

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            ce = new Cities(this);
            simpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(ce.ITEMS);
            simpleItemRecyclerViewAdapter.initMap();
            //为RecyclerView添加默认动画效果，测试不写也可以
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(simpleItemRecyclerViewAdapter);
        }catch (JSONException je){
            je.printStackTrace();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
            implements View.OnClickListener, View.OnLongClickListener {

        private final List<LocationInfo> mValues;
        public SimpleItemRecyclerViewAdapter(List<LocationInfo> items) {
            mValues = items;
        }

        //是否显示单选框,默认false
        private boolean isShowBox = false;
        // 存储勾选框状态的map集合
        private Map<Integer, Boolean> map = new HashMap<>();
        //接口实例
        private RecyclerViewOnItemClickListener onItemClickListener;

        //初始化map集合,默认为不选中
        private void initMap() {
            for (int i = 0; i < getItemCount(); i++) {
                map.put(i, false);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.city_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).getName());
            holder.mLatitudeView.setText(String.valueOf(mValues.get(position).getLatitude()));
            holder.mLongitudeView.setText(String.valueOf(mValues.get(position).getLongitude()));

            //长按显示/隐藏
            if (isShowBox) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else {
                holder.checkBox.setVisibility(View.INVISIBLE);
            }

            //设置Tag
            holder.mView.setTag(position);
            //设置checkBox改变监听
            holder.checkBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    //用map集合保存
                    map.put(position, isChecked);
                }
            });
            // 设置CheckBox的状态
            if (map.get(position) == null) {
                map.put(position, false);
            }
            //设置checkBox显示的动画
            if (isShowBox)
                holder.checkBox.setChecked(map.get(position), true);
            else
                holder.checkBox.setChecked(map.get(position), false);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CityLocationDetailFragment.CITY_ARG_ITEM_ID, holder.mItem.getName());
                        CityLocationDetailFragment fragment = new CityLocationDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.city_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CityLocationDetailActivity.class);
                        intent.putExtra(CityLocationDetailFragment.CITY_ARG_ITEM_ID, holder.mItem.getName());
                        startActivityForResult(intent, CITY_DETAIL_REQUEST_CODE);
                    }
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(), "长按了" + holder.mItem.getName(), Toast.LENGTH_SHORT).show();

                    String confirmDelete = String.format(view.getContext().getString(R.string.confirm_delete_message),holder.mItem.getName());
                    final RandomDialog dialogBuilder = new RandomDialog(view.getContext());
                    dialogBuilder.onConfirm(confirmDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.mView.setVisibility(View.GONE);
                            mValues.remove(holder.mItem);
                            ce.deleteItem(holder.mItem);
                            // 修改文件
                            JsonLoad js = new JsonLoad(view.getContext(), LocationInfo.citiesJsonFile);
                            ArrayList<LocationInfo> al = new ArrayList<LocationInfo>();
                            for (LocationInfo l:  mValues){
                                al.add(l);
                            }
                            try {
                                js.saveCities(al);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            simpleItemRecyclerViewAdapter.notifyItemChanged(holder.getAdapterPosition());
                            dialogBuilder.getDialogBuilder().dismiss();
                        }
                    });
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mLatitudeView;
            public final TextView mLongitudeView;

            private SmoothCheckBox checkBox;

            public LocationInfo mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (CustomTextView) view.findViewById(R.id.city_list_name);
                mLatitudeView = (CustomTextView) view.findViewById(R.id.city_list_latitude);
                mLongitudeView = (CustomTextView) view.findViewById(R.id.city_list_longitude);
                checkBox = view.findViewById(R.id.city_select_checkbox);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }

        //点击事件
        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
            }
        }

        //长按事件
        @Override
        public boolean onLongClick(View v) {
            //不管显示隐藏，清空状态
            initMap();
            return onItemClickListener != null && onItemClickListener.onItemLongClickListener(v, (Integer) v.getTag());
        }

        //设置点击事件
        public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        //设置是否显示CheckBox
        public void setShowBox() {
            //取反
            isShowBox = !isShowBox;
        }

        //点击item选中CheckBox
        public void setSelectItem(int position) {
            //对当前状态取反
            if (map.get(position)) {
                map.put(position, false);
            } else {
                map.put(position, true);
            }
            notifyItemChanged(position);
        }

        //返回集合给MainActivity
        public Map<Integer, Boolean> getMap() {
            return map;
        }

        //接口回调设置点击事件
        /*
        public interface RecyclerViewOnItemClickListener {
            //点击事件
            void onItemClickListener(View view, int position);

            //长按事件
            boolean onItemLongClickListener(View view, int position);
        }
        */
    }
}
