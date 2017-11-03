package cn.xhl.client.manga.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.utils.DpUtil;

/**
 * 提供一个浮层展示在屏幕中间, 一般使用 {@link Builder} 生成。
 *
 * @author cginechen
 * @date 2016-10-14
 */

public class QMUITipDialog extends Dialog {

    public QMUITipDialog(Context context) {
        this(context, R.style.QMUI_TipDialog);
    }

    public QMUITipDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();
    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(wmLp);
        }
    }

    public static class Builder {

        private Context mContext;

        private CharSequence mTipWord;

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置显示的文案
         */
        public Builder setTipWord(CharSequence tipWord) {
            mTipWord = tipWord;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        public QMUITipDialog create() {
            QMUITipDialog dialog = new QMUITipDialog(mContext);
            dialog.setContentView(R.layout.qmui_tip_dialog_layout);
            ViewGroup contentWrap = dialog.findViewById(R.id.contentWrap);

            QMUILoadingView loadingView = new QMUILoadingView(mContext);
            loadingView.setColor(Color.WHITE);
            loadingView.setSize(DpUtil.dp2Px(mContext, 32));
            LinearLayout.LayoutParams loadingViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            loadingView.setLayoutParams(loadingViewLP);
            contentWrap.addView(loadingView);

            if (mTipWord != null && mTipWord.length() > 0) {
                TextView tipView = new TextView(mContext);
                LinearLayout.LayoutParams tipViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                tipViewLP.topMargin = DpUtil.dp2Px(mContext, 12);
                tipView.setLayoutParams(tipViewLP);

                tipView.setEllipsize(TextUtils.TruncateAt.END);
                tipView.setGravity(Gravity.CENTER);
                tipView.setMaxLines(2);
                tipView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tipView.setText(mTipWord);

                contentWrap.addView(tipView);
            }
            return dialog;
        }

    }

}
