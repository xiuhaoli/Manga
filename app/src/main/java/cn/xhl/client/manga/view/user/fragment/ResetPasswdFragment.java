package cn.xhl.client.manga.view.user.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.contract.ResetPasswdContract;
import cn.xhl.client.manga.custom.CountDownButton;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.view.user.AuthActivity;

/**
 * Created by lixiuhao on 2017/9/22 0022.
 *
 * 重置密码View
 */
public class ResetPasswdFragment extends BaseFragment implements ResetPasswdContract.View, View.OnClickListener {
    private ResetPasswdContract.Presenter presenter;
    private TextInputLayout emailInputLayout;
    private TextInputLayout verifyInputLayout;
    private TextInputLayout passwordInputLayout;
    private LoginFragment loginFragment;
    private CountDownButton button;

    @Override
    protected int layoutId() {
        return R.layout.fragment_reset_password;
    }

    @Override
    public void setPresenter(ResetPasswdContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        emailInputLayout = view.findViewById(R.id.email_fragment_reset_password);
        verifyInputLayout = view.findViewById(R.id.verify_fragment_reset_password);
        passwordInputLayout = view.findViewById(R.id.password_fragment_reset_password);

        emailInputLayout.setHint(getResources().getString(R.string.prompt_email));
        verifyInputLayout.setHint(getResources().getString(R.string.prompt_verify));
        passwordInputLayout.setHint(getResources().getString(R.string.prompt_password));

        button = (CountDownButton) ControlUtil.initControlOnClick(R.id.verify_button_fragment_reset_password, view, this);
        ControlUtil.initControlOnClick(R.id.submit_button_fragment_reset_password, view, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
        loginFragment = (LoginFragment) mActivity.getFragmentManager().findFragmentByTag(AuthActivity.LOGIN_TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void createView() {
        mActivity.createLoading();
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
    public void showTipMsg(String msg) {
        mActivity.showToast(msg);
    }

    @Override
    public void back2Login() {
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
        }
        ActivityUtil.switchContentRemoveCurrent(mActivity, this, loginFragment, AuthActivity.LOGIN_TAG, R.id.contentFrame_activity_auth);
    }

    @Override
    public void countDown() {
        button.startCountDown();
    }

    @Override
    public void hideKeyboard() {
        mActivity.hideKeyboard();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_button_fragment_reset_password:
                String email = mActivity.getTextFromEt(emailInputLayout.getEditText());
                if (presenter.isEmailValid(email)) {
                    countDown();
                    presenter.obtainVerify(email);
                }
                break;
            case R.id.submit_button_fragment_reset_password:
                String email_ = mActivity.getTextFromEt(emailInputLayout.getEditText());
                String password = mActivity.getTextFromEt(passwordInputLayout.getEditText());
                String verify = mActivity.getTextFromEt(verifyInputLayout.getEditText());
                if (presenter.isEmailValid(email_) && presenter.isPasswordValid(password) && StringUtil.isNotEmpty(verify)) {
                    presenter.submit(email_, password, verify);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.switchContentHideCurrent(mActivity, this, loginFragment, AuthActivity.LOGIN_TAG, R.id.contentFrame_activity_auth);
        backHandledInterface.setSelectedFragment(loginFragment);
    }
}
