package com.edroplet.sanetel.activities.functions;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edroplet.sanetel.R;
//import com.edroplet.sanetel.services.AsyncTextLoadTask;
import com.edroplet.sanetel.utils.FileUtils;
//import com.edroplet.sanetel.view.BorderScrollView;
//import com.edroplet.sanetel.view.custom.CustomTextView;
import com.hw.txtreaderlib.bean.TxtMsg;
import com.hw.txtreaderlib.interfaces.ILoadListener;
import com.hw.txtreaderlib.main.TxtReaderView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReaderTextActivity extends AppCompatActivity {
    public static final String ReadTextFilename = "readTextFilename";
    @BindView(R.id.read_text_scroll)
    //    BorderScrollView readTextScroll;
    TxtReaderView readTextScroll;


//    @BindView(R.id.read_text_content)
//    CustomTextView readerTextContent;

    @BindView(R.id.read_text_toolbar)
    Toolbar toolbar;

    private boolean isLoading;


    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean getLoading() {
        return isLoading;
    }

//    public BorderScrollView getReadTextScroll() {
//        return readTextScroll;
//    }

    public TxtReaderView getReadTextScroll() {
        return readTextScroll;
    }

//    public CustomTextView getReaderTextContent() {
//        return readerTextContent;
//    }

    private BufferedReader br;

    private Context context;
    private static int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_text);
        context = this;

        ButterKnife.bind(this);

        // 返回键处理
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String fileName = null;
        if (intent != null && intent.hasExtra(ReadTextFilename)) {
            fileName = intent.getStringExtra(ReadTextFilename);
        }
        readTextScroll.loadTxtFile(fileName, new ILoadListener() {
            @Override
            public void onSuccess() {
//                Toast.makeText(ReaderTextActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                Log.e("READ TEXT", "onSuccess: " + "加载成功");
            }

            @Override
            public void onFail(TxtMsg txtMsg) {
//                Toast.makeText(ReaderTextActivity.this, txtMsg.toString(), Toast.LENGTH_SHORT).show();
                Log.e("READ TEXT", "onFail: " + txtMsg.toString());
            }

            @Override
            public void onMessage(String s) {
//                Toast.makeText(ReaderTextActivity.this, "加载中……", Toast.LENGTH_SHORT).show();
                Log.e("READ TEXT", "onMessage: " + "加载中……");
            }
        });

        /**
        readTextScroll.setOnScrollChangedListener(new BorderScrollView.OnScrollChangedListener(){
            @Override
            public void onScrollBottom() {
                synchronized (ReaderTextActivity.class){
                    if(!isLoading){
                        isLoading = true;
                        new AsyncTextLoadTask(context, br, ++page).execute();
                    }
                }
            }
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) { }

            @Override
            public void onScrollTop() {
            }

            @Override
            public void onLoadPrePage() {
                // 往回滚
                synchronized (ReaderTextActivity.class){
                    if(!isLoading && page > 0){
                        isLoading = true;
                        new AsyncTextLoadTask(context, br, --page).execute();
                    }
                }
            }
        });
        */

        try{
            if (fileName != null && !fileName.isEmpty()) {
                toolbar.setTitle(FileUtils.getBaseName(fileName));
                // 从assets中获取
                // InputStream is = context.getAssets().open(fileName);
                // 从私有目录获取
                // FileInputStream fis = context.openFileInput(fileName);
                // 从绝对路径获取
//                FileInputStream fis = new FileInputStream(fileName);
//                br = new BufferedReader(new InputStreamReader(fis));
//                asyncTextLoadTask = new AsyncTextLoadTask(context, br, ++page);
//                asyncTextLoadTask.execute();
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG);
        }
    }

//    private AsyncTextLoadTask asyncTextLoadTask;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(null != br){
//            try {
//                br.close();
//                if (asyncTextLoadTask != null){
//                    asyncTextLoadTask.cancel(true);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
