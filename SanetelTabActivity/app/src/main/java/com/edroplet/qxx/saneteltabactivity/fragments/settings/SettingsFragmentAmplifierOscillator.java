package com.edroplet.qxx.saneteltabactivity.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 */

public class SettingsFragmentAmplifierOscillator extends Fragment {
    public static SettingsFragmentAmplifierOscillator newInstance(String info) {
        Bundle args = new Bundle();
        SettingsFragmentAmplifierOscillator fragment = new SettingsFragmentAmplifierOscillator();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.settings_amplifier_oscillator_group)
    CustomRadioGroupWithCustomRadioButton oscillatorGroup;

    @BindView(R.id.settings_amplifier_oscillator_custom_value)
    CustomEditText oscillatorCustomValue;

    public static final String Key_amplifier_oscillator_id="Key_amplifier_oscillator_id";
    public static final String Key_amplifier_oscillator_value="Key_amplifier_oscillator_value";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settings_fragment_amplifier_oscillator, null);

        ButterKnife.bind(this, view);
        final Context context = getContext();
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = oscillatorGroup.getCheckedRadioButtonId();
                CustomSP.putInt(context, Key_amplifier_oscillator_id, checkedId);
                if (checkedId != R.id.settings_amplifier_oscillator_value_3)
                    CustomSP.putString(context,Key_amplifier_oscillator_value,((CustomRadioButton)view.findViewById(checkedId)).getText().toString());
                else
                    CustomSP.putString(context,Key_amplifier_oscillator_value, oscillatorCustomValue.getText().toString());
            }
        });

        int checkedId = CustomSP.getInt(context, Key_amplifier_oscillator_id, R.id.settings_amplifier_oscillator_value_1);
        if (checkedId == 0)
            checkedId = R.id.settings_amplifier_oscillator_value_1;

        if (view.findViewById(checkedId) instanceof CustomRadioButton ) {
            CustomRadioButton radioButton = (CustomRadioButton) view.findViewById(checkedId);
            radioButton.setChecked(true);
        }

        if (checkedId == R.id.settings_amplifier_oscillator_value_3)
            oscillatorCustomValue.setText(CustomSP.getString(context,Key_amplifier_oscillator_value,""));

        // 设置自定义框内容
        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PopDialog.SHOW_SECOND, true);
        bundle.putBoolean(PopDialog.SHOW_THIRD, true);
        bundle.putString(PopDialog.SECOND, context.getString(R.string.settings_amplifier_manufacture_message_second));
        bundle.putString(PopDialog.START, context.getString(R.string.settings_amplifier_manufacture_message_third_start));
        bundle.putString(PopDialog.END, context.getString(R.string.follow_me_forever));
        popDialog.setBundle(bundle);
        popDialog.setButtonText(context, context.getString(R.string.setting_button_text));
        popDialog.show();

        return view;
    }
}
