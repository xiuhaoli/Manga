package cn.xhl.client.manga.view.user;

import android.app.FragmentTransaction;
import android.os.Bundle;

import cn.xhl.client.manga.BaseActivity;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.presenter.LoginPresenter;
import cn.xhl.client.manga.view.user.fragment.LoginFragment;

/**
 * 权限认证、申请等页面
 */
public class AuthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        LoginFragment loginFragment = (LoginFragment) getFragmentManager().findFragmentById(R.id.contentFrame_activity_auth);

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.contentFrame_activity_auth, loginFragment);
            fragmentTransaction.commit();
        }

        new LoginPresenter(loginFragment);
    }

}

