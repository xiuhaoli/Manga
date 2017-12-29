package cn.xhl.client.manga.presenter.gallery;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.facebook.common.internal.Closeables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import cn.xhl.client.manga.MyApplication;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.gallery.BrowseImageContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.gallery.Req_BrowseImage;
import cn.xhl.client.manga.model.bean.response.gallery.Res_BrowseImage;
import cn.xhl.client.manga.utils.FileUtil;
import cn.xhl.client.manga.utils.LogUtil;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.utils.SystemUtil;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */
public class BrowseImagePresenter implements BrowseImageContract.Presenter {
    private BrowseImageContract.View view;
    /**
     * 这里的key值都是按照1为起始位置
     */
    private SparseArray<Uri> uris;
    private SparseArray<String> imgkeys;
    private int gid;
    private String showkey;
    private final static String LAST_IMG_KEY = "end";
    private int filecount;
    private CompositeDisposable compositeDisposable;

    public BrowseImagePresenter(BrowseImageContract.View view,
                                int filecount, String secondImgkey,
                                int gid, String showkey, String firstImgUrl) {
        this.view = view;
        this.filecount = filecount;
        this.gid = gid;
        this.showkey = showkey;
        view.setPresenter(this);
        uris = new SparseArray<>(filecount);
        uris.put(1, Uri.parse(firstImgUrl));
        imgkeys = new SparseArray<>(filecount);
        imgkeys.put(2, secondImgkey);

        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {
        for (int i = 1; i <= uris.size(); i++) {
            Uri uri = uris.get(i);
            view.clearUriFromMemoryCache(uri);
            view.clearUriFromDiskCache(uri);
        }
        compositeDisposable.clear();
    }

    @Override
    public void reqImgUrl(final int page) {
        // 如果发现请求的图片url不为空就直接返回
        if (StringUtil.isNotEmpty(uris.get(page))) {
            view.setUrl(page, uris.get(page));
            return;
        }
        if (imgkeys.get(page) == null) {
            return;
        }
        Req_BrowseImage req_browseImage = new Req_BrowseImage();
        req_browseImage.setGid(gid);
        req_browseImage.setImgkey(imgkeys.get(page));
        req_browseImage.setPage(page);
        req_browseImage.setShowkey(showkey);
        compositeDisposable.add(RetrofitFactory
                .getApiEh()
                .obtainImageUrl(StringUtil.getRequestBody(new Gson().toJson(req_browseImage)))
                .compose(RxSchedulesHelper.<Res_BrowseImage>io_ui())
                .subscribeWith(new DisposableObserver<Res_BrowseImage>() {
                    @Override
                    public void onNext(Res_BrowseImage res_browseImage) {
                        String imgkey = LAST_IMG_KEY;
                        if (page != filecount) {
                            // 这是下一张图的imgkey
                            imgkey = res_browseImage.getI3().split("'")[1].substring(0, 10);
                        }
                        // 这是当前的请求的图片URL
                        String url = res_browseImage.getI3().split("src")[1].split("\"")[1];
                        Uri uri = Uri.parse(url);
                        uris.put(page, uri);
                        imgkeys.put(page + 1, imgkey);
                        view.setUrl(page, uri);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    /**
     * 将当前页面的隔一页的地方的图片清除
     *
     * @param currPage 当前的页数
     */
    @Override
    public void startClearUriMemoryTask(int currPage) {
        Uri before = uris.get(currPage - 2);
        Uri after = uris.get(currPage + 2);
        view.clearUriFromMemoryCache(before);
        view.clearUriFromMemoryCache(after);
    }

    @Override
    public boolean haveImgkey(int page) {
        return imgkeys.get(page) != null;
    }

    @Override
    public void saveImage2Local(final int page) {
        if (uris.get(page) == null) {
            view.showTipMsg("fail");
            return;
        }
        compositeDisposable.add(Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        DataSource<CloseableReference<PooledByteBuffer>> dataSource =
                                Fresco.getImagePipeline().fetchEncodedImage(ImageRequest.fromUri(uris.get(page)), null);
                        while (!dataSource.hasResult() && !dataSource.isFinished() && !dataSource.hasFailed()) {
                            // 当且仅当dataSource没有失败、没有结束、没有结果时才会循坏
                            Thread.sleep(100);
                        }
                        CloseableReference<PooledByteBuffer> reference = dataSource.getResult();
                        if (reference == null) {
                            emitter.onNext("fail");
                            return;
                        }
                        InputStream is = new PooledByteBufferInputStream(reference.get());
                        try {
                            ImageFormat imageFormat = ImageFormatChecker.getImageFormat(is);
                            FileUtil.getInstance().saveInputStream(is, FileUtil.getInstance().getImagePath() + File.separator + SystemUtil.getTimeStamp() + "." + imageFormat.getName().toLowerCase());
                        } catch (IOException e) {
                            emitter.onNext("fail");
                        } finally {
                            Closeables.closeQuietly(is);
                        }
                        emitter.onNext("success");
                    }
                })
                .compose(RxSchedulesHelper.<String>computation_ui())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        view.showTipMsg(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );

    }
}
