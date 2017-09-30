package cn.xhl.client.manga.view.user.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.contract.LoginContract;
import cn.xhl.client.manga.model.bean.response.Res_Login;
import cn.xhl.client.manga.presenter.RegisterPresenter;
import cn.xhl.client.manga.presenter.ResetPasswdPresenter;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.PrefUtil;
import cn.xhl.client.manga.view.MainActivity;
import cn.xhl.client.manga.view.user.AuthActivity;


/**
 * Created by lixiuhao on 2017/9/22 0022.
 * <p>
 * login view
 */
public class LoginFragment extends BaseFragment implements LoginContract.View, View.OnClickListener {

    private LoginContract.Presenter presenter;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private RegisterFragment registerFragment;
    private ResetPasswdFragment resetPasswdFragment;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
        registerFragment = (RegisterFragment) mActivity.getFragmentManager().findFragmentByTag(AuthActivity.REGISTER_TAG);
        resetPasswdFragment = (ResetPasswdFragment) mActivity.getFragmentManager().findFragmentByTag(AuthActivity.RESET_TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        emailInputLayout = view.findViewById(R.id.email_fragment_login);
        passwordInputLayout = view.findViewById(R.id.password_fragment_login);

        emailInputLayout.setHint(getResources().getString(R.string.prompt_email));
        passwordInputLayout.setHint(getResources().getString(R.string.prompt_password));

        ControlUtil.initControlOnClick(R.id.sign_in_button_fragment_login, view, this);
        ControlUtil.initControlOnClick(R.id.register_fragment_login, view, this);
        ControlUtil.initControlOnClick(R.id.reset_password_fragment_login, view, this);
    }

    @Override
    public void createView() {
        mActivity.createLoading();
        mActivity.createFail();
        mActivity.addLeftTopBackButton();
        mActivity.changeTopbarColor(R.color.login_background);

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
    public void change2MainActivity() {
        ActivityUtil.jump2Activity(mActivity, MainActivity.class);
        mActivity.finish();
    }

    @Override
    public void change2Register() {
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
            ActivityUtil.switchContentHideCurrent(mActivity, this, registerFragment, AuthActivity.REGISTER_TAG, R.id.contentFrame_activity_auth);
            new RegisterPresenter(registerFragment);
            return;
        }
        ActivityUtil.switchContentHideCurrent(mActivity, this, registerFragment, AuthActivity.REGISTER_TAG, R.id.contentFrame_activity_auth);
        backHandledInterface.setSelectedFragment(registerFragment);
    }

    @Override
    public void change2Reset() {
        if (resetPasswdFragment == null) {
            resetPasswdFragment = new ResetPasswdFragment();
            ActivityUtil.switchContentHideCurrent(mActivity, this, resetPasswdFragment, AuthActivity.RESET_TAG, R.id.contentFrame_activity_auth);
            new ResetPasswdPresenter(resetPasswdFragment);
            return;
        }
        ActivityUtil.switchContentHideCurrent(mActivity, this, resetPasswdFragment, AuthActivity.RESET_TAG, R.id.contentFrame_activity_auth);
        backHandledInterface.setSelectedFragment(resetPasswdFragment);
    }

    @Override
    public void hideKeyboard() {
        mActivity.hideKeyboard();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button_fragment_login:
                String email = mActivity.getTextFromEt(emailInputLayout.getEditText());
                String password = mActivity.getTextFromEt(passwordInputLayout.getEditText());
                if (presenter.isEmailValid(email) && presenter.isPasswordValid(password)) {
                    presenter.activateLoginTask(email, password);
                }
                break;
            case R.id.reset_password_fragment_login:
                change2Reset();
                break;
            case R.id.register_fragment_login:
                change2Register();
                break;
            default:
                break;
        }
    }

    @Override
    public void saveLoginInfo(Res_Login res_login) {
        UserInfo userInfo = UserInfo.getInstance();
        String token = res_login.getAccess_token();
        String salt = res_login.getSalt();
        String uid = res_login.getUid();
        userInfo.setSalt(salt);
        userInfo.setToken(token);
        userInfo.setUid(uid);
        SharedPreferences.Editor editor = PrefUtil.get(mActivity).edit();
        editor.putString("token", token);
        editor.putString("salt", salt);
        editor.putString("uid", uid);
        editor.putString("expire_time", res_login.getExpire_time());
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        mActivity.finish();
    }
}
