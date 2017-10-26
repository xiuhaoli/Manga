package cn.xhl.client.manga.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * @author Mike on 2017/9/22 0022.
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
        mActivity.onBackPressed();
//        if (backHandledInterface == null) {
//            return;
//        }
//        if (mActivity.getFragmentManager().getBackStackEntryCount() != 1) {
//            mActivity.getFragmentManager().popBackStack();
//        } else {
//            mActivity.onBackPressed();
//        }
    }


    /**
     * 如果需要Fragment的返回监听，就在对应的activity中实现该接口
     * <p>
     * 实现该接口后，就可以在fragment里面重写onBackPressed方法
     */
    public interface BackHandledInterface {
        void setSelectedFragment(BaseFragment selectedFragment);
    }
}
