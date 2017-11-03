package cn.xhl.client.manga.utils;

import android.util.Log;

import java.io.IOException;


/**
 * @author Mike on 2017/9/25 0025.
 */

public class LogUtil {
    private static boolean IS_DEBUG = AppUtil.isApkInDebug();// 是否是debug模式

    public static void v(String tag, String msg, Throwable throwable) {
        if (IS_DEBUG) {
            if (throwable == null) {
                Log.v(tag, msg);
            } else {
                Log.v(tag, msg, throwable);
            }
        }
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (IS_DEBUG) {
            if (throwable == null) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, msg, throwable);
            }
        }
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (IS_DEBUG) {
            if (throwable == null) {
                Log.i(tag, msg);
            } else {
                Log.i(tag, msg, throwable);
            }
        }
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (IS_DEBUG) {
            if (throwable == null) {
                Log.w(tag, msg);
            } else {
                Log.w(tag, msg, throwable);
            }
        }
    }

    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (IS_DEBUG) {
            if (throwable == null) {
                Log.e(tag, msg);
            } else {
                Log.e(tag, msg, throwable);
            }
        }
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void eLocal(String filename, String content) {
        if (IS_DEBUG) {
            try {
                FileUtil.getInstance().printLog(filename, content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void eLocal(String content) {
        eLocal(SystemUtil.getTimeStamp(), content);
    }
}
