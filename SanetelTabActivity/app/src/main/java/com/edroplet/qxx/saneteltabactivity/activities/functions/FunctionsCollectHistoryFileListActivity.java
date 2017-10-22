package com.edroplet.qxx.saneteltabactivity.activities.functions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.settings.SatelliteDetailActivity;
import com.edroplet.qxx.saneteltabactivity.activities.settings.SatelliteDetailFragment;
import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.beans.SatelliteParameters;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * An activity representing a list of Cities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SatelliteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class FunctionsCollectHistoryFileListActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions_collect_history_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.history_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.back);
            // 设置居中的时候不能含有原标题
            ab.setDisplayShowTitleEnabled(false);
        }

        View recyclerView = findViewById(R.id.collect_history_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            CollectHistoryFileInfo collectHistoryFileInfo = new CollectHistoryFileInfo(this);
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(collectHistoryFileInfo.getList()));
        }catch (Exception je){
            je.printStackTrace();
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<CollectHistoryFileInfo> mValues;

        public SimpleItemRecyclerViewAdapter(List<CollectHistoryFileInfo> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.collect_history_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(holder.mItem.getFileName());
            holder.mDateView.setText(holder.mItem.getDateTime());
        }

        @Override
        public int getItemCount() {
            if (mValues == null){
                return 0;
            }
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final CustomTextView mNameView;
            public final CustomTextView mDateView;
            public CollectHistoryFileInfo mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView =  view.findViewById(R.id.collect_history_list_file_name);
                assert mNameView != null;
                mDateView = view.findViewById(R.id.collect_history_list_save_date);
                assert mDateView != null;
            }

            @Override
            public String toString() {
                return super.toString() + "'" + mNameView.getText() + "'";
            }
        }
    }
}
