package cn.xhl.client.manga.presenter.main;

import com.google.gson.Gson;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.main.LatestContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.Req_GalleryList;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
import cn.xhl.client.manga.utils.StringUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

/**
 * @author Mike on 2017/10/23 0023.
 */

public class LatestPresenter implements LatestContract.Presenter {
    private LatestContract.View view;
    private CompositeDisposable compositeDisposable;
    private int page = 0;
    private int size = 10;
    private boolean loadMore = true;

    public LatestPresenter(LatestContract.View view) {
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
    }

    @Override
    public void list(String category, String type, final boolean isLoadMore) {
        if (!loadMore) {
            view.noMoreToLoad();
            return;
        }
        view.hideReTry();
//        if ((IConstants.RECOMMEND.equals(type) || IConstants.CATEGORY_LATEST.equals(type)
//                || IConstants.HISTORY.equals(type) || IConstants.FAVORITE.equals(type)) && !isLoadMore) {
//            view.showLoading();
//        }
        if (!isLoadMore) {
            view.showLoading();
        }
        Req_GalleryList reqGalleryList = new Req_GalleryList();
        reqGalleryList.setCategory(category);
        reqGalleryList.setPage(page);
        reqGalleryList.setSize(size);
        reqGalleryList.setType(type);
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .galleryList(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setSign(SignUtil.generateSign(String.valueOf(size), String.valueOf(page), category, type))
                        .setData(reqGalleryList)
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<Res_GalleryList>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .subscribeWith(new BaseObserver<Res_GalleryList>() {
                    @Override
                    protected void onHandleSuccess(Res_GalleryList galleryList) {
                        ++page;
                        loadMore = galleryList.isLoadMore();
                        view.notifyAdapter(galleryList);
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        if (isLoadMore) {
                            view.failLoadMore();
                        } else {
                            view.showReTry();
                        }
                    }
                }));
    }
}
