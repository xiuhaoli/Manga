package cn.xhl.client.manga;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.base.BaseFragment;

/**
 * Created by lixiuhao on 2017/9/30 0030.
 */

public class MyFragmentManager {
    private static List<BaseFragment> fragments = new ArrayList<>();

    public static void add(BaseFragment fragment) {
        if (fragment == null) {
            throw new NullPointerException("fragment is null");
        }
        fragments.add(fragment);
    }

    public static void remove(BaseFragment fragment) {
        if (fragment == null) {
            throw new NullPointerException("fragment is null");
        }
        fragments.remove(fragment);
    }

    public static BaseFragment pop() {
        int length = fragments.size();
        if (length == 0) {
            return null;
        }
        return fragments.remove(length - 1);
    }

    public static BaseFragment peek() {
        int length = fragments.size();
        if (length == 0) {
            return null;
        }
        return fragments.get(length - 1);
    }

    public static int size() {
        return fragments.size();
    }

    public static void clear() {
        fragments.clear();
    }
}
