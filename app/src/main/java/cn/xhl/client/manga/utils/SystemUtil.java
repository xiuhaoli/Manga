package cn.xhl.client.manga.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by lixiuhao on 2017/5/5 0005.
 */

public class SystemUtil {

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 判断str是否为数字
     *
     * @param str 传入的字符串
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pkName = packageInfos.get(i).packageName;
                if (pkName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String formatNumber6(double d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");
        return df.format(d);
    }

    public static String formatNumber2(double d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        return df.format(d);
    }

}
