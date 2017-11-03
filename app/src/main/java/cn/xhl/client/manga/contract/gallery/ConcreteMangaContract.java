package cn.xhl.client.manga.contract.gallery;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;

/**
 * @author lixiuhao on 2017/10/30 0030.
 */
public interface ConcreteMangaContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void showToastMsg(String msg);

        void changeStarNumber(int delta);

        void plusViewedNumber();
    }

    interface Presenter extends BasePresenter {
        void parsePage(Res_GalleryList.GalleryEntity galleryEntity);

        String getShowKey();

        String getImgKey();

        String getFirstImg();

        void favorite(int galleryId);

        void viewed(int galleryId);
    }
}
