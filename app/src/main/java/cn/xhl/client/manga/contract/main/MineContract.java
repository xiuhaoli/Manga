package cn.xhl.client.manga.contract.main;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

/**
 * 我的协议
 * <p>
 *
 * @author Mike on 2017/10/9 0009.
 */
public interface MineContract {
    interface View extends BaseView<Presenter> {
        void initAdapter();

        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void createPromptDialog();

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
