package cn.xhl.client.manga.presenter;

import com.google.gson.Gson;

import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.SplashContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.auth.Res_RefreshToken;
import cn.xhl.client.manga.utils.AnalyticsUtil;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.utils.SystemUtil;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Mike on 2017/9/28 0028.
 * <p>
 * splash presenter
 */
public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View splashView;
    private CompositeDisposable compositeDisposable;

    public SplashPresenter(SplashContract.View splashView) {
        this.splashView = splashView;
        splashView.setPresenter(this);

        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        splashView.initUserInfo();
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
        compositeDisposable = null;
        splashView = null;
    }

    @Override
    public boolean isUserInfoAvailable(String token, int uid, String salt, int expire_time) {
        if (StringUtil.isEmpty(token) || StringUtil.isEmpty(uid) || StringUtil.isEmpty(salt) || StringUtil.isEmpty(expire_time)) {
            return false;
        }
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.setToken(token);
        userInfo.setSalt(salt);
        userInfo.setUid(uid);
        return true;
    }

    @Override
    public void refreshToken(String token, int uid, String salt, int expire_time) {
        int timestamp = SystemUtil.getTimestamp();
        int diff = expire_time - timestamp;
        if (diff > 604800) {
            splashView.change2MainActivity();
            return;// 如果过期时间是在一个星期之后，则不进行刷新token的行为
        } else if (diff <= 0) {
            splashView.change2AuthActivity();// 如果时间已经过期则跳转登录页
            return;
        }
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .refreshToken(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setSign(SignUtil.generateSign())
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<Res_RefreshToken>>io_ui())
                .subscribeWith(new BaseObserver<Res_RefreshToken>() {
                    @Override
                    protected void onHandleSuccess(Res_RefreshToken res_refreshToken) {
                        splashView.handleUserInfo(res_refreshToken);
                        splashView.change2MainActivity();
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        splashView.change2MainActivity();
                    }
                }));
    }
}
