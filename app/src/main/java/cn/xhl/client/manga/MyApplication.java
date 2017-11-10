package cn.xhl.client.manga;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.utils.CrashUtil;
import cn.xhl.client.manga.utils.DeviceUtil;
import cn.xhl.client.manga.utils.FileUtil;
import cn.xhl.client.manga.utils.HttpsUtil;
import cn.xhl.client.manga.utils.UserAgentInterceptor;
import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Mike on 2017/9/21 0021.
 */

public class MyApplication extends Application {

    @SuppressWarnings("StaticFieldLeak")
    private static Context context;

    private static OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        super.onCreate();
        context = getApplicationContext();

        initOKHttpClient();// OkHttp
        initFresco();// Fresco
        initRetrofit();// retrofit
        initCrashUtil();// 初始化奔溃日志收集工具
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
                // -------尝试重试在失败的时候----------
                .retryOnConnectionFailure(true)
                //--------添加信任https----------
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(IConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(IConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    private void initFresco() {
        FileUtil fileUtil = FileUtil.getInstance();
        File cacheFile = new File(fileUtil.getImagePath());

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                //------设置磁盘缓存目录--------
                .setBaseDirectoryPath(cacheFile)
                // 最大缓存容量单位MB
                .setMaxCacheSize(100 * ByteConstants.MB)
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                //-------添加磁盘缓存配置---------
                .setMainDiskCacheConfig(diskCacheConfig)
                //-------添加网络组件-----------------
                .setNetworkFetcher(new OkHttpNetworkFetcher(okHttpClient))
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(this, config);
    }

    private void initRetrofit() {
        RetrofitFactory.init(okHttpClient);
    }

    private void initCrashUtil() {
        FileUtil fileUtil = FileUtil.getInstance();
        File log = new File(fileUtil.getLogPath());
        CrashUtil.init(log);
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

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
