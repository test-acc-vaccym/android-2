package com.example.tcpdemo;

import com.example.mytt.dao.CommandInfoDao;
import com.example.udp_tcp_demo.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCommandActivity extends BaseActivity implements
		OnClickListener {

	private final Handler handler = new Handler();
	public TextView cmd_textview;
	public StringBuilder cmd = new StringBuilder();
	private ScrollView cmd_scroll;
	private String nowDeviceMac;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cmd_fragmnet);
		nowDeviceMac = getIntent().getStringExtra(BaseVolume.DEVICE_MAC);
		initWidget();

	}

	private void initWidget() {
		findViewById(R.id.tvDelete).setOnClickListener(this);
		findViewById(R.id.title_save).setOnClickListener(this);
		findViewById(R.id.btnSend).setOnClickListener(this);
		
		cmd_scroll = (ScrollView) findViewById(R.id.cmd_scroll);
		cmd_textview = (TextView) findViewById(R.id.cmd_textview);
		
//		cmd_textview.setText(BaseVolume.LIGHT_ONE_OPEN);
//		cmd.append(BaseVolume.LIGHT_ONE_OPEN);
		
		findViewById(R.id.num_0).setOnTouchListener(numTouch);
		findViewById(R.id.num_1).setOnTouchListener(numTouch);
		findViewById(R.id.num_2).setOnTouchListener(numTouch);
		findViewById(R.id.num_3).setOnTouchListener(numTouch);
		findViewById(R.id.num_4).setOnTouchListener(numTouch);
		findViewById(R.id.num_5).setOnTouchListener(numTouch);
		findViewById(R.id.num_6).setOnTouchListener(numTouch);
		findViewById(R.id.num_7).setOnTouchListener(numTouch);
		findViewById(R.id.num_8).setOnTouchListener(numTouch);
		findViewById(R.id.num_9).setOnTouchListener(numTouch);
		findViewById(R.id.num_a).setOnTouchListener(numTouch);
		findViewById(R.id.num_b).setOnTouchListener(numTouch);
		findViewById(R.id.num_c).setOnTouchListener(numTouch);
		findViewById(R.id.num_d).setOnTouchListener(numTouch);
		findViewById(R.id.num_e).setOnTouchListener(numTouch);
		findViewById(R.id.num_f).setOnTouchListener(numTouch);
		findViewById(R.id.num_del).setOnClickListener(this);
		findViewById(R.id.num_del).setOnTouchListener(new OnTouchListener() {// 一直按着del键，能一直在删除

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							isFocus = true;
							handler.postDelayed(delCmdHandler,
									ViewConfiguration.getLongPressTimeout());
							break;
						case MotionEvent.ACTION_UP:
							isFocus = false;
							handler.removeCallbacks(delCmdHandler);
							break;

						default:
							break;
						}

						return false;
					}
				});
	}

	boolean isNumFocus;
	long lastTime;
	boolean isFocus = false;
	private Runnable delCmdHandler = new Runnable() {

		@Override
		public void run() {
			if (isFocus) {
				delCmd();
				handler.postDelayed(delCmdHandler, 200);
			}
		}
	};

	abstract class NumRunnableHandler implements Runnable {
		public String num_handler;

	}

	private NumRunnableHandler numAddHandler = new NumRunnableHandler() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (isNumFocus && num_handler != null) {
				cmdAdd(num_handler);
				handler.postDelayed(numAddHandler, 100);
			}

		}
	};

	private void cmdAdd(String num) {
		if (cmd == null) {
			cmd = new StringBuilder();
		}
		cmd.append(num);
		if (cmd.length() % 2 == 0) {
			cmd_textview.append(num + " ");
		} else {
			cmd_textview.append(num);
		}
		handler.post(runnable);
	}

	private OnClickListener numListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			String num = btn.getText().toString().trim();
			cmdAdd(num);
		}
	};
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			cmd_scroll.fullScroll(ScrollView.FOCUS_DOWN);
		}
	};

	private void saveCmd() {
		
		AreaAddWindow areaAddDialog = new AreaAddWindow(this,R.style.Dialogstyle, "指令名称",
				new AreaAddWindow.PeriodListener() {
					public void refreshListener(String newValue) {
						String name = newValue;
						CommandInfoCache cmdBean = new CommandInfoCache();
						cmdBean.setName(name);
						cmdBean.setData(cmd.toString());
						cmdBean.setMac(nowDeviceMac);
						
						CommandInfoDao commandInfoDao = new CommandInfoDao(CreateCommandActivity.this);
						commandInfoDao.insertSingleData(cmdBean);
						commandInfoDao.closeDb();
						
						sendBroadcast(new Intent(BaseVolume.UPDATE_CMD));
						
						finish();
					}

					public void dismissListener() {
						
					}
				}, "",false);
			areaAddDialog.show();
		
	}

	void delCmd() {
		if (cmd == null) {
			return;
		}
		if (cmd.length() == 0) {
			return;
		}
		String cmdText = cmd_textview.getText().toString();
		char c = cmdText.charAt(cmdText.length() - 1);
		if (c == ' ') {
			cmdText = cmdText.substring(0, cmdText.length() - 2);
		} else {
			cmdText = cmdText.substring(0, cmdText.length() - 1);
		}
		cmd.delete(cmd.length() - 1, cmd.length());
		cmd_textview.setText(cmdText);
		handler.post(runnable);
	}

	private OnTouchListener numTouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Button bv = (Button) v;
				isNumFocus = true;
				numAddHandler.num_handler = bv.getText().toString().trim();
				handler.postDelayed(numAddHandler, 500);// 500毫秒的等待时间,
				break;
			case MotionEvent.ACTION_UP:
				isNumFocus = false;
				numAddHandler.num_handler = null;
				handler.removeCallbacks(numAddHandler);
				numListener.onClick(v);// 调用点击事件
				break;

			default:
				break;
			}

			return false;
		}
	};

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_save:
			if (cmd == null||cmd.length() == 0) {
		    	Toast.makeText(CreateCommandActivity.this,
		    			"请编写指令！", Toast.LENGTH_SHORT).show();
			return;
		    }
		    if ( cmd.length() % 2 != 0) {
		    	Toast.makeText(CreateCommandActivity.this,
		    			"指令长度必须是偶数！", Toast.LENGTH_SHORT).show();
			return;
		    }
		    saveCmd();
		    break;
		case R.id.num_del:
		    delCmd();
		    break;
		case R.id.tvDelete:
			finish();
			break;
		case R.id.btnSend:
			if (cmd == null||cmd.length() == 0) {
		    	Toast.makeText(CreateCommandActivity.this,
		    			"请编写指令！", Toast.LENGTH_SHORT).show();
			return;
		    }
		    if ( cmd.length() % 2 != 0) {
		    	Toast.makeText(CreateCommandActivity.this,
		    			"指令长度必须是偶数！", Toast.LENGTH_SHORT).show();
			return;
		    }
		    sendBroadcast(new Intent(BaseVolume.SEND_DATA).putExtra(BaseVolume.SEND_DATA_MSG, cmd.toString()));
//			finish();
			break;
			
		default:
		    break;
		}

	}

}
