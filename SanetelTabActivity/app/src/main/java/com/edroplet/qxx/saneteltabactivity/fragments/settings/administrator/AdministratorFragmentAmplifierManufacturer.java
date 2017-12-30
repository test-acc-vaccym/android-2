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
    public static final String KEY_amplifier_manufacture_position="KEY_amplifier_manufacture_position";

    private Unbinder unbinder;

    static SparseIntArray mapAmplifierManufacturePosId = new SparseIntArray(7);

    public static AdministratorFragmentAmplifierManufacturer newInstance() {
        Bundle args = new Bundle();
        AdministratorFragmentAmplifierManufacturer fragment = new AdministratorFragmentAmplifierManufacturer();
        fragment.setArguments(args);
        return fragment;
    }

    void initSparseIntArray(){
        mapAmplifierManufacturePosId.put(0, R.id.settings_amplifier_manufacture_1);
        mapAmplifierManufacturePosId.put(1, R.id.settings_amplifier_manufacture_2);
        mapAmplifierManufacturePosId.put(2, R.id.settings_amplifier_manufacture_3);
        mapAmplifierManufacturePosId.put(3, R.id.settings_amplifier_manufacture_4);
        mapAmplifierManufacturePosId.put(4, R.id.settings_amplifier_manufacture_5);
        mapAmplifierManufacturePosId.put(5, R.id.settings_amplifier_manufacture_6);
        mapAmplifierManufacturePosId.put(6, R.id.settings_amplifier_manufacture_7);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initSparseIntArray();
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
                CustomSP.putInt(getContext(), KEY_amplifier_manufacture_position, mapAmplifierManufacturePosId.indexOfValue(checkedId));
                if (checkedId == R.id.settings_amplifier_manufacture_7){
                    CustomSP.putString(getContext(), KEY_amplifier_manufacture, manufactureCustomValue.getText().toString());
                }
                // TODO: 2017/10/23 设置命令

                // 退出
                getActivity().finish();
            }
        });

        int checkedId = CustomSP.getInt(getContext(),KEY_amplifier_manufacture_position, 0);
        manufactureGroup.check(mapAmplifierManufacturePosId.get(checkedId));
        if (checkedId == 6){
            manufactureCustomValue.setText(CustomSP.getString(getContext(),KEY_amplifier_manufacture,""));
        }
        /*
        switch (checkedId){
            case 6:
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
        */
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
