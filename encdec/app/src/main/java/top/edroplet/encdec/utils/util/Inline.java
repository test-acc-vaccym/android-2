package top.edroplet.encdec.utils.util;

/**
 * Created by xw on 2016/10/27.
 */

public class Inline {
    public static final String getLineNoFileName(){
        return "["+Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]";
    }
}
