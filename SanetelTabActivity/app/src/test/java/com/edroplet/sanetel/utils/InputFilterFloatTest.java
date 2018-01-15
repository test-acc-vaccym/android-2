package com.edroplet.sanetel.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2018/1/15.
 *
 * @author qxs
 */
public class InputFilterFloatTest {
    @Test
    public void isInRange() throws Exception {
        boolean inRange = InputFilterFloat.isInRange(6000,30000,1111);
        assertEquals(inRange, false);
    }

}