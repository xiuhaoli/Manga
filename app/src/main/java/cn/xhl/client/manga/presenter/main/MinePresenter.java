package cn.xhl.client.manga.presenter.main;

import com.google.gson.Gson;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.main.MineContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.user.Req_ModifyProfileHeader;
import cn.xhl.client.manga.model.bean.request.user.Req_ModifyUsername;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
import cn.xhl.client.manga.utils.StringUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

/**
 * @author Mike on 2017/10/9 0009.
 */
public class MinePresenter implements MineContract.Presenter {
    private MineContract.View view;
    private CompositeDisposable compositeDisposable;

    public MinePresenter(MineContract.View view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void modifyUsername(final String newName) {
        view.showLoading();
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .modifyUsername(StringUtil.getRequestBody(
                        new Gson().toJson(
                                new BaseRequest.Builder()
                                        .setSign(SignUtil.generateSign(newName))
                                        .setData(new Req_ModifyUsername(newName))
                                        .build()
                        )
                ))
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .compose(RxSchedulesHelper.<BaseResponse<String>>io_ui())
                .subscribeWith(new BaseObserver<String>() {
                    @Override
                    protected void onHandleSuccess(String s) {
                        view.showTipMsg(s);
                        view.notifyUI2ChangeUsername(newName);
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showTipMsg(msg);
                    }
                })
        );
    }

    @Override
    public void modifyProfileHeader(final String url) {
        view.showLoading();
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .modifyProfileHeader(StringUtil.getRequestBody(
                        new Gson().toJson(
                                new BaseRequest.Builder()
                                        .setSign(SignUtil.generateSign(url))
                                        .setData(new Req_ModifyProfileHeader(url))
                                        .build()
                        )
                ))
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .compose(RxSchedulesHelper.<BaseResponse<String>>io_ui())
                .subscribeWith(new BaseObserver<String>() {
                    @Override
                    protected void onHandleSuccess(String s) {
                        view.showTipMsg(s);
                        view.notifyUI2ChangeHeader(url);
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showTipMsg(msg);
                    }
                })
        );
    }
}
