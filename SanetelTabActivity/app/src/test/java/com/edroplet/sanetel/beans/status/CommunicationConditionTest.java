package com.edroplet.sanetel.beans.status;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2018/2/12.
 *
 * @author qxs
 */
public class CommunicationConditionTest {
    @Test
    public void testGetValue(){
        System.out.println("12: " + String.valueOf(CommunicationCondition.getValue("001100", 1, 2)));
        System.out.println("23: " + String.valueOf(CommunicationCondition.getValue("001100", 2, 3)));
    }
}