package cn.xhl.client.manga.presenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.ResetPasswdContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.Res_GetVerify;
import cn.xhl.client.manga.model.bean.response.Res_ResetPassword;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

/**
 * Created by lixiuhao on 2017/9/29 0029.
 * <p>
 * 重置密码Presenter
 */
public class ResetPasswdPresenter implements ResetPasswdContract.Presenter {
    private ResetPasswdContract.View view;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private CompositeDisposable compositeDisposable;

    public ResetPasswdPresenter(ResetPasswdContract.View view) {
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
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .resetPassword(email, password, verify)
                .compose(RxSchedulesHelper.<BaseResponse<Res_ResetPassword>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .subscribeWith(new BaseObserver<Res_ResetPassword>() {
                    @Override
                    protected void onHandleSuccess(Res_ResetPassword res_resetPassword) {
                        view.showTipMsg(res_resetPassword.getMsg());
                        view.back2Login();
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showTipMsg(msg);
                    }
                }));
    }
}
