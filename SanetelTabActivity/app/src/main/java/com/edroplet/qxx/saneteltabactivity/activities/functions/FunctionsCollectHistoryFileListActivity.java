package com.edroplet.qxx.saneteltabactivity.activities.functions;

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
import android.widget.Toast;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.activities.settings.SatelliteDetailActivity;
import com.edroplet.qxx.saneteltabactivity.adapters.CollectHistoryRecyclerViewAdapter;
import com.edroplet.qxx.saneteltabactivity.beans.CollectHistoryFileInfo;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.will.ireader.IReaderMainActivity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import static com.ssa.afilechooser.FileChooserActivity2.PATHS;

/**
 * An activity representing a list of Cities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SatelliteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class FunctionsCollectHistoryFileListActivity extends AppCompatActivity {

    /** 做成滑动菜单
    @BindId(R.id.main_collect_data_history_list_open)
    CustomButton collectHistoryOpen;
    @BindId(R.id.main_collect_data_history_list_delete)
    CustomButton collectHistoryDelete;
     */
    @BindId(R.id.collect_history_list)
    private RecyclerView mRv;

    private LinearLayoutManager mLayoutManager;
    private List<CollectHistoryFileInfo> mDatas;

    private CollectHistoryRecyclerViewAdapter mAdapter;
    private Context context;

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

        ViewInject.inject(this, this);
        context = this;

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
            CollectHistoryFileInfo collectHistoryFileInfo = new CollectHistoryFileInfo(this);
            mDatas = collectHistoryFileInfo.getList();
            mAdapter = new CollectHistoryRecyclerViewAdapter(this, mDatas);
            mAdapter.setOnSwipListener(new CollectHistoryRecyclerViewAdapter.onSwipeListener() {
                @Override
                public void onDel(int pos) {
                    if (pos >= 0 && pos < mDatas.size()) {
                        Toast.makeText(FunctionsCollectHistoryFileListActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                        mDatas.remove(pos);
                        mAdapter.notifyItemRemoved(pos);//推荐用这个
                        //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                        //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                        //mAdapter.notifyDataSetChanged();

                        // 删除文件
                        deleteFile(mDatas.get(pos).getFileName());
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
                    mDatas.clear();
                    mAdapter.notifyDataSetChanged();//推荐用这个

                    // 删除文件
                    File file =  context.getFilesDir();
                    if (file.exists()) { // 判断文件是否存在
                        /*
                        if (file.isFile()) { // 判断是否是文件
                            file.delete(); // delete()方法 你应该知道 是删除的意思;
                        } else if (file.isDirectory()) { // 否则如果它是一个目录
                            File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                            for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                                context.deleteFile(files[i].getName()); // 把每个文件 用这个方法进行迭代
                            }
                        }
                        file.delete();
                        */
                        for (CollectHistoryFileInfo fileInfo: mDatas){
                            deleteFile(fileInfo.getFileName());
                        }

                    } else {
                        Log.e("OnDel", "文件不存在！\n");
                    }
                }
            });
            recyclerView.setAdapter(mAdapter);

            recyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(this, 2));
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
    private void finishWithResults(List<File> files) {
        Intent intent = new Intent();
        intent.putExtra(PATHS, (Serializable) files);
        setResult(RESULT_OK, intent);
        finish();
    }

}
