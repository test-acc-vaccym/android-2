package com.edroplet.sanetel.activities.main;

import com.edroplet.sanetel.beans.AppVersion;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2018/1/16.
 *
 * @author qxs
 */
public class HttpDownloaderTaskTest {
    @Test
    public void testVersionCompare(){
        String s1 = "1.2.3.4";
        String s2 = "1.12.3.4";
        boolean isNew = AppVersion.compareVersion(s2,s1) > 0;
        assertEquals(isNew, true);
    }

}