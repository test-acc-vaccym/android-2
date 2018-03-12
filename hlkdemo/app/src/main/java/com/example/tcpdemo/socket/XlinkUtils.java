package com.example.tcpdemo.socket;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;

public class XlinkUtils {
    /**
     * Map 转换为json
     * 
     * @param map
     * @return
     */
    public static JSONObject getJsonObject(Map<String, Object> map) {
	JSONObject jo = new JSONObject();
	Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
	while (iter.hasNext()) {
	    Entry<String, Object> entry = iter.next();
	    try {
		jo.put(entry.getKey(), entry.getValue());
	    } catch (JSONException e) {
		e.printStackTrace();
	    }
	}
	return jo;

    }

    /**
     * BASE64解密
     * 
     * @param key
     * @return
     * @throws IOException
     */
    public static byte[] base64Decrypt(String key) {
	byte[] bs = Base64.decode(key, Base64.DEFAULT);
	if (bs == null || bs.length == 0) {
	    bs = key.getBytes();
	}
	return bs;
    }

    public static String getHexBinString(byte[] bs) {
	StringBuffer log = new StringBuffer();
	for (int i = 0; i < bs.length; i++) {
	    log.append(String.format("%02X", (byte) bs[i]) + " ");
	}
	return log.toString();
    }

    /**
     * 把byte转化�?二进�?
     * 
     * @param aByte
     * @return
     */
    public static String getBinString(byte aByte) {
	String out = "";
	int i = 0;
	for (i = 0; i < 8; i++) {
	    int v = (aByte << i) & 0x80;
	    v = (v >> 7) & 1;
	    out += v;
	}
	return out;
    }

    static private final int bitValue0 = 0x01; // 0000 0001
    static private final int bitValue1 = 0x02; // 0000 0010
    static private final int bitValue2 = 0x04; // 0000 0100
    static private final int bitValue3 = 0x08; // 0000 1000
    static private final int bitValue4 = 0x10; // 0001 0000
    static private final int bitValue5 = 0x20; // 0010 0000
    static private final int bitValue6 = 0x40; // 0100 0000
    static private final int bitValue7 = 0x80; // 1000 0000

    /**
     * 设置flags
     * 
     * @param index
     *            第几个bit，从零开始排
     * @param value
     *            byte�?
     * @return
     */
    public static byte setByteBit(int index, byte value) {
	if (index > 7) {
	    throw new IllegalAccessError("setByteBit error index>7!!! ");
	}
	byte ret = value;
	if (index == 0) {
	    ret |= bitValue0;
	} else if (index == 1) {
	    ret |= bitValue1;
	} else if (index == 2) {
	    ret |= bitValue2;
	} else if (index == 3) {
	    ret |= bitValue3;
	} else if (index == 4) {
	    ret |= bitValue4;
	} else if (index == 5) {
	    ret |= bitValue5;
	} else if (index == 6) {
	    ret |= bitValue6;
	} else if (index == 7) {
	    ret |= bitValue7;
	}
	return ret;
    }

    /**
     * 读取 flags 里的小bit
     * 
     * @param anByte
     * @param index
     * @return
     */
    public static boolean readFlagsBit(byte anByte, int index) {
	if (index > 7) {
	    throw new IllegalAccessError("readFlagsBit error index>7!!! ");
	}
	int temp = anByte << (7 - index);
	temp = temp >> 7;
	temp &= 0x01;
	if (temp == 1) {
	    return true;
	}
	// if((anByte & (01<<index)) !=0){
	// return true;
	// }
	return false;
    }

