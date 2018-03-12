package mediatek.android.IoTManager;

import com.example.udp_tcp_demo.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;


public class LoadingDialog extends Dialog {

	private TextView mTextView;

	public LoadingDialog(Context context) {
		super(context);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loading_dialog);
		this.setCanceledOnTouchOutside(false);
		mTextView = (TextView) findViewById(R.id.loading_text);
	}

	public void updateStatusText(String text) {
		this.mTextView.setText(text);
	}

}
