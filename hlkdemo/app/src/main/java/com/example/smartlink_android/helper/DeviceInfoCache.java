package com.example.smartlink_android.helper;

import java.io.Serializable;

public class DeviceInfoCache implements Serializable{

	private String mac = "";
	private String ip = "";
	private String name = "";
	private String verType = "";
	private String maxVerNumber = "";
	private String custom = "";
	private String time = "";
	
	public DeviceInfoCache() {
		
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
		String strInfo = "";
		if (verType.equalsIgnoreCase("a")) {
			strInfo += "airkiss普通,";
		}
		else if (verType.equalsIgnoreCase("b")) {
			strInfo += "elian普通,";
		}
		else if (verType.equalsIgnoreCase("c")) {
			strInfo += "airkiss微信,";
		}
		else if (verType.equalsIgnoreCase("d")) {
			strInfo += "airkiss云智易,";
		}
		
		if (custom.equals("1")) {
			strInfo += "非定制版";
		}
		else if (custom.equals("2")) {
			strInfo += "定制版";
		}
		else if (custom.equals("4")) {
			strInfo += "自动生成版";
		}
		
		if (strInfo.substring(strInfo.length() - 1).equals(","))
			strInfo = strInfo.substring(0, strInfo.length() - 1);
		
		strInfo += "";
		
		return strInfo;
	}
	
}
