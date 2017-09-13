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

/**
 * ������Tabhost����ʾ�Ĺߵ���Ϣ
 * @author lei
 *
 */
public class InertialFragment extends Fragment
{

		private View fragmentView;
		EditText editText1,editText2,editText3,editText4;
		private Button btn;
		Bundle bundle=new Bundle();
		
		
	    public static InertialFragment newInstance(Bundle bundle) 
	    {
	        Bundle args = new Bundle();
	        
	        InertialFragment fragment = new InertialFragment();
	        fragment.setArguments(args);
	        return fragment;
	    }
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{  
			super.onCreate(savedInstanceState);
			bundle=this.getArguments();
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
//			
			fragmentView = inflater.inflate(R.layout.imu_layout,container,false);
//			//Bundle bundle=this.getArguments();
			editText1=(EditText)fragmentView.findViewById(R.id.editText01);
			editText2=(EditText)fragmentView.findViewById(R.id.editText02);
			editText3=(EditText)fragmentView.findViewById(R.id.editText04);
			editText4=(EditText)fragmentView.findViewById(R.id.editText05);
//			editText1.setText( bundle.getString("INSHeading"));
//			editText2.setText( bundle.getString("INSPitch"));
//			editText3.setText( bundle.getString("INSRoll"));
//			if(bundle.getString("INSStatus")=="0")
//			{
//				editText4.setText("��Ч");
//			}
//			else if(bundle.getString("INSStatus")=="1")
//			{
//				editText4.setText("��λ");
//			}
//			else if(bundle.getString("INSStatus")=="2")
//			{
//				editText4.setText("����");
//			}
//			else if(bundle.getString("INSStatus")=="3")
//			{
//				editText4.setText("�ڵ�");
//			}
//				
//			
//	
		return fragmentView;
		}

		
		//���ݸ���
	    public void updateData(Bundle bundle) {

			editText1=(EditText)fragmentView.findViewById(R.id.editText01);
			editText2=(EditText)fragmentView.findViewById(R.id.editText02);
			editText3=(EditText)fragmentView.findViewById(R.id.editText04);
			editText4=(EditText)fragmentView.findViewById(R.id.editText05);
	    }

		public void setList(Bundle tmp) {
			// TODO Auto-generated method stub
			
			try{
			editText1.setText(tmp.getString("INSHeading"));
			editText2.setText(tmp.getString("INSPitch"));
			editText3.setText(tmp.getString("INSRoll"));
			//editText4.setText(tmp.getString("INSHeading"));
			if("0".equals(tmp.getString("INSStatus")))
			{
				editText4.setText("��Ч");//��ЧInvalid
			}
			else if("1".equals(tmp.getString("INSStatus")))
			{
				editText4.setText("��λ");//��λPositioning
			}
			else if("2".equals(tmp.getString("INSStatus")))
			{
				editText4.setText("����");//����Dirction
			}
			else if("3".equals(tmp.getString("INSStatus")))
			{
				editText4.setText("�ڵ�");//�ڵ�Mask
			}
			else if("--".equals(tmp.getString("INSStatus")))
			{
				editText4.setText("--");
			}
			}catch(NullPointerException e){
				System.err.flush();
			}
		}
}