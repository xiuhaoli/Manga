package cn.xhl.client.manga.contract.gallery;

import android.net.Uri;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */

public interface BrowseImageContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        /**
         * @param page 页数，起始是1
         * @param uri  当前图片的URL
         */
        void setUrl(int page, Uri uri);

        /**
         * 将uri对应的图片清出内存缓存中
         *
         * @param uri
         */
        void clearUriFromMemoryCache(Uri uri);

        void clearUriFromDiskCache(Uri uri);
    }

    interface Presenter extends BasePresenter {
        /**
         * @param page 页数，起始是1
         */
        void reqImgUrl(int page);

        /**
         * 开始请除缓存任务
         *
         * @param page 当前的页数
         */
        void startClearUriMemoryTask(int page);

        boolean haveImgkey(int page);
    }

}
