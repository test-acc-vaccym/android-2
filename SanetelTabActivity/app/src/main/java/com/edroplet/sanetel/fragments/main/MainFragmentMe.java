package com.edroplet.sanetel.fragments.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.edroplet.sanetel.R;
import com.edroplet.sanetel.activities.main.MainMeAboutActivity;
import com.edroplet.sanetel.activities.main.MainMeAdviceActivity;
import com.edroplet.sanetel.activities.main.MainMeAppActivity;
import com.edroplet.sanetel.activities.main.MainMeErrorReportActivity;
import com.edroplet.sanetel.activities.main.MainMeLanguageActivity;
import com.edroplet.sanetel.utils.ChangeTypeFace;
import com.edroplet.sanetel.utils.CustomSP;
import com.edroplet.sanetel.services.network.SystemServices;
import com.edroplet.sanetel.view.custom.CustomButton;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

/**
 * Created by qxs on 2017/9/19.
 */

public class MainFragmentMe extends Fragment implements View.OnClickListener{
    private int[] languages = new int[]{R.string.main_bottom_nav_me_language_zh_cn, R.string.main_bottom_nav_me_language_english};
    private int[] fontsArray = new int[]{R.string.main_me_font_simhei, R.string.main_me_font_default};
    private RadioOnClick languageOnClick;
    private RadioOnClick fontOnClick;
    String[] areas = new String[2];
    String[] fonts = new String[2];
    private ListView areaListView;

    class RadioClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_me_font:
                    fontOnClick.setKey(CustomSP.globalFont);
                    AlertDialog adFont = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.main_me_font))
                            .setSingleChoiceItems(fonts, fontOnClick.getIndex(), fontOnClick)
                            .create();
                    areaListView = adFont.getListView();
                    adFont.show();
                    break;
                default:
                    languageOnClick.setKey(CustomSP.globalLanguage);
                    AlertDialog ad = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.main_bottom_nav_me_language))
                            .setSingleChoiceItems(areas, languageOnClick.getIndex(), languageOnClick)
                            .create();
                    areaListView = ad.getListView();
                    ad.show();
                    break;
            }
        }
    }
    class RadioOnClick implements DialogInterface.OnClickListener{
        private int index;
        private String spKey;
        public RadioOnClick(int index){
            this.index = index;
        }
        public void setKey(String key){
            this.spKey = key;
        }
        public void setIndex(int index){
            this.index=index;
        }
        public int getIndex(){
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton){
            setIndex(whichButton);
            Toast.makeText(getContext(), "您已经选择了 " +  ":" + ((AppCompatCheckedTextView)areaListView.getChildAt(index)).getText().toString(), Toast.LENGTH_LONG).show();
            if (spKey.equals(CustomSP.globalFont)){
                ChangeTypeFace.changeFont(getContext(), index);
            }
            CustomSP.putInt(getContext(),spKey,index);
            dialog.dismiss();
            SystemServices.restartAPP(getContext(), 1000);
        }
    }
    public static MainFragmentMe newInstance(String info) {
        Bundle args = new Bundle();
        MainFragmentMe fragment = new MainFragmentMe();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_me, null);
        Context context = getContext();
        int languageIndex = CustomSP.getInt(context, CustomSP.globalLanguage, 0);
        int fontIndex = CustomSP.getInt(context, CustomSP.globalFont, 1);

        languageOnClick = new RadioOnClick(languageIndex);
        fontOnClick = new RadioOnClick(fontIndex);

        CustomButton language = (CustomButton) view.findViewById(R.id.main_bottom_nav_me_language);
        CustomButton font = view.findViewById(R.id.main_me_font);
        areas[0]=getString(languages[0]);
        areas[1]=getString(languages[1]);
        fonts[0] = getString(fontsArray[0]);
        fonts[1] = getString(fontsArray[1]);

        language.setOnClickListener(new RadioClickListener());
        font.setOnClickListener(new RadioClickListener());

        view.findViewById(R.id.main_bottom_nav_me_version).setOnClickListener(this);
        view.findViewById(R.id.main_bottom_nav_me_error_report).setOnClickListener(this);
        view.findViewById(R.id.main_bottom_nav_me_advices).setOnClickListener(this);
        view.findViewById(R.id.main_bottom_nav_me_switch_device).setOnClickListener(this);
        view.findViewById(R.id.main_bottom_nav_me_about).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.main_bottom_nav_me_language:
                intent = new Intent(getActivity(), MainMeLanguageActivity.class);
                break;
            case R.id.main_bottom_nav_me_version:
                intent = new Intent(getContext(), MainMeAppActivity.class);
                break;
            case R.id.main_bottom_nav_me_error_report:
                intent = new Intent(getContext(), MainMeErrorReportActivity.class);
                break;
            case R.id.main_bottom_nav_me_advices:
                intent = new Intent(getContext(), MainMeAdviceActivity.class);
                break;
            case R.id.main_bottom_nav_me_switch_device:
                Context mContext = getContext();
                final NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(mContext);
                // 重点设置
                builder.withEffect(Effectstype.Fadein)        //设置对话框弹出样式
                        //.setCustomView(R.layout.custom, MainActivity.this) // 设置自定义对话框的布局
                        .withDuration(getResources().getInteger(R.integer.operate_confirm_animate_time))              //动画显现的时间（时间长就类似放慢动作）
                        // 基本设置
                        .withTitle(getString(R.string.operate_confirm_title))         //设置对话框标题
                        .withTitleColor(ContextCompat.getColor(mContext, R.color.operate_confirm_title_color))          //设置标题字体颜色
                        .withDividerColor(ContextCompat.getColor(mContext, R.color.operate_confirm_divider_color))      //设置分隔线的颜色
                        .withMessage(getString(R.string.main_me_switch_device_confirm))//设置对话框显示内容
                        .withMessageColor(ContextCompat.getColor(mContext, R.color.operate_confirm_message_color))       //设置消息字体的颜色
                        .withDialogColor(ContextCompat.getColor(mContext, R.color.operate_confirm_dialog_color))        //设置对话框背景的颜色
                        //.withIcon(getResources().getDrawable(R.drawable.logo)) //设置标题的图标
                        // 设置是否模态，默认false，表示模态，
                        //要求必须采取行动才能继续进行剩下的操作 | isCancelable(true)
                        .isCancelableOnTouchOutside(true)
                        .withButton1Text(getResources().getString(R.string.operate_confirm_ok))             //设置按钮1的文本
                        .withButton2Text(getResources().getString(R.string.operate_confirm_cancel))         //设置按钮2的文本
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // TODO 切换设备
                                SystemServices.startWifiManager(getActivity(),
                                        SystemServices.REQUEST_WIFI_CONNECT_MANAGER);
                                builder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), v.getContext().getString(R.string.operate_confirm_cancel_toast), Toast.LENGTH_SHORT).show();
                                builder.dismiss();
                            }
                        })
                        .show();
                break;
            default:
            case R.id.main_bottom_nav_me_about:
                intent = new Intent(getContext(), MainMeAboutActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
            // getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
