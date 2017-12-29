package cn.xhl.client.manga.presenter.gallery;

import java.io.IOException;

import cn.xhl.client.manga.contract.gallery.SummaryContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.utils.LogUtil;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.StringUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/19
 *     version: 1.0
 * </pre>
 */
public class SummaryPresenter implements SummaryContract.Presenter {
    private SummaryContract.View view;
    private String showkey;
    private String imgkey;
    private String firstImg;
    private CompositeDisposable compositeDisposable;

    public SummaryPresenter(SummaryContract.View view) {
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
                        if (e.toString().contains("HTTP 404")) {
                            view.showToastMsg("This gallery has been removed or is unavailable.");
                        } else {
                            view.showToastMsg("request failed");
                        }
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
}
