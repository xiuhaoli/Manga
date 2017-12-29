package cn.xhl.client.manga.contract.gallery;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.gallery.Res_CommentList;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/18
 *     version: 1.0
 * </pre>
 */
public interface CommentContract {
    interface View extends BaseView<Presenter> {
        void showToast(String msg);

        void showLoading();

        void hideLoading();

        void failLoadMore();

        void noMoreToLoad();

        void showReTry();

        void hideReTry();

        void notifyAdapter(Res_CommentList res_commentList);

        void notifyAddSingleComment(Res_CommentList.CommentEntity commentEntity);

        void notifyDeleteComment(int position);

        void showNoData();

        void hideNoData();

        void showEmptyLoading();

        void hideEmptyLoading();

        void clearEditText();
    }

    interface Presenter extends BasePresenter {
        void listComment(int gid, boolean isLoadMore);

        void comment(int gid, String comment);

        /**
         * 删除评论
         *
         * @param id       这是评论在数据库中的主键id
         * @param gid      galleryId
         * @param position 删除的item所在的位置索引
         */
        void deleteComment(int id, int gid, int position);
    }
}
