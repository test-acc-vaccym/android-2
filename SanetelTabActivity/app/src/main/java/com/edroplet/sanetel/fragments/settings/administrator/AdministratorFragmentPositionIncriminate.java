package com.edroplet.sanetel.fragments.settings.administrator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.beans.Protocol;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.utils.InputFilterFloat;
import com.edroplet.sanetel.utils.PopDialog;
import com.edroplet.sanetel.utils.sscanf.Sscanf;
import com.edroplet.sanetel.view.BroadcastReceiverFragment;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.edroplet.sanetel.view.custom.CustomEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qxs on 2017/9/19.
 * 位置标定
 */

public class AdministratorFragmentPositionIncriminate extends BroadcastReceiverFragment {
    private static final String PositionIncriminateFoldKey = "PositionIncriminateFold";
    private static final String PositionIncriminateExplodeKey = "PositionIncriminateExplode";
    public static final String PositionIncriminateAction = "com.edroplet.sanetel.PositionIncriminateAction";
    public static final String PositionIncriminateData = "com.edroplet.sanetel.PositionIncriminateData";
    private static final int[] icons = {R.drawable.antenna_exploded};

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.administrator_position_incriminate_et_fold)
    CustomEditText PositionIncriminateFold;
    @BindView(R.id.administrator_position_incriminate_et_explode)
    CustomEditText PositionIncriminateExplode;

    Unbinder unbinder;

    String foldAngle="0.0", explodeAngle="0.0";
    Context context;

    public static AdministratorFragmentPositionIncriminate newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                       String secondLine, boolean showThird, String thirdLineStart,
                                                                       int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentPositionIncriminate fragment = new AdministratorFragmentPositionIncriminate();
        args.putBoolean(PopDialog.SHOW_FIRST,showFirst);
        args.putString(PopDialog.FIRST, firstLine);
        args.putBoolean(PopDialog.SHOW_SECOND,showSecond);
        args.putString(PopDialog.SECOND, secondLine);
        args.putBoolean(PopDialog.SHOW_THIRD,showThird);
        args.putString(PopDialog.START, thirdLineStart);
        args.putInt(PopDialog.ICON, icon);
        args.putString(PopDialog.BUTTON_TEXT, buttonText);
        args.putString(PopDialog.END, thirdLineEnd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        String[] action = {PositionIncriminateAction};
        setAction(action);
        super.onCreate(savedInstanceState);
        context = getContext();
        Protocol.sendMessage(context, Protocol.cmdGetLift);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_incriminate, null);
        if (view == null){
            return null;
        }
        context = getContext();
        unbinder = ButterKnife.bind(this, view);

        PositionIncriminateFold.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.azimuthMin,InputFilterFloat.latitudeMax,InputFilterFloat.incriminateValidBit)});
        PositionIncriminateExplode.setFilters(new InputFilter[]{new InputFilterFloat(InputFilterFloat.pitchMin,InputFilterFloat.latitudeMax,InputFilterFloat.incriminateValidBit)});

        foldAngle = CustomSP.getString(context,PositionIncriminateFoldKey, "");
        PositionIncriminateFold.setText(foldAngle);
        explodeAngle = CustomSP.getString(context,PositionIncriminateExplodeKey, "");
        PositionIncriminateExplode.setText(explodeAngle);

        thirdButton = view.findViewById(R.id.pop_dialog_third_button);

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foldAngle = PositionIncriminateFold.getText().toString();
                CustomSP.putString(context,PositionIncriminateFoldKey,foldAngle);
                explodeAngle = PositionIncriminateExplode.getText().toString();
                CustomSP.putString(context,PositionIncriminateExplodeKey,explodeAngle);
                // send command
                // 方位,俯仰，备用，极化
                Protocol.sendMessage(context, String.format(Protocol.cmdSetLift,foldAngle, explodeAngle));
                getActivity().finish();
            }
        });

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);

            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon >= 0) {
                popDialog.setDrawable(ContextCompat.getDrawable(context,icons[0]));
            }
        }
        return popDialog.show();
    }

    @Override
    public void processData(Intent intent) {
        super.processData(intent);
        String rawData = intent.getStringExtra(PositionIncriminateData);
        Object[] o = Sscanf.scan(rawData,Protocol.cmdGetCalibAntResult,foldAngle,explodeAngle);
        foldAngle = (String) o[0];
        explodeAngle  = (String) o[1];

        if (foldAngle != null && foldAngle.length() > 0) {
            PositionIncriminateFold.setText(foldAngle);
            CustomSP.putString(context, PositionIncriminateFoldKey, foldAngle);
        }
        if (foldAngle != null && foldAngle.length() > 0) {
            PositionIncriminateExplode.setText(explodeAngle);
            CustomSP.putString(context, PositionIncriminateExplodeKey, explodeAngle);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
    }
}
