package com.youmi.android.toolkit.sample;

import net.youmi.toolkit.android.PullCallback;
import net.youmi.toolkit.android.SaveCallback;
import net.youmi.toolkit.android.TKException;
import net.youmi.toolkit.android.TKMap;
import net.youmi.toolkit.android.YoumiToolkit;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button saveBtn;
    Button pullBtn;
    Button removeBtn;
    Button incBtn;
    Button pushBtn;
    TextView resultView;
    
    TKMap foodMap;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        YoumiToolkit.init(this, "Your AppID", "Your AppSecret");
        
        setContentView(R.layout.main);

        foodMap = new TKMap("food");
        foodMap.put("icecream", "冰激凌");
        foodMap.put("cookie", "饼干");
        
        saveBtn = (Button) findViewById(R.id.save);
        pullBtn = (Button) findViewById(R.id.pull);
        removeBtn = (Button) findViewById(R.id.remove);
        incBtn = (Button) findViewById(R.id.inc);
        pushBtn = (Button) findViewById(R.id.push);
        resultView = (TextView) findViewById(R.id.result);
        
        saveBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {

                foodMap.put("bread", "面包");
                foodMap.saveInBackground(new SaveCallback() {


                    @Override
                    public void done(TKException e) {
                        if (e==null) {
                            Toast.makeText(MainActivity.this, "数据成功保存至服务器！", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(MainActivity.this, "数据成功保存至服务器失败！", Toast.LENGTH_LONG).show();
                        }                            
                    }
                });
            }
        });
        
        pullBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {

                    foodMap.pullInBackground(new PullCallback() {
                        
                        @Override
                        public void done(TKMap arg0, TKException e) {
                            if (e==null) {
                                Toast.makeText(MainActivity.this, "更新数据成功！", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(MainActivity.this, "更新数据失败！", Toast.LENGTH_LONG).show();
                            }                           
                        }
                    });
            }
        });
        
        removeBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                foodMap.remove("bread");
                foodMap.saveInBackground(new SaveCallback() {
                        
                        @Override
                        public void done(TKException e) {
                            if (e==null) {
                                Toast.makeText(MainActivity.this, "删除bread数据成功！", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(MainActivity.this, "删除bread数据失败！", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            }
        });
        
        incBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                foodMap.increment("age"); // age = age+1
                foodMap.increment("num", 5); // num = num+5
                foodMap.saveInBackground(new SaveCallback() {
                        
                        @Override
                        public void done(TKException e) {
                            if (e==null) {
                                Toast.makeText(MainActivity.this, "递增数据成功！", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(MainActivity.this, "递增数据失败！", Toast.LENGTH_LONG).show();
                            }
                        }
                 });
            }
        });
        
        pushBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String message = "请在开发者工具网站http://tk.youmi.net开通push服务，并填写相关的push信息，即可在手机上接收到数据。";
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setMessage(message).create();
                dialog.show();    
            }
        });
    }
}