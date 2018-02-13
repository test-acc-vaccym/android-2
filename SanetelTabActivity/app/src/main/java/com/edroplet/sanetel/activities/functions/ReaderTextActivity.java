package com.edroplet.sanetel.activities.functions;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.services.AsyncTextLoadTask;
import com.edroplet.sanetel.utils.FileUtils;
import com.edroplet.sanetel.view.BorderScrollView;
import com.edroplet.sanetel.view.custom.CustomTextView;

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
    BorderScrollView readTextScroll;


    @BindView(R.id.read_text_content)
    CustomTextView readerTextContent;

    @BindView(R.id.read_text_toolbar)
    Toolbar toolbar;

    private boolean isLoading;


    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean getLoading() {
        return isLoading;
    }

    public BorderScrollView getReadTextScroll() {
        return readTextScroll;
    }

    public CustomTextView getReaderTextContent() {
        return readerTextContent;
    }

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
        readTextScroll.setOnScrollChangedListener(new BorderScrollView.OnScrollChangedListener(){
            @Override
            public void onScrollBottom() {
                synchronized (ReaderTextActivity.class){
                    if(!isLoading){
                        isLoading = true;
                        new AsyncTextLoadTask(context, br, ++page).execute();
                        page++;
                    }
                }
            }
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) { }

            @Override
            public void onScrollTop() {
                // 往回滚
                synchronized (ReaderTextActivity.class){
                    if(!isLoading && page > 0){
                        isLoading = true;
                        new AsyncTextLoadTask(context, br, --page).execute();
                    }
                }
            }

        });

        try{
            if (fileName != null && !fileName.isEmpty()) {
                toolbar.setTitle(FileUtils.getBaseName(fileName));
                // 从assets中获取
                // InputStream is = context.getAssets().open(fileName);
                // 从私有目录获取
                // FileInputStream fis = context.openFileInput(fileName);
                // 从绝对路径获取
                FileInputStream fis = new FileInputStream(fileName);
                br = new BufferedReader(new InputStreamReader(fis));
                asyncTextLoadTask = new AsyncTextLoadTask(context, br, ++page);
                asyncTextLoadTask.execute();
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG);
        }
    }

    private AsyncTextLoadTask asyncTextLoadTask;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != br){
            try {
                br.close();
                if (asyncTextLoadTask != null){
                    asyncTextLoadTask.cancel(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
