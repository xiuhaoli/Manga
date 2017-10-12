package cn.xhl.client.manga.utils;


import java.io.UnsupportedEncodingException;

/**
 * Created by lixiuhao on 2017/9/25 0025.
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
}
