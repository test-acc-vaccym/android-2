package com.jianchi.fsp.buddhismnetworkradio.api;

/**
 * Created by fsp on 16-7-21.
 */
public class Program {
    public String title;
    public String startTime;
    public String endTime;

    public Program(){}

    public Program(String title, String startTime, String endTime){
        this.title=title;
        this.startTime=startTime;
        this.endTime=endTime;
    }
}
