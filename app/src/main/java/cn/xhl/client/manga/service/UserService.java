package cn.xhl.client.manga.service;

import cn.xhl.client.manga.msg.response.Res_Login;
import cn.xhl.client.manga.rxjava.BaseEntity;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lixiuhao on 2017/9/18 0018.
 */

public interface UserService {
    @FormUrlEncoded
    @POST("v1/User/UserLogin")
    Observable<BaseEntity<Res_Login>> login(
            @Field("email") String phone,
            @Field("password") String password
    );
}
