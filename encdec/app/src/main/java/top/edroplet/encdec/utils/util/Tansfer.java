package top.edroplet.encdec.utils.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;

public class Tansfer {
	//public static final String DefaultSrcEncodeFormat = "GBK";
	public static final String DefaultSrcEncodeFormat = "GB2312";
	public static final String DefaultDestEncodeFormat = "UTF-8";
	public static final String UnsupportedEncodingExceptionError = "编码格式错误！";
	public static final String FileNotFoundExceptionError = "文件不存在！";
	public static final String IOExceptionError = "文件读写错误！";
	public static final String IsUtf8File = "文件是UTF-8编码格式！";
	public static final String IsNotUtf8File = "文件不是UTF-8编码格式！";
	static final String FLAG = "TANSFER";

	public static String readFileAndTransfer(String fileName, String srcFormat, String destFormat) {
		// Format fm;
		if ( ( srcFormat == null || srcFormat.equals("") ) ) {
			if ( isUTF8File(fileName) )
				srcFormat = DefaultDestEncodeFormat;
			else
				srcFormat = DefaultSrcEncodeFormat;
		}
		if ( ( destFormat == null || destFormat.equals("") ) ) {
			if ( isUTF8File(fileName) )
				destFormat = DefaultDestEncodeFormat;
			else
				destFormat = DefaultSrcEncodeFormat;
		}

		try {
			fileName = URLDecoder.decode(fileName, Utils.UTF_8);
		} catch (UnsupportedEncodingException e) {
			Utils.loge(FLAG, e.toString(), Thread.currentThread().getStackTrace()[2].getFileName() + "," + Thread.currentThread().getStackTrace()[2].getLineNumber());
			return "";
		}

		try {
			
			RandomAccessFile raf = new RandomAccessFile(fileName, "r");
			byte[] b = new byte[(int)raf.length()];
			byte [] t =  new byte[(int)raf.length() + (int)raf.length()];
			//将文件按照字节方式读入到字节缓存中
			raf.read(b);
			Utils.loge(FLAG,b.length, srcFormat,destFormat);
			for(int i = 0,j = 0; i < b.length; i++, j++){
				int ib0, ib1, ib2, ib3;
				ib0 = b[i]&0xff;

				if (ib0 > 'z'){
					ib1 = b[++i]&0xff;
					if (ib1>'z') {
						if (srcFormat == Utils.UTF_8){
							ib2 = b[++i]&0xff;
						}
						String s = new String(b,i-1,2,srcFormat);
						s = new String(s.getBytes(), destFormat);
						Utils.logd(FLAG,i,Integer.toHexString(ib0),Integer.toHexString( ib1),s);
					}
				}else{
					t[j] = b[i];
				}
			}
			//将字节转换为utf-8 格式的字符串
			//String input = new String(b, "utf-8");
			// Utils.loge(FLAG,input);
			
		} catch (Exception e) {
			Utils.loge(FLAG, e.toString(), Thread.currentThread().getStackTrace()[2].getFileName() + "," + Thread.currentThread().getStackTrace()[2].getLineNumber());
			return "";
		}
		
		return "";
	}
	/*
	 public static boolean isUTF8File(String path) {
	 try {
	 File file = new File(path);
	 CharsetPrinter detector = new CharsetPrinter();
	 String charset = detector.guessEncoding(file);
	 if ( charset.equalsIgnoreCase(DefaultDestEncodeFormat) ) {
	 Utils.logd(FLAG,IsUtf8File,Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber());
	 return true;
	 }
	 } catch (FileNotFoundException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
	 Utils.logd(FLAG,FileNotFoundExceptionError,Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber());
	 } catch (IOException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
	 Utils.logd(FLAG,IOExceptionError,Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber());
	 }

	 Utils.logd(FLAG,IsNotUtf8File,Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber());
	 return false;
	 }
	 */

	 public static String transfer(String context, String encodeFormat) {
	 if ( encodeFormat == null || encodeFormat.equals("") )
	 encodeFormat = DefaultDestEncodeFormat;
	 try {
	 byte[] content = context.getBytes();
	 String result = new String(content, encodeFormat);
	 Utils.logd(FLAG,result,Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber());
	 return result;
	 } catch (UnsupportedEncodingException e) {
	 // TODO Auto-generated catch block
	 Utils.logd(FLAG,UnsupportedEncodingExceptionError,Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber());
	 e.printStackTrace();
	 }
	 return "";
	 }

	
	public static boolean isUTF8File(String path) {
		try {
			File file = new File(path);
			// CharsetPrinter detector = new CharsetPrinter();   
			// String charset = detector.guessEncoding(file);
			FileInputStream in = new FileInputStream(file);
			byte[] b = new byte[3];
			in.read(b);
			in.close();
			Utils.logd(FLAG, b[0] + " " + b[1] + " " + b[2]);
			if ( b[0] == 0xEF && b[1] == 0xBB && b[2] == 0XBF ) {
				Utils.logd(FLAG, IsUtf8File);
				return true;
			}
			if ( b[0] == -17 && b[1] == -69 && b[2] == -65 ) {
				Utils.logd(FLAG, IsUtf8File);
				return true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.logd(FLAG, FileNotFoundExceptionError);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.logd(FLAG, IOExceptionError);
		}

		Utils.logd(FLAG, IsNotUtf8File);
		return false;   
	}

	public static void writeFile(String context, String path, String destEncode) {
		File file = new File(path);
		if ( file.exists() )
			file.delete();
		BufferedWriter  writer;
		try {
			FileOutputStream fos = new FileOutputStream(path, true);    
			writer = new BufferedWriter(new OutputStreamWriter(fos, destEncode));
			// Utils.logd(FLAG,context,Thread.currentThread().getStackTrace()[2].getFileName()+","+Thread.currentThread().getStackTrace()[2].getLineNumber());
			writer.append(context);
			writer.close();
		} catch (IOException e) {
			Utils.logd(FLAG, IOExceptionError, Thread.currentThread().getStackTrace()[2].getFileName() + "," + Thread.currentThread().getStackTrace()[2].getLineNumber());
			e.printStackTrace();
		}
	}


	public static void writeFile(String context, String path) {
		File file = new File(path);
		if ( file.exists() )
			file.delete();
		Writer  writer;
		try {
			writer = new FileWriter(file, true);
			writer.append(context);
			writer.close();
		} catch (IOException e) {
			Utils.logd(FLAG, IOExceptionError, Thread.currentThread().getStackTrace()[2].getFileName() + "," + Thread.currentThread().getStackTrace()[2].getLineNumber());
			e.printStackTrace();
		}
	}

	public static void transfer(String srcPath, String destPath, String srcEncode, String destEncode) {
		if ( destPath == null || destPath.equals("") )
			destPath = srcPath;

		String context = readFileAndTransfer(srcPath, srcEncode,destEncode);
		//context = transfer(context, destEncode);
		writeFile(context, destPath, destEncode);
	}

	public static void transfer(String srcPath, String destPath, String destEncode) {
		if ( isUTF8File(srcPath) ) {
			Utils.loge(FLAG, "utf8 file", Thread.currentThread().getStackTrace()[2].getLineNumber());
			transfer(srcPath, destPath, DefaultDestEncodeFormat, destEncode);
		} else {
			Utils.loge(FLAG, "not utf8 file", Thread.currentThread().getStackTrace()[2].getLineNumber());
			transfer(srcPath, destPath, DefaultSrcEncodeFormat, destEncode);
		}
	}

}
