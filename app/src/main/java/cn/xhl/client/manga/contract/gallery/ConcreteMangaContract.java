package cn.xhl.client.manga.contract.gallery;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;

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

        void failLoadMore();

        void noMoreToLoad();

        void showReTry();

        void hideReTry();

        void notifyAdapterFolder(Res_FavoriteFolder favoriteFolder);

        void showNoData();

        void hideNoData();

        void createBottomSheet();

        void startRefreshing();

        void stopRefreshing();

        void showBottomSheet();
    }

    interface Presenter extends BasePresenter {
        void parsePage(Res_GalleryList.GalleryEntity galleryEntity);

        String getShowKey();

        String getImgKey();

        String getFirstImg();

        /**
         * 初始化请求folder列表的相关参数
         */
        void initReqListData();

        void favorite(int galleryId, String folder);

        void viewed(int galleryId);

        void listFolder(boolean isLoadMore, int id);

        void sort(Res_FavoriteFolder favoriteFolder);
    }
}
