package com.example.SubFragments;


import com.example.CommonFunction.Dao;
import com.example.CommonFunction.EditTextLocker;
import com.example.xwwt.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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

public class PopupCityAdd extends PopupWindow
{
	private Button btnCreate,btnCancel;
	private View mMenuView;
	
	private EditText etName,etLongitude,etLatitude;
	
	public PopupCityAdd(Activity context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.citymodify_layout, null);
		etName=(EditText)mMenuView.findViewById(R.id.editText01);
		etLongitude=(EditText)mMenuView.findViewById(R.id.editText04);
		EditTextLocker decimalEditTextLockerLogi = new EditTextLocker(etLongitude,-180.0,180.0);
		decimalEditTextLockerLogi.limitFractionDigitsinDecimal(6);
		
		etLatitude=(EditText)mMenuView.findViewById(R.id.editText05);
		EditTextLocker decimalEditTextLockerLati = new EditTextLocker(etLatitude,-90.0,90.0);
		decimalEditTextLockerLati.limitFractionDigitsinDecimal(6);
		

		btnCancel = (Button) mMenuView.findViewById(R.id.button03);
		//ȡ����ť
		btnCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//���ٵ�����
				dismiss();
			}
		});
		//���ð�ť����
		btnCreate = (Button) mMenuView.findViewById(R.id.button02);
		btnCreate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etName.getText();
				if("".equals(etName.getText().toString())||"".equals(etLongitude.getText().toString())
						||"".equals(etLatitude.getText().toString()))
				{
				}
				else
				{
					new Thread()
					{
						@Override
						public void run()
						{
							Dao dao=new Dao();
							dao.AddCity(etName.getText().toString(), etLatitude.getText().toString(),
									etLongitude.getText().toString());
						}
					}.start();
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dismiss();
				}
				
			}
			
		});
		//����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		//����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.FILL_PARENT);
		//����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		//����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.AnimBottom);
		//ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		//mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_city).getTop();
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