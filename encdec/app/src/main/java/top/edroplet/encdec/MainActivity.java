package top.edroplet.encdec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    //	private static final String TAG="MainUI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        Button btnTransfer, btnFindReplace, btnAnim;

        btnTransfer = (Button) findViewById(R.id.main_btn_transfer);
        btnFindReplace = (Button) findViewById(R.id.main_btn_find_replace);
        btnAnim = (Button) findViewById(R.id.main_btn_anim);

        btnAnim.setOnClickListener(this);
        btnTransfer.setOnClickListener(this);
        btnFindReplace.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.main_btn_transfer:
                intent = new Intent(this, EncodingTransferActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_find_replace:
                intent = new Intent(this, FindReplaceActivity.class);
                startActivity(intent);
                break;
            case R.id.main_btn_anim:
                intent = new Intent(this, AnimatorActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
