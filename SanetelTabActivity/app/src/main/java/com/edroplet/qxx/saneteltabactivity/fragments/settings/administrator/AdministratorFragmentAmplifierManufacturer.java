package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

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
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 */

public class AdministratorFragmentAmplifierManufacturer extends Fragment {

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.settings_amplifier_manufacture_group)
    CustomRadioGroupWithCustomRadioButton manufactureGroup;
    @BindView(R.id.settings_amplifier_manufacture_1)
    CustomRadioButton manufacture1;
    @BindView(R.id.settings_amplifier_manufacture_2)
    CustomRadioButton manufacture2;
    @BindView(R.id.settings_amplifier_manufacture_3)
    CustomRadioButton manufacture3;
    @BindView(R.id.settings_amplifier_manufacture_4)
    CustomRadioButton manufacture4;
    @BindView(R.id.settings_amplifier_manufacture_5)
    CustomRadioButton manufacture5;
    @BindView(R.id.settings_amplifier_manufacture_6)
    CustomRadioButton manufacture6;
    @BindView(R.id.settings_amplifier_manufacture_7)
    CustomRadioButton manufacture7;
    @BindView(R.id.settings_amplifier_manufacture_custom_value)
    CustomEditText manufactureCustomValue;


    public static final String KEY_amplifier_manufacture="KEY_amplifier_manufacture";
    public static final String KEY_amplifier_manufacture_id="KEY_amplifier_manufacture_id";

    private Unbinder unbinder;


    public static AdministratorFragmentAmplifierManufacturer newInstance() {
        Bundle args = new Bundle();
        AdministratorFragmentAmplifierManufacturer fragment = new AdministratorFragmentAmplifierManufacturer();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_amplifier_manufacture, null);
        if (view == null){
            return null;
        }

        unbinder = ButterKnife.bind(this, view);

        final Context context = getContext();
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = manufactureGroup.getCheckedRadioButtonId();
                CustomSP.putInt(getContext(), KEY_amplifier_manufacture_id, checkedId);
                if (checkedId != R.id.settings_amplifier_manufacture_7){
                    CustomSP.putString(getContext(), KEY_amplifier_manufacture, ((CustomRadioButton) view.findViewById(checkedId)).getText().toString());
                }else {
                    CustomSP.putString(getContext(), KEY_amplifier_manufacture, manufactureCustomValue.getText().toString());
                }
            }
        });

        int checkedId = CustomSP.getInt(getContext(),KEY_amplifier_manufacture_id, R.id.settings_amplifier_manufacture_1);
        if (checkedId == 0)
            checkedId = R.id.settings_amplifier_manufacture_1;
        switch (checkedId){
            case R.id.settings_amplifier_manufacture_7:
                manufacture7.setChecked(true);
                manufactureCustomValue.setText(CustomSP.getString(getContext(),KEY_amplifier_manufacture,""));
                break;
            default:
                if (view.findViewById(checkedId) instanceof CustomRadioButton ) {
                    CustomRadioButton radioButton = (CustomRadioButton) view.findViewById(checkedId);
                    radioButton.setChecked(true);
                }
                break;
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
