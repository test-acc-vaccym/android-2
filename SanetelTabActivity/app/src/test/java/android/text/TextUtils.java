package android.text;

import android.support.annotation.NonNull;

/**
 * Created by qxs on 2017/11/25.
 */

public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }
}
