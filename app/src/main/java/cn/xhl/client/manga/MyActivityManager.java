package cn.xhl.client.manga;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiuhao on 2017/9/26 0026.
 */

public class MyActivityManager {
    private static List<Activity> activities = new ArrayList<>();

    public static void add(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        activities.add(activity);
    }

    public static void remove(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        activities.remove(activity);
    }

    public static Activity pop() {
        int length = activities.size();
        if (length == 0) {
            return null;
        }
        return activities.remove(length - 1);
    }

    public static Activity peek() {
        int length = activities.size();
        if (length == 0) {
            return null;
        }
        return activities.get(length - 1);
    }

    public static void finishAll() {
        Activity activity;
        while (null != (activity = pop())) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
