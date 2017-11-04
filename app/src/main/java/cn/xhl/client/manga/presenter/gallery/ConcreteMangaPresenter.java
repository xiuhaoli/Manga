package cn.xhl.client.manga.presenter.gallery;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;

import cn.xhl.client.manga.MyApplication;
import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.gallery.ConcreteMangaContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.Req_Subscribe;
import cn.xhl.client.manga.model.bean.request.Req_Viewed;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;
import cn.xhl.client.manga.model.bean.response.Res_Subscribe;
import cn.xhl.client.manga.model.bean.response.Res_Viewed;
import cn.xhl.client.manga.utils.LogUtil;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.utils.SystemUtil;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */
public class ConcreteMangaPresenter implements ConcreteMangaContract.Presenter {
    private static final String TAG = "ConcreteMangaPresenter";
    private ConcreteMangaContract.View view;
    private Call call;
    private String showkey;
    private String imgkey;
    private String firstImg;
    private CompositeDisposable compositeDisposable;

    public ConcreteMangaPresenter(ConcreteMangaContract.View view) {
        this.view = view;
        view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
        if (call.isCanceled()) {
            return;
        }
        call.cancel();
    }

    @Override
    public void parsePage(Res_GalleryList.GalleryEntity galleryEntity) {
        if (StringUtil.isNotEmpty(showkey)) {
            return;
        }
        view.showLoading();
        String firstImgkey = galleryEntity.getThumb().split("/")[5].substring(0, 10);
        int gid = galleryEntity.getGid();
        String url = "https://e-hentai.org/s/" + firstImgkey + "/" + gid + "-1";

        call = MyApplication.getOkHttpClient().newCall(new Request.Builder()
                .url(url)
                .get()
                .build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@Nullable Call call, @Nullable IOException e) {
                view.hideLoading();
                view.showTipMsg("request failed");
            }

            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
                ResponseBody body = null;
                try {
                    view.hideLoading();
                    if (response == null) {
                        view.showTipMsg("request failed");
                        return;
                    }
                    body = response.body();
                    if (body == null) {
                        view.showTipMsg("request failed");
                        return;
                    }
                    String text = body.string();
                    if (!text.contains("showkey") || !text.contains("next") || !text.contains("load_image")) {
                        LogUtil.eLocal(TAG + SystemUtil.getTimeStamp(), text);
                        view.showTipMsg("parse content failed");
                        return;
                    }
                    showkey = text.split("showkey")[1].substring(2, 13);
                    imgkey = text.split("next")[1].split("load_image")[1].split("'")[1].substring(0, 10);
                    firstImg = text.split("i3")[1].split("src")[1].split("\"")[1];
                } finally {
                    if (body != null) {
                        body.close();
                    }
                }

            }
        });

    }

    @Override
    public String getShowKey() {
        return showkey;
    }

    @Override
    public String getImgKey() {
        return imgkey;
    }

    @Override
    public String getFirstImg() {
        return firstImg;
    }

    @Override
    public void favorite(int galleryId) {
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .subscribe(StringUtil.getRequestBody(new Gson().toJson(
                        new BaseRequest.Builder()
                                .setData(new Req_Subscribe(galleryId))
                                .setSign(SignUtil.generateSign(String.valueOf(galleryId)))
                                .build()
                )))
                .compose(RxSchedulesHelper.<BaseResponse<Res_Subscribe>>io_ui())
                .subscribeWith(new BaseObserver<Res_Subscribe>() {
                    @Override
                    protected void onHandleSuccess(Res_Subscribe res_subscribe) {
                        view.changeStarNumber(res_subscribe.getDelta());
                        view.showToastMsg(res_subscribe.getMsg());
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showToastMsg(msg);
                    }
                }));
    }

    @Override
    public void viewed(int galleryId) {
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .viewed(StringUtil.getRequestBody(new Gson().toJson(
                        new BaseRequest.Builder()
                                .setData(new Req_Viewed(galleryId))
                                .setSign(SignUtil.generateSign(String.valueOf(galleryId)))
                                .build()
                )))
                .compose(RxSchedulesHelper.<BaseResponse<Res_Viewed>>io_ui())
                .subscribeWith(new BaseObserver<Res_Viewed>() {
                    @Override
                    protected void onHandleSuccess(Res_Viewed res_subscribe) {
                        view.plusViewedNumber();
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                    }
                }));
    }

}
