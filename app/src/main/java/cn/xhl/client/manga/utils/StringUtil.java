package cn.xhl.client.manga.utils;


import java.io.UnsupportedEncodingException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Mike on 2017/9/25 0025.
 */

public class StringUtil {

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String utf_8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static RequestBody getRequestBody(String jsonText) {
        return RequestBody.create(MediaType.parse("Content-Type, application/json"), jsonText);
    }

    /**
     * 按照ascii码表排序
     *
     * @param s array
     * @return array
     */
    public static String[] bubbleSort(String s[]) {
        for (int i = 0, size = s.length; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                String temp;
                if (s[j].compareTo(s[j + 1]) > 0) {
                    temp = s[j + 1];
                    s[j + 1] = s[j];
                    s[j] = temp;
                }
            }
        }
        return s;
    }
}
