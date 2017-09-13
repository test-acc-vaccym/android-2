package com.example.xwwt;


import java.io.BufferedReader;   
    import java.io.IOException;   
    import java.io.InputStream;   
    import java.io.InputStreamReader;   
    import java.io.UnsupportedEncodingException;   
    import java.net.HttpURLConnection;   
    import java.net.MalformedURLException;   
    import java.net.URL;   
    import java.util.ArrayList;   
    import java.util.List;   
      
    import org.apache.http.HttpEntity;   
    import org.apache.http.HttpResponse;   
    import org.apache.http.HttpStatus;   
    import org.apache.http.NameValuePair;   
    import org.apache.http.client.ClientProtocolException;   
    import org.apache.http.client.HttpClient;   
    import org.apache.http.client.entity.UrlEncodedFormEntity;   
    import org.apache.http.client.methods.HttpGet;   
    import org.apache.http.client.methods.HttpPost;   
    import org.apache.http.impl.client.DefaultHttpClient;   
    import org.apache.http.message.BasicNameValuePair;   
    import org.apache.http.util.EntityUtils;   
      
    import android.app.Activity;   
    import android.os.Bundle;   
    import android.os.Handler;   
    import android.os.Message;   
    import android.view.View;   
    import android.widget.Button;   
    import android.widget.TextView;   
      
    public class TimerData extends Activity implements Runnable{   
        /** Called when the activity is first created. */   
            private Button btn_get = null;   
            private Button btn_post = null;   
            private TextView tv_rp = null;   
        @Override   
        public void onCreate(Bundle savedInstanceState) {   
            super.onCreate(savedInstanceState);   
            setContentView(R.layout.mainwindow);   
        }  
        public void refresh(){   
                String httpUrl = "http://192.168.1.113:8080/Android/httpreq.jsp";   //http«Î«Ûµÿ÷∑
                try {   
                            URL url = new URL(httpUrl);   
                            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();   
                            urlConn.connect();   
                            InputStream input = urlConn.getInputStream();   
                            InputStreamReader inputreader = new InputStreamReader(input);   
                            BufferedReader reader = new BufferedReader(inputreader);   
                            String str = null;   
                            StringBuffer sb = new StringBuffer();   
                            while((str = reader.readLine())!= null){   
                                    sb.append(str).append("\n");   
                            }   
                            if(sb != null){   
                                    tv_rp.setText(sb.toString());   
                            }else{   
                                    tv_rp.setText("NULL");   
                            }   
                            reader.close();   
                            inputreader.close();   
                            input.close();   
                            reader = null;   
                            inputreader = null;   
                            input = null;   
                    } catch (MalformedURLException e) {   
                            e.printStackTrace();   
                    } catch (IOException e) {   
                            // TODO Auto-generated catch block   
                            e.printStackTrace();   
                    }   
        }   
        public Handler handler = new Handler(){   
                public void handleMessage(Message msg){   
                        super.handleMessage(msg);   
                        refresh();   
                }   
        };   
            public void run() {   
                    // TODO Auto-generated method stub   
                    while(true){   
                            try {   
                                    Thread.sleep(1000);   
                                    handler.sendMessage(handler.obtainMessage());   
                            } catch (InterruptedException e) {   
                                    // TODO Auto-generated catch block   
                                    e.printStackTrace();   
                            }   
                    }   
            }   
    }   