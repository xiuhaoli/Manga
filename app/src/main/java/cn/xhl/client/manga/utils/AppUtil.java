package cn.xhl.client.manga.utils;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.List;

import cn.xhl.client.manga.MyApplication;

/**
 * Created by lixiuhao on 2017/9/25 0025.
 */

public class AppUtil {
    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug() {
        try {
            ApplicationInfo info = MyApplication.getAppContext().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断应用是否是在后台
     */
    public static boolean isBackground() {
        Context context = MyApplication.getAppContext();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (TextUtils.equals(appProcess.processName, context.getPackageName())) {
                boolean isBackground = (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE);
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                return isBackground || isLockedState;
            }
        }
        return false;
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        PackageManager manager = MyApplication.getAppContext().getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(MyApplication.getAppContext().getPackageName(), 0);
            String versionName = info.versionName;
            if (versionName.endsWith("-debug")) {
                versionName = versionName.substring(0, versionName.lastIndexOf('-'));
            }
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0.0";
        }
    }
}
