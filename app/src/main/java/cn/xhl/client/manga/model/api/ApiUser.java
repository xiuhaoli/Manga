package cn.xhl.client.manga.model.api;

import cn.xhl.client.manga.model.bean.response.Res_RefreshToken;
import cn.xhl.client.manga.model.bean.response.Res_GetVerify;
import cn.xhl.client.manga.model.bean.response.Res_Login;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.Res_Register;
import cn.xhl.client.manga.model.bean.response.Res_ResetPassword;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lixiuhao on 2017/9/18 0018.
 * <p>
 * user模块的api
 */
public interface ApiUser {
    @FormUrlEncoded
    @POST("v1/User/Login")
    Observable<BaseResponse<Res_Login>> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("v1/User/RefreshToken")
    Observable<BaseResponse<Res_RefreshToken>> refreshToken(
            @Field("token") String token,
            @Field("uid") String uid,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    @FormUrlEncoded
    @POST("v1/User/GetVerify")
    Observable<BaseResponse<Res_GetVerify>> getVerify(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("v1/User/ResetPassword")
    Observable<BaseResponse<Res_ResetPassword>> resetPassword(
            @Field("email") String email,
            @Field("password") String password,
            @Field("verify") String verify
    );

    @FormUrlEncoded
    @POST("v1/User/Register")
    Observable<BaseResponse<Res_Register>> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("verify") String verify
    );
}
