package com.example.SubFragments;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.xwwt.R;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



/**
 * 在TabHost中显示的天线角度信息
 * @author lei
 *
 */
public class AngleFragment extends Fragment implements Update
{

		private View fragmentView;
		private String ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8;
		private Button btn;
		EditText editText1,editText2,editText3,editText4,editText5,editText6,editText7,editText8;

		
		
	    public static AngleFragment newInstance(Bundle bundle) {
	        Bundle args = new Bundle();
	        AngleFragment fragment = new AngleFragment();
	        fragment.setArguments(args);
	        return fragment;
	    }
		
		//通过bundle进行值得传递,或者使用接口进行实现
		@Override
		public void onCreate(Bundle savedInstanceState)
		{  
			super.onCreate(savedInstanceState);
			//bundle=this.getArguments();
		}
		

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	    }
	    
	    @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	    }
	    
	    @Override
	    public void onDetach() {
	        super.onDetach();
	    }   
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.monitor_layout,container,false);
			
			editText1=(EditText)fragmentView.findViewById(R.id.editText01);
			editText2=(EditText)fragmentView.findViewById(R.id.editText02);
			editText3=(EditText)fragmentView.findViewById(R.id.editText04);
			editText4=(EditText)fragmentView.findViewById(R.id.editText05);
			editText5=(EditText)fragmentView.findViewById(R.id.editText07);
			editText6=(EditText)fragmentView.findViewById(R.id.editText08);
			editText7=(EditText)fragmentView.findViewById(R.id.editText09);
			editText8=(EditText)fragmentView.findViewById(R.id.editText10);

			return fragmentView;
		}
		
		
		//使用bundle对数据进行更新
		public void SetValue(Bundle bundle)
		{
			ed1=bundle.getString("azimuth");
			ed2=bundle.getString("pitch");
			ed3=bundle.getString("rollEmit");
			ed4=bundle.getString("rollReceive");
			ed5=bundle.getString("currentAzimuth");
			ed6=bundle.getString("currentPitch");
			ed7=bundle.getString("currentRollEmit");
			ed8=bundle.getString("currentrollReceive");
			editText1.setText(ed1);
			editText2.setText(ed2);
			editText3.setText(ed3);
			editText4.setText(ed4);
			editText5.setText(ed5);
			editText6.setText(ed6);
			editText7.setText(ed7);
			editText8.setText(ed8);
		}
				
		//定义数据更新接口
		public interface dataChanged
		{
			public void dataChagedUI(); 
		}
		
		//对接口进行具体实现
		@Override
		public void doUpdate(Bundle bundle) {
			// TODO Auto-generated method stub
			this.editText1.setText(bundle.getString("azimuth"));
		}
		

		public void setList(Bundle tmp) {
			// TODO Auto-generated method stub
			try{
			editText1.setText(tmp.getString("azimuth"));
			editText2.setText(tmp.getString("pitch"));
			editText3.setText(tmp.getString("rollEmit"));
			editText4.setText(tmp.getString("rollReceive"));
			editText5.setText(tmp.getString("currentAzimuth"));
			editText6.setText(tmp.getString("currentPitch"));
			editText7.setText(tmp.getString("currentRollEmit"));
			editText8.setText(tmp.getString("currentrollReceive"));
			}catch(NullPointerException e){
				System.err.flush();
			}
		}
}