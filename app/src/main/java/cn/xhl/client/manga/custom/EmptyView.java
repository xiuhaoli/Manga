package cn.xhl.client.manga.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.xhl.client.manga.R;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/19
 *     version: 1.0
 * </pre>
 */
public class EmptyView extends FrameLayout {
    private QMUILoadingView mLoadingView;
    private TextView mNodataView;
    private View mRetryLayout;
    private Button mRetryButton;
    private OnClickListener onClickListener;

    public EmptyView(@NonNull Context context) {
        this(context, null, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.empty_layout, this, true);
        mLoadingView = findViewById(R.id.loading_empty_layout);
        mRetryLayout = findViewById(R.id.linear_empty_layout);
        mRetryButton = findViewById(R.id.btn_empty_layout);
        mNodataView = findViewById(R.id.no_data_empty_layout);
    }

    public void showRetry(View.OnClickListener clickListener) {
        mRetryLayout.setVisibility(VISIBLE);
        mRetryButton.setOnClickListener(clickListener);
    }

    public void hideRetry() {
        mRetryLayout.setVisibility(GONE);
    }

    public void showNodata() {
        mNodataView.setVisibility(VISIBLE);
    }

    public void hideNodata() {
        mNodataView.setVisibility(GONE);
    }

    public void showLoading() {
        mLoadingView.setVisibility(VISIBLE);
    }

    public void hideLoading() {
        mLoadingView.setVisibility(GONE);
    }
}
