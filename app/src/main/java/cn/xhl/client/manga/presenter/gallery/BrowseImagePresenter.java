package cn.xhl.client.manga.presenter.gallery;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.google.gson.Gson;

import java.io.IOException;

import cn.xhl.client.manga.MyApplication;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.gallery.BrowseImageContract;
import cn.xhl.client.manga.model.bean.request.Req_BrowseImage;
import cn.xhl.client.manga.model.bean.response.Res_BrowseImage;
import cn.xhl.client.manga.utils.LogUtil;
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
    private SparseArray<String> urls;
    private SparseArray<String> imgkeys;
    private int gid;
    private String showkey;
    private final static String LAST_IMG_KEY = "end";
    private int filecount;

    public BrowseImagePresenter(BrowseImageContract.View view, int filecount, String secondImgkey, int gid, String showkey) {
        this.view = view;
        this.filecount = filecount;
        this.gid = gid;
        this.showkey = showkey;
        view.setPresenter(this);
        urls = new SparseArray<>(filecount);
        imgkeys = new SparseArray<>(filecount);
        imgkeys.put(2, secondImgkey);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if (call.isCanceled()) {
            return;
        }
        call.cancel();
    }

    @Override
    public void reqImgUrl(final int page) {
        // 如果发现请求的图片url不为空就直接返回
        if (StringUtil.isNotEmpty(urls.get(page))) {
            view.setUrl(page, urls.get(page));
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
                    urls.put(page, url);
                    imgkeys.put(page + 1, imgkey);
                    view.setUrl(page, url);

                } finally {
                    if (body != null) {
                        body.close();
                    }
                }
            }
        });
    }
}
