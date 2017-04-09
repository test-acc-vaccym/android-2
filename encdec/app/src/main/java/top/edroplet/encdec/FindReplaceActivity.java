package top.edroplet.encdec;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FindReplaceActivity extends Activity {
    private static final String TAG = "Find&Replace";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_find_replace);
    }

    public void findInFiles(View v) {
        // 需要查找的字符串
        EditText etFindString = (EditText) findViewById(R.id.edit_find_string);
        // 是否使用正则表达式
        CheckBox cbRegex = (CheckBox) findViewById(R.id.check_regex);
        // 文件路径
        EditText et_main_filepath = (EditText) findViewById(R.id.et_main_filepath);
        // 结果保存
        TextView text_find_result = (TextView) findViewById(R.id.text_find_result);
        text_find_result.setMovementMethod(ScrollingMovementMethod.getInstance());
        // 是否保留原来的文本
        CheckBox cb_reserve_text = (CheckBox) findViewById(R.id.cb_reserve_text);
        // 是否显示详细信息
        CheckBox cb_show_detail = (CheckBox) findViewById(R.id.cb_show_detail);
        // 是否区分大小写
        CheckBox cb_ignore_case = (CheckBox) findViewById(R.id.cb_ignore_case);

        String path = et_main_filepath.getText().toString();
        if (path.isEmpty()) {
            Log.e(TAG, "file is NONE!");
            Toast.makeText(mContext, "NO Selected Files!", Toast.LENGTH_LONG).show();
            return;
        }

        String toFind = etFindString.getText().toString();
        if (toFind.isEmpty()) {
            Log.e(TAG, "Nothing to find!");
            Toast.makeText(mContext, "Nothing to find!", Toast.LENGTH_LONG).show();
            return;
        }

        // 是否使用正则表达式
        boolean isRegex = false;
        if (cbRegex.isChecked()) {
            isRegex = true;
        }
        // 清空内容
        if (!cb_reserve_text.isChecked()) {
            text_find_result.setText("");
        }
        boolean showDetail = cb_show_detail.isChecked();
        boolean ignoreCase = cb_ignore_case.isChecked();

        // 使用lru
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int memorySize = maxMemory / 16;
        ArrayList keyList = new ArrayList();
        textCache tc = new textCache(memorySize);

        String[] files = path.split(";");
        for (String file : files) {
            asyncTask at = new asyncTask(text_find_result, keyList);
            asyncInput ai = new asyncInput(mContext, tc, keyList, file, toFind, isRegex, showDetail, ignoreCase);
            at.execute(ai);
        }
        Log.i(TAG, "findInFiles: Done!");
    }

    class asyncInput {
        boolean showDetail, ignoreCase, isRegex;
        String filePath, toFind;
        Context ctx;
        textCache tc;
        ArrayList keyList;

        asyncInput(Context ctx, textCache tc, ArrayList keyList, String filePath, String toFind, boolean isRegex, boolean showDetail, boolean ignoreCase) {
            this.ctx = ctx;
            this.tc = tc;
            this.keyList = keyList;
            this.filePath = filePath;
            this.toFind = toFind;
            this.isRegex = isRegex;
            this.showDetail = showDetail;
            this.ignoreCase = ignoreCase;
        }
    }

    class asyncTask extends AsyncTask<asyncInput, TextView, syncTaskResponseData> {
        private TextView tv = null;
        private List keyList = new ArrayList();
        private textCache tc;

        asyncTask(TextView tv, List keyList) {
            super();
            this.tv = tv;
            this.keyList = keyList;
        }

        @Override
        protected syncTaskResponseData doInBackground(asyncInput[] p1) {
            syncTaskResponseData srd;// new syncTaskResponseData(p1[0].keyList, p1[0].tc);
            srd = Utils.findInFiles(mContext, p1[0].tc, p1[0].keyList, p1[0].filePath, p1[0].toFind, p1[0].isRegex, p1[0].showDetail, p1[0].ignoreCase);
            return srd;
        }

        @Override
        protected void onPostExecute(syncTaskResponseData result) {
            super.onPostExecute(result);
            tc = result.getTc();
            keyList = result.getKeyList();

            StringBuilder sb = new StringBuilder();
            for (Iterator it2 = keyList.iterator(); it2.hasNext(); ) {
                String msg = it2.next().toString();
                // Log.e(TAG, "onPostExecute: " + msg);
                String txt = tc.get(msg);
                Log.e(TAG, "txt: " + txt);
                sb.append(txt);
                sb.append('\n');
            }
            tv.setEnabled(true);
            tv.setText(sb.toString());
            Log.e(TAG, "onPostExecute Done ");
        }

        @Override
        protected void onProgressUpdate(TextView[] values) {
            values[0].setText(tc.toString());
            for (Iterator it2 = keyList.iterator(); it2.hasNext(); ) {
                String msg = it2.next().toString();
                Log.e(TAG, "onProgressUpdate: " + msg);
                tv.append(msg);
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            tv.setVisibility(View.VISIBLE);
            tv.setEnabled(true);
            tv.setText(R.string.app_name);
            super.onPreExecute();
        }

    }

}