    /**
     * �?6位的short转换成byte数组
     * 
     * @param s
     *            short
     * @return byte[] 长度�?
     * */
    public static byte[] shortToByteArray(short s) {
	byte[] targets = new byte[2];
	for (int i = 0; i < 2; i++) {
	    int offset = (targets.length - 1 - i) * 8;
	    targets[i] = (byte) ((s >>> offset) & 0xff);
	}
	return targets;
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T getAdapterView(View convertView, int id) {
	SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
	if (viewHolder == null) {
	    viewHolder = new SparseArray<View>();
	    convertView.setTag(viewHolder);
	}
	View childView = viewHolder.get(id);
	if (childView == null) {
	    childView = convertView.findViewById(id);
	    viewHolder.put(id, childView);
	}
	return (T) childView;
    }

    public final static String MD5(String s) {
	char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F' };
	try {
	    byte[] btInput = s.getBytes();
	    // 获得MD5摘要算法�?MessageDigest 对象
	    MessageDigest mdInst = MessageDigest.getInstance("MD5");
	    // 使用指定的字节更新摘�?
	    mdInst.update(btInput);
	    // 获得密文
	    byte[] md = mdInst.digest();
	    // 把密文转换成十六进制的字符串形式
	    int j = md.length;
	    char str[] = new char[j * 2];
	    int k = 0;
	    for (int i = 0; i < j; i++) {
		byte byte0 = md[i];
		str[k++] = hexDigits[byte0 >>> 4 & 0xf];
		str[k++] = hexDigits[byte0 & 0xf];
	    }
	    return new String(str);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * BASE64加密
     * 
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public static String base64EncryptUTF(byte[] key)
	    throws UnsupportedEncodingException {
	return new String(Base64.encode(key, Base64.DEFAULT), "UTF-8");
    }

    public static String base64Encrypt(byte[] key) {
	return new String(Base64.encode(key, Base64.DEFAULT));
    }


    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
	Intent intent = null;
	if (android.os.Build.VERSION.SDK_INT > 10) {
	    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
	} else {
	    intent = new Intent("/");
	    ComponentName cm = new ComponentName("com.android.settings",
		    "com.android.settings.WirelessSettings");
	    intent.setComponent(cm);
	    intent.setAction("android.intent.action.VIEW");
	}
	activity.startActivityForResult(intent, 0);
    }
    
    
    /**
     * 验证字符串是否不为空：全为空白字符\"NULL"\"null"也算为空 方法�? </br> 详述: </br> �?��人员: liangxiaxu
     * </br> 创建时间: 2013-5-27 </br>
     * 
     * @param string
     * @return
     */
    public static boolean isNotNull(String string) {
	if ((string != null) && (!"null".equalsIgnoreCase(string))
		&& (string.length() > 0) && (!string.trim().equals(""))) {
	    return true;
	} else {
	    return false;
	}
    }
    
    /**
	 * 将字符串转换成二进制字符�?
	 * @param str
	 * @return
	 */
	 public static String hexString2binaryString(String hexString)  
    {  
        if (hexString == null || hexString.length() % 2 != 0)  
            return null;  
        String bString = "", tmp;  
        for (int i = 0; i < hexString.length(); i++)  
        {  
            tmp = "0000"  
                    + Integer.toBinaryString(Integer.parseInt(hexString  
                            .substring(i, i + 1), 16));  
            bString += tmp.substring(tmp.length() - 4)+"";  
        }  
        return bString;  
    }
	 
	/**
	 * 二进制转十六进制
	 * @param a
	 * @return
	 */
	 public static String BToH(String a) {
	 // 将二进制转为十进制再从十进制转为十六进制
	 String b = Integer.toHexString(Integer.valueOf(toD(a, 2)));
	 return b;
	 }
	 
	// 任意进制数转为十进制数
	 public static String toD(String a, int b) {
	 int r = 0;
	 for (int i = 0; i < a.length(); i++) {
	 r = (int) (r + formatting(a.substring(i, i + 1))* Math.pow(b, a.length() - i - 1));
	 }
	 return String.valueOf(r);
	 }
	 
	// 将十六进制中的字母转为对应的数字
	 public static int formatting(String a) {
	 int i = 0;
	 for (int u = 0; u < 10; u++) {
	 if (a.equals(String.valueOf(u))) {
	 i = u;
	 }
	 }
	 if (a.equals("a")) {
	 i = 10;
	 }
	 if (a.equals("b")) {
	 i = 11;
	 }
	 if (a.equals("c")) {
	 i = 12;
	 }
	 if (a.equals("d")) {
	 i = 13;
	 }
	 if (a.equals("e")) {
	 i = 14;
	 }
	 if (a.equals("f")) {
	 i = 15;
	 }
	 return i;
	 }
	 
	/**
	 *  将十进制中的数字转为十六进制对应的字母
	 * @param a
	 * @return
	 */
	 public static String formattingH(int a) {
		 String i = String.valueOf(a);
		 switch (a) {
		 case 10:
			 i = "a";
		 break;
		 case 11:
			 i = "b";
		 break;
		 case 12:
			 i = "c";
		 break;
		 case 13:
			 i = "d";
		 break;
		 case 14:
			 i = "e";
		 break;
		 case 15:
			 i = "f";
		 break;
		 }
		 return i;
	 }
	 
	 /**
		 * 字节转16进制字符串
		 * @param src
		 * @return
		 */
		public static String bytesToHexString(byte[] src){  
	        StringBuilder stringBuilder = new StringBuilder("");  
	        if (src == null || src.length <= 0) {  
	            return null;  
	        }  
	        for (int i = 0; i < src.length; i++) {  
	            int v = src[i] & 0xFF;  
	            String hv = Integer.toHexString(v);  
	            if (hv.length() < 2) {  
	                stringBuilder.append(0);  
	            }  
	            stringBuilder.append(hv);  
	        }  
	        return stringBuilder.toString();  
	    }

		/** 
	     * Convert hex string to byte[] 
	     * @param hexString the hex string 
	     * @return byte[] 
	     */  
	    public static byte[] hexStringToBytes(String hexString) {  
	        if (hexString == null || hexString.equals("")) {  
	            return null;  
	        }  
	        hexString = hexString.toUpperCase();  
	        int length = hexString.length() / 2;  
	        char[] hexChars = hexString.toCharArray();  
	        byte[] d = new byte[length];  
	        for (int i = 0; i < length; i++) {  
	            int pos = i * 2;  
	            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	        }  
	        return d;  
	    }  
	    
	    /** 
	     * Convert char to byte 
	     * @param c char 
	     * @return byte 
	     */  
	    public static byte charToByte(char c) {  
	        return (byte) "0123456789ABCDEF".indexOf(c);  
	    } 

	    /** 
		    * 判断某个界面是否在前台 
		    *  
		    * @param context 
		    * @param className 
		    *            某个界面名称 
		    */  
		   public static boolean isForeground(Context context, String className) {  
		       if (context == null || TextUtils.isEmpty(className)) {  
		           return false;  
		       }  
		       ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		       List<RunningTaskInfo> list = am.getRunningTasks(1);  
		       if (list != null && list.size() > 0) {  
		           ComponentName cpn = list.get(0).topActivity;  
		           String firstName = cpn.getClassName();
		           if (className.equals(firstName)) {  
		               return true;  
		           }  
		       }  
		  
		       return false;  
		   } 
		   
		   /**
		     * ASCII码字符串转数字字符串
		     * 
		     * @param String
		     *            ASCII字符串
		     * @return 字符串
		     */
		    public static String AsciiStringToString(String content) {
		        String result = "";
		        int length = content.length() / 2;
		        for (int i = 0; i < length; i++) {
		            String c = content.substring(i * 2, i * 2 + 2);
		            int a = hexStringToAlgorism(c);
		            char b = (char) a;
		            String d = String.valueOf(b);
		            result += d;
		        }
		        return result;
		    }

		    
		    /**
		     * 十六进制字符串装十进制
		     * 
		     * @param hex
		     *            十六进制字符串
		     * @return 十进制数值
		     */
		    public static int hexStringToAlgorism(String hex) {
		        hex = hex.toUpperCase();
		        int max = hex.length();
		        int result = 0;
		        for (int i = max; i > 0; i--) {
		            char c = hex.charAt(i - 1);
		            int algorism = 0;
		            if (c >= '0' && c <= '9') {
		                algorism = c - '0';
		            } else {
		                algorism = c - 55;
		            }
		            result += Math.pow(16, max - i) * algorism;
		        }
		        return result;
		    }
		    
		    /** 字符转ASCII */
		    public static String stringToAscii(String value)  
		    {  
		        StringBuffer sbu = new StringBuffer();  
		        char[] chars = value.toCharArray();   
		        for (int i = 0; i < chars.length; i++) {  
		            if(i != chars.length - 1)  
		            {  
		                sbu.append((int)chars[i]).append(",");  
		            }  
		            else {  
		                sbu.append((int)chars[i]);  
		            }  
		        }  
		        return sbu.toString();  
		    } 
		
}
