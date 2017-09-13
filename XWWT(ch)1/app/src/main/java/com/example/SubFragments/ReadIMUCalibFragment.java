package com.example.SubFragments;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.CommonFunction.SerialCom;
import com.example.xwwt.R;

import android.support.v4.app.Fragment;
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
public class ReadIMUCalibFragment extends Fragment
{

		private View fragmentView;
		private Button btnRead;//��ȡ�ߵ��궨ָ��
		private EditText editText1,editText2,editText3;
		JSONObject json;
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.readimucalib_layout,container,false);
		    initialView(fragmentView);
			return fragmentView;
		}

	public void initialView(View view) {
		
		editText1=(EditText)view.findViewById(R.id.editText01);//�����
		editText2=(EditText)view.findViewById(R.id.editText02);//������
		editText3=(EditText)view.findViewById(R.id.editText04);//���伫��
		
		//��ȡ�ߵ��궨����
		btnRead=(Button)view.findViewById(R.id.button01);
		btnRead.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
					//����λ�����Ͷ�ȡ�ߵ��궨ָ��
				
					SerialCom newSer=new SerialCom();
					json=newSer.ReadIMUCalib();
					try
					{
						editText1.setText(json.getString("heading").toString());
						editText2.setText(json.getString("pitch").toString());
						editText3.setText(json.getString("roll").toString());
					}
					catch(JSONException e)
					{
						
					}
			}
		});
	}
}