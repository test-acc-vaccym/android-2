package com.example.CommonFunction;

//IP��ַ��� 
public class IPCheck{
	   public static String matches(String text) {  
	        if (text != null && !text.isEmpty()) {  
	            // ����������ʽ  
	            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";  
	            // �ж�ip��ַ�Ƿ���������ʽƥ��  
	            if (text.matches(regex)) {  
	            // �����ж���Ϣ  
	                return text + "\n��һ���Ϸ���IP��ַ��";  
	            } else {  
	            // �����ж���Ϣ  
	              return text + "\n����һ���Ϸ���IP��ַ��";  
	            }  
	        }  
	        // �����ж���Ϣ  
	        return "������Ҫ��֤��IP��ַ��";  
	    }  
	
}