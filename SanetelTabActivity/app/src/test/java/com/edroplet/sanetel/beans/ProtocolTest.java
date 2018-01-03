package com.edroplet.sanetel.beans;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by qxs on 2018/1/3.
 */
public class ProtocolTest {
    @Test
    public void verifyData() throws Exception {
        String cmd = "$cmd,get system state*ff\r\n";
        String v = Protocol.verifyData(cmd);
        System.out.println(v);
        String command = cmd;
        int lastIndexStar = cmd.lastIndexOf('*');
        if (lastIndexStar > 0) {
            command = cmd.substring(0, lastIndexStar) + "*" + v + "\r\n";
        }
        System.out.println(command);
    }

}