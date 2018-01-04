package cn.xhl.client.manga.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Scanner;

import cn.xhl.client.manga.MyApplication;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.utils.DpUtil;
import cn.xhl.client.manga.utils.ImageUtil;
import cn.xhl.client.manga.utils.LogUtil;


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
    private Bitmap bitmap;
    private boolean isText;// 是否展示文字
    private String content;// 展示的文字内容

    private SlipBackLayout(Builder builder) {
        super(builder.context, builder.attrs, builder.defStyleAttr);
        mOnWindowCloseListener = builder.listener;
        mActivity = (Activity) builder.context;
        isText = builder.isText;
        content = builder.content;
        initView();
    }

    private void initView() {
        this.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
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
                if (mMoveLeft > mContentWidth * 0.34) {
                    isClose = true;// 滑动超出0.3屏，则触发关闭操作
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
        if (isText) {
            int imageSize = DpUtil.dp2Px(mActivity, 20);
            bitmap = ImageUtil.decodeSampledBitmapFromResource(mActivity.getResources(),
                    R.mipmap.logout, imageSize, imageSize);
            mActivity.getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            initPaint();
        }
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

    /**
     * 该方法返回true将不对事件进行传递会在此拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!dispatchEvent2Child(ev)) {
            mViewDragHelper.processTouchEvent(ev);
            invalidate();
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean dispatchEvent2Child(MotionEvent event) {
        if (this.getLeft() > 0) {
            return false;
        }
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < this.getWidth() * 0.05) {
                    // 如果小于该值则拦截事件
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 事件如果被拦截了，该方法会被反复调用直到手势离开屏幕
     *
     * @param event
     * @return
     */
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
            if (isText) {
                drawText(canvas, child);
            } else {
                drawScrim(canvas, child);
            }
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

    private int textColor = Color.parseColor("#747474");
    private int textSize = DpUtil.dp2Px(MyApplication.getAppContext(), 16);
    private Paint paint;
    private int translateText = DpUtil.dp2Px(MyApplication.getAppContext(), 7);

    private void drawText(Canvas canvas, View child) {
        canvas.translate(translateText, translateText);
        canvas.drawText(content, child.getLeft() - bitmap.getWidth() * 2 - textSize - 5,
                getHeight() * 0.5f, paint);
        canvas.translate(-translateText, -translateText);
        canvas.drawBitmap(bitmap, child.getLeft() - bitmap.getWidth() - 10,
                getHeight() * 0.5f - bitmap.getHeight() * 0.5f, paint);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setColor(textColor);
    }

    public interface OnWindowCloseListener {
        void onFinish();
    }

    public static class Builder {
        private Context context;
        private AttributeSet attrs = null;
        @AttrRes
        private int defStyleAttr = 0;
        private boolean isText = false;
        private String content = "";
        private OnWindowCloseListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setText(boolean text) {
            isText = text;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setAttrs(AttributeSet attrs) {
            this.attrs = attrs;
            return this;
        }

        public Builder setDefStyleAttr(int defStyleAttr) {
            this.defStyleAttr = defStyleAttr;
            return this;
        }

        public Builder setListener(OnWindowCloseListener listener) {
            this.listener = listener;
            return this;
        }

        public SlipBackLayout build() {
            return new SlipBackLayout(this);
        }
    }

}
