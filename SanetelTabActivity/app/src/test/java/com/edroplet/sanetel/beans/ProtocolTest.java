package com.edroplet.sanetel.beans;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by qxs on 2018/1/3.
 */
public class ProtocolTest {
    @Test
    public void verifyData() throws Exception {
        System.out.print(Protocol.verifyData("$cmd,get system state*ff\r\n"));
    }

}