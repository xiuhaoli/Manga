package cn.xhl.client.manga.custom;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.utils.StringUtil;

/**
 * @author Mike on 2017/9/29 0029.
 * <p>
 * 自定义的倒计时按钮
 */
public class CountDownButton extends android.support.v7.widget.AppCompatButton {
    private Activity context;
    private int millisInFuture = 60 * 1000;// 倒计时长度，默认60秒
    private int countDownInterval = 1000;// 时间间隔，默认1秒
    private String textAfterSeconds = "s";// 倒计时数字后面的字符串,默认为s
    private int textColorAfter = Color.parseColor("#FF4081");// 倒计时的文字颜色
    private String textBeforeStart;// 倒计时开始前的字符串
    private int textColorBeforeStart;// 倒计时开始前的文字颜色

    public CountDownButton(Context context) {
        this(context, null, 0);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = (Activity) context;
        textBeforeStart = getText().toString();
        textColorBeforeStart = getCurrentTextColor();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CountDownButton, defStyleAttr, 0);
        int a = typedArray.getIndexCount();
        for (int i = 0; i < a; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CountDownButton_millisInFuture:
                    int temp = typedArray.getInt(attr, 0);
                    if (temp > 0) {
                        millisInFuture = temp * 1000;
                    }
                    break;
                case R.styleable.CountDownButton_countDownInterval:
                    int temp_ = typedArray.getInt(attr, 0);
                    if (temp_ > 0) {
                        countDownInterval = temp_ * 1000;
                    }
                    break;
                case R.styleable.CountDownButton_textAfterSeconds:
                    String temp_text = typedArray.getString(attr);
                    if (StringUtil.isNotEmpty(temp_text)) {
                        textAfterSeconds = temp_text;
                    }
                    break;
                case R.styleable.CountDownButton_textColorAfter:
                    textColorAfter = typedArray.getColor(attr, textColorAfter);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();

    }

    public void startCountDown() {
        CountDown countDown = new CountDown(millisInFuture, countDownInterval);
        countDown.start();
        CountDownButton.this.setTextColor(textColorAfter);
        CountDownButton.this.setClickable(false);
    }

    private class CountDown extends CountDownTimer {

        private CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (context.isFinishing()) {
                this.cancel();
                return;
            }
            CountDownButton.this.setText(context.getResources().getString(R.string.countdown, millisUntilFinished / 1000, textAfterSeconds));
        }

        @Override
        public void onFinish() {
            CountDownButton.this.setText(textBeforeStart);
            CountDownButton.this.setTextColor(textColorBeforeStart);
            CountDownButton.this.setClickable(true);
            this.cancel();
        }
    }
}
