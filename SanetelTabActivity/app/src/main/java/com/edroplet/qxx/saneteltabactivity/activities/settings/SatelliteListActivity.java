package com.edroplet.qxx.saneteltabactivity.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;

import com.edroplet.qxx.saneteltabactivity.beans.SatelliteInfo;
import com.edroplet.qxx.saneteltabactivity.beans.Satellites;
import com.edroplet.qxx.saneteltabactivity.utils.JsonLoad;
import com.edroplet.qxx.saneteltabactivity.utils.RandomDialog;
import com.edroplet.qxx.saneteltabactivity.utils.SystemServices;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import org.json.JSONException;

import java.io.IOException;
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
                satelliteInfo = data.getParcelableExtra(SatelliteInfo.objectKey);
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
                SystemServices.copyAssetsFiles2FileDir(SatelliteListActivity.this, SatelliteInfo.satelliteJsonFile);
                try {
                    sp = new Satellites(SatelliteListActivity.this,true);
                }catch (Exception e){
                    e.printStackTrace();
                }
                satelliteItemRecyclerViewAdapter.notifyDataSetChanged();
                // SystemServices.restartAPP(SatelliteListActivity.this, 1000);
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


    public class SatelliteItemRecyclerViewAdapter extends RecyclerView.Adapter<SatelliteItemRecyclerViewAdapter.ViewHolder>
        /*implements View.OnClickListener, View.OnLongClickListener */{

        // TODO: 2017/10/25 然并卵
    /*
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnRecyclerItemLongListener mOnItemLongClickListener = null;
    */
        private final List<SatelliteInfo> mValues;
        private boolean mTwoPane;
        private FragmentActivity activity;
        // TODO: 2017/10/25 然并卵
    /*
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnItemLongClickListener != null && mOnItemLongClickListener.onItemLongClick(v, (Integer) v.getTag());
    }
    */

        public SatelliteItemRecyclerViewAdapter(FragmentActivity activity, List<SatelliteInfo> items, boolean mTwoPane) {
            mValues = items;
            this.mTwoPane = mTwoPane;
            this.activity = activity;
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            // holder.mIdView.setText(holder.mItem.id);
            holder.mNameView.setText(holder.mItem.name);
            holder.mPolarizationView.setText(holder.mItem.polarization);
            holder.mLongitudeView.setText(holder.mItem.longitude);
            holder.mBeaconView.setText(holder.mItem.beacon);
            holder.mThresholdView.setText(holder.mItem.threshold);
            holder.mSymbolRateView.setText(holder.mItem.symbolRate);
            // holder.mComentView.setText(holder.mItem.comment);
            // TODO: 2017/10/25 然并卵 只有这个靠谱
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID, holder.mItem.mId.toString());
                        SatelliteDetailFragment fragment = new SatelliteDetailFragment();
                        fragment.setArguments(arguments);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.satellite_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, SatelliteDetailActivity.class);
                        intent.putExtra(SatelliteDetailFragment.SATELLITE_ARG_ITEM_ID, holder.mItem.mId.toString());

                        Toast.makeText(context, "点击了" + holder.mItem.mId.toString(), Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);
                    }
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(), "长按了" + holder.mItem.mId.toString(), Toast.LENGTH_SHORT).show();

                    String confirmDelete = String.format(view.getContext().getString(R.string.confirm_delete_message),holder.mItem.name);
                    final RandomDialog dialogBuilder = new RandomDialog(view.getContext());
                    dialogBuilder.onConfirm(confirmDelete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.mView.setVisibility(View.GONE);
                            mValues.remove(holder.mItem);
                            // 修改文件
                            JsonLoad js = new JsonLoad(view.getContext(), SatelliteInfo.satelliteJsonFile);
                            ArrayList<SatelliteInfo> al = new ArrayList<SatelliteInfo>();
                            for (SatelliteInfo l:  mValues){
                                al.add(l);
                            }
                            try {
                                js.saveSatellites(al);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            satelliteItemRecyclerViewAdapter.notifyItemChanged(holder.getAdapterPosition());
                            dialogBuilder.getDialogBuilder().dismiss();
                        }
                    });
                    return true;
                }
            });

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.satellite_list_content, parent, false);
            ViewHolder vh =
                    new ViewHolder(view /*, mOnItemClickListener,mOnItemLongClickListener*/);
            // view.setOnClickListener(this);
            // view.setOnLongClickListener(this);
            return vh;
        }

        public class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener,View.OnLongClickListener */{
            // TODO: 2017/10/25 然并卵
        /*
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;
        private OnRecyclerItemLongListener mOnItemLong = null;
        */

            public final View mView;
            // public final TextView mIdView;
            public final CustomTextView mNameView;
            public final CustomTextView mPolarizationView;
            public final CustomTextView mLongitudeView;
            public final CustomTextView mBeaconView;
            public final CustomTextView mThresholdView;
            public final CustomTextView mSymbolRateView;
            // public final TextView mComentView;
            public SatelliteInfo mItem;

            public ViewHolder(View view/*, OnRecyclerViewItemClickListener mListener, OnRecyclerItemLongListener longListener */) {
                super(view);
            /*
            this.mOnItemClickListener = mListener;
            this.mOnItemLong = longListener;
            */
                mView = view;
                // mIdView = view.findViewById(R.id.id);
                // assert mIdView != null;
                mNameView = view.findViewById(R.id.name);
                assert mNameView != null;
                mPolarizationView = view.findViewById(R.id.polarization);
                assert mPolarizationView == null;
                mLongitudeView = view.findViewById(R.id.longitude);
                assert mLongitudeView == null;
                mBeaconView = view.findViewById(R.id.beacon);
                assert mBeaconView != null;
                mThresholdView = view.findViewById(R.id.threshold);
                assert mThresholdView != null;
                mSymbolRateView = view.findViewById(R.id.symbolRate);
                assert mSymbolRateView != null;
                // mComentView = view.findViewById(R.id.comment);
                // assert mComentView != null;
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        /*
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mOnItemLong != null){
                mOnItemLong.onItemLongClick(v,getAdapterPosition());
            }
            return true;
        }
        */
        }
        // TODO: 2017/10/25 然并卵
    /*
    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int data);

    }
    public interface OnRecyclerItemLongListener{
        boolean onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnRecyclerItemLongListener listener){
        this.mOnItemLongClickListener =  listener;
    }
    */
    }

}
