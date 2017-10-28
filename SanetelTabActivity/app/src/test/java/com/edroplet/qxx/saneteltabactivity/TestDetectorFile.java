package com.edroplet.qxx.saneteltabactivity;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * Created by qxs on 2017/10/19.
 */

public class TestDetectorFile {
    public static void main (String[] args) throws java.io.IOException {
        if (args.length != 1) {
            System.err.println("Usage: java TestDetectorFile FILENAME");
            System.exit(1);
        }
        java.io.File file = new java.io.File(args[0]);
        String encoding = UniversalDetector.detectCharset(file);
        if (encoding != null) {
            System.out.println("Detected encoding = " + encoding);
        } else {
            System.out.println("No encoding detected.");
        }
    }
}
