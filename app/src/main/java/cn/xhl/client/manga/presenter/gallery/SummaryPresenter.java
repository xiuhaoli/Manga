package cn.xhl.client.manga.presenter.gallery;

import com.google.gson.Gson;

import java.io.IOException;

import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.gallery.SummaryContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.user.Req_Attention;
import cn.xhl.client.manga.model.bean.request.user.Req_FollowExist;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.model.bean.response.user.Res_FollowExist;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
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
//        view.showLoading();
        String firstImgkey = galleryEntity.getThumb().split("/")[5].substring(0, 10);
        int gid = galleryEntity.getGid();
        String url = "https://e-hentai.org/s/" + firstImgkey + "/" + gid + "-1";
        compositeDisposable.add(RetrofitFactory
                .getApiEh()
                .parseThirdPage(url)
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
                            view.notifyActivity();
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

    @Override
    public void attendArtist(String artist) {
        view.showLoading();
        Req_Attention attention = new Req_Attention();
        attention.setFollow(artist);
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .attendArtist(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setSign(SignUtil.generateSign(artist))
                        .setData(attention)
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<String>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .subscribeWith(new BaseObserver<String>() {
                    @Override
                    protected void onHandleSuccess(String msg) {
                        view.showToastMsg(msg);
                        view.changeArtistFollowButton(msg.equals("followed"));
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showToastMsg(msg);
                    }
                }));
    }

    @Override
    public void attendUploader(String uploader) {
        view.showLoading();
        Req_Attention attention = new Req_Attention();
        attention.setFollow(uploader);
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .attendUploader(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setSign(SignUtil.generateSign(uploader))
                        .setData(attention)
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<String>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .subscribeWith(new BaseObserver<String>() {
                    @Override
                    protected void onHandleSuccess(String msg) {
                        view.showToastMsg(msg);
                        view.changeUploaderFollowButton(msg.equals("followed"));
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showToastMsg(msg);
                    }
                }));
    }

    @Override
    public void isFollowed(String artist, String uploader) {
        Req_FollowExist followExist = new Req_FollowExist();
        followExist.setArtist(artist);
        followExist.setUploader(uploader);
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .isFollowed(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setSign(SignUtil.generateSign(uploader, artist))
                        .setData(followExist)
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<Res_FollowExist>>io_ui())
                .subscribeWith(new BaseObserver<Res_FollowExist>() {
                    @Override
                    protected void onHandleSuccess(Res_FollowExist resFollowExist) {
                        view.changeArtistFollowButton(resFollowExist.isArtist());
                        view.changeUploaderFollowButton(resFollowExist.isUploader());
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showToastMsg(msg);
                    }
                }));
    }
}
