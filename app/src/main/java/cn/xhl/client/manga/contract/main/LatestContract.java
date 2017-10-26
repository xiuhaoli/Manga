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

        void notifyAdapter(Res_GalleryList galleryList);

    }

    interface Presenter extends BasePresenter {
        void list(String category, String type);
    }
}
