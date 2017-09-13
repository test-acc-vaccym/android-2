package com.example.xwwt;

import android.os.Bundle;  
import android.support.v4.app.Fragment;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  



//该文件定义了设置菜单包含的控件及其功能
public class RightSetMenu extends Fragment  
{  
    private View mView;  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
        if(mView == null)  
        {  
            mView = inflater.inflate(R.layout.right_menu, container, false);  
        }  
        return mView ;  
    }  
} 