package cn.xhl.client.manga.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import cn.xhl.client.manga.R;


/**
 * @author lixiuhao on 2017/5/13 0013.
 * 自定义加载圈
 */

public class DefaultLoading extends Dialog {
    private TextView tv;

    public DefaultLoading(@NonNull Context context) {
        super(context, R.style.loadingProgressbarStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbar_default);

        tv = findViewById(R.id.tv_progressbar_default);
        // 默认点击外界不被取消
        this.setCanceledOnTouchOutside(false);
    }

    public void setText(String str) {
        tv.setText(str);
    }
}
