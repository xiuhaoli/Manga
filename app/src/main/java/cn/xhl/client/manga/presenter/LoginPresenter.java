package cn.xhl.client.manga.presenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xhl.client.manga.contract.LoginContract;

/**
 * Created by lixiuhao on 2017/9/22 0022.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View loginView;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
        loginView.setPresenter(this);
    }

    @Override
    public void subscibe() {

    }

    @Override
    public boolean checkValid(String email, String password) {
        int length = password.length();
        matcher = pattern.matcher(email);
        return matcher.matches() &&  length > 5 && length < 33;
    }

    @Override
    public void activateTask() {

    }

    @Override
    public void change2Register() {

    }

    @Override
    public void change2Reset() {

    }
}
