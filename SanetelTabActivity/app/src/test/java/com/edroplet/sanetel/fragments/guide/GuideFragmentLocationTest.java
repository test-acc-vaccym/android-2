package com.edroplet.sanetel.fragments.guide;

import android.util.SparseIntArray;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2018/1/7.
 *
 * @author qxs
 */
public class GuideFragmentLocationTest {
    @Test
    public void getBundle() throws Exception {
        SparseIntArray sparseIntArray = new SparseIntArray(2);
        int[] ids = {2131296539,2131296540};
        int i = 0;
        for (int id : ids){
            sparseIntArray.put(i++, id);
        }
        int pos= sparseIntArray.indexOfValue(2131296540);
        if (pos < 0){
            System.out.println("nima:" + pos);
        }else {
            System.out.println("cheng gong");
        }
    }

}