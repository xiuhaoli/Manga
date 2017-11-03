package cn.xhl.client.manga.contract.main;


import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;

/**
 * @author Mike on 2017/10/23 0023.
 */

public interface LatestContract {
    interface View extends BaseView<LatestContract.Presenter> {

        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void failLoadMore();

        void noMoreToLoad();

        void showReTry();

        void hideReTry();

        void notifyAdapter(Res_GalleryList galleryList);

    }

    interface Presenter extends BasePresenter {
        /**
         * 请求列表
         *
         * @param category   书籍类型
         * @param type       请求类型
         * @param isLoadMore 是否是在加载更多, 该值用于判断是否展示加载圈
         */
        void list(String category, String type, boolean isLoadMore);
    }
}
