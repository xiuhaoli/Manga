package cn.xhl.client.manga.model.api;

import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.model.bean.response.gallery.Res_BrowseImage;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Eh的相关接口放这儿
 * <p>
 * Created by xiuhaoli on 2017/12/4.
 */
public interface ApiEh {
    /**
     * 获取图片的url
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(IConstants.POST_URL)
    Observable<Res_BrowseImage> obtainImageUrl(@Body RequestBody requestBody);

    /**
     * 这玩意是用来访问三级页面来获取ImageKey的
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResponseBody> parseThirdPage(@Url String url);

    /**
     * 下载apk
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResponseBody> downloadApk(@Url String url);
}
