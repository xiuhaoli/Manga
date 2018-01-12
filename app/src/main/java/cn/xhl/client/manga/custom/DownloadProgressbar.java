package cn.xhl.client.manga.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import cn.xhl.client.manga.R;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/09
 *     version: 1.0
 * </pre>
 */
public class DownloadProgressbar extends Dialog {
    public DownloadProgressbar(@NonNull Context context) {
        this(context, 0);
    }

    public DownloadProgressbar(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progressbar);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
}
