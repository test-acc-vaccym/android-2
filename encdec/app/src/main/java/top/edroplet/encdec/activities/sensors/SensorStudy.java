package top.edroplet.encdec.activities.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import top.edroplet.encdec.R;

public class SensorStudy extends Activity implements SensorEventListener {
    TextView tv_number, tv_nameType;
    ListView lv;
    ArrayList<SensorData> sensors = new ArrayList<SensorData>();
    LayoutInflater inflater;
    Sensor stepCount, stepDetector;
    SensorManager sm;
    float count, detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_study);

        tv_number = (TextView) findViewById(R.id.sensor_studyTextView_number);

        lv = (ListView) findViewById(R.id.sensor_studyListView_sensorList);


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCount = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetector = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sm.registerListener(this, stepCount, sm.SENSOR_DELAY_FASTEST);
        sm.registerListener(this, stepDetector, sm.SENSOR_DELAY_FASTEST);
        List<Sensor> ls = sm.getSensorList(Sensor.TYPE_ALL);

        tv_number.setText(String.valueOf(ls.size()));

        // StringBuffer sb = new StringBuffer();
        for (Sensor s : ls) {
            sensors.add(new SensorData(s.getType(), s.getName()));
            // sb.append(s.getName()+":"+ String.valueOf(s.getType())+";");
        }
        // Log.e("sensor", data.toString());
        Collections.sort(sensors, new SensorComparator());
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lv.setAdapter(new SensorListAdapter());
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (id == -1) {
                    // 点击headerview 或者 foot view
                    return;
                }
                int realPosition = (int) id;
                SensorData item = (SensorData) parent.getItemAtPosition(realPosition);
                Toast.makeText(null, item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        // tv_nameType.setText(sb.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor s, int accuracy) {
        // TODO: Implement this method
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            setStepCount(event.values[0]);
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                detector++;
            }
        } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float[] v = event.values;
            for (float f : v) {
                Log.d("orientation", String.valueOf(f));
            }
        }
    }

    public float getStepCount() {
        return count;
    }

    private void setStepCount(float c) {
        this.count = c;
    }

    private float getStepDetector() {
        return detector;
    }

    class SensorData {
        int type;
        String name;

        SensorData(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public int getType() {
            return this.type;
        }

        public String getName() {
            return this.name;
        }
    }

    class SensorComparator implements Comparator {
        @Override
        public int compare(Object p1, Object p2) {
            SensorData sd1 = (SensorData) p1;
            SensorData sd2 = (SensorData) p2;

            if (sd1.getType() < sd2.getType()) {
                return -1;
            }
            return 1;
        }
    }

    public class SensorListAdapter extends BaseAdapter implements OnItemClickListener {
        @Override
        public long getItemId(int p1) {
            return p1;
        }

        @Override
        public Object getItem(int p1) {
            return sensors.get(p1);
        }

        @Override
        public int getCount() {
            return sensors.size();
        }


        @Override
        public View getView(int p1, View p2, ViewGroup p3) {
            SensorData data = (SensorData) sensors.get(p1);
            if (p2 == null) {
                p2 = inflater.inflate(R.layout.sensor_list_item, null);
            }

            TextView tv_name = (TextView) p2.findViewById(R.id.sensor_list_itemTextView_name);
            final String name = data.getName();
            final int type = data.getType();
            TextView tv_type = (TextView) p2.findViewById(R.id.sensor_list_itemTextView_type);
            // int type = ((intKey)data.entrySet().toArray()[p1]).ikey;
            tv_type.setText(String.valueOf(data.getType()));
            tv_name.setText(data.getName());
            p2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    // Log.e("NAME", na);
                    String msg = name;
                    if (type == Sensor.TYPE_STEP_COUNTER) {
                        msg += String.valueOf(count);
                    } else if (type == Sensor.TYPE_STEP_DETECTOR) {
                        msg += String.valueOf(detector);
                    }
                    Toast.makeText(p1.getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
            return p2;
        }

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            if (id == -1) {
                // 点击headerview 或者 foot view
                return;
            }
            int realPosition = (int) id;
            SensorData item = (SensorData) parent.getItemAtPosition(realPosition);
            Toast.makeText(parent.getContext(), item.getName(), Toast.LENGTH_SHORT).show();
        }

    }
}

