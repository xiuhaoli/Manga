package cn.xhl.client.manga.model.api;

import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;
import cn.xhl.client.manga.model.bean.response.Res_Subscribe;
import cn.xhl.client.manga.model.bean.response.Res_Viewed;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author Mike on 2017/9/21 0021.
 * <p>
 * Comics模块api
 */
public interface ApiComics {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("v1/comics/list")
    Observable<BaseResponse<Res_GalleryList>> galleryList(
            @Body RequestBody requestBody
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("v1/comics/subscribe")
    Observable<BaseResponse<Res_Subscribe>> subscribe(
            @Body RequestBody requestBody
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("v1/comics/viewed")
    Observable<BaseResponse<Res_Viewed>> viewed(
            @Body RequestBody requestBody
    );
}
