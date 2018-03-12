package com.example.tcpdemo;

import com.example.udp_tcp_demo.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AreaAddWindowHint extends  Dialog implements View.OnClickListener {
	private Context context;
	private Button confirmBtn;
	private Button cancelBtn;
	private TextView tvContent;

	
	private TextView titleTv;
	private String period = "";
	private PeriodListener listener;
	private String defaultName = "",title;
	public AreaAddWindowHint(Context context) {
		super(context);
		this.context = context;
	}

	public AreaAddWindowHint(Context context, int theme, String titleName,PeriodListener listener,String defaultName) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
		this.defaultName = defaultName;
		this.title = titleName;
	}

	
	/****
	 * 
	 * @author mqw
	 *
	 */
	public interface PeriodListener {
		public void refreshListener(String string);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.window_area_hint);
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		tvContent = (TextView) findViewById(R.id.areaName);
		titleTv = (TextView) findViewById(R.id.dialog_title);
		titleTv.setText(title);
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		tvContent.setText(defaultName);
		
	}
		 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.cancel_btn:
			dismiss();
			break;
		case R.id.confirm_btn: 
				dismiss();
				listener.refreshListener(period);
			break;

		default:
			break;
		}
	}
}