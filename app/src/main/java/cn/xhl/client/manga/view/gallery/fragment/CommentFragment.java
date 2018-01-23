package cn.xhl.client.manga.view.gallery.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.adapter.gallery.CommentAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.gallery.CommentContract;
import cn.xhl.client.manga.custom.CommentItemDecoration;
import cn.xhl.client.manga.custom.EmptyView;
import cn.xhl.client.manga.model.bean.response.gallery.Res_CommentList;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.view.gallery.ConcreteMangaActivity;
import cn.xhl.client.manga.view.gallery.FavoriteActivity;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/18
 *     version: 1.0
 * </pre>
 */
public class CommentFragment extends BaseFragment implements CommentContract.View,
        View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    private CommentContract.Presenter presenter;
    private EmptyView emptyView;
    private List<Res_CommentList.CommentEntity> mRecyclerData;
    private CommentAdapter mRecyclerAdapter;
    private Res_GalleryList.GalleryEntity galleryEntity;
    private EditText commentEditText;

    @Override
    protected int layoutId() {
        return R.layout.fragment_manga_comment;
    }

    private void initData() {
        Bundle bundle = getArguments();
        galleryEntity = (Res_GalleryList.GalleryEntity) bundle
                .getSerializable(ConcreteMangaActivity.GALLERY_ENTITY);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initData();
        emptyView = view.findViewById(R.id.empty_fragment_manga_comment);
        commentEditText = view.findViewById(R.id.input_fragment_manga_comment);
        ControlUtil.initControlOnClick(R.id.submit_fragment_manga_comment, view, this);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_manga_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new CommentItemDecoration(mActivity));
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new CommentAdapter(mRecyclerData);
        mRecyclerAdapter.openLoadAnimation();
        mRecyclerAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(mRecyclerAdapter);
        if (galleryEntity.getComment() != 0) {
            presenter.listComment(galleryEntity.getId(), false);
        } else {
            showNoData();
        }
    }

    @Override
    public void setPresenter(CommentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(String msg) {
        mActivity.showToast(msg);
    }

    @Override
    public void showLoading() {
        mActivity.showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        mActivity.hideLoadingDialog();
    }

    @Override
    public void failLoadMore() {
        mRecyclerAdapter.loadMoreFail();
    }

    @Override
    public void noMoreToLoad() {
        mRecyclerAdapter.loadMoreEnd();
    }

    @Override
    public void showReTry() {
        emptyView.showRetry(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.listComment(galleryEntity.getId(), false);
            }
        });
    }

    @Override
    public void hideReTry() {
        emptyView.hideRetry();
    }

    @Override
    public void notifyAdapter(Res_CommentList res_commentList) {
        // 给每个item设置楼层数
        if (mRecyclerData.size() == 0) {
            int count = galleryEntity.getComment();
            for (int i = 0, len = res_commentList.getData().size(); i < len; i++) {
                res_commentList.getData().get(i).setFloor(count - i);
            }
        } else {
            int count = galleryEntity.getComment() - mRecyclerData.size();
            for (int i = 0, len = res_commentList.getData().size(); i < len; i++) {
                res_commentList.getData().get(i).setFloor(count - i);
            }
        }
        mRecyclerData.addAll(res_commentList.getData());
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerAdapter.loadMoreComplete();
    }

    @Override
    public void notifyAddSingleComment(Res_CommentList.CommentEntity commentEntity) {
        commentEntity.setFloor(mRecyclerData.size() + 1);
        galleryEntity.setComment(galleryEntity.getComment() + 1);
        mRecyclerData.add(0, commentEntity);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyDeleteComment(int position) {
        mRecyclerAdapter.remove(position);
        galleryEntity.setComment(galleryEntity.getComment() - 1);
        if (mRecyclerData.size() == 0) {
            showNoData();
        }
    }

    @Override
    public void showNoData() {
        emptyView.showNodata();
    }

    @Override
    public void hideNoData() {
        emptyView.hideNodata();
    }

    @Override
    public void showEmptyLoading() {
        emptyView.showLoading();
    }

    @Override
    public void hideEmptyLoading() {
        emptyView.hideLoading();
    }

    @Override
    public void clearEditText() {
        commentEditText.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_fragment_manga_comment:
                String comment = commentEditText.getText().toString().replace(" ", "");
                if (comment.length() < 4) {
                    showToast("at least 4 words");
                    return;
                }
                presenter.comment(galleryEntity.getId(), comment);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (getString(R.string.delete).equals(view.getTag())) {
            presenter.deleteComment(mRecyclerData.get(position).getId(),
                    galleryEntity.getId(), position);
        } else if (getString(R.string.others_profile_header).equals(view.getTag())) {
            int from_uid = mRecyclerData.get(position).getFrom_uid();
            if (from_uid == UserInfo.getInstance().getUid()) {
                FavoriteActivity.start(mActivity, IConstants.FAVORITE);
                return;
            }
            FavoriteActivity.startOthers(mActivity, IConstants.OTHERS_FAVORITE, from_uid);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        presenter.listComment(galleryEntity.getId(), true);
    }
}
