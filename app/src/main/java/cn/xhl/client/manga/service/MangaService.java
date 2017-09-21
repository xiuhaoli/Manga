package cn.xhl.client.manga.service;

import cn.xhl.client.manga.rxjava.BaseEntity;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lixiuhao on 2017/9/21 0021.
 */

public interface MangaService {
    @FormUrlEncoded
    @POST("v1/Manga/list")
    Observable<BaseEntity<?>> reqList(
            @Field("token") String token,
            @Field("salt") String salt,
            @Field("uid") String uid,
            @Field("timestamp") String timestamp
    );
}
