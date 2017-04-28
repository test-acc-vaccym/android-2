package top.edroplet.encdec.utils;

import java.util.ArrayList;

/**
 * Created by xw on 2017/3/6.
 */

public class syncTaskResponseData {
    private ArrayList<String> keyList;
    private textCache tc;

    public syncTaskResponseData(ArrayList keyList, textCache tc) {
        this.keyList = keyList;
        this.tc = tc;
    }

    public void setTextCache(textCache tc) {
        this.tc = tc;
    }

    public textCache getTc() {
        return this.tc;
    }

    public ArrayList<String> getKeyList() {
        return this.keyList;
    }

    public void setKeyList(ArrayList keyList) {
        this.keyList = keyList;
    }

}

