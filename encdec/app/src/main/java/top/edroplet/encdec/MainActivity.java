package top.edroplet.encdec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.File;
import top.edroplet.encdec.MainActivity;
import top.edroplet.encdec.Utils;

public class MainActivity extends Activity {
	private static final String TAG="MainUI";
    private final int REQUEST_EX = 10;

	Spinner spinnerFrom, spinnerTo;
	public String encodingFrom = null, encodingTo = null;
	private static Context mContext;
	
	String[] formats = new String[]{Utils.GBK, Utils.UTF_8,Utils.US_ASCII,Utils.UTF_16,Utils.ISO_8859_1,Utils.UTF_16BE,Utils.UTF_16LE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
        setContentView(R.layout.main);
		spinnerFrom = (Spinner) findViewById(R.id.mainSpinnerFrom);
        spinnerFrom.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,formats));
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
					Toast.makeText(MainActivity.this,formats[i],Toast.LENGTH_LONG).show();
					encodingFrom = formats[i];
				}

				@Override
				public void onNothingSelected(AdapterView<?> adapterView) {

				}
		});
		spinnerTo = (Spinner) findViewById(R.id.mainSpinnerTo);
        spinnerTo.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,formats));
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
					Toast.makeText(mContext,formats[i],Toast.LENGTH_LONG).show();
					encodingTo = formats[i];
				}

				@Override
				public void onNothingSelected(AdapterView<?> adapterView) {

				}
			});
        // Button btn_get_files= (Button) findViewById(R.id.btn_get_files); 
    }

    public void getFiles(View v) {
		CheckBox cbmd = (CheckBox) findViewById(R.id.mainCheckBox_multiDir);
		CheckBox cbsa = (CheckBox) findViewById(R.id.mainCheckBox_allFiles);
		
		// System.out.println("btn_get_files");
		if(true) {
			Log.d("main","enter exdlg");
			Intent intent = new Intent();
			intent.putExtra("explorer_title", getString(R.string.dialog_read_from_dir));         // 设置文件管理器标题
			intent.putExtra("allow_multidir",cbmd.isChecked());
			intent.putExtra("show_all",cbsa.isChecked());
			intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "text/*");   // 设置起始文件夹和文件类型
			intent.setClass(this, ExDialog.class);
			// intent.setClass(this, MulSelect.class);
			startActivityForResult(intent, REQUEST_EX);
		}else {
			Log.d("main","enter action get content");
			String apkRoot = "chmod 777 " + getPackageCodePath();
			RootCommand(apkRoot);
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("text/*");
			startActivityForResult(intent, REQUEST_EX);
		}

	}

    public void transfer(View v) {
		EditText et_main_filepath = (EditText) findViewById(R.id.et_main_filepath);
		/*RadioButton mainRadioButton_utf8 = (RadioButton)findViewById(R.id.mainRadioButton_utf8);

		if ( mainRadioButton_utf8.isChecked() ) {
			Utils.TranferToUTF8(ctx, et_main_filepath.getText().toString());
		}
		*/
		Utils.TranferTo(mContext,et_main_filepath.getText().toString(),encodingFrom,encodingTo);
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String path;
		if ( resultCode == RESULT_OK ) {
			if ( requestCode == REQUEST_EX ) {
				Uri uri = data.getData();    // 接收用户所选文件的路径
				TextView text = (TextView) findViewById(R.id.et_main_filepath);
				path = uri.decode(uri.toString()); // .toString();
				if ( path.contains("file://") ) {
					path = path.substring("file://".length(), path.length());
				}
				text.setText(path); // 在界面上显示路径
			}
		}
	}

	public boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if ( os != null ) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public void findInFiles(View v){
		// 需要查找的字符串
		EditText etFindString = (EditText) findViewById(R.id.edit_find_string);
		// 是否使用正则表达式
		CheckBox cbRegex = (CheckBox)findViewById(R.id.check_regex);
		// 文件路径
		EditText et_main_filepath = (EditText) findViewById(R.id.et_main_filepath);
		// 结果保存
		TextView text_find_result = (TextView) findViewById(R.id.text_find_result);
		// 是否保留原来的文本
		CheckBox cb_reserve_text = (CheckBox) findViewById(R.id.cb_reserve_text);
		// 是否显示详细信息
		CheckBox cb_show_detail = (CheckBox) findViewById(R.id.cb_show_detail);
		// 是否区分大小写
		CheckBox cb_ignore_case = (CheckBox) findViewById(R.id.cb_ignore_case);

		String path = et_main_filepath.getText().toString();
		if (path.isEmpty()){
			Log.e(TAG,"file is NONE!");
			Toast.makeText(mContext,"NO Selected Files!",Toast.LENGTH_LONG);
			return;
		}

		String toFind = etFindString.getText().toString();
		if (toFind.isEmpty()){
			Log.e(TAG,"Nothing to find!");
			Toast.makeText(mContext,"Nothing to find!",Toast.LENGTH_LONG);
			return;
		}

		// 是否使用正则表达式
		boolean isRegex = false;
		if (cbRegex.isChecked()){
			isRegex = true;
		}
		// 清空内容
		if (!cb_reserve_text.isChecked()){
			text_find_result.setText("");
		}
		boolean showDetail = cb_show_detail.isChecked();
		boolean ignoreCase = cb_ignore_case.isChecked();

		// 使用lru
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int memorySize = maxMemory / 16;
		textCache tc = new textCache(memorySize);
		
		String [] files = path.split(";");
		for (String file:files){
			asyncTask at = new asyncTask(text_find_result);
			asyncInput ai = new asyncInput(mContext, tc, file, toFind, isRegex,showDetail,ignoreCase);
			at.execute(ai);
		}
	}
	
	class asyncInput {
		boolean showDetail, ignoreCase,isRegex;
		String filePath,toFind;
		Context ctx;
		textCache tc;
		public asyncInput(Context ctx, textCache tc, String filePath,String toFind, boolean isRegex, boolean showDetail, boolean ignoreCase){
			this.ctx = ctx;
			this.tc = tc;
			this.filePath = filePath;
			this.toFind = toFind;
			this.isRegex = isRegex;
			this.showDetail = showDetail;
			this.ignoreCase = ignoreCase;
		}
	}
	
	class asyncTask extends AsyncTask<asyncInput, TextView, textCache>{
		TextView tv = null;
		//private StringBuffer sb = new StringBuffer();
		private textCache tc;
		public asyncTask(TextView tv){
			super();
			this.tv=tv;
		}

		@Override
		protected textCache doInBackground(asyncInput[] p1) {
			// TODO: Implement this method
			textCache result = Utils.findInFiles(mContext,p1[0].tc, p1[0].filePath, p1[0].toFind, p1[0].isRegex, p1[0].showDetail, p1[0].ignoreCase);
			//tv.append(result.toString());
			return result;
		}

		@Override
		protected void onPostExecute(textCache result) {
			// TODO: Implement this method
			super.onPostExecute(result);
			this.tc = result;
		}

		@Override
		protected void onProgressUpdate(TextView[] values) {
			// TODO: Implement this method
			// values[0].append(sb.toString());
			values[0].setText(tc.toString());
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPreExecute() {
			// TODO: Implement this method
			tv.setEnabled(true);
			super.onPreExecute();
		}

		
		
	}
}
