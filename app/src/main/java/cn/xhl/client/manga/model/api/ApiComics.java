package cn.xhl.client.manga.model.api;

import cn.xhl.client.manga.model.bean.response.BaseResponse;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lixiuhao on 2017/9/21 0021.
 * <p>
 * Comics模块api
 */
interface ApiComics {
    @FormUrlEncoded
    @POST("v1/Comics/list")
    Observable<BaseResponse<?>> reqList(
            @Field("token") String token,
            @Field("salt") String salt,
            @Field("uid") String uid,
            @Field("timestamp") String timestamp
    );
}
