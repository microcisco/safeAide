package com.cool.baigu.safeaide.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by baigu on 2017/8/18.
 */

public class BufferToString {

    public static String Stream2String(InputStream inputStream) {

        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line = reader.readLine();

            while (line != null) {
                stringBuffer.append(line);
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return stringBuffer.toString();
    }

}
