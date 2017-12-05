package cn.xhl.client.manga.presenter.main;

import com.google.gson.Gson;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.main.LatestContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.gallery.Req_GalleryList;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
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
    private int size = 15;
    private boolean loadMore = false;

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
        if (!isLoadMore) {
            view.showLoading();
        } else {
            if (!loadMore) {
                view.noMoreToLoad();
                return;
            }
        }
        view.hideReTry();
        view.hideNoData();
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
                        loadMore = (size == galleryList.getData().size());
                        // 如果不是加载更多且返回的集合长度为0，则显示noData
                        if (!isLoadMore && galleryList.getData().size() == 0) {
                            view.showNoData();
                        } else {
                            view.notifyAdapter(galleryList);
                        }
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showTipMsg(msg);
                        if (isLoadMore) {
                            view.failLoadMore();
                        } else {
                            view.showReTry();
                        }
                    }
                }));
    }
}
