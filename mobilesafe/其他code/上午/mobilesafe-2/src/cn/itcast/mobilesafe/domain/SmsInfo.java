package cn.itcast.mobilesafe.domain;

public class SmsInfo {

	private String address;
	private String date;
	private String type;
	private String body;
	
	
	public SmsInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SmsInfo(String address, String date, String type, String body) {
		super();
		this.address = address;
		this.date = date;
		this.type = type;
		this.body = body;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "SmsInfo [address=" + address + ", date=" + date + ", type="
				+ type + ", body=" + body + "]";
	}
	
	
}
