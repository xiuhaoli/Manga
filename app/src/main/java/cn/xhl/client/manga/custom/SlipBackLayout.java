package cn.xhl.client.manga.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/27
 *     version: 1.0
 * </pre>
 */
public class SlipBackLayout extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private View mContentView;
    private int mContentWidth;
    private int mMoveLeft;
    private boolean isClose = false;
    private OnWindowCloseListener mOnWindowCloseListener;//自定义内部的回调函数，下面写
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
    private int mScrimColor = DEFAULT_SCRIM_COLOR;
    private float mScrimOpacity;
    private float mScrollPercent;
    private Activity mActivity;
//    private boolean isFragment;

    public static void init(@NonNull Context activity, OnWindowCloseListener listener) {
        init(activity, null, 0, listener);
    }

    public static void init(@NonNull Context activity, @Nullable AttributeSet attrs,
                            OnWindowCloseListener listener) {
        init(activity, attrs, 0, listener);
    }

    public static void init(@NonNull Context activity, @Nullable AttributeSet attrs,
                            int defStyleAttr, OnWindowCloseListener listener) {
        new SlipBackLayout(activity, attrs, defStyleAttr, listener);
    }

//    public SlipBackLayout(@NonNull Context context, OnWindowCloseListener listener, View view) {
//        super(context);
//        isFragment = true;
//        mOnWindowCloseListener = listener;
//        mActivity = (Activity) context;
//        initView();
//        this.addView(view);
//    }

    private SlipBackLayout(@NonNull Context context, @Nullable AttributeSet attrs,
                           int defStyleAttr, OnWindowCloseListener listener) {
        super(context, attrs, defStyleAttr);
        mOnWindowCloseListener = listener;
        mActivity = (Activity) context;
        initView();
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mContentView;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                mScrollPercent = Math.abs((float) left / (mContentView.getWidth()));
                mMoveLeft = left;
                if (isClose && left == mContentWidth) {
                    if (mOnWindowCloseListener != null) {
                        mOnWindowCloseListener.onFinish();
                    }
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (mMoveLeft > mContentWidth / 2) {
                    isClose = true;// 滑动超出半屏，则触发关闭操作
                    mViewDragHelper.settleCapturedViewAt(mContentWidth, releasedChild.getTop());
                } else {
                    mViewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
                }
                invalidate();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return Math.min(mContentWidth, Math.max(left, 0));
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mContentWidth;
            }

        });
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        bindView(mActivity);
    }

    private void bindView(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View child = decorView.getChildAt(0);
        decorView.removeView(child);// 先将顶级的view的子view移除
        this.addView(child);
        decorView.addView(this);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        mScrimOpacity = 1 - mScrollPercent;
        if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mContentView = getChildAt(0);// 获取到当前布局的子布局，即你在xml中定义的根布局
        mContentWidth = mContentView.getWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        invalidate();
        return true;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == mContentView;
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (drawContent && mViewDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawScrim(canvas, child);
        }
        return ret;
    }

    // 覆盖在空白区的黑色透明图层
    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * mScrimOpacity);
        final int color = alpha << 24 | (mScrimColor & 0xffffff);
        canvas.clipRect(0, 0, child.getLeft(), getHeight());
        canvas.drawColor(color);
    }

    public interface OnWindowCloseListener {
        void onFinish();
    }

}
