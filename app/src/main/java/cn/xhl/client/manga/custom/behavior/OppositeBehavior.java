package cn.xhl.client.manga.custom.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/19
 *     version: 1.0
 * </pre>
 */
public class OppositeBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    /**
     * 这个类是用来让button随布局上下移动消失和显现
     */
    public OppositeBehavior(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    private FloatingActionButton.OnVisibilityChangedListener fabListener = new FloatingActionButton
            .OnVisibilityChangedListener() {
        @Override
        public void onShown(FloatingActionButton fab) {
            super.onShown(fab);
        }

        /**
         * 该方法会在fab.hide()调用后调用
         *
         * @param fab
         */
        @Override
        public void onHidden(FloatingActionButton fab) {
            super.onHidden(fab);
            fab.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target,
                dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0) {
            hide(child);
        } else if (dyConsumed < 0) {
            show(child);
        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent,
                                 FloatingActionButton child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    private void hide(final FloatingActionButton fab) {
        if (fab.getVisibility() == View.VISIBLE) {
            fab.hide(fabListener);
        }
    }

    private void show(FloatingActionButton fab) {
        if (fab.getVisibility() == View.INVISIBLE) {
            fab.show(fabListener);
        }
    }

}
