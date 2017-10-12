package cn.xhl.client.manga.model.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lixiuhao on 2017/9/18 0018.
 * <p>
 * Retrofit工厂类，实例化Retrofit并提供各模块实例
 */
public class RetrofitFactory {
    private static final String BASE_URL = "http://192.168.1.107:9090/";

    private static Retrofit retrofit;

    private static ApiUser apiUser;
    private static ApiComics apiComics;

    public static void init(OkHttpClient okHttpClient) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(builGson()))
                // Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        initService();
    }

    // 通过Retrofit实例构建很多个service，可以按模块进行分类，避免集中在一个类里面不好管理
    private static void initService() {
        apiUser = retrofit.create(ApiUser.class);
        apiComics = retrofit.create(ApiComics.class);
    }

    public static ApiUser getApiUser() {
        return apiUser;
    }

    public static ApiComics getApiComics() {
        return apiComics;
    }

    // 可以个性化定制Gson的相关规则
//    private static Gson buildGson() {
//        return new GsonBuilder()
//                .serializeNulls()
//                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
//                .registerTypeAdapter(Res_LoginData.class,)
//    }
    private static Gson builGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }
}
