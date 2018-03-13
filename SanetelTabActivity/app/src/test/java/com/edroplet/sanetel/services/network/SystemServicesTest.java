package com.edroplet.sanetel.services.network;

import org.junit.Test;

import static com.edroplet.sanetel.services.network.SystemServices.XWWT_PREFIX;
import static org.junit.Assert.*;

/**
 * Created on 2018/3/13.
 *
 * @author qxs
 */
public class SystemServicesTest {
    @Test
    public void checkConnectedSsid() throws Exception {
        boolean a =  "XWWT-P120-2018".startsWith(XWWT_PREFIX);
        System.out.println(a);
        boolean b =  "XWWT-P120-2018".substring(0,5).equalsIgnoreCase(XWWT_PREFIX);
        System.out.println(b);
    }

}