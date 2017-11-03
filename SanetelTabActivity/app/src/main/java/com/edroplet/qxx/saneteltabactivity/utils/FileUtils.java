package com.edroplet.qxx.saneteltabactivity.utils;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by qxs on 2017/11/3.
 */

public class FileUtils {
    public static void inputstreamtofile(InputStream ins, File file)throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

    public static void saveFile(Context context, String filePath, int mode, String message) throws IOException{
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

}
