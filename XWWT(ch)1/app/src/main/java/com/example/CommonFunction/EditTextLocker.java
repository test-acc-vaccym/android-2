package com.example.CommonFunction;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class EditTextLocker {

	private EditText editText;

	private double minNumber;
	private double maxNumber;

	private int fractionLimit;

	public EditTextLocker(EditText editText, double minNumber, double maxNumber) {

		this.editText = editText;
		this.minNumber = minNumber;
		this.maxNumber = maxNumber;

		editText.setOnKeyListener(editTextOnKeyListener);
	}

	private OnKeyListener editTextOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if (keyCode == KeyEvent.KEYCODE_DEL) {
				startStopEditing(false);
			}

			return false;
		}
	};


	private TextWatcher editTextWatcherForFractionLimit = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

			if (!editText.getText().toString().equalsIgnoreCase("")) {

				String editTextString = editText.getText().toString().trim();
				int decimalIndexOf = editTextString.indexOf(".");

				if (decimalIndexOf >= 0) {

					if (editTextString.substring(decimalIndexOf).length() > fractionLimit) {

						startStopEditing(true);
						
					}
				}
				if(editText.getText().toString().startsWith("0"))//输入的数字以0开始
				{
					//if(decimalIndexOf)
					Log.i(("decimal"),"true");
					if(decimalIndexOf>1)
					{
						editText.setText(editText.getText().toString().substring(1));
						editText.setSelection(editText.getText().length());
					}
				}
				if(editText.getText().toString().startsWith("."))//输入的数字以.开始
				{
					editText.setText("0"+editText.getText().toString());
					editText.setSelection(editText.getText().length());
				}

			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if(s.toString()!=null&&!s.toString().equals(""))
			{
				if(minNumber!=-1&&maxNumber!=-1)
				{
					try
					{
					   Double toWrite=Double.parseDouble(s.toString());
						if(toWrite>maxNumber)
						{
							startStopEditing(false);
							editText.setText(maxNumber+"");
						}
						else if(toWrite<minNumber)
						{
							startStopEditing(false);
							editText.setText(minNumber+"");
						}
					}catch(NumberFormatException  e){
						System.err.println("NumberFormatException in valueOf, "+ e.getMessage());
					}
				}
			    editText.setSelection(editText.getText().length());
			}
		}
	};


	public void limitFractionDigitsinDecimal(int fractionLimit) {

		this.fractionLimit = fractionLimit;
		editText.addTextChangedListener(editTextWatcherForFractionLimit);
	}

	public void unlockEditText() {

		startStopEditing(false);
	}

	public void startStopEditing(boolean isLock) {

		if (isLock) {

			editText.setFilters(new InputFilter[] { new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
					return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
				}
			} });

		} else {

			editText.setFilters(new InputFilter[] { new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
					return null;
				}
			} });
		}
	}
}