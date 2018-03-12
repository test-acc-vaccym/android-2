package com.example.tcpdemo;

import com.example.udp_tcp_demo.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AreaAddWindow extends  Dialog implements View.OnClickListener {
	private Context context;
	private Button confirmBtn;
	private Button cancelBtn;
	private EditText areaNameEt;
	private TextView titleTv;
	private String period = "";
	private PeriodListener listener;
	private String defaultName = "",title;
	private boolean isNumber = false;
	public AreaAddWindow(Context context) {
		super(context);
		this.context = context;
	}

	public AreaAddWindow(Context context, int theme, String titleName,PeriodListener listener,String defaultName) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
		this.defaultName = defaultName;
		this.title = titleName;
	}
	
	public AreaAddWindow(Context context, int theme, String titleName,PeriodListener listener,String defaultName,boolean isNumber) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
		this.defaultName = defaultName;
		this.title = titleName;
		this.isNumber = isNumber;
	}

	
	/****
	 * 
	 * @author mqw
	 *
	 */
	public interface PeriodListener {
		public void refreshListener(String string);
		public void dismissListener();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.window_area_set_name);
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		areaNameEt = (EditText) findViewById(R.id.areaName);
		if (isNumber) {
			areaNameEt.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		titleTv = (TextView) findViewById(R.id.dialog_title);
		titleTv.setText(title);
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		areaNameEt.setText(defaultName);
		areaNameEt.setSelection(defaultName.length());


		areaNameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			   @Override
			   public void onFocusChange(View v, boolean hasFocus) {
			       if (hasFocus) {
			            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			       }
			   }
			});
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.cancel_btn:
			dismiss();
			listener.dismissListener();
			break;
		case R.id.confirm_btn:
			period = areaNameEt.getText().toString();
			if(period.equals(""))
			{
				Toast.makeText(context,"值不能为空！", Toast.LENGTH_SHORT).show();
				
			}else{
				dismiss();
				listener.refreshListener(period);
			}
			
			break;

		default:
			break;
		}
	}
}