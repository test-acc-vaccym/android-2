package com.example.tcpdemo.smartlink;

import java.io.Serializable;

public class DeviceInfoCache implements Serializable{

	private String mac = "";
	private String ip = "";
	private String name = "";
	private String verType = "";
	private String maxVerNumber = "";
	private String custom = "";
	private String time = "";
	private boolean isConnectiong = false;
	private String verInfo = "";
	public DeviceInfoCache() {
		
	}
	
	public boolean isConnectiong() {
		return isConnectiong;
	}

	public void setConnectiong(boolean isConnectiong) {
		this.isConnectiong = isConnectiong;
	}

	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVerType() {
		return verType;
	}

	public void setVerType(String verType) {
		this.verType = verType;
	}

	public String getMaxVerNumber() {
		return maxVerNumber;
	}

	public void setMaxVerNumber(String maxVerNumber) {
		this.maxVerNumber = maxVerNumber;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getDeviceInfo() {
		
		return verInfo;
	}
	
	public void setVerInfo(String info) {
		
		this.verInfo = info;
	}
	
}
