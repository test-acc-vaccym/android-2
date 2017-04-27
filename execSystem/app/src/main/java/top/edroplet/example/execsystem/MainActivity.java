package top.edroplet.example.execsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    private TextView tvResult;
    private EditText etCmd;
    private RadioButton rbCmd,rbShell;
    private Button btCancel,btRun;
    private final String TAG="QxxExecCmd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView)findViewById(R.id.tvResult);
        tvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        etCmd=(EditText)findViewById(R.id.etCmd);
        rbCmd=(RadioButton)findViewById(R.id.rbCmd);
        rbShell=(RadioButton)findViewById(R.id.rbShell);
        btCancel=(Button)findViewById(R.id.btCancel);
        btRun=(Button)findViewById(R.id.btRun);

        try {
            execCommand(etCmd.getText().toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCmd.setText("");
            }
        });

        btRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmd=etCmd.getText().toString();
                    try {
                        if (rbCmd.isChecked()) {
                            execCommand(cmd);
                        }else if (rbShell.isChecked()){
                            execCommand(cmd);
                        }
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
            }
        });
    }

    public void execCommand(String command) throws IOException {
        // start the ls command running
        //String[] args = new String[]{"sh", "-c", command};
        Runtime runtime = Runtime.getRuntime();
        if (command.isEmpty()){
            Log.e(TAG, "execCommand: command is EMPTY");
            return;
        }
        Process proc = runtime.exec(command); //这句话就是shell与高级语言间的调用
        //如果有参数的话可以用另外一个被重载的exec方法

        //实际上这样执行时启动了一个子进程,它没有父进程的控制台
        //也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
        InputStream inputstream = proc.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

        // read the ls output

        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            //System.out.println(line);
            sb.append(line);
            sb.append('\n');
        }
        String response=sb.toString();
        tvResult.setText(response);

        //使用exec执行不会等执行成功以后才返回,它会立即返回
        //所以在某些情况下是很要命的(比如复制文件的时候)
        //使用wairFor()可以等待命令执行完成以后才返回
        try {
            if (proc.waitFor() != 0) {
                response += "执行出错返回：" + proc.exitValue();
                System.err.println("exit value = " + proc.exitValue());
                tvResult.setText(response);
            }
        }
        catch (InterruptedException e) {
            System.err.println(e);
            response += e.toString();
            tvResult.setText(response);
        }
    }
}
