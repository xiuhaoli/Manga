package cn.xhl.client.manga.utils;

import android.app.Activity;
import android.view.View;

/**
 * @author Mike on 2017/4/18 0018.
 */

public class ControlUtil {
    /**
     * 初始化控件
     *
     * @param resId    控件id
     * @param listener 监听器
     * @param activity 上下文
     * @return
     */
    public static View initControlOnClick(int resId, View.OnClickListener listener, Activity activity) {
        if (activity == null) return null;
        View view = activity.findViewById(resId);
        view.setOnClickListener(listener);
        return view;
    }

    /**
     * @param resId
     * @param listener
     * @param view     parent View
     * @return
     */
    public static View initControlOnClick(int resId, View view, View.OnClickListener listener) {
        if (view == null) return null;
        view = view.findViewById(resId);
        view.setOnClickListener(listener);
        return view;
    }

    public static View initControlOnClick(int resId, Activity activity) {
        return initControlOnClick(resId, null, activity);
    }

    public static View initControlOnClick(int resId, View view) {
        return initControlOnClick(resId, view, null);
    }
}
