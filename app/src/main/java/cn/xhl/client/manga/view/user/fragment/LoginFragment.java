package cn.xhl.client.manga.view.user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import cn.xhl.client.manga.BaseFragment;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.contract.LoginContract;

/**
 * Created by lixiuhao on 2017/9/22 0022.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {

    private LoginContract.Presenter presenter;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscibe();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        TextInputLayout email = view.findViewById(R.id.email_fragment_login);
        TextInputLayout password = view.findViewById(R.id.password_fragment_login);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.auto_fragment_login);

        email.setHint(getResources().getString(R.string.prompt_email));
        password.setHint(getResources().getString(R.string.prompt_password));

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void hideTopBar() {
        mActivity.hideTopBar();
    }

    @Override
    public void showLoading() {
        mActivity.showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        mActivity.hideLoadingDialog();
    }

    @Override
    public void showErrorDialog() {

    }

    @Override
    public void hideErrorDialog() {

    }

    @Override
    public void hideKeyboard() {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
