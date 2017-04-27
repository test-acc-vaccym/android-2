package top.edroplet.encdec;
import android.app.Activity;
import android.content.*;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.*;
import android.widget.TextView;
import java.util.List;

public class SensorStudy extends Activity 
{
	TextView tv_number, tv_nameType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_study);
		
		tv_number = (TextView) findViewById(R.id.sensor_studyTextView_number);
		tv_nameType = (TextView) findViewById(R.id.sensor_studyTextView_typename);
		
		SensorManager sm;
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		List<Sensor> ls = sm.getSensorList(Sensor.TYPE_ALL);
	
		tv_number.setText(String.valueOf(ls.size()));
		StringBuilder sb = new StringBuilder();
		for(Sensor s:ls){
			sb.append(s.getName()+":"+ String.valueOf(s.getType())+";");
		}
		tv_nameType.setText(sb.toString());
	}
	
}
