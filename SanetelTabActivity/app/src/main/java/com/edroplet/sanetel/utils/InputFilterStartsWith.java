package com.edroplet.sanetel.utils;

import android.text.InputFilter;
import android.text.Spanned;

import static com.edroplet.sanetel.utils.SystemServices.XWWT_PREFIX;

/**
 * Created by qxs on 2017/10/23.
 */

public class InputFilterStartsWith implements InputFilter {
    private String startWith;

    public InputFilterStartsWith(String startString) {
        this.startWith = startString;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String inputString = dest.toString() + source.toString();
            int inputLength = inputString.length();
            int prefixLength = startWith.length();
            for (int i = 0; i < prefixLength; i++) {
                if (inputString.length() >= i+1 && !inputString.substring(0, i).equals(startWith.substring(0, i))) {
                    return "";
                }
            }
            return null;
    }

}
