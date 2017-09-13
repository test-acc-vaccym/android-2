package com.edroplet.qxs.wificommunication.bean;

/**
 * Created by qxx on 2017/7/24.
 */
public class DeviceBean {
    public String message;
    public boolean isReceive;

    public DeviceBean(String msg, boolean isReceive) {
        this.message = msg;
        this.isReceive = isReceive;
    }
}
