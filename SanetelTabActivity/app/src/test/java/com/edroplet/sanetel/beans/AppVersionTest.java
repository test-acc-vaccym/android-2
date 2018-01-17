package com.edroplet.sanetel.beans;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2018/1/16.
 *
 * @author qxs
 */
public class AppVersionTest {
    @Test
    public void compareVersion() throws Exception {
        assertEquals(AppVersion.compareVersion("V2.2.3","v12.3"), -1);
        assertEquals(AppVersion.compareVersion("V2.2.3","v12U.3"), -1);
        assertEquals(AppVersion.compareVersion("V23u.2.3","v12.3"), 1);
        assertEquals(AppVersion.compareVersion("V23u.2.3u","v12.3u"), 1);
        assertEquals(AppVersion.compareVersion("V23u.2.3","v23u.3.4"), -1);
    }

}