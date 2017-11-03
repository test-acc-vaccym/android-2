package com.edroplet.qxx.saneteltabactivity.beans;

import java.io.Serializable;

/**
 * Created by qxs on 2017/11/3.
 */

public class PresetAngleInfo implements Serializable {
    float azimuth;
    float pitch;
    float polarization;

    public float getAzimuth() {
        return azimuth;
    }

    public float getPitch() {
        return pitch;
    }

    public float getPolarization() {
        return polarization;
    }

    public PresetAngleInfo setAzimuth(float azimuth) {
        this.azimuth = azimuth;
        return this;
    }

    public PresetAngleInfo setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public PresetAngleInfo setPolarization(float polarization) {
        this.polarization = polarization;
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
