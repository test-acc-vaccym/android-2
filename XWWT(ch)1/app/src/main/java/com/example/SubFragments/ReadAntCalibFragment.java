package com.example.SubFragments;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.SerialCom;
import com.example.xwwt.R;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * ��TabHost����ʾ�����߽Ƕ���Ϣ
 * @author lei
 *
 */
public class ReadAntCalibFragment extends Fragment
{

		private View fragmentView;
		private Button btnRead;
		private EditText editText1,editText2,editText3,editText4;
		JSONObject json;
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.readantcalib_layout,container,false);
			initialView(fragmentView);
			return fragmentView;
		}

		//�Խ�����в����Ļ�������
		public void initialView(View view)
		{
			editText1=(EditText)view.findViewById(R.id.editText01);//�����
			editText2=(EditText)view.findViewById(R.id.editText02);//������
			editText3=(EditText)view.findViewById(R.id.editText04);//���伫��
			editText4=(EditText)view.findViewById(R.id.editText05);//���ռ���
					
			btnRead=(Button)view.findViewById(R.id.button01);
			btnRead.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//�ж����е������Ƿ����ֵ
						//����λ������λ�ÿ�������
						//String actionName="referenceStar";					
						SerialCom newSer=new SerialCom();
						json=newSer.ReadAntennaCalib();
						try
						{
							editText1.setText(json.getString("azimuth").toString());
							editText2.setText(json.getString("pitch").toString());
							editText3.setText(json.getString("rollEmit").toString());
							editText4.setText(json.getString("rollReceive").toString());
						}
						catch(JSONException e)
						{
							
						}
				}
			});

		}
}