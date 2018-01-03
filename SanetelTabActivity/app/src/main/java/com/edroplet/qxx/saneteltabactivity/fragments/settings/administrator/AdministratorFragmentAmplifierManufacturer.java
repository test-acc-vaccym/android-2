package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
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
import com.edroplet.qxx.saneteltabactivity.utils.sscanf.Sscanf;
import com.edroplet.qxx.saneteltabactivity.view.BroadcastReceiverFragment;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomEditText;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 功放厂家
 */

public class AdministratorFragmentAmplifierManufacturer extends BroadcastReceiverFragment {
    public static final String AmplifierManufacturerAction = "com.edroplet.sanetel.AmplifierManufacturerAction";
    public static final String AmplifierManufacturerData = "com.edroplet.sanetel.AmplifierManufacturerData";

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.settings_amplifier_manufacture_group)
    CustomRadioGroupWithCustomRadioButton manufactureGroup;

    @BindView(R.id.settings_amplifier_manufacture_custom_value)
    CustomEditText manufactureCustomValue;

    @BindArray(R.array.amplifier_manufacture)
    String[] amplifierManufacture;

    Context context;

    int[] amplifierManufactureIds = {R.id.settings_amplifier_manufacture_1,R.id.settings_amplifier_manufacture_2,
            R.id.settings_amplifier_manufacture_3,R.id.settings_amplifier_manufacture_4,
            R.id.settings_amplifier_manufacture_5,R.id.settings_amplifier_manufacture_6,
            R.id.settings_amplifier_manufacture_7};

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
        int i = 0;
        for (int id: amplifierManufactureIds){
            mapAmplifierManufacturePosId.put(i++,id);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String []action = {AmplifierManufacturerAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        Protocol.sendMessage(context,Protocol.cmdGetBucFactory);
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

        context = getContext();
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = manufactureGroup.getCheckedRadioButtonId();
                String val = getString(R.string.settings_amplifier_manufacture_1);
                CustomSP.putInt(getContext(), KEY_amplifier_manufacture_position, mapAmplifierManufacturePosId.indexOfValue(checkedId));
                if (checkedId == R.id.settings_amplifier_manufacture_7){
                    val = manufactureCustomValue.getText().toString();
                    CustomSP.putString(getContext(), KEY_amplifier_manufacture, val);
                }else{
                    val = ((CustomRadioButton) view.findViewById(checkedId)).getText().toString();
                }
                // 设置命令
                Protocol.sendMessage(context, String.format(Protocol.cmdSetBucFactory,val));
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
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(AmplifierManufacturerData);

        String manufacture = getString(R.string.settings_amplifier_manufacture_1);
        Object[] o = Sscanf.scan(rawData,Protocol.cmdGetBucFactoryResult,manufacture);
        manufacture = (String) o[0];

        int pos = Arrays.asList(amplifierManufacture).indexOf(manufacture);
        if (pos == -1 || pos >= mapAmplifierManufacturePosId.size()){
            pos = mapAmplifierManufacturePosId.size() - 1;
        }
        manufactureGroup.check(mapAmplifierManufacturePosId.get(pos));
        if (pos == 6 ){
            manufactureCustomValue.setText(manufacture);
            CustomSP.putString(getContext(), KEY_amplifier_manufacture, manufacture);
        }
        CustomSP.putInt(getContext(), KEY_amplifier_manufacture_position, pos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
