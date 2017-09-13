/*
 * @author by zxl
 */
package com.example.fragment;

import java.util.HashMap;

import com.example.xwwt.R;
import com.example.SubFragments.AntCalibFragment;
import com.example.fragment.IMUCalibFragment.BackHandlerInterface;
import com.example.xwwt.MainUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class MenuFragment extends Fragment
{
	private LinearLayout layout;
	private Button btn_Mon;//�����Ϣ��ȡ��ť
	private Button btn_Ref;//�ο��ǰ�ť
	private Button btn_Thr;//����ֵ��ť
	private Button btn_Man;//�ֶ�����
	private Button btn_AntCal;//���߱궨
	private Button btn_IMUCal;//���߱궨
	private Button btn_Help;//���߱궨
	private Button btn_Set;//���߱궨
	private Button btn_DataBase;//���߱궨
	
    private boolean mHandledPress = false;
    protected BackHandlerInterface backHandlerInterface;

    public interface BackHandlerInterface {
        public void setSelectedFragment(MenuFragment menuFragment);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = (LinearLayout) inflater.inflate(R.layout.menu_layout,
				container, false);
		initView();
		return layout;
	}
	
	private void initView() { 
		
		//����״̬��ؽ���
		btn_Mon=(Button)layout.findViewById(R.id.button1);
		btn_Mon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MonitorActivity frag=new MonitorActivity();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
		//����ο������ý���
		btn_Ref=(Button)layout.findViewById(R.id.button2);
		btn_Ref.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment frag=new RefStarFragment();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
		//����Ѱ������
		btn_Thr=(Button)layout.findViewById(R.id.button3);
		btn_Thr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment frag=new ReadThreshold();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
		
		//��ת���ֶ����ƽ���
		btn_Man=(Button)layout.findViewById(R.id.button6);
		btn_Man.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment frag=new ManualControl();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
		
		//��ת�����߱궨����
		btn_AntCal=(Button)layout.findViewById(R.id.button10);
		btn_AntCal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment frag=new AntennaCalibFragment();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
		//��ת��IMU�궨ҳ��
		btn_IMUCal=(Button)layout.findViewById(R.id.button12);
		btn_IMUCal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment frag=new IMUCalibFragment();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
		//��ת������ҳ��
		btn_Help=(Button)layout.findViewById(R.id.button5);
		btn_Help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment frag=new HelpFragment();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
		//��ת������ҳ��
		btn_Set=(Button)layout.findViewById(R.id.button4);
		btn_Set.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment frag=new SetFragment();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
		//��ת�����ݿ�ҳ��
		btn_DataBase=(Button)layout.findViewById(R.id.button9);
		btn_DataBase.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment frag=new DatabaseFragment();
				((MainUI) getActivity()).switchContent(frag);
			}
		});
	}

	//��ȡ�ؼ���ID
	public EditText GetId() {
		// TODO Auto-generated method stub
	EditText edT;
	edT=(EditText)layout.findViewById(R.id.editText01);
	return edT;
	}

    @Override
    public void onStart() {
        super.onStart();
        backHandlerInterface.setSelectedFragment(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            backHandlerInterface = (BackHandlerInterface) getActivity();
        } catch (Exception e) {
            throw new ClassCastException("Hosting activity must implement BackHandlerInterface");
        }
    }
	
    public boolean onBackPressed() {
        if (!mHandledPress) {
            mHandledPress = true;
    		Intent aIntent = new Intent();
    		aIntent.setClass(getActivity(),MainUI.class);
    		startActivity(aIntent);
            return true;
        }
        return false;
    }
}