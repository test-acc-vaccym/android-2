package com.edroplet.qxx.saneteltabactivity.activities.functions;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.services.AsyncTextLoadTask;
import com.edroplet.qxx.saneteltabactivity.view.BorderScrollView;
import com.edroplet.qxx.saneteltabactivity.view.ViewInject;
import com.edroplet.qxx.saneteltabactivity.view.annotation.BindId;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomTextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReaderTextActivity extends AppCompatActivity {
    public static final String ReadTextFilename = "readTextFilename";
    @BindId(R.id.read_text_scroll)
    BorderScrollView readTextScroll;


    @BindId(R.id.read_text_content)
    CustomTextView readerTextContent;

    @BindId(R.id.read_text_toolbar)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_text);
        context = this;

        ViewInject.inject(this, this);

        // 返回键处理
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String fileName = null;
        if (intent != null) {
            fileName = intent.getStringExtra(ReadTextFilename);
        }
        readTextScroll.setOnScrollChangedListener(new BorderScrollView.OnScrollChangedListener(){
            @Override
            public void onScrollBottom() {
                synchronized (ReaderTextActivity.class){
                    if(!isLoading){
                        isLoading = true;
                        new AsyncTextLoadTask(context, br).execute();
                    }
                }
            }
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) { }

            @Override
            public void onScrollTop() {}

        });

        try{
            if (!fileName.isEmpty()) {
                toolbar.setTitle(fileName);
                // InputStream is = context.getAssets().open(fileName);
                FileInputStream fis = context.openFileInput(fileName);
                br = new BufferedReader(new InputStreamReader(fis));
                new AsyncTextLoadTask(context, br).execute();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != br){
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
