package com.edroplet.qxx.saneteltabactivity.fragments.settings.administrator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.edroplet.qxx.saneteltabactivity.R;
import com.edroplet.qxx.saneteltabactivity.beans.Protocol;
import com.edroplet.qxx.saneteltabactivity.utils.CustomSP;
import com.edroplet.qxx.saneteltabactivity.utils.PopDialog;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioButton;
import com.edroplet.qxx.saneteltabactivity.view.custom.CustomRadioGroupWithCustomRadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qxs on 2017/9/19.
 * 寻星范围
 */

public class AdministratorFragmentSearchingRange extends Fragment {
    private static final String SearchingRangeKey = "searchingRange";
    private  final int[] icons = {R.drawable.antenna_exploded };

    @BindView(R.id.pop_dialog_third_button)
    CustomButton thirdButton;

    @BindView(R.id.administrator_setting_searching_range_radio_group)
    RadioGroup searchRangeGroup;

    private String selected;

    int [] ids = {R.id.administrator_setting_searching_range_1, R.id.administrator_setting_searching_range_2, R.id.administrator_setting_searching_range_3, R.id.administrator_setting_searching_range_4, R.id.administrator_setting_searching_range_5};

    int [] values = {20,25,30,35,45};

    static SparseIntArray searchRangeArray = new SparseIntArray(5);

    public static AdministratorFragmentSearchingRange newInstance(boolean showFirst, String firstLine, boolean showSecond,
                                                                  String secondLine, boolean showThird, String thirdLineStart,
                                                                  int icon, String buttonText, String thirdLineEnd) {
        Bundle args = new Bundle();
        AdministratorFragmentSearchingRange fragment = new AdministratorFragmentSearchingRange();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_administrator_settings_searching_range, null);
        if (view == null){
            return null;
        }

        ButterKnife.bind(this,view);

        for (int i = 0; i < ids.length; i++){
            searchRangeArray.put(i, ids[i]);
        }
        final Context context = getContext();
        int position = CustomSP.getInt(context, SearchingRangeKey, 0);
        searchRangeGroup.check(searchRangeArray.get(position));

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = searchRangeArray.indexOfValue(searchRangeGroup.getCheckedRadioButtonId());
                CustomSP.putInt(getContext(), SearchingRangeKey, index);
                // 5.4 send command
                Protocol.sendMessage(context, String.format(Protocol.cmdSetSearchRange, values[index], "0.0","0.0","0.0"));
                getActivity().finish();
            }
        });

        PopDialog popDialog = new PopDialog();
        popDialog.setView(view);
        popDialog.setContext(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            popDialog.setBundle(bundle);
            popDialog.setSetFirstColor(true);

            int icon = bundle.getInt(PopDialog.ICON, -1);
            if (icon >= 0) {
                popDialog.setDrawable(ContextCompat.getDrawable(getContext(),icons[0]));
            }
        }
        popDialog.show();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
