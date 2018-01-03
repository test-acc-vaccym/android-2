// Code barrowed heavily from:
// Copyright (c) 2003-2011, Jodd Team (jodd.org). All Rights Reserved.

package com.edroplet.qxx.saneteltabactivity.utils.sscanf;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Sscanf {

	public static Object[] scan(String source, String format, Object... params) {
		List<Object>outs = new ArrayList<Object>();
		SscanfFormat sf = new SscanfFormat(source, format);
		
		for(Object param : params) {
			Object o = parse(sf, param);
			if(o == null)
				break;
			else
				outs.add(o);
		}
		return outs.toArray();
	}
	
	public static int scan2(String source, String format, Object params[]) {
		SscanfFormat sf = new SscanfFormat(source, format);
		int parseCount = 0;
		
		for(int i = 0; i < params.length; ++i) {
			params[i] = parse(sf, params[i]);
			if(params[i] == null)
				break;
			else
				++parseCount;
		}
		
		return parseCount;
	}
	
	
	private static Object parse(SscanfFormat sf, Object param) {
		if(!sf.prepareNextParseParam()) {
			return null;
		}
		Object o = null;
		
		if (param instanceof Number) {
			if (param instanceof Integer) {
				o = sf.parse((Integer) param);
			} else if (param instanceof Long) {
				o = sf.parse((Long) param);
			} else if (param instanceof Double) {
				o = param; //sf.parse((Double) param);
			} else if (param instanceof Float) {
				o = sf.parse((Float) param);
			} else {
				//o = sf.parse((Number)param);
			}
		} else if (param instanceof Character) {
			o = sf.parse((Character) param);
		} else {
			o = sf.parse(param.toString());
		}
		
		return o;
	}


	public static Object[] sscanf(String result, String format){
		Scanner scan = new Scanner(result);
		// 默认使用空格作为分割符来分隔文本，但允许你指定新的分隔符, 这里使用空格或逗号或点号作为分隔符
		scan.useDelimiter(" |,|\\.");
		scan.nextFloat();
		return Sscanf.scan(result, format);
	}
}
