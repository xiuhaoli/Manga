package cn.xhl.client.manga.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 自定义的viewpager可以竖直滑动
 * Created by xiuhaoli on 2017/11/28.
 */
public class VerticalViewPager extends ViewPager {
    private boolean isVerticalScrollMode;
    public static final int DEFAULT_TRANSFORMER = 0;
    public static final int STACK_TRANSFORMER = 1;
    public static final int ZOOM_OUT_TRANSFORMER = 2;

    public VerticalViewPager(Context context) {
        super(context);
        init(context);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setTransformer(DEFAULT_TRANSFORMER);
    }

    public void setVerticalScrollMode(boolean verticalScrollMode) {
        isVerticalScrollMode = verticalScrollMode;
        if (isVerticalScrollMode) {
            this.setOverScrollMode(OVER_SCROLL_NEVER);
        } else {
            this.setOverScrollMode(OVER_SCROLL_ALWAYS);
        }
    }

    public void setTransformer(int transformer) {
        this.setPageTransformer(true, getTransformer(transformer));
    }

    private ViewPager.PageTransformer getTransformer(int transformer) {
        ViewPager.PageTransformer pageTransformer;
        switch (transformer) {
            case 1:
                pageTransformer = new StackTransformer();
                break;
            case 2:
                pageTransformer = new ZoomOutTransformer();
                break;
            case 0:
            default:
                pageTransformer = new DefaultTransformer();
                break;
        }
        return pageTransformer;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isVerticalScrollMode) {
            boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(ev));
            swapTouchEvent(ev);// 再一次调用是为了让事件复原，不干扰其他函数对事件的处理
            return intercept;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isVerticalScrollMode) {
            return super.onTouchEvent(swapTouchEvent(ev));
        }
        return super.onTouchEvent(ev);
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float w = getWidth();
        float h = getHeight();
        event.setLocation((event.getY() / h) * w, (event.getX() / w) * h);
        return event;
    }

    private class DefaultTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            if (isVerticalScrollMode) {
                view.setTranslationX(view.getWidth() * -position);
                view.setTranslationY(view.getHeight() * position);
            }
        }
    }

    private class StackTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            page.setTranslationX(page.getWidth() * -position);
            page.setTranslationY(position < 0 ? position * page.getHeight() : 0f);
        }
    }

    private class ZoomOutTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.90f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            float alpha = 0;
            if (0 <= position && position <= 1) {
                alpha = 1 - position;
            } else if (-1 < position && position < 0) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
                float horizontalMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horizontalMargin - verticalMargin / 2);
                } else {
                    view.setTranslationX(-horizontalMargin + verticalMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                alpha = position + 1;
            }

            view.setAlpha(alpha);
            view.setTranslationX(view.getWidth() * -position);
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }

    }

}
