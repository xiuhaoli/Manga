package cn.xhl.client.manga.presenter.main;

import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.Callable;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.main.MineContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.gallery.Req_ModifyProfileHeader;
import cn.xhl.client.manga.model.bean.request.gallery.Req_ModifyUsername;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.utils.FileUtil;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.utils.SystemUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Mike on 2017/10/9 0009.
 */
public class MinePresenter implements MineContract.Presenter {
    private MineContract.View view;
    private FileUtil fileUtil = FileUtil.getInstance();
    private File file = new File(fileUtil.getStorageDirectory());
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
    public String cacheSize() {
        return SystemUtil.formatNumber2(fileUtil.getDirectorySize(file, 0) / 1024 / 1024) + "M";
    }

    @Override
    public void clearCache() {
        view.showLoading();
        Flowable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                fileUtil.deleteFile(file);
                return "success";
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        view.notifyAdapterForCacheItem(cacheSize());
                        view.hideLoading();
                    }
                });
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
