package cn.xhl.client.manga.presenter.gallery;

import com.google.gson.Gson;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.gallery.FavoriteContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.gallery.Req_Favorite;
import cn.xhl.client.manga.model.bean.request.gallery.Req_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
import cn.xhl.client.manga.utils.StringUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

/**
 * Created by xiuhaoli on 2017/11/17.
 */
public class FavoritePresenter implements FavoriteContract.Presenter {
    private FavoriteContract.View view;
    private CompositeDisposable compositeDisposable;
    private int page = 0;
    private int size = 15;
    private boolean loadMore = false;

    public FavoritePresenter(FavoriteContract.View view) {
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
    public void listFolder(final boolean isLoadMore) {
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
        Req_FavoriteFolder reqFavoriteFolder = new Req_FavoriteFolder();
        reqFavoriteFolder.setPage(page);
        reqFavoriteFolder.setSize(size);
        reqFavoriteFolder.setId(0);
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .listFavoriteFolders(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setSign(SignUtil.generateSign(String.valueOf(0),String.valueOf(size), String.valueOf(page)))
                        .setData(reqFavoriteFolder)
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<Res_FavoriteFolder>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .subscribeWith(new BaseObserver<Res_FavoriteFolder>() {
                    @Override
                    protected void onHandleSuccess(Res_FavoriteFolder resFavoriteFolder) {
                        ++page;
                        loadMore = (size == resFavoriteFolder.getFolders().size());
                        // 如果不是加载更多且返回的集合长度为0，则显示noData
                        if (!isLoadMore && resFavoriteFolder.getFolders().size() == 0) {
                            view.showNoData();
                        } else {
                            view.notifyAdapterFolder(resFavoriteFolder);
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

    @Override
    public void listFavorite(final boolean isLoadMore, String folder) {
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
        Req_Favorite reqFavorite = new Req_Favorite();
        reqFavorite.setPage(page);
        reqFavorite.setSize(size);
        reqFavorite.setFolder(folder);
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .listFavorite(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setSign(SignUtil.generateSign(folder, String.valueOf(size), String.valueOf(page)))
                        .setData(reqFavorite)
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
                            view.notifyAdapterFavorite(galleryList);
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
