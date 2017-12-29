package cn.xhl.client.manga.view.auth;

import android.os.Bundle;

import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.presenter.auth.LoginPresenter;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.view.auth.fragment.LoginFragment;

/**
 * 登录认证、注册等页面
 */
public class AuthActivity extends BaseActivity implements BaseFragment.BackHandledInterface {

    public static final String LOGIN_TAG = "loginFragment";
    public static final String RESET_TAG = "resetFragment";
    public static final String REGISTER_TAG = "registerFragment";

    private BaseFragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_alpha_out);// 载入载出动画
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentByTag(LOGIN_TAG);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
        }
        ActivityUtil.switchContentHideCurrent(this, null, loginFragment, LOGIN_TAG, R.id.content_activity_auth);// google的demo里是把这句话放到上面的判空括号中
        new LoginPresenter(loginFragment);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_auth;
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        selectedFragment.onBackPressed();
    }


}

