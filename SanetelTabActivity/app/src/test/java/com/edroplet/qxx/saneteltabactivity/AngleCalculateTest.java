package com.edroplet.qxx.saneteltabactivity;

import com.edroplet.qxx.saneteltabactivity.utils.AngleCalculate;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by qxs on 2017/11/23.
 */
public class AngleCalculateTest {
    @Test
    public void pitch_isCorrect() throws Exception {
        AngleCalculate angleCalculate = new AngleCalculate();
        double res = angleCalculate.getPitch(180.0, 116.3, 39.7);
        System.out.println(res);
        assertEquals(res, 11.4,0.1);
    }

    @Test
    public void azimuth_isCorrect() throws Exception {
        AngleCalculate angleCalculate = new AngleCalculate();
        double res = angleCalculate.getAzimuth(180.0, 116.3, 39.7);
        System.out.println(res);
        assertEquals(res, 107.6,0.1);
    }

    @Test
    public void polarization_isCorrect() throws Exception {
        AngleCalculate angleCalculate = new AngleCalculate();
        double res = angleCalculate.getPolarization(180.0, 116.3, 39.7);
        System.out.println(res);
        assertEquals(res, 47.2,0.1);
    }
}