package com.edroplet.qxx.bluetoothcommunication;

/**
 * Created by qxx on 2017/7/24.
 */
public class DeviceBean {
    protected String message;
    protected boolean isReceive;

    public DeviceBean(String msg, boolean isReceive) {
        this.message = msg;
        this.isReceive = isReceive;
    }
}
