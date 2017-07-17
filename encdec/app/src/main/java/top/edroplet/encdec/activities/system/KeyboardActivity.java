package top.edroplet.encdec.activities.system;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import top.edroplet.encdec.R;
import top.edroplet.encdec.utils.util.KeyboardUtil;

public class KeyboardActivity extends Activity {
    private Context ctx;
    private Activity act;
    private EditText edit;
    private EditText edit1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        ctx = this;
        act = this;

        edit = (EditText) this.findViewById(R.id.edit);
        edit.setInputType(InputType.TYPE_NULL);

        edit1 = (EditText) this.findViewById(R.id.edit1);

        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new KeyboardUtil(act, ctx, edit).showKeyboard();
                return false;
            }
        });

        edit1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inputback = edit1.getInputType();
                edit1.setInputType(InputType.TYPE_NULL);
                new KeyboardUtil(act, ctx, edit1).showKeyboard();
                edit1.setInputType(inputback);
                return false;
            }
        });
    }

}
