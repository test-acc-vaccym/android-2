package com.sanetel.control.bean;

/**
 * Created by qxs on 2017/8/14.
 */

public class ServerInfo {
    private String ip;
    private String port;
    private String id;

    public ServerInfo(){}

    public ServerInfo( String ip, String port, String id)
    {
        this.ip=ip;
        this.port=port;
        this.id=id;
    }

    public String GetID()
    {
        return id;
    }

    public void SetID(String id)
    {
        this.id=id;
    }

    public void SetIP(String ip)
    {
        this.ip=ip;
    }

    public String GetIP()
    {
        return ip;
    }

    public void SetPort(String port)
    {
        this.port=port;
    }

    public String GetPort()
    {
        return port;
    }
}
