package com.example.SubFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.Map;

import com.example.xwwt.R;

public class PositionFragment extends Fragment
{

		private View fragmentView;
		private Button btn;
		EditText editText1,editText2,editText3,etlongitude,etLatitude;
		Bundle bundle=new Bundle();
		
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{  
			super.onCreate(savedInstanceState);
			bundle=this.getArguments();
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.position_layout,container,false);
			
			editText1=(EditText)fragmentView.findViewById(R.id.editText01);
			editText2=(EditText)fragmentView.findViewById(R.id.editText02);
			editText3=(EditText)fragmentView.findViewById(R.id.editText05);
			etlongitude=(EditText)fragmentView.findViewById(R.id.etlongitude);
			etLatitude=(EditText)fragmentView.findViewById(R.id.etlatitude);

			//bundle.getDouble("roll");
			return fragmentView;
		}
		

		public void setList(Bundle tmp) {
			// TODO Auto-generated method stub
			Double longitude = 0.0;
			Double latitude = 0.0;
			try{
				longitude=Double.parseDouble(tmp.getString("longitude"));
			if(longitude>0)
			{
				etlongitude.setText("E");
			}
			else if(longitude<0)
			{
				longitude=(-1)*longitude;
				etlongitude.setText("W");
			}
			editText1.setText(longitude+"");
			
			latitude=Double.parseDouble(tmp.getString("latitude"));
			
			if(latitude>0)
			{
				etLatitude.setText("N");
			}
			else if(latitude<0)
			{
				latitude=(-1)*latitude;
				etLatitude.setText("S");
			}
			
			editText2.setText(latitude+"");
			
		    editText3.setText(tmp.getString("altitude"));
			}catch(NullPointerException e){
				System.err.flush();
			}
		}
}