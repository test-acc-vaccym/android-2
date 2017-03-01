package top.edroplet.encdec;
import android.content.Context;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static final String TAG="Utils";
	public static void TranferToUTF8(Context context, String files) {
		if ( files.length() == 0 ) {
			Toast.makeText(context, "您没有选择文件!", Toast.LENGTH_LONG).show();
			return;
		}

		String [] fileArray = files.split(";");
		for ( String aFile: fileArray ) {
			try {
				FileInputStream fis = new FileInputStream(new File(aFile));
				File convertFile = new File(aFile+".txt");
				if(convertFile.exists()){
					convertFile.delete();
				}
				FileOutputStream fos = new FileOutputStream(convertFile);
				byte [] byteArray = new byte [fis.available()];
				fis.read(byteArray);
				String srcStr = new String(byteArray,"GBK");
				//String destStr = new String(srcStr.getBytes(destCharsetName),destCharsetName);
				fos.write(srcStr.getBytes("UTF-8"));
				fis.close();
				fos.close();
			} catch (Exception e) {
				Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public static void TranferTo(Context context, String files, String encodingFrom,String encodingTo) {
		if ( files.length() == 0 ) {
			Toast.makeText(context, "您没有选择文件!", Toast.LENGTH_LONG).show();
			return;
		}

		String [] fileArray = files.split(";");
		for ( String aFile: fileArray ) {
			try {

				FileInputStream fis = new FileInputStream(new File(aFile));
				File convertFile = new File(aFile+".txt");
				if(convertFile.exists()){
					convertFile.delete();
				}
				FileOutputStream fos = new FileOutputStream(convertFile);
				byte [] byteArray = new byte [fis.available()];
				fis.read(byteArray);
				String srcStr = new String(byteArray,encodingFrom);
				//String destStr = new String(srcStr.getBytes(destCharsetName),destCharsetName);
				fos.write(srcStr.getBytes(encodingTo));
				fis.close();
				fos.close();
			} catch (Exception e) {
				Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
	public static final String US_ASCII = "US-ASCII";

	/** ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/** 8 位 UCS 转换格式 */
	public static final String UTF_8 = "UTF-8";

	/** 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 */
	public static final String UTF_16BE = "UTF-16BE";

	/** 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 */
	public static final String UTF_16LE = "UTF-16LE";

	/** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
	public static final String UTF_16 = "UTF-16";

	/** 中文超大字符集 */
	public static final String GBK = "GBK";
	/**
	 * 将字符编码转换成US-ASCII码
	 */
	public String toASCII(String str) throws UnsupportedEncodingException {
		return this.Utils(str, US_ASCII);
	}
	/**
	 * 将字符编码转换成ISO-8859-1码
	 */
	public String toISO_8859_1(String str) throws UnsupportedEncodingException {
		return this.Utils(str, ISO_8859_1);
	}
	/**
	 * 将字符编码转换成UTF-8码
	 */
	public static String toUTF_8(String str) throws UnsupportedEncodingException {
		return Utils(str, UTF_8);
	}
	/**
	 * 将字符编码转换成UTF-16BE码
	 */
	public String toUTF_16BE(String str) throws UnsupportedEncodingException {
		return this.Utils(str, UTF_16BE);
	}
	/**
	 * 将字符编码转换成UTF-16LE码
	 */
	public String toUTF_16LE(String str) throws UnsupportedEncodingException {
		return this.Utils(str, UTF_16LE);
	}
	/**
	 * 将字符编码转换成UTF-16码
	 */
	public String toUTF_16(String str) throws UnsupportedEncodingException {
		return this.Utils(str, UTF_16);
	}
	/**
	 * 将字符编码转换成GBK码
	 */
	public String toGBK(String str) throws UnsupportedEncodingException {
		return this.Utils(str, GBK);
	}

	/**
	 * 字符串编码转换的实现方法
	 * @param str  待转换编码的字符串
	 * @param newCharset 目标编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String Utils(String str, String newCharset)
	throws UnsupportedEncodingException {
		if ( str != null ) {
			//用默认字符编码解码字符串。
			byte[] bs = str.getBytes();
			//用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}
	/**
	 * 字符串编码转换的实现方法
	 * @param str  待转换编码的字符串
	 * @param oldCharset 原编码
	 * @param newCharset 目标编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String Utils(String str, String oldCharset, String newCharset)
	throws UnsupportedEncodingException {
		if ( str != null ) {
			//用旧的字符编码解码字符串。解码可能会出现异常。
			byte[] bs = str.getBytes(oldCharset);
			//用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		Utils test = new Utils();
		String str = "This is a 中文的 String!";
		System.out.println("str: " + str);
		String gbk = test.toGBK(str);
		System.out.println("转换成GBK码: " + gbk);
		System.out.println();
		String ascii = test.toASCII(str);
		System.out.println("转换成US-ASCII码: " + ascii);
		gbk = test.Utils(ascii, Utils.US_ASCII, Utils.GBK);
		System.out.println("再把ASCII码的字符串转换成GBK码: " + gbk);
		System.out.println();
		String iso88591 = test.toISO_8859_1(str);
		System.out.println("转换成ISO-8859-1码: " + iso88591);
		gbk = test.Utils(iso88591, Utils.ISO_8859_1, Utils.GBK);
		System.out.println("再把ISO-8859-1码的字符串转换成GBK码: " + gbk);
		System.out.println();
		String utf8 = test.toUTF_8(str);
		System.out.println("转换成UTF-8码: " + utf8);
		gbk = test.Utils(utf8, Utils.UTF_8, Utils.GBK);
		System.out.println("再把UTF-8码的字符串转换成GBK码: " + gbk);
		System.out.println();
		String utf16be = test.toUTF_16BE(str);
		System.out.println("转换成UTF-16BE码:" + utf16be);
		gbk = test.Utils(utf16be, Utils.UTF_16BE, Utils.GBK);
		System.out.println("再把UTF-16BE码的字符串转换成GBK码: " + gbk);
		System.out.println();
		String utf16le = test.toUTF_16LE(str);
		System.out.println("转换成UTF-16LE码:" + utf16le);
		gbk = test.Utils(utf16le, Utils.UTF_16LE, Utils.GBK);
		System.out.println("再把UTF-16LE码的字符串转换成GBK码: " + gbk);
		System.out.println();
		String utf16 = test.toUTF_16(str);
		System.out.println("转换成UTF-16码:" + utf16);
		gbk = test.Utils(utf16, Utils.UTF_16LE, Utils.GBK);
		System.out.println("再把UTF-16码的字符串转换成GBK码: " + gbk);
		String s = new String("中文".getBytes("UTF-8"), "UTF-8");
		System.out.println(s);
	}


	//参数string为你的文件名
	public static String readFileContent(Context context, String fileName) throws IOException {

		fileName = URLDecoder.decode(fileName,UTF_8);
		Log.d("read",fileName);
		File file = new File(fileName);
		
		if ( !file.exists() ) {
			Toast.makeText(context, "文件不存在，请检查!"+fileName, Toast.LENGTH_LONG).show();
			return null;
		}
		BufferedReader bf = new BufferedReader(new FileReader(file));

		String content = "";
		StringBuilder sb = new StringBuilder();

		while ( content != null ) {
			content = bf.readLine();
			if ( content == null ) {
				break;
			}

			Log.d("content",content);
			sb.append(content.trim()).append("\n");
		}

		bf.close();
		Log.d("content",sb.toString());
		return sb.toString();
	}


	public static void writeContent(Context context, String fileName, String json) {
		
		try {
			fileName = URLDecoder.decode(fileName, UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		File file = new File(fileName);
		File parentFile = file.getParentFile();
		Boolean bn = false, bl = false;
		if(!file.exists()){
			bn = parentFile.mkdirs();
			if(bn){
				try {
					bl = file.createNewFile();
                } catch (IOException e) {
                    System.out.println("创建文件失败");
                    e.printStackTrace();
                }
            }
        }
		try {
			FileOutputStream writerStream = new FileOutputStream(fileName);

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));

			writer.write(json);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}
	}


	public static String getMD5Data(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for ( int i = 0;i < data.length;i++ ) {
				String result = Integer.toHexString(data[i] & 0xff);
				String temp = null;
				if ( result.length() == 1 ) {
					temp = "0" + result;
				} else {
					temp = result;
				}
				sb.append(temp);
			}
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void getS() {
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		// System.out.println(regEx);
		String str = "字符串";
		// System.out.println(str);
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		System.out.print("提取出来的中文有：");
		while ( m.find() ) {
			System.out.print(m.group(0) + " ");
		}
		System.out.println();
		System.out.println(p.matches(regEx, str));
	}
	
	private static String getMsgString(Object ...msgs){
		StringBuffer sb = new StringBuffer();
		for (Object msg : msgs){
			sb.append("{");
			sb.append(String.valueOf(msg));
			sb.append("} ");
		}
		return sb.toString();
	}
	public static void logd(String flag, Object ...msgs){
		Log.d(flag, getMsgString(msgs));
	}
	public static void loge(String flag, Object ...msgs){
		Log.e(flag, getMsgString(msgs));
	}

	public static textCache findInFiles(final Context context, textCache tc, final String filePath, final String strPattern, final boolean isRegex, final boolean showDetail, boolean ignoreCase){

		if ( filePath.length() == 0 ) {
			Toast.makeText(context, "您没有选择文件!", Toast.LENGTH_LONG).show();
			return null;
		}

		if (strPattern.isEmpty()){
			Toast.makeText(context,"Nothing to find!",Toast.LENGTH_LONG);
			return null;
		}

		try {
			//StringBuffer sb = new StringBuffer();
			
			File f = new File(filePath);
			if (!f.exists()){
				Toast.makeText(context,"Nothing to find!",Toast.LENGTH_SHORT);
				return null;
			}
			Log.e(TAG,filePath);
			if (f.isDirectory()) {
				File[] files = f.listFiles(); // 列出所有文件
				Utils ut = new Utils();
						// 创建一个线程池
						ExecutorService pool = Executors.newFixedThreadPool(files.length);
						// 创建两个有返回值的任务
						Callable c1 = ut.new FindCallable(context, tc, files, strPattern, isRegex, showDetail, ignoreCase);
						// 执行任务并获取Future对象
						Future f1 = pool.submit(c1);
						// 从Future对象上获取任务的返回值，并添加到sb
						// sb.append(f1.get().toString());
						tc = f1.get();
						// return sb.append(findInFiles(context, fi.getPath(),strPattern,isRegex));
			}
			return tc;
		}catch (Exception e) {
			Log.e(TAG,e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
			return tc;
		}
	}

	class FindCallable implements Callable{
		private String strPattern;
		private boolean isRegex, showDetail, ignoreCase;
		private Context context;
		private textCache tc;
		private File [] files;

		FindCallable(Context context, textCache tc, File [] files, String strPattern, boolean isRegex, boolean showDetail, boolean ignoreCase) {
			this.context = context;
			this.tc = tc;
			this.strPattern = strPattern;
			this.files = files;
			this.isRegex = isRegex;
			this.showDetail = showDetail;
			this.ignoreCase = ignoreCase;
		}
		
		public textCache call() {
			try {
				for (File fi : files) {
					String filePath = fi.getPath();
					if (fi.isDirectory()) {
						// sb.append(findInFiles(context, fi.getPath(), strPattern, isRegex, showDetail, ignoreCase));
						findInFiles(context, tc, filePath, strPattern, isRegex, showDetail, ignoreCase);
					} else {
				StringBuffer sb = new StringBuffer();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fi)));
				int lineNo = 0;
				for (String line = br.readLine(); line != null; line = br.readLine()) {
					lineNo++;
					if (isRegex) {
						Pattern p = Pattern.compile(strPattern);
						Matcher m = p.matcher(line);
						if (ignoreCase){
							// 不区分大小写
							p = Pattern.compile(strPattern.toUpperCase());
							m = p.matcher(line.toUpperCase());
						}
						if (m.matches()) {
							if (showDetail) {
								// 显示详细文本
								sb.append(filePath + " " + lineNo + ":" + line);
							}else {
								sb.append(filePath + " " + lineNo );
							}
						}
					} else {
						boolean isExists = line.contains(strPattern);
						if (ignoreCase){
							// 不区分大小写
							isExists = line.toUpperCase().contains(strPattern.toUpperCase());
						}
						if (isExists) {
							if (showDetail) {
								// 显示详细文本
								sb.append(filePath + " " + lineNo + ":" + line);
							}else {
								sb.append(filePath + " " + lineNo );
							}
						}
					}
				}
				br.close();
						String key = MD5.getMD5(filePath);
				tc.put(key,sb.toString());
				}
				}
			} catch (Exception e) {
				Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
			}
			return tc;
		}

	}
	
	
}

class textCache extends LruCache <String, String>{
	textCache(int maxSize){
		super(maxSize);
	}

	@Override
	protected int sizeOf(String key, String value) {
		// TODO: Implement this method
		return super.sizeOf(key, value);
	}

}

/** * 对外提供getMD5(String)方法 * @author randyjia * */ 
class MD5 { 
public static String getMD5(String val) throws NoSuchAlgorithmException{ 
	MessageDigest md5 = MessageDigest.getInstance("MD5");
	md5.update(val.getBytes()); 
	byte[] m = md5.digest();
	//加密
	return getString(m); 
} 
private static String getString(byte[] b){
	StringBuffer sb = new StringBuffer();
	for(int i = 0; i < b.length; i ++){
		sb.append(b[i]); } return sb.toString();
	}
}
