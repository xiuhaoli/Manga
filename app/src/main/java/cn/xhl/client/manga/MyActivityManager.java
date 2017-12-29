package cn.xhl.client.manga;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mike on 2017/9/26 0026.
 */
public class MyActivityManager {
    private static List<Activity> activities = new ArrayList<>();

    public static void add(@NonNull Activity activity) {
        activities.add(activity);
    }

    public static void remove(@NonNull Activity activity) {
        activities.remove(activity);
    }

    @Nullable
    public static Activity pop() {
        int length = activities.size();
        if (length == 0) {
            return null;
        }
        return activities.remove(length - 1);
    }

    @Nullable
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
