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

public class OtherView extends Fragment
{

	    private View fragmentView;
		private Button btn;
		EditText editText1,editText2,editText3,editText4;
		String state;
		
		Bundle bundle=new Bundle();
		
	    public static OtherView newInstance(Bundle bundle) 
	    {
	        Bundle args = new Bundle();
	        
	        OtherView fragment = new OtherView();
	        fragment.setArguments(args);
	        return fragment;
	    }
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{  
			super.onCreate(savedInstanceState);
			//bundle=this.getArguments();
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			
			fragmentView = inflater.inflate(R.layout.other_layout,container,false);
			
			editText1=(EditText)fragmentView.findViewById(R.id.editText01);
			editText2=(EditText)fragmentView.findViewById(R.id.editText05);
			editText3=(EditText)fragmentView.findViewById(R.id.editText02);
			editText4=(EditText)fragmentView.findViewById(R.id.editText06);
 
			return fragmentView;
		}
		
		//数据更新
	    public void updateData(Bundle bundle) {

			editText1=(EditText)fragmentView.findViewById(R.id.editText01);
			editText2=(EditText)fragmentView.findViewById(R.id.editText02);
			editText3=(EditText)fragmentView.findViewById(R.id.editText04);
			editText4=(EditText)fragmentView.findViewById(R.id.editText05);

	    }

		public void setList(Bundle tmp) {
			// TODO Auto-generated method stub
			try{
			state=tmp.getString("searchState");
			if("0".equals(state))
			{editText1.setText("初始");}//初始Initialize
			else if("1".equals(state))
			{editText1.setText("寻星");}//寻星Search
			else if("2".equals(state))
			{editText1.setText("手动");}//手动Manual
			else if("3".equals(state))
			{editText1.setText("锁定");}//锁定Lock
			else if("4".equals(state))
			{editText1.setText("停止");}//停止Stop
			else if("5".equals(state))
			{editText1.setText("失锁");}//失锁UnLock
			else if("6".equals(state))
			{editText1.setText("其他");}//其他other
			
			editText2.setText(tmp.getString("beaconFrequency"));//
			editText3.setText(tmp.getString("AGCLevel"));
			editText4.setText(tmp.getString("pitchOffset"));
			}catch(NullPointerException e){
				System.err.flush();
			}
		}
}
