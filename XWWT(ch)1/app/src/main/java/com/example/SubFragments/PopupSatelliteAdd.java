package com.example.SubFragments;

import com.example.CommonFunction.Dao;
import com.example.xwwt.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class PopupSatelliteAdd extends PopupWindow
{
	private Button btnCreate,btnCancel;
	private View mMenuView;
	
	private EditText etName,etOld,etLongitude,rtHorizontal,etVertical,etMark;
	
	public PopupSatelliteAdd(Activity context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.satellite_add, null);
		btnCreate = (Button) mMenuView.findViewById(R.id.button02);
		btnCancel = (Button) mMenuView.findViewById(R.id.button03);
		
		etName=(EditText)mMenuView.findViewById(R.id.editText01);
		etOld=(EditText)mMenuView.findViewById(R.id.EditText01);
		etLongitude=(EditText)mMenuView.findViewById(R.id.editText04);
		rtHorizontal=(EditText)mMenuView.findViewById(R.id.EditText02);
		etVertical=(EditText)mMenuView.findViewById(R.id.EditText04);
		etMark=(EditText)mMenuView.findViewById(R.id.editText05);
		//取消按钮
		btnCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		//设置按钮监听
		btnCreate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub			
				if("".equals(etName.getText().toString())||"".equals(etLongitude.getText().toString()))
				{
					//Log.i("1", "有数据为空");
				}
				else
				{
					new Thread()
					{
						@Override
						public void run()
						{
							Dao dao=new Dao();
							dao.AddSatellite(etName.getText().toString(), etLongitude.getText().toString(),etOld.getText().toString(),
									etVertical.getText().toString(),
									rtHorizontal.getText().toString(),etMark.getText().toString());
						}
					}.start();
					
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dismiss();
				}
				
			}
		
		});
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_sat).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}
}