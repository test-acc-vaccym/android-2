package com.jianchi.fsp.buddhismnetworkradio;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by fsp on 16-7-25.
 */
public class TW2CN {
    static private Map<Character, Character> ts ;
    static private TW2CN instance;
    static private boolean isCN;
    private TW2CN(){
        Locale locale = Locale.getDefault();
        isCN = locale.getCountry().equals("CN");
    }

    public String toLocalString(String str){
        if(isCN)
            return t2s(str);
        else
            return str;
    }

    public static TW2CN getInstance(Context context) {
        if (instance == null) {
            try {
                instance = new TW2CN();
                ts = new HashMap<Character, Character>();
                Resources resources=context.getResources();
                InputStream is=resources.openRawResource(R.raw.ts);
                StringBuffer sBuffer = new StringBuffer();
                InputStreamReader inputreader = new InputStreamReader(is);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line = null;
                while((line =  buffreader.readLine()) != null) {
                    if (line.isEmpty()) {
                        continue;
                    }
                    char[] chararry = line.toCharArray();
                    if (chararry.length != 2) {
                        continue;
                    }
                    if (chararry[0] != chararry[1]) {
                        ts.put(chararry[0], chararry[1]);
                    }
                }
                buffreader.close();
                inputreader.close();
                is.close();
            } catch (Exception e) {
                throw new IllegalStateException("Can not create new instance of JChineseConvertor : " + e.getMessage(), e);
            }
        }
        return instance;
    }

    public String t2s(String str) {
        char[] result = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            char tchar = str.charAt(i);
            Character schar = ts.get(tchar);
            if (schar != null) {
                result[i] = schar;
            } else {
                result[i] = tchar;
            }
        }
        return new String(result);
    }
}
