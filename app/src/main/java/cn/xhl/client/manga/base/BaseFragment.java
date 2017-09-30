package cn.xhl.client.manga.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by lixiuhao on 2017/9/22 0022.
 */
public abstract class BaseFragment extends RxFragment {
    protected BaseActivity mActivity;
    protected BackHandledInterface backHandledInterface;

    /**
     * 获得全局的，防止使用getActivity()为空 * @param context
     */
    @Override
    public void onAttach(android.app.Activity context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
        if (mActivity instanceof BackHandledInterface) {
            backHandledInterface = (BackHandledInterface) mActivity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (backHandledInterface != null) {
            backHandledInterface.setSelectedFragment(this);
        }
    }

    /**
     * 获取fragment的layout
     */
    protected abstract int layoutId();

    /**
     * 该抽象方法就是 初始化view * @param view * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    public void onBackPressed() {
        if (backHandledInterface == null) {
            return;
        }
        mActivity.getFragmentManager().popBackStack();
    }

    public interface BackHandledInterface {
        void setSelectedFragment(BaseFragment selectedFragment);
    }
}
