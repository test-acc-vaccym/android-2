package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.BroadcastReceiverFragment;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 功放本振
 */

public class AdministratorFragmentAmplifierOscillator extends BroadcastReceiverFragment {
    public static final String AmplifierOscillatorAction="com.edroplet.sanetel.AmplifierOscillatorAction";
    public static final String AmplifierOscillatorData="com.edroplet.sanetel.AmplifierOscillatorData";

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.settings_amplifier_oscillator_group)
    CustomRadioGroupWithCustomRadioButton oscillatorGroup;

    @BindView(R.id.settings_amplifier_oscillator_custom_value)
    CustomEditText oscillatorCustomValue;

    public static final String Key_amplifier_oscillator_id="Key_amplifier_oscillator_id";
    public static final String Key_amplifier_oscillator_value="Key_amplifier_oscillator_value";

    private Unbinder unbinder;

    public static AdministratorFragmentAmplifierOscillator newInstance() {
        Bundle args = new Bundle();
        AdministratorFragmentAmplifierOscillator fragment = new AdministratorFragmentAmplifierOscillator();
        fragment.setArguments(args);
        return fragment;
    }

    static SparseIntArray mapAmplifierOscillatorPosId = new SparseIntArray(3);
    void initSparseIntArray(){
        mapAmplifierOscillatorPosId.put(0, R.id.id_administrator_settings_amplifier_oscillator_value_1);
        mapAmplifierOscillatorPosId.put(1, R.id.id_administrator_settings_amplifier_oscillator_value_2);
        mapAmplifierOscillatorPosId.put(2, R.id.id_administrator_settings_amplifier_oscillator_value_3);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String []action = {AmplifierOscillatorAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        Protocol.sendMessage(getContext(), Protocol.cmdGetBucLf);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initSparseIntArray();
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_amplifier_oscillator, null);
        if (view == null){
            return null;
        }

        unbinder = ButterKnife.bind(this, view);

        final Context context = getContext();
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = oscillatorGroup.getCheckedRadioButtonId();
                String val;
                CustomSP.putInt(context, Key_amplifier_oscillator_id, mapAmplifierOscillatorPosId.indexOfValue(checkedId));
                if (checkedId == R.id.id_administrator_settings_amplifier_oscillator_value_3) {
                    val = oscillatorCustomValue.getText().toString();
                    CustomSP.putString(context, Key_amplifier_oscillator_value, val);
                } else {
                    val = ((CustomRadioButton) view.findViewById(checkedId)).getText().toString();
                }

                // 2017/10/23 设置命令
                Protocol.sendMessage(context, String.format(Protocol.cmdSetBucLf, val));
                // 退出
                getActivity().finish();
            }
        });

        int checkedPos = CustomSP.getInt(context, Key_amplifier_oscillator_id, 0);
        oscillatorGroup.check(mapAmplifierOscillatorPosId.get(checkedPos));

        if (checkedPos == 2) {
            oscillatorCustomValue.setText(CustomSP.getString(context, Key_amplifier_oscillator_value, ""));
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
