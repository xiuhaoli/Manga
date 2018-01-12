package cn.xhl.client.manga.contract.main;

import java.io.File;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.user.Res_CheckUpdate;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/04
 *     version: 1.0
 * </pre>
 */
public interface SettingContract {
    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void createClearCachePromptDialog();

        void createLogoutPromptDialog();

        /**
         * 更新缓存item
         *
         * @param cacheSize 清除缓存后的大小
         */
        void notifyAdapterForCacheItem(String cacheSize);

        void showNewVersionPrompt(Res_CheckUpdate res_checkUpdate);

        void install(File apkPath);
    }

    interface Presenter extends BasePresenter {
        String cacheSize();

        void clearCache();

        void checkNewVersion(int versionCode, String versionName);

        void startDownloadApk(String url);
    }
}
