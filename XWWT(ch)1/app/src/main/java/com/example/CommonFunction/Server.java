package com.example.CommonFunction;

public class Server
{
	private String ip;
	private String port;
	private Integer id;
	
	public Server()
	{}
	
	public Server(Integer id,String ip,String port)
	{
		this.ip=ip;
		this.port=port;
		this.id=id;
	}
	
	public Integer GetID()
	{
		return id;
	}
	
	public void SetID(Integer id)
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