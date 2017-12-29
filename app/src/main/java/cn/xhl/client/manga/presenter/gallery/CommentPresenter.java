package cn.xhl.client.manga.presenter.gallery;

import com.google.gson.Gson;

import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.base.BaseObserver;
import cn.xhl.client.manga.contract.gallery.CommentContract;
import cn.xhl.client.manga.model.api.RetrofitFactory;
import cn.xhl.client.manga.model.bean.request.BaseRequest;
import cn.xhl.client.manga.model.bean.request.gallery.Req_Comment;
import cn.xhl.client.manga.model.bean.request.gallery.Req_CommentList;
import cn.xhl.client.manga.model.bean.request.gallery.Req_DeleteComment;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.model.bean.response.gallery.Res_Comment;
import cn.xhl.client.manga.model.bean.response.gallery.Res_CommentList;
import cn.xhl.client.manga.model.bean.response.gallery.Res_DeleteComment;
import cn.xhl.client.manga.utils.LogUtil;
import cn.xhl.client.manga.utils.RxSchedulesHelper;
import cn.xhl.client.manga.utils.SignUtil;
import cn.xhl.client.manga.utils.StringUtil;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/18
 *     version: 1.0
 * </pre>
 */
public class CommentPresenter implements CommentContract.Presenter {
    private CommentContract.View view;
    private CompositeDisposable compositeDisposable;
    private int page = 0;
    private int size = 15;
    private boolean canBeLoadMore = false;

    public CommentPresenter(CommentContract.View view) {
        this.view = view;
        view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void listComment(int gid, final boolean isLoadMore) {
        if (!isLoadMore) {
            view.showEmptyLoading();
        } else {
            if (!canBeLoadMore) {
                view.noMoreToLoad();
                return;
            }
        }
        view.hideReTry();
        view.hideNoData();
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .listComment(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setData(new Req_CommentList(page, size, gid))
                        .setSign(SignUtil.generateSign(String.valueOf(gid),
                                String.valueOf(page), String.valueOf(size)))
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<Res_CommentList>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideEmptyLoading();
                    }
                })
                .subscribeWith(new BaseObserver<Res_CommentList>() {
                    @Override
                    protected void onHandleSuccess(Res_CommentList res_commentList) {
                        ++page;
                        canBeLoadMore = (size == res_commentList.getData().size());
                        // 如果不是加载更多且返回的集合长度为0，则显示noData
                        if (!isLoadMore && res_commentList.getData().size() == 0) {
                            view.showNoData();
                        } else {
                            view.notifyAdapter(res_commentList);
                        }
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showToast(msg);
                        // 如果是加载更多，显示加载更多失败，否则显示重试
                        if (isLoadMore) {
                            view.failLoadMore();
                        } else {
                            view.showReTry();
                        }
                    }
                }));
    }

    @Override
    public void comment(final int gid, final String comment) {
        view.showLoading();
        view.hideNoData();
        view.hideReTry();
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .comment(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setData(new Req_Comment(gid, comment))
                        .setSign(SignUtil.generateSign(String.valueOf(gid), comment))
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<Res_Comment>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .subscribeWith(new BaseObserver<Res_Comment>() {
                    @Override
                    protected void onHandleSuccess(Res_Comment resComment) {
                        Res_CommentList.CommentEntity commentEntity = new Res_CommentList.CommentEntity();
                        commentEntity.setId(resComment.getId());
                        commentEntity.setContent(comment);
                        commentEntity.setGalleryId(gid);
                        commentEntity.setFrom_uid(UserInfo.getInstance().getUid());
                        commentEntity.setUsername(UserInfo.getInstance().getUsername());
                        commentEntity.setProfile_picture(UserInfo.getInstance().getProfile_picture());
                        commentEntity.setCreate_time(resComment.getCreate_time());
                        view.notifyAddSingleComment(commentEntity);
                        view.clearEditText();
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                        view.showToast(msg);
                    }
                }));
    }

    @Override
    public void deleteComment(int id, int gid, final int position) {
        view.showLoading();
        compositeDisposable.add(RetrofitFactory
                .getApiComics()
                .deleteComment(StringUtil.getRequestBody(new Gson().toJson(new BaseRequest.Builder()
                        .setData(new Req_DeleteComment(id, gid))
                        .setSign(SignUtil.generateSign(String.valueOf(gid), String.valueOf(id)))
                        .build())))
                .compose(RxSchedulesHelper.<BaseResponse<Res_DeleteComment>>io_ui())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                })
                .subscribeWith(new BaseObserver<Res_DeleteComment>() {
                    @Override
                    protected void onHandleSuccess(Res_DeleteComment res_deleteComment) {
                        view.notifyDeleteComment(position);
                    }

                    @Override
                    protected void onHandleError(long code, String msg) {
                    }
                }));
    }

}
