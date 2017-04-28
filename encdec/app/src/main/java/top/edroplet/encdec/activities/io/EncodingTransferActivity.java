package top.edroplet.encdec.activities.io;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import top.edroplet.encdec.R;
import top.edroplet.encdec.utils.Utils;

public class EncodingTransferActivity extends Activity {
    private final int REQUEST_EX = 10;
    public String encodingFrom = null, encodingTo = null;
    Spinner spinnerFrom, spinnerTo;
    String[] formats = new String[]{Utils.GBK, Utils.UTF_8, Utils.US_ASCII, Utils.UTF_16, Utils.ISO_8859_1, Utils.UTF_16BE, Utils.UTF_16LE};
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_encoding_transfer);
        spinnerFrom = (Spinner) findViewById(R.id.mainSpinnerFrom);
        spinnerFrom.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formats));
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(EncodingTransferActivity.this, formats[i], Toast.LENGTH_LONG).show();
                encodingFrom = formats[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerTo = (Spinner) findViewById(R.id.mainSpinnerTo);
        spinnerTo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formats));
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(mContext, formats[i], Toast.LENGTH_LONG).show();
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
        if (true) {
            Log.d("main", "enter exdlg");
            Intent intent = new Intent();
            intent.putExtra("explorer_title", getString(R.string.dialog_read_from_dir));         // 设置文件管理器标题
            intent.putExtra("allow_multidir", cbmd.isChecked());
            intent.putExtra("show_all", cbsa.isChecked());
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath())), "text/*");   // 设置起始文件夹和文件类型
            intent.setClass(this, ExDialog.class);
            startActivityForResult(intent, REQUEST_EX);
        } else {
            Log.d("main", "enter action get content");
            String apkRoot = "chmod 777 " + getPackageCodePath();
            RootCommand(apkRoot);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/*");
            startActivityForResult(intent, REQUEST_EX);
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
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void transfer(View v) {
        EditText et_main_filepath = (EditText) findViewById(R.id.et_main_filepath);
        /*RadioButton mainRadioButton_utf8 = (RadioButton)findViewById(R.id.mainRadioButton_utf8);

		if ( mainRadioButton_utf8.isChecked() ) {
			Utils.TranferToUTF8(ctx, et_main_filepath.getText().toString());
		}
		*/
        Utils.TransferTo(mContext, et_main_filepath.getText().toString(), encodingFrom, encodingTo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path;
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EX) {
                Uri uri = data.getData();    // 接收用户所选文件的路径
                TextView text = (TextView) findViewById(R.id.et_main_filepath);
                path = uri.decode(uri.toString()); // .toString();
                if (path.contains("file://")) {
                    path = path.substring("file://".length(), path.length());
                }
                text.setText(path); // 在界面上显示路径
            }
        }
    }
}
