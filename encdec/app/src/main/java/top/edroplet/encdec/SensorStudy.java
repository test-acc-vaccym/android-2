package top.edroplet.encdec;
import android.app.Activity;
import android.content.*;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SensorStudy extends Activity 
{
	TextView tv_number, tv_nameType;
	ListView lv;
	ArrayList sensors = new ArrayList();
	private LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_study);
		
		tv_number = (TextView) findViewById(R.id.sensor_studyTextView_number);
		tv_nameType = (TextView) findViewById(R.id.sensor_studyTextView_typename);
		lv = (ListView) findViewById(R.id.sensor_studyListView_sensorList);
		SensorManager sm;
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		List<Sensor> ls = sm.getSensorList(Sensor.TYPE_ALL);
	
		tv_number.setText(String.valueOf(ls.size()));
		// StringBuilder sb = new StringBuilder();
		for(Sensor s:ls){
			// sb.append(s.getName()+":"+ String.valueOf(s.getType())+";");
			sensors.add(new SensorData(s.getType(),s.getName()));
		}

		SensorComparator sc = new SensorComparator();
		Collections.sort(sensors,sc);
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lv.setAdapter(new SensorListAdapter());
		// tv_nameType.setText(sb.toString());
	}

	class SensorData {
		private int type;
		private String name;
		SensorData(int type, String name){
			this.type = type;
			this.name = name;
		}

		public int getType() {
			return type;
		}

		public String getName() {
			return name;
		}
	}

	class SensorComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			SensorData d1 = (SensorData) o1;
			SensorData d2 = (SensorData) o2;
			if (d1.getType() < d2.getType())
				return -1;
			return 1;
		}
	}

	class SensorListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return sensors.size();
		}

		@Override
		public Object getItem(int position) {
			return sensors.get(position);
		}

		@Override
		public long getItemId(int position) {
			return ((SensorData)sensors.get(position)).getType();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SensorData sd = (SensorData)sensors.get(position);
			TextView type, name;

			convertView = inflater.inflate(R.layout.sensor_list_item, null);
			type = (TextView) convertView.findViewById(R.id.sensor_studyTextView_typenumber);
			name = (TextView) convertView.findViewById(R.id.sensor_studyTextView_name);
			type.setText(String.valueOf(sd.getType()));
			name.setText(sd.getName());
			return convertView;
		}
	}
}
