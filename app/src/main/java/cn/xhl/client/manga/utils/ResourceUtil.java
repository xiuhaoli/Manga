package cn.xhl.client.manga.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;

import cn.xhl.client.manga.MyActivityManager;
import cn.xhl.client.manga.MyApplication;
import cn.xhl.client.manga.R;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/09
 *     version: 1.0
 * </pre>
 */
public class ResourceUtil {
    public static int getAttrData(@AttrRes int attrId) {
        return getAttrData(attrId, new TypedValue());
    }

    public static int getAttrData(@AttrRes int attrId, TypedValue typedValue) {
        Context context = MyActivityManager.peek();
        if (context != null) {
            context.getTheme().resolveAttribute(attrId, typedValue, true);
        }
        return typedValue.data;
    }

    public static GradientDrawable getDrawableWithRound5(@AttrRes int attrId) {
        int attr = getAttrData(attrId);
        Context context = MyApplication.getAppContext();
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(DpUtil.dp2Px(context, 5));
        drawable.setStroke(1, attr);
        drawable.setColor(attr);
        return drawable;
    }
}
