package com.edroplet.sanetel.activities.functions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.settings.SatelliteDetailActivity;
import com.edroplet.sanetel.adapters.CollectHistoryRecyclerViewAdapter;
import com.edroplet.sanetel.beans.CollectHistoryFileInfo;
import com.edroplet.sanetel.utils.FileUtils;
import com.edroplet.sanetel.view.ViewInject;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ssa.afilechooser.FileChooserActivity2.PATHS;

/**
 * An activity representing a list of Cities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SatelliteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class FunctionsCollectHistoryFileListActivity extends AppCompatActivity implements View.OnClickListener{
    public static  final String KEY_IS_SELECT = "KEY_IS_SELECT";

    /** 做成滑动菜单
    @BindView(R.id.main_collect_data_history_list_open)
    CustomButton collectHistoryOpen;
    @BindView(R.id.main_collect_data_history_list_delete)
    CustomButton collectHistoryDelete;
     */
    @BindView(R.id.collect_history_list)
    RecyclerView mRv;

    @BindView(R.id.main_collect_data_history_list_select_ok)
    CustomButton selectOk;

    @BindView(R.id.main_collect_data_history_list_select_cancel)
    CustomButton selectCancel;

    @BindView(R.id.collect_history_list_operator)
    LinearLayout linearLayoutOperator;

    @BindView(R.id.polarization_title_select_all_or_not)
    CustomButton selectAllOrNot;

    private LinearLayoutManager mLayoutManager;
    private List<CollectHistoryFileInfo> mDatas;

    private CollectHistoryRecyclerViewAdapter mAdapter;
    private Context context;

    // 是否选择
    boolean isSelect;

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

        ButterKnife.bind(this);

        context = this;
        Intent intent = getIntent();

        if (intent.hasExtra (KEY_IS_SELECT))
            isSelect = intent.getBooleanExtra(KEY_IS_SELECT, false);


        if (isSelect){
            selectOk.setOnClickListener(this);
            selectCancel.setOnClickListener(this);
            selectAllOrNot.setOnClickListener(this);
            linearLayoutOperator.setVisibility(View.VISIBLE);
            selectAllOrNot.setVisibility(View.VISIBLE);
        }else {
            linearLayoutOperator.setVisibility(View.GONE);
            selectAllOrNot.setVisibility(View.GONE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.history_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.back);
            // 设置居中的时候不能含有原标题
            ab.setDisplayShowTitleEnabled(false);
        }

        setupRecyclerView(mRv);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            final CollectHistoryFileInfo collectHistoryFileInfo = new CollectHistoryFileInfo(this);
            mDatas = collectHistoryFileInfo.getList();
            mAdapter = new CollectHistoryRecyclerViewAdapter(this, mDatas, isSelect);
            mAdapter.setOnSwipListener(new CollectHistoryRecyclerViewAdapter.onSwipeListener() {
                @Override
                public void onDel(int pos) {
                    if (pos >= 0 && pos < mDatas.size()) {
                        Toast.makeText(FunctionsCollectHistoryFileListActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                        // 删除文件
                        // deleteFile(mDatas.get(pos).getFileName());
                        FileUtils.deleteFile(mDatas.get(pos).getFileName());

                        mDatas.remove(pos);
                        mAdapter.notifyItemRemoved(pos);//推荐用这个
                        try {
                            collectHistoryFileInfo.saveHistoryFileList(mDatas);
                        }catch (Exception e){
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                        }

                        //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                        //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                        //mAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onOpen(int pos) {
                    CollectHistoryFileInfo collectHistoryFileInfo1 = mDatas.get(pos);
                    Intent intent = new Intent(FunctionsCollectHistoryFileListActivity.this, ReaderTextActivity.class);
                    intent.putExtra(ReaderTextActivity.ReadTextFilename,collectHistoryFileInfo1.getFileName());
                    startActivity(intent);
                }

                @Override
                public void onDelAll() {
                    Toast.makeText(FunctionsCollectHistoryFileListActivity.this, "删除所有", Toast.LENGTH_SHORT).show();

                    // 删除文件
                    File file =  context.getFilesDir();
                    if (file.exists()) { // 判断文件是否存在
                        for (CollectHistoryFileInfo fileInfo: mDatas){
                            // deleteFile(fileInfo.getFileName());
                            if (FileUtils.isFileExist(fileInfo.getFileName())) {
                                FileUtils.deleteFile(fileInfo.getFileName());
                            }
                        }
                    } else {
                        Log.e("OnDel", "文件不存在！\n");
                    }
                    mDatas.clear();
                    mAdapter.notifyDataSetChanged();//推荐用这个

                    try {
                        collectHistoryFileInfo.saveHistoryFileList(mDatas);
                    }catch (Exception e){
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            recyclerView.setAdapter(mAdapter);

            recyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(this, 1));
            //6 2016 10 21 add , 增加viewChache 的 get()方法，
            // 可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。我个人觉得意义不大，
            mRv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                        if (null != viewCache) {
                            viewCache.smoothClose();
                        }
                    }
                    return false;
                }
            });

        }catch (Exception je){
            je.printStackTrace();
        }
    }
    /**
     * Finish this Activity with a result code and URI of the selected file.
     *
     * @param files The file selected.
     */
    private void finishWithResults(List<String> files) {
        Intent intent = new Intent();
        intent.putExtra(PATHS, (Serializable) files);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_collect_data_history_list_select_ok:
                // 获取选中项
                final Map<Integer, Boolean> map = mAdapter.getMap();
                int size = map.size();
                boolean hasCheckedAny = false;
                final List<String> checkedFile = new ArrayList<>();

                for (int i = size - 1; i >= 0; i--) {
                    if (map.get(i)) {
                        hasCheckedAny = true;
                        checkedFile.add(mDatas.get(i).getFileName());
                    }
                }
                if (hasCheckedAny){
                    finishWithResults(checkedFile);
                }
                finish();
                break;
            case R.id.main_collect_data_history_list_select_cancel:
                finish();
                break;
            case R.id.polarization_title_select_all_or_not:
                if (selectAllOrNot.getText() == getString(R.string.select_all)) {
                    mAdapter.fillMap();
                    selectAllOrNot.setText(R.string.select_none);
                }else {
                    selectAllOrNot.setText(R.string.select_all);
                    mAdapter.initMap();
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
