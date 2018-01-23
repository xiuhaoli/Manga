package cn.xhl.client.manga.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.utils.DpUtil;

/**
 * 自定义的ImageSpan
 * image大小根据文字大小设定
 * 目前只提供左侧图片
 *
 * @author lixiuhao on 2017/11/1 0001.
 */
public class TextImageSpan extends android.support.v7.widget.AppCompatTextView {
    private Context mContext;

    public TextImageSpan(Context context) {
        this(context, null, 0);
    }

    public TextImageSpan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextImageSpan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextImageSpan, defStyleAttr, 0);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.TextImageSpan_imageSpanLeft:
                    Drawable leftDrawable = ActivityCompat.getDrawable(context,
                            typedArray.getResourceId(attr, 0));
                    if (leftDrawable != null) {
                        // 根据文字的高度来设置drawable的长宽
                        int h = getFontHeight(getTextSize());
                        leftDrawable.setBounds(0, 0, h, h);
                        this.setCompoundDrawables(leftDrawable, null, null, null);
                        this.setCompoundDrawablePadding(DpUtil.dp2Px(context, 5));
                    }
                    break;
                case R.styleable.TextImageSpan_imageSpanRight:
                    Drawable rightDrawable = ActivityCompat.getDrawable(context,
                            typedArray.getResourceId(attr, 0));
                    if (rightDrawable != null) {
                        // 根据文字的高度来设置drawable的长宽
                        int h = getFontHeight(getTextSize());
                        rightDrawable.setBounds(0, 0, h, h);
                        this.setCompoundDrawables(null, null, rightDrawable, null);
                        this.setCompoundDrawablePadding(DpUtil.dp2Px(context, 5));
                    }
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    public void setLeftImageSpan(int imgId) {
        Drawable leftDrawable = ActivityCompat.getDrawable(mContext, imgId);
        if (leftDrawable != null) {
            // 根据文字的高度来设置drawable的长宽
            int h = getFontHeight(getTextSize());
            leftDrawable.setBounds(0, 0, h, h);
            this.setCompoundDrawables(leftDrawable, null, null, null);
            this.setCompoundDrawablePadding(DpUtil.dp2Px(mContext, 5));
        }
    }

    /**
     * 根据textSize获取高度
     *
     * @param fontSize textSize
     * @return 高度
     */
    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent) + 2;
    }

}
