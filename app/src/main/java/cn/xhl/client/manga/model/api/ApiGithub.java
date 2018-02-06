package cn.xhl.client.manga.model.api;

import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.model.bean.response.user.Res_CheckUpdate;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/23
 *     version: 1.0
 * </pre>
 */
public interface ApiGithub {
    @GET(IConstants.APK_URL)
    Observable<Res_CheckUpdate> checkNewVersion();

    /**
     * 下载apk
     *
     * @param url
     * @return
     */
    @GET
    Observable<ResponseBody> downloadApk(@Url String url);
}
