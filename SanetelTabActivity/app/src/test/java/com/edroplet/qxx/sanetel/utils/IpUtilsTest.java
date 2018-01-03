package com.edroplet.qxx.sanetel.utils;

import org.junit.Test;

/**
 * Created by qxs on 2018/1/3.
 */
public class IpUtilsTest {
    @Test
    public void getLowAddr() throws Exception {
        String ip = IpUtils.getLowAddr("192.168.1.111","255.255.0.0");
        System.out.println(ip);
    }

}