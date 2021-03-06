package com.edroplet.qxs.wificommunication.utils;

/**
 * Created by qxs on 2017/7/25.
 */
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
                is.close();
                os.close();
            }
        }
        catch(Exception ex){}
    }
}