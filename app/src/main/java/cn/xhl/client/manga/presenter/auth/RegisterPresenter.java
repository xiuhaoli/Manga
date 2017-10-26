package cn.xhl.client.manga.presenter.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.auth.RegisterContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.Res_GetVerify;
import cn.xhl.client.manga.model.bean.response.Res_Register;
import cn.xhl.client.manga.utils.MD5Util;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * @author Mike on 2017/9/29 0029.
 * <p>
 * 注册逻辑
 */
public class RegisterPresenter implements RegisterContract.Presenter {
    private RegisterContract.View view;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private CompositeDisposable compositeDisposable;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        view.createView();
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void obtainVerify(String email) {
        view.hideKeyboard();
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .getVerify(email)
                .compose(RxSchedulesHelper.<BaseResponse<Res_GetVerify>>io_ui())
                .subscribeWith(new BaseObserver<Res_GetVerify>() {
                    @Override
                    protected void onHandleSuccess(Res_GetVerify res_getVerify) {
                        view.showTipMsg(res_getVerify.getMsg());
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showTipMsg(msg);
                    }
                }));
    }

    @Override
    public boolean isEmailValid(String email) {
        Matcher matcher = pattern.matcher(email);
        boolean result = matcher.matches();
        if (!result) {
            view.showTipMsg("邮箱格式不对喔～");
        }
        return result;
    }

    @Override
    public boolean isPasswordValid(String password) {
        int length = password.length();
        boolean result = length > 5 && length < 33;
        if (!result) {
            view.showTipMsg("密码长度不符合要求!");
        }
        return result;
    }

    @Override
    public void submit(String email, String password, String verify) {
        view.hideKeyboard();
        view.showLoading();
        password = MD5Util.encrypt(email + password + IConstants.PASSWORD_SALT);
        compositeDisposable.add(
                RetrofitFactory
                        .getApiUser()
                        .register(email, password, verify)
                        .compose(RxSchedulesHelper.<BaseResponse<Res_Register>>io_ui())
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                view.hideLoading();
                            }
                        })
                        .subscribeWith(new BaseObserver<Res_Register>() {
                            @Override
                            protected void onHandleSuccess(Res_Register res_register) {
                                view.showTipMsg(res_register.getMsg());
                                view.back2Login();
                            }

                            @Override
                            protected void onHandleError(long code, String msg) {
                                view.showTipMsg(msg);
                            }
                        }));
    }
}
