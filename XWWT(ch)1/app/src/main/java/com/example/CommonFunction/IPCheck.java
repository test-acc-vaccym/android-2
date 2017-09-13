package com.example.CommonFunction;

//IP地址检查 
public class IPCheck{
	   public static String matches(String text) {  
	        if (text != null && !text.isEmpty()) {  
	            // 定义正则表达式  
	            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";  
	            // 判断ip地址是否与正则表达式匹配  
	            if (text.matches(regex)) {  
	            // 返回判断信息  
	                return text + "\n是一个合法的IP地址！";  
	            } else {  
	            // 返回判断信息  
	              return text + "\n不是一个合法的IP地址！";  
	            }  
	        }  
	        // 返回判断信息  
	        return "请输入要验证的IP地址！";  
	    }  
	
}