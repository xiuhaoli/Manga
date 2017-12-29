package cn.xhl.client.manga.contract.gallery;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/19
 *     version: 1.0
 * </pre>
 */
public interface SummaryContract {
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void showToastMsg(String msg);
    }

    interface Presenter extends BasePresenter{
        void parsePage(Res_GalleryList.GalleryEntity galleryEntity);

        String getShowKey();

        String getImgKey();

        String getFirstImg();
    }
}
