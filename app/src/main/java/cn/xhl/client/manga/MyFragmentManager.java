package cn.xhl.client.manga;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.base.BaseFragment;

/**
 * @author Mike on 2017/9/30 0030.
 */

public class MyFragmentManager {
    private static List<BaseFragment> fragments = new ArrayList<>();

    public static void add(@NonNull BaseFragment fragment) {
        fragments.add(fragment);
    }

    public static void remove(@NonNull BaseFragment fragment) {
        fragments.remove(fragment);
    }

    @Nullable public static BaseFragment pop() {
        int length = fragments.size();
        if (length == 0) {
            return null;
        }
        return fragments.remove(length - 1);
    }

    @Nullable public static BaseFragment peek() {
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
