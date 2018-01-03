package com.edroplet.sanetel.utils.gui;


import com.edroplet.sanetel.utils.fenglu.util.FileEncodingConverter;
import com.edroplet.sanetel.utils.fenglu.util.JavaSouceFilePickup;

public class FileEncodingThread implements Runnable {
	private String rootPath;
	public FileEncodingThread(String rootPath){
		this.rootPath = rootPath;
	}
	private FileEncodingConverter converter;
	
	@Override
	public void run() {
		converter.toUTF8(JavaSouceFilePickup.pickup(rootPath));
	}
	
	public FileEncodingConverter getConverter() {
		return converter;
	}
	public void setConverter(FileEncodingConverter converter) {
		this.converter = converter;
	}


}
