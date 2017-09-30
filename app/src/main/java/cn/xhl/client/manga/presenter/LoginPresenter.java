package cn.xhl.client.manga.presenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.contract.LoginContract;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.Res_Login;
import cn.xhl.client.manga.utils.MD5Util;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

/**
 * Created by lixiuhao on 2017/9/22 0022.
 * <p>
 * login presenter
 */
public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View loginView;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private CompositeDisposable compositeDisposable;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
        loginView.setPresenter(this);

        compositeDisposable = new CompositeDisposable();

    }

    @Override
    public void subscribe() {
        loginView.createView();// 初始化各类控件
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public boolean isEmailValid(String email) {
        Matcher matcher = pattern.matcher(email);
        boolean result = matcher.matches();
        if (!result) {
            loginView.showTipMsg("邮箱格式不对喔～");
        }
        return result;
    }

    @Override
    public boolean isPasswordValid(String password) {
        int length = password.length();
        boolean result = length > 5 && length < 33;
        if (!result) {
            loginView.showTipMsg("密码长度不符合要求!");
        }
        return result;
    }

    @Override
    public void activateLoginTask(String email, String password) {
        loginView.hideKeyboard();
        loginView.showLoading();
//        password = BCrypt.hashpw(password, BCrypt.gensalt());
        password = MD5Util.encrypt(email + password + email);// md5加密password
        compositeDisposable.add(RetrofitFactory.getApiUser().login(email, password)
                .compose(RxSchedulesHelper.<BaseResponse<Res_Login>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        loginView.hideLoading();
                    }
                })
                .subscribeWith(new BaseObserver<Res_Login>() {
                    @Override
                    protected void onHandleSuccess(Res_Login res_login) {
                        loginView.showTipMsg(res_login.toString());
                        loginView.saveLoginInfo(res_login);
                        loginView.change2MainActivity();
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        loginView.showTipMsg(msg);
                    }
                }));

    }


}
