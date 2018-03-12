package com.example.tcpdemo;

public class CommandInfoCache {

	private int id = -1;
	private String name = "";
	private String mac = "";
	private String data = "";
	
	public CommandInfoCache() {
		
	}
	
	public CommandInfoCache(int i,String n,String m,String d) {
		this.id = i;
		this.name = n;
		this.mac = m;
		this.data = d;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
	
}
