package cn.xhl.client.manga.contract.gallery;

import android.net.Uri;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */

public interface BrowseImageContract {
    interface View extends BaseView<Presenter> {
        void createPopupWindow();

        void showPopupWindow();

        void hidePopupWindow();

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

        /**
         * 页面向左滑动
         */
        void switch2Left();

        void switch2Right();

        void switch2VerticalScreen();

        void switch2HorizontalScreen();

        void switch2VerticalScroll();

        void switch2HorizontalScroll();

        void changeBrightness(int progress);

        void changeSwitchDirection(boolean isChecked);

        /**
         * 切换横竖屏
         *
         * @param isChecked
         */
        void changeScreen(boolean isChecked);

        /**
         * 切换滚动的模式（水平滚动/竖直滚动）
         *
         * @param isChecked
         */
        void changeScrollMode(boolean isChecked);

    }

    interface Presenter extends BasePresenter {
        /**
         * @param page 页数，起始是1
         */
        void reqImgUrl(int page);

        /**
         * 开始请除缓存任务
         *
         * @param currPage 当前的页数
         */
        void startClearUriMemoryTask(int currPage);

        /**
         * 判断page对应的imgkey是否存在
         *
         * @param page
         * @return
         */
        boolean haveImgkey(int page);

        /**
         * 将图片保存到本地
         *
         * @param page
         */
        void saveImage2Local(int page);

    }

}
