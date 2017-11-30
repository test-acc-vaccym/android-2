package com.edroplet.qxx.saneteltabactivity;

import com.edroplet.qxx.saneteltabactivity.utils.ConvertUtil;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void bitGetTest() throws Exception {
        String s = Integer.toBinaryString(2);
        System.out.println(s);
        String c = String.valueOf(s.charAt(s.length() - 1 - 0));
        System.out.println(c);
        assertEquals("0", c);
    }

    @Test
    public void getBitInt() throws Exception {
        int i = ConvertUtil.getBitValue(5, 4, 0);
        assertEquals(1,i);
    }

    @Test
    public void getBitString() throws Exception {
        int i = ConvertUtil.getBitValue("5", 0, 0);
        assertEquals(1,i);
    }

    @Test
    public void formatString() throws  Exception {
        String s = String.format("%s: %d","he is", 9);
        assertEquals("he is: 9", s);
    }
}