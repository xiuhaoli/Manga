package cn.xhl.client.manga.presenter.gallery;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.gallery.ConcreteMangaContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.gallery.Req_FavoriteFolder;
import cn.xhl.client.manga.model.bean.request.gallery.Req_Subscribe;
import cn.xhl.client.manga.model.bean.request.gallery.Req_Viewed;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.model.bean.response.gallery.Res_Subscribe;
import cn.xhl.client.manga.model.bean.response.gallery.Res_Viewed;
import cn.xhl.client.manga.utils.LogUtil;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.utils.SystemUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */
public class ConcreteMangaPresenter implements ConcreteMangaContract.Presenter {
    private static final String TAG = "ConcreteMangaPresenter";
    private ConcreteMangaContract.View view;
    private String showkey;
    private String imgkey;
    private String firstImg;
    private CompositeDisposable compositeDisposable;
    private int page = 0;
    private int size = 15;
    private boolean loadMore = false;

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
        view.hideLoading();
        view.dismissBottomSheet();
        compositeDisposable.clear();
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
        compositeDisposable.add(RetrofitFactory
                .getApiEh()
                .parseThirdPage(url)
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .compose(RxSchedulesHelper.<ResponseBody>io_ui())
                .subscribeWith(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String text = responseBody.string();
                            if (!text.contains("showkey") || !text.contains("next") || !text.contains("load_image")) {
                                LogUtil.eLocal(TAG + SystemUtil.getTimeStamp(), text);
                                view.showToastMsg("This gallery has been removed or is unavailable.");
                                return;
                            }
                            showkey = text.split("showkey")[1].substring(2, 13);
                            imgkey = text.split("next")[1].split("load_image")[1].split("'")[1].substring(0, 10);
                            firstImg = text.split("i3")[1].split("src")[1].split("\"")[1];
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            responseBody.close();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showToastMsg("request failed: onFailure");
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );

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
    public void initReqListData() {
        page = 0;
    }

    @Override
    public void favorite(int galleryId, String folder) {
        view.showLoading();
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .subscribe(StringUtil.getRequestBody(new Gson().toJson(
                        new BaseRequest.Builder()
                                .setData(new Req_Subscribe(galleryId, folder))
                                .setSign(SignUtil.generateSign(String.valueOf(galleryId), folder))
                                .build()
                )))
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
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

    @Override
    public void listFolder(final boolean isLoadMore, int id) {
        if (!isLoadMore) {
            view.startRefreshing();
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
        reqFavoriteFolder.setId(id);
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .listFavoriteFolders(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setSign(SignUtil.generateSign(String.valueOf(id), String.valueOf(size), String.valueOf(page)))
                        .setData(reqFavoriteFolder)
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<Res_FavoriteFolder>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.stopRefreshing();
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
                            if (!isLoadMore) {
                                sort(resFavoriteFolder);
                            }
                            view.notifyAdapterFolder(resFavoriteFolder);
                        }
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showToastMsg(msg);
                        if (isLoadMore) {
                            view.failLoadMore();
                        } else {
                            view.showReTry();
                        }
                    }
                }));

    }

    @Override
    public void sort(Res_FavoriteFolder favoriteFolder) {
        // 如果发现有参数是true，将其移动到第一位去
        List<Res_FavoriteFolder.Data> folders = favoriteFolder.getFolders();
        Iterator<Res_FavoriteFolder.Data> iterator = folders.iterator();
        while (iterator.hasNext()) {
            Res_FavoriteFolder.Data folder = iterator.next();
            if (folder.isChecked()) {
                iterator.remove();
                favoriteFolder.getFolders().add(0, folder);
                break;
            }
        }
    }

}
