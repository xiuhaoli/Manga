package cn.xhl.client.manga.model.api;

import cn.xhl.client.manga.model.bean.response.auth.Res_RefreshToken;
import cn.xhl.client.manga.model.bean.response.auth.Res_GetVerify;
import cn.xhl.client.manga.model.bean.response.auth.Res_Login;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.auth.Res_Register;
import cn.xhl.client.manga.model.bean.response.auth.Res_ResetPassword;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Mike on 2017/9/18 0018.
 *         <p>
 *         user模块的api
 */
public interface ApiUser {
    @FormUrlEncoded
    @POST("v1/auth/login")
    Observable<BaseResponse<Res_Login>> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("v1/auth/refreshtoken")
    Observable<BaseResponse<Res_RefreshToken>> refreshToken(
            @Body RequestBody requestBody
    );

    @GET("v1/auth/verifycode")
    Observable<BaseResponse<Res_GetVerify>> getVerify(
            @Query("email") String email
    );

    @FormUrlEncoded
    @POST("v1/auth/resetpassword")
    Observable<BaseResponse<Res_ResetPassword>> resetPassword(
            @Field("email") String email,
            @Field("password") String password,
            @Field("verifycode") String verify
    );

    @FormUrlEncoded
    @POST("v1/auth/register")
    Observable<BaseResponse<Res_Register>> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("verifycode") String verify
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("v1/user/userinfo/profileheader")
    Observable<BaseResponse<String>> modifyProfileHeader(
            @Body RequestBody requestBody
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("v1/user/userinfo/username")
    Observable<BaseResponse<String>> modifyUsername(
            @Body RequestBody requestBody
    );
}
