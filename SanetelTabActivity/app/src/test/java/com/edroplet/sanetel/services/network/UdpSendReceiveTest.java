package com.edroplet.sanetel.services.network;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2018/3/11.
 *
 * @author qxs
 */
public class UdpSendReceiveTest {
    @Test
    public void server() throws Exception {
        String [] source = {"1\r\n"};
        String [] target= {"2\r\n"};

        UdpSendReceive.server("0.0.0.0", 2000,"127.0.0.1",998, source,target);
    }

}