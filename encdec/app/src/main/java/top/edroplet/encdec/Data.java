package top.edroplet.encdec;

import android.util.LruCache;

import java.util.ArrayList;

/**
 * Created by xw on 2017/3/6.
 */

public class Data {

}

class syncTaskResponseData {
    private ArrayList<String> keyList;
    private textCache tc;

    syncTaskResponseData(ArrayList keyList, textCache tc) {
        this.keyList = keyList;
        this.tc = tc;
    }

    void setTextCache(textCache tc) {
        this.tc = tc;
    }

    textCache getTc() {
        return this.tc;
    }

    ArrayList<String> getKeyList() {
        return this.keyList;
    }

    void setKeyList(ArrayList keyList) {
        this.keyList = keyList;
    }

}


class textCache extends LruCache<String, String> {
    textCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, String value) {
        // TODO: Implement this method
        return super.sizeOf(key, value);
    }

}
