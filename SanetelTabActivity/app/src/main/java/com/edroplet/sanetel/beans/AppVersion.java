/**
 * @author Leestar54 
 * http://www.cnblogs.com/leestar54
 */
package com.edroplet.sanetel.beans;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppVersion implements Serializable{
	private String appName;// app名称
	private String apkName;// apk名称
	private String versionName;// 给用户看的版本
	private int verCode;// 开发版本号
	private String url;// 下载地址
	private String content;// 描述
	private String sha1;// 验证完整性

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public int getVerCode() {
		return verCode;
	}

	public void setVerCode(int verCode) {
		this.verCode = verCode;
	}

	 //需要重写DefaultHandler的方法
	 public static class AppVersionHandler extends DefaultHandler {

		private List<AppVersion> appVersions;
		private AppVersion appVersion;
		private StringBuilder builder;

		//返回解析后得到的AppVersion对象集合
		public List<AppVersion> getAppVersion() {
			return appVersions;
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			appVersions = new ArrayList<AppVersion>();
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("appVersion")) {
				appVersion = new AppVersion();
			}
			builder.setLength(0);   //将字符长度设置为0 以便重新开始读取元素内的字符节点
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length);  //将读取的字符数组追加到builder中
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("apkName")) {
				appVersion.setApkName(builder.toString());
			} else if (localName.equals("appName")) {
				appVersion.setAppName(builder.toString());
			} else if (localName.equals("sha1")) {
				appVersion.setSha1(builder.toString());
			} else if (localName.equals("url")) {
				appVersion.setUrl(builder.toString());
			} else if (localName.equals("version")) {
				appVersion.setVersionName(builder.toString());
			} else if (localName.equals("code")) {
				appVersion.setVerCode(Integer.parseInt(builder.toString()));
			} else if (localName.equals("description")) {
				appVersion.setContent(builder.toString());
			} else if (localName.equals("appVersion")) {
				appVersions.add(appVersion);
			}
		}
	}

}
