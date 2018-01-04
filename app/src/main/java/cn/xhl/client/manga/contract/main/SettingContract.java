package cn.xhl.client.manga.contract.main;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

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
        void initAdapter();

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

    }

    interface Presenter extends BasePresenter {
        String cacheSize();

        void clearCache();

    }
}
