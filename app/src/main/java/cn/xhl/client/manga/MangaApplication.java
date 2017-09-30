package cn.xhl.client.manga;

import android.app.Application;
import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.utils.DeviceUtil;
import cn.xhl.client.manga.utils.FileUtil;
import cn.xhl.client.manga.utils.HttpsUtil;
import cn.xhl.client.manga.utils.UserAgentInterceptor;
import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by lixiuhao on 2017/9/21 0021.
 */

public class MangaApplication extends Application {

    @SuppressWarnings("StaticFieldLeak")
    private static Context context;

    private OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        initOKHttpClient();// OkHttp
        initFresco();// Fresco
        initRetrofit();// retrofit
        initLeadCanary();// 内存泄漏的检测工具

    }

    private void initOKHttpClient() {
        FileUtil fileUtil = FileUtil.getInstance();

        File cacheFile = new File(fileUtil.getCachePath());
        Cache cache = new Cache(cacheFile, IConstants.CACHE_MAX_SIZE);

        HttpsUtil.SSLParams sslParams = HttpsUtil.getSslSocketFactory(null, null, null);
        okHttpClient = new OkHttpClient.Builder()
                //------添加User-Agent拦截器-------
                .addInterceptor(new UserAgentInterceptor(DeviceUtil.getUserAgent()))
                //-------添加缓存设置-------------
                .cache(cache)
                //--------添加信任https----------
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(IConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(IConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    private void initFresco() {
        FileUtil fileUtil = FileUtil.getInstance();
        File cacheFile = new File(fileUtil.getCachePath());

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                //------设置磁盘缓存目录--------
                .setBaseDirectoryPath(cacheFile)
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                //-------添加磁盘缓存配置---------
                .setMainDiskCacheConfig(diskCacheConfig)
                //-------添加网络组件-----------------
                .setNetworkFetcher(new OkHttpNetworkFetcher(okHttpClient))
                .build();
        Fresco.initialize(this, config);
    }

    private void initRetrofit() {
        RetrofitFactory.init(okHttpClient);
    }

    private void initLeadCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    public static Context getAppContext() {
        return context;
    }

}
