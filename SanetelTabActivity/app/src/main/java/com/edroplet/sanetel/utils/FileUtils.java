package com.edroplet.sanetel.utils;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;

/**
 * Created by qxs on 2017/11/3.
 */

public class FileUtils {
    public static long FILE_LIMIT = 10485760; // 10MB

    public static void inputStreamToFile(InputStream ins, File file)throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

    public static String getParentDir(String path){
        if (null == path){
            return "../";
        }
        int index = path.lastIndexOf("/");
        if (index == -1){
            return "";
        }
        return path.substring(0,index);
    }

    public static String getBaseName(String path){
        int index = path.lastIndexOf("/");
        if (index == -1){
            return path;
        }
        return path.substring(index+1);
    }

    public static void saveFile(String filePath, String message, boolean append) throws IOException{
        FileOutputStream out;
        PrintStream ps;
        String parentDir = getParentDir(filePath);
        if (!isFileExist(parentDir)){
            createDir(parentDir);
        }
        out = new FileOutputStream(filePath, append);
        ps = new PrintStream(out);
        ps.println(message);
        if (out != null) {
            out.close();
            ps.close();
        }
    }

    public static void saveFileToSdcard(String filePath, String message) throws IOException{
        FileOutputStream out;
        PrintStream ps;
        String parentDir = getParentDir(filePath);
        if (!isFileExist(parentDir)){
            createDir(parentDir);
        }
        out = new FileOutputStream(filePath);
        ps = new PrintStream(out);
        ps.println(message);
        if (out != null) {
            out.close();
            ps.close();
        }
    }

    public static void savePrivateFile(Context context, String filePath, int mode, String message) throws IOException{
        FileOutputStream out;
        PrintStream ps;

        out = context.openFileOutput(filePath, mode);
        ps = new PrintStream(out);
        ps.println(message);
        if (out != null) {
            out.close();
            ps.close();
        }
    }
    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param filePath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }


    public static String getSDPath() {
        String sdPath = "";
        //判断SDCard是否存在并且可读写
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            sdPath = Environment.getExternalStorageDirectory().toString() + File.pathSeparator;
        }
        if (sdPath.endsWith(":")){
            sdPath = sdPath.substring(0,sdPath.length() -1);
        }
        return sdPath;
    }

    private static String dataDir;
    public static String getDataDir(Context context){
        if (dataDir != null || context == null){
            return dataDir;
        }
        File dataFile = ContextCompat.getDataDir(context);
        dataDir = dataFile.getAbsolutePath();
        return dataDir;
    }
    /**
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    public static boolean isFileExist(String fileName){
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 在SD卡上创建文件
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(getSDPath() + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     * @param dirName 目录名字
     * @return 文件目录
     */
    public static File createDir(String dirName){
        File dir = new File(dirName);
        dir.mkdirs();
        return dir;
    }

    public File write2SDFromInput(String path,String fileName,InputStream input){
        File file = null;
        OutputStream output = null;

        try {
            createDir(path);
            file =createSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte [] buffer = new byte[4 * 1024];
            while(input.read(buffer) != -1){
                output.write(buffer);
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * Get the sha1 value of the filepath specified file
     * @param filePath The filepath of the file
     * @return The sha1 value
     */
    public static String getFileSHA1(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath); // Create an FileInputStream instance according to the filepath
            byte[] buffer = new byte[1024]; // The buffer to read the file
            MessageDigest digest = MessageDigest.getInstance("SHA-1"); // Get a SHA-1 instance
            int numRead = 0; // Record how many bytes have been read
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0)
                    digest.update(buffer, 0, numRead); // Update the digest
            }
            byte [] sha1Bytes = digest.digest(); // Complete the hash computing
            return convertHashToString(sha1Bytes); // Call the function to convert to hex digits
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close(); // Close the InputStream
                } catch (Exception e) { }
            }
        }
    }

    /**
     * Get the md5 value of the filepath specified file
     * @param filePath The filepath of the file
     * @return The md5 value
     */
    public String getFileMD5(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath); // Create an FileInputStream instance according to the filepath
            byte[] buffer = new byte[1024]; // The buffer to read the file
            MessageDigest digest = MessageDigest.getInstance("MD5"); // Get a MD5 instance
            int numRead = 0; // Record how many bytes have been read
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0)
                    digest.update(buffer, 0, numRead); // Update the digest
            }
            byte [] md5Bytes = digest.digest(); // Complete the hash computing
            return convertHashToString(md5Bytes); // Call the function to convert to hex digits
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close(); // Close the InputStream
                } catch (Exception e) { }
            }
        }
    }

    /**
     * Convert the hash bytes to hex digits string
     * @param hashBytes
     * @return The converted hex digits string
     */
    private static String convertHashToString(byte[] hashBytes) {
        String returnVal = "";
        for (int i = 0; i < hashBytes.length; i++) {
            returnVal += Integer.toString(( hashBytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return returnVal.toLowerCase();
    }

    /**
     * 获取指定文件大小
     * @param file
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 获取指定文件大小
     * @param filePath
     * @return
     * @throws Exception 　　
     */
    public static long getFileSize(String filePath) {
        long size = 0;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return size;
    }
}
