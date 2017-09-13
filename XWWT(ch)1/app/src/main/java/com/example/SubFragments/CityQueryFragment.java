package com.example.SubFragments;

import org.json.JSONObject;

import com.example.CommonFunction.Dao;
import com.example.xwwt.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * ���г������ݲ�ѯ����
 * @author zxl
 *
 */
public class CityQueryFragment extends Fragment {

	private View fragmentView;
	private Button btn;
	private Spinner spCity;//����
	private EditText edLogi,edLati;//������γ��
	private ArrayAdapter<String> adapter;
	private String[] spinnerValue={};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		fragmentView = inflater.inflate(R.layout.cityquery_layout,container,false);
	
		initialView(fragmentView);
		return fragmentView;
	}
	
	//��ʼ����ͼ
	public void initialView(View view)
	{
		edLogi=(EditText)view.findViewById(R.id.editText04);
		edLati=(EditText)view.findViewById(R.id.editText05);
		spCity=(Spinner)view.findViewById(R.id.spinner01);
		
		
		//spinnerValue={""};
		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item);
	}
}
