package cn.xhl.client.manga.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.view.user.AuthActivity;
import cn.xhl.client.manga.config.IConstants;

/**
 * Created by lixiuhao on 2017/4/19 0019.
 */

public class ActivityUtil {
    /**
     * @param from
     * @param to
     * @param flag
     * @param code 标识码
     */
    public static void jump2ActivityForResult(Activity from, Class to, int flag, int code) {
        Intent intent = new Intent();
        intent.setClass(from, to);
        if (flag != -1) {
            intent.setFlags(flag);
        }
        from.startActivityForResult(intent, code);
    }

    public static void jump2ActivityForResult(Activity from, Class to, int code) {
        jump2ActivityForResult(from, to, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT, code);
    }

    /**
     * 跳转到登录界面，并清空数据，此方法仅在签名验证失败的情况下调用
     *
     * @param from
     */
    public static void jump2LoginPage(Activity from, boolean isClearData) {
        if (isClearData) {
            PrefUtil.clear(from);
        }
        jump2LoginPage(from, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void jump2LoginPage(Activity from, int flag) {
        jump2Activity(from,AuthActivity.class, flag);
    }

    /**
     * 跳转activity
     *
     * @param from
     * @param to
     * @param flag 需要设置的跳转属性
     */
    public static void jump2Activity(Activity from, Class to, int flag) {
        Intent intent = new Intent(from, to);
        intent.setFlags(flag);
        from.startActivity(intent);
    }

    /**
     * 默认的跳转方式
     *
     * @param from
     * @param to
     */
    public static void jump2Activity(Activity from, Class to) {
        jump2Activity(from, to, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
    }

    /**
     * 回到主界面，相当于系统的home键
     *
     * @param context 上下文
     */
    public static void goHome(Activity context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    public static void clearActivity(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
    }

    /**
     * <p>切换Fragment</p>
     *
     * @param from      来自哪个Fragment
     * @param to        要去哪个Fragment
     * @param name      为Fragment标记名字
     * @param contentId frame id
     */
    public static void switchContentHideCurrent(Activity activity, Fragment from, Fragment to, String name, int contentId) {
        if (from == null) {
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.add(contentId, to, name).commit();
            return;
        }
        if (from != to) {
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(from).add(contentId, to, name).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    /**
     * <p>切换Fragment</p>
     *
     * @param from      来自哪个Fragment
     * @param to        要去哪个Fragment
     * @param name      为Fragment标记名字
     * @param contentId frame id
     */
    public static void switchContentRemoveCurrent(Activity activity, Fragment from, Fragment to, String name, int contentId) {
        if (from == null) {
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.add(contentId, to, name).commit();
            return;
        }
        if (from != to) {
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.remove(from).add(contentId, to, name).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.remove(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
}
