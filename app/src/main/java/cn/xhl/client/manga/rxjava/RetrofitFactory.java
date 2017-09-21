package cn.xhl.client.manga.rxjava;

import cn.xhl.client.manga.service.MangaService;
import cn.xhl.client.manga.service.UserService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lixiuhao on 2017/9/18 0018.
 */

public class RetrofitFactory {
    private static final String BASE_URL = "http://localhost:8080/";

    private static Retrofit retrofit;

    private static UserService userService;
    private static MangaService mangaService;

    public static void init(OkHttpClient okHttpClient) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        initService();
    }

    // 通过Retrofit实例构建很多个service，可以按模块进行分类，避免集中在一个类里面不好管理
    private static void initService() {
        userService = retrofit.create(UserService.class);
        mangaService = retrofit.create(MangaService.class);
    }

    public static UserService getUserService() {
        return userService;
    }

    public static MangaService getMangaService() {
        return mangaService;
    }

    // 可以个性化定制Gson的相关规则
//    private static Gson buildGson() {
//        return new GsonBuilder()
//                .serializeNulls()
//                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
//                .registerTypeAdapter(Res_LoginData.class,)
//    }
}
