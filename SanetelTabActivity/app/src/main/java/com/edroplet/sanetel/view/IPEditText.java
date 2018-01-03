package com.edroplet.sanetel.view;

/**
 * Created by qxs on 2017/10/29.
 */
import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.view.custom.CustomEditText;

public class IPEditText extends LinearLayout {
    private Context mContext;
    private CustomEditText firstIPEdit;
    private CustomEditText secondIPEdit;
    private CustomEditText thirdIPEdit;
    private CustomEditText fourthIPEdit;

    private String firstIP;
    private String secondIP;
    private String thirdIP;
    private String fourthIP;

    public IPEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        // TODO Auto-generated constructor stub
        View view = LayoutInflater.from(context).inflate(R.layout.ip_edit, this);

        firstIPEdit =  view.findViewById(R.id.firstIPField);
        secondIPEdit = view.findViewById(R.id.secondIPField);
        thirdIPEdit = view.findViewById(R.id.thirdIPField);
        fourthIPEdit = view.findViewById(R.id.fourthIPField);

        firstIPEdit.setFilters(new InputFilter[]{new InputFilterFloat(0,255)});
        secondIPEdit.setFilters(new InputFilter[]{new InputFilterFloat(0,255)});
        thirdIPEdit.setFilters(new InputFilter[]{new InputFilterFloat(0,255)});
        fourthIPEdit.setFilters(new InputFilter[]{new InputFilterFloat(0,255)});

        setIPEditTextListener(context);
    }

    public void setIPEditTextListener(final Context context){

        mContext = context;
        //设置第一个字段的事件监听
        firstIPEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                Log.i("test",s.toString());
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                            firstIP = s.toString().trim().substring(0,s.length()-1);
                            if (firstIP.length() == 0){
                                firstIP = "0";
                            }
                        }else{
                            firstIP = s.toString().trim();
                        }
                        if (Integer.parseInt(firstIP) > 255) {
                            Toast.makeText(context, "IP大小在0-255之间",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        secondIPEdit.setFocusable(true);
                        secondIPEdit.requestFocus();
                    }
                    else
                    {
                        firstIP = s.toString().trim();
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                firstIPEdit.removeTextChangedListener(this);
                firstIPEdit.setText(firstIP);
                firstIPEdit.setSelection(firstIP.length());
                firstIPEdit.addTextChangedListener(this);
            }
        });
        //设置第二个IP字段的事件监听
        secondIPEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                            secondIP = s.toString().trim().substring(0,s.length()-1);
                            if (secondIP.length() == 0){
                                secondIP = "0";
                            }

                        }else{
                            secondIP = s.toString().trim();
                        }
                        if (Integer.parseInt(secondIP) > 255) {
                            Toast.makeText(context, "IP大小在0-255之间",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        thirdIPEdit.setFocusable(true);
                        thirdIPEdit.requestFocus();
                    }
                    else
                    {
                        secondIP = s.toString().trim();
                    }

                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                secondIPEdit.removeTextChangedListener(this);
                secondIPEdit.setText(secondIP);
                secondIPEdit.setSelection(secondIP.length());
                secondIPEdit.addTextChangedListener(this);
            }
        });
        //设置第三个IP字段的事件监听
        thirdIPEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if(null!=s && s.length()>0){
                    if(s.length() > 2 || s.toString().trim().contains(".")){
                        if(s.toString().trim().contains(".")){
                            thirdIP = s.toString().trim().substring(0,s.length()-1);

                            if (thirdIP.length() == 0){
                                thirdIP = "0";
                            }
                        }else{
                            thirdIP = s.toString().trim();
                        }
                        if (Integer.parseInt(thirdIP) > 255) {
                            Toast.makeText(context, "IP大小在0-255之间",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        fourthIPEdit.setFocusable(true);
                        fourthIPEdit.requestFocus();
                    }else{
                        thirdIP = s.toString().trim();
                    }

                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                thirdIPEdit.removeTextChangedListener(this);
                thirdIPEdit.setText(thirdIP);
                thirdIPEdit.setSelection(thirdIP.length());
                thirdIPEdit.addTextChangedListener(this);

            }
        });
        //设置第四个IP字段的事件监听
        fourthIPEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(null!=s && s.length()>0){
                    fourthIP = s.toString().trim();
                    if (Integer.parseInt(fourthIP) > 255) {
                        Toast.makeText(context, "请输入合法的ip地址", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    public String getText(Context context) {
        mContext = context;
        if (TextUtils.isEmpty(firstIP) || TextUtils.isEmpty(secondIP)
                || TextUtils.isEmpty(thirdIP) || TextUtils.isEmpty(fourthIP)) {
            Toast.makeText(context, context.getString(R.string.administrator_setting_ip_toast), Toast.LENGTH_LONG).show();
        }
        return firstIP + "." + secondIP + "." + thirdIP + "." + fourthIP;
    }

//    public void setText(@IdRes int resId){
//        String ipAddress = mContext.getString(resId);
//        setIps(ipAddress);
//    }

    public void setText(String ipAddress){
        setIps(ipAddress);
    }

    private void setIps(String ipAddress){
        String[] ip = ipAddress.split(".");
        if (ip.length == 4) {
            firstIPEdit.setText(ip[0]);
            secondIPEdit.setText(ip[1]);
            thirdIPEdit.setText(ip[2]);
            fourthIPEdit.setText(ip[3]);
        }
    }
}
