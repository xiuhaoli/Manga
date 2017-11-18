package cn.xhl.client.manga.presenter.gallery;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.google.gson.Gson;

import java.io.IOException;

import cn.xhl.client.manga.MyApplication;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.gallery.BrowseImageContract;
import cn.xhl.client.manga.model.bean.request.gallery.Req_BrowseImage;
import cn.xhl.client.manga.model.bean.response.gallery.Res_BrowseImage;
import cn.xhl.client.manga.utils.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */
public class BrowseImagePresenter implements BrowseImageContract.Presenter {
    private static final String TAG = "BrowseImagePresenter";
    private BrowseImageContract.View view;
    private Call call;
    /**
     * 这里的key值都是按照1为起始位置
     */
    private SparseArray<Uri> uris;
    private SparseArray<String> imgkeys;
    private int gid;
    private String showkey;
    private final static String LAST_IMG_KEY = "end";
    private int filecount;

    public BrowseImagePresenter(BrowseImageContract.View view, int filecount, String secondImgkey, int gid, String showkey, String firstImgUrl) {
        this.view = view;
        this.filecount = filecount;
        this.gid = gid;
        this.showkey = showkey;
        view.setPresenter(this);
        uris = new SparseArray<>(filecount);
        uris.put(1, Uri.parse(firstImgUrl));
        imgkeys = new SparseArray<>(filecount);
        imgkeys.put(2, secondImgkey);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        for (int i = 1; i < uris.size(); i++) {
            Uri uri = uris.get(i);
            view.clearUriFromMemoryCache(uri);
            view.clearUriFromDiskCache(uri);
        }
        if (call.isCanceled()) {
            return;
        }
        call.cancel();
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
        call = MyApplication.getOkHttpClient().newCall(new Request.Builder()
                .url(IConstants.POST_URL)
                .post(StringUtil.getRequestBody(new Gson().toJson(req_browseImage)))
                .build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@Nullable Call call, @Nullable IOException e) {

            }

            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {
                ResponseBody body = null;
                try {
                    if (response == null) {
                        return;
                    }
                    body = response.body();
                    if (body == null) {
                        return;
                    }
                    Res_BrowseImage res_browseImage = new Gson().fromJson(body.string(), Res_BrowseImage.class);
                    // imgkey默认值，如果当前请求的page是最后一页，那就不需要去获取imgkey
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
                } finally {
                    if (body != null) {
                        body.close();
                    }
                }
            }
        });
    }

    /**
     * 将当前页面的隔一页的地方的图片清除
     *
     * @param page 当前的页数
     */
    @Override
    public void startClearUriMemoryTask(int page) {
        Uri before = uris.get(page - 2);
        Uri after = uris.get(page + 2);
        view.clearUriFromMemoryCache(before);
        view.clearUriFromMemoryCache(after);
    }

    @Override
    public boolean haveImgkey(int page) {
        return imgkeys.get(page) != null;
    }
}
