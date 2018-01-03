package com.edroplet.sanetel.utils;

import android.content.Context;

/**
 * Created by qxs on 2017/11/23.
 */

public class AngleCalculate {
//    Context context;
//    AngleCalculate(Context context){
//        this.context = context;
//    }

    /**
     *
     * @param satelliteLongitude
     * @param longitude
     * @param latitude
     * @return
     */
    private double getSouthDeclinationAngle(double satelliteLongitude, double longitude, double latitude){
        // =-DEGREES(ATAN(TAN(RADIANS(C9-D9))/SIN(RADIANS(E9))))
        return -1 * Math.toDegrees(Math.atan(Math.tan(Math.toRadians(satelliteLongitude - longitude))/Math.sin(Math.toRadians(latitude))));
    }

    public double getAzimuth(double satelliteLongitude, double longitude, double latitude){
        return 180 + getSouthDeclinationAngle(satelliteLongitude, longitude, latitude);
    }

    public double getPitch(double satelliteLongitude, double longitude, double latitude){
        // =DEGREES(ATAN((COS(RADIANS(ABS(C9-D9)))*COS(RADIANS(E9))-0.1513)/SQRT(1-(COS(RADIANS(ABS(C9-D9)))^2)*(COS(RADIANS(E9))^2))))
        double delta = Math.abs(satelliteLongitude - longitude);
        double cosDelta = Math.cos(Math.toRadians(delta));
        double cosLatitude = Math.cos(Math.toRadians(latitude));
        return Math.toDegrees(Math.atan(( cosDelta * cosLatitude - 0.1513 )/ Math.sqrt(1- (cosDelta * cosDelta) * cosLatitude * cosLatitude)));
    }

    public double getPolarization(double satelliteLongitude, double longitude, double latitude){
        return Math.toDegrees(Math.atan(Math.sin(Math.toRadians(satelliteLongitude - longitude))/Math.tan(Math.toRadians(latitude))));
    }
}
