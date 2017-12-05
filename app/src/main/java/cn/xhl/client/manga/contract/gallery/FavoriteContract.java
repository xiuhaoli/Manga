package cn.xhl.client.manga.contract.gallery;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;

/**
 * Created by xiuhaoli on 2017/11/17.
 */

public interface FavoriteContract {
    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void failLoadMore();

        void noMoreToLoad();

        void showReTry();

        void hideReTry();

        void notifyAdapterFolder(Res_FavoriteFolder favoriteFolder);

        void notifyAdapterFavorite(Res_GalleryList res_galleryList);

        void showNoData();

        void hideNoData();

        void notifyAdapter2Remove();

        void notifyAdapter2Rename(String newFolder);
    }

    interface Presenter extends BasePresenter {
        /**
         * 请求文件夹
         *
         * @param isLoadMore 是否是在加载更多, 该值用于判断是否展示加载圈
         */
        void listFolder(boolean isLoadMore);

        /**
         * 请求收藏列表
         *
         * @param isLoadMore
         * @param folder     文件夹名字
         */
        void listFavorite(boolean isLoadMore, String folder);

        void renameFolder(String oldName, String newName);

        void deleteFolder(String folder);
    }
}
