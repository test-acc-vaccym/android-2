package bawei.com.checkboxrecyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView tv= (TextView) findViewById(R.id.tv);
        Intent intent = getIntent();
        String zd = intent.getStringExtra("zd");
        Toast.makeText(this, zd, Toast.LENGTH_SHORT).show();
    }
}
