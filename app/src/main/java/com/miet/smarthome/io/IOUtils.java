package com.miet.smarthome.io;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class IOUtils {
    public static String readStreamToString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        int i;
        StringBuilder sb = new StringBuilder();

        while ((i = in.read()) != -1) {
            sb.append((char) i);
        }

        return sb.toString();
    }

    public static void writeStringToStream(OutputStream fos, String defaultConfig) throws IOException {
        for (int i = 0; i < defaultConfig.length(); i++) {
            fos.write((byte)defaultConfig.charAt(i));
        }
    }
}
