package cn.xhl.client.manga.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.xhl.client.manga.config.IConstants;

/**
 * Created by lixiuhao on 2017/9/28 0028.
 */

public class PrefUtil {
    public static SharedPreferences get(Context ctx) {
        return ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
    }

    public static void putBoolean(String key, boolean value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(String key, String value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(String key, int value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void remove(String key, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    public static void clear(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(IConstants.USER_INFO, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
