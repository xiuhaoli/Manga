package cn.xhl.client.manga.contract.gallery;

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
         * @param page       页数，起始是1
         * @param url        当前图片的URL
         */
        void setUrl(int page, String url);
    }

    interface Presenter extends BasePresenter {
        /**
         * @param page    页数，起始是1
         */
        void reqImgUrl(int page);
    }

}
