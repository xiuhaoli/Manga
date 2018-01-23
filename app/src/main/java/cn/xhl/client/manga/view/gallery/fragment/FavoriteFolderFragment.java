package cn.xhl.client.manga.view.gallery.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.adapter.gallery.FavoriteFolderAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.gallery.FavoriteContract;
import cn.xhl.client.manga.custom.CustomDialog;
import cn.xhl.client.manga.custom.EmptyView;
import cn.xhl.client.manga.custom.FavoriteItemDecoration;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.presenter.gallery.FavoritePresenter;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.view.gallery.ConcreteCategoryActivity;

/**
 * Created by xiuhaoli on 2017/11/17.
 */

public class FavoriteFolderFragment extends BaseFragment
        implements FavoriteContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemLongClickListener {
    public static final String TAG = "FavoriteFolderFragment";
    private FavoriteContract.Presenter presenter;
    private List<Res_FavoriteFolder.Data> mRecyclerData;
    private FavoriteFolderAdapter mRecyclerAdapter;
    private CustomDialog renameDialog;
    private int longClickItemPosition;
    private EmptyView emptyView;
    private String type;
    private int uid;// 用于标识当前被请求的用户的uid

    @Override
    protected int layoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initData();
        new FavoritePresenter(this);
        emptyView = view.findViewById(R.id.empty_fragment_favorite);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_favorite);
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new FavoriteFolderAdapter(mRecyclerData);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        mRecyclerAdapter.openLoadAnimation();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new FavoriteItemDecoration(mActivity));

        if (type.equals(IConstants.FAVORITE)) {
            mRecyclerAdapter.setOnItemLongClickListener(this);
            presenter.listFolder(false);
        } else if (type.equals(IConstants.OTHERS_FAVORITE)) {
            presenter.listOthersFolder(false, uid);
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        uid = bundle.getInt("uid");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setPresenter(FavoriteContract.Presenter presenter) {
        this.presenter = presenter;
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
    public void showTipMsg(String msg) {
        mActivity.showToast(msg);
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
        if (type.equals(IConstants.FAVORITE)) {
            emptyView.showRetry(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.listFolder(false);
                }
            });
        } else if (type.equals(IConstants.OTHERS_FAVORITE)) {
            emptyView.showRetry(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.listOthersFolder(false, uid);
                }
            });
        }
    }

    @Override
    public void hideReTry() {
        emptyView.hideRetry();
    }

    @Override
    public void notifyAdapterFolder(Res_FavoriteFolder favoriteFolder) {
        // 将新获取的数据添加到末尾
        mRecyclerData.addAll(favoriteFolder.getFolders());
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerAdapter.loadMoreComplete();
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
    public void notifyAdapter2Remove() {
        mRecyclerAdapter.remove(longClickItemPosition);
    }

    @Override
    public void notifyAdapter2Rename(String newFolder) {
        for (int i = 0, len = mRecyclerData.size(); i < len; i++) {
            if (mRecyclerData.get(i).getFolder().equals(newFolder)) {
                // 如果rename的folder和原来集合中的一样那就合并
                mRecyclerData.get(i).setCount(mRecyclerData.get(i).getCount()
                        + mRecyclerData.get(longClickItemPosition).getCount());
                mRecyclerAdapter.remove(longClickItemPosition);
                mRecyclerAdapter.notifyItemChanged(i);
                return;
            }
        }
        mRecyclerData.get(longClickItemPosition).setFolder(newFolder);
        mRecyclerAdapter.notifyItemChanged(longClickItemPosition);
    }

    @Override
    public void notifyAdapter2Encrypt() {
        mRecyclerData.get(longClickItemPosition).setEncrypt(
                mRecyclerData.get(longClickItemPosition).getEncrypt() == 1 ? 0 : 1
        );
        mRecyclerAdapter.notifyItemChanged(longClickItemPosition);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (type.equals(IConstants.OTHERS_FAVORITE)) {
            ConcreteCategoryActivity.start(mActivity,
                    UserInfo.getInstance().getCategoryMode() + ":" +
                            mRecyclerData.get(position).getFolder() + ":" + uid,
                    IConstants.OTHERS_FAVORITE);
            return;
        }
        ConcreteCategoryActivity.start(mActivity,
                UserInfo.getInstance().getCategoryMode() + ":" +
                        mRecyclerData.get(position).getFolder(),
                IConstants.FAVORITE);
    }

    @Override
    public void onLoadMoreRequested() {
        if (type.equals(IConstants.FAVORITE)) {
            presenter.listFolder(true);
        } else if (type.equals(IConstants.OTHERS_FAVORITE)) {
            presenter.listOthersFolder(true, uid);
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        longClickItemPosition = position;
        String encrypt = mRecyclerData.get(position).getEncrypt() == 1 ? "decrypt" : "encrypt";
        final String clickedFolderName = mRecyclerData.get(position).getFolder();
        String[] items = {"rename", "delete", encrypt};
        new CustomDialog.SingleSelectViewBuilder(getActivity())
                .setTitle(mRecyclerData.get(position).getFolder())
                .setSelectType(items)
                .setPositiveListener(new CustomDialog.OnSingleSelectClickListener() {
                    @Override
                    public void onClick(View view, int checkedPosition) {
                        if (checkedPosition == 0) {
                            createRenameDialog(clickedFolderName);
                        } else if (checkedPosition == 1) {
                            createDeleteDialog(clickedFolderName);
                        } else {
                            presenter.encryptFolder(clickedFolderName);
                        }
                    }
                }).create().show();
        return true;
    }

    private void createRenameDialog(final String clickedFolderName) {
        if (renameDialog == null) {
            renameDialog = new CustomDialog.EditTextBuilder(getActivity())
                    .setTitle(R.string.rename)
                    .setHint(R.string.rename_hint)
                    .setPositiveListener(new CustomDialog.OnInputClickListener() {
                        @Override
                        public void onClick(CustomDialog dialog, View view, String inputText) {
                            if (StringUtil.isNotEmpty(inputText) && inputText.length() < 17
                                    && StringUtil.isValidName(inputText)) {
                                presenter.renameFolder(clickedFolderName, inputText);
                                renameDialog.dismiss();
                            } else {
                                showTipMsg("invalid name");
                            }
                        }
                    }).create();
        }
        renameDialog.show();
    }

    private void createDeleteDialog(final String clickedFolderName) {
        new CustomDialog.DefaultBuilder(mActivity)
                .setTitle(R.string.delete)
                .setContent(getString(R.string.delete_hint, clickedFolderName))
                .setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.deleteFolder(clickedFolderName);
                    }
                }).create().show();
    }
}
