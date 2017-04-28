package top.edroplet.encdec.utils;

import android.util.LruCache;

/**
 * Created by xw on 2017/4/28.
 */

public class textCache extends LruCache<String, String> {
    public textCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public int sizeOf(String key, String value) {
        // TODO: Implement this method
        return super.sizeOf(key, value);
    }
}
