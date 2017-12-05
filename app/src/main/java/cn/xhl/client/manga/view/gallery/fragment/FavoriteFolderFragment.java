package cn.xhl.client.manga.view.gallery.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.gallery.FavoriteFolderAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.contract.gallery.FavoriteContract;
import cn.xhl.client.manga.custom.CustomDialog;
import cn.xhl.client.manga.custom.FavoriteItemDecoration;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.presenter.gallery.FavoritePresenter;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.StringUtil;

/**
 * Created by xiuhaoli on 2017/11/17.
 */

public class FavoriteFolderFragment extends BaseFragment
        implements FavoriteContract.View, View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemLongClickListener {
    public static final String TAG = "FavoriteFolderFragment";
    private FavoriteContract.Presenter presenter;
    private LinearLayout retry;
    private TextView noData;// 没有数据的时候显示
    private List<Res_FavoriteFolder.Data> mRecyclerData;
    private FavoriteFolderAdapter mRecyclerAdapter;
    private CustomDialog renameDialog;
    private int longClickItemPosition;

    @Override
    protected int layoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        new FavoritePresenter(this);
        retry = view.findViewById(R.id.linear_fragment_favorite);
        noData = view.findViewById(R.id.no_data_fragment_favorite);
        ControlUtil.initControlOnClick(R.id.btn_fragment_favorite, view, this);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_favorite);
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new FavoriteFolderAdapter(mRecyclerData);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnItemLongClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        mRecyclerAdapter.openLoadAnimation();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new FavoriteItemDecoration(mActivity));
        presenter.listFolder(false);

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
        retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReTry() {
        retry.setVisibility(View.GONE);
    }

    @Override
    public void notifyAdapterFolder(Res_FavoriteFolder favoriteFolder) {
        // 将新获取的数据添加到末尾
        mRecyclerData.addAll(favoriteFolder.getFolders());
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerAdapter.loadMoreComplete();
    }

    @Override
    public void notifyAdapterFavorite(Res_GalleryList res_galleryList) {
        // discard
    }

    @Override
    public void showNoData() {
        noData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoData() {
        noData.setVisibility(View.GONE);
    }

    @Override
    public void notifyAdapter2Remove() {
        mRecyclerAdapter.remove(longClickItemPosition);
    }

    @Override
    public void notifyAdapter2Rename(String newFolder) {
        mRecyclerData.get(longClickItemPosition).setFolder(newFolder);
        mRecyclerAdapter.notifyItemChanged(longClickItemPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment_favorite:
                presenter.listFolder(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FavoriteFragment.FOLDER, mRecyclerData.get(position).getFolder());
        favoriteFragment.setArguments(bundle);
        ActivityUtil.switchContentHideCurrent(mActivity, this, favoriteFragment, FavoriteFragment.TAG, R.id.framelayout_activity_favorite);
    }

    @Override
    public void onLoadMoreRequested() {
        presenter.listFolder(true);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        longClickItemPosition = position;
        final String clickedFolderName = mRecyclerData.get(position).getFolder();
        String[] items = {"rename", "delete"};
        new CustomDialog.SingleSelectViewBuilder(getActivity())
                .setTitle(mRecyclerData.get(position).getFolder())
                .setSelectType(items)
                .setPositiveListener(new CustomDialog.OnSingleSelectClickListener() {
                    @Override
                    public void onClick(View view, int checkedPosition) {
                        if (checkedPosition == 0) {
                            createRenameDialog(clickedFolderName);
                        } else {
                            createDeleteDialog(clickedFolderName);
                        }
                    }
                }).create().show();
        return true;
    }

    private void createRenameDialog(final String clickedFolderName) {
        renameDialog = new CustomDialog.EditTextBuilder(getActivity())
                .setTitle(R.string.rename)
                .setHint(R.string.rename_hint)
                .setPositiveListener(new CustomDialog.OnInputClickListener() {
                    @Override
                    public void onClick(View view, String inputText) {
                        if (StringUtil.isNotEmpty(inputText) && inputText.length() < 17
                                && StringUtil.isValidName(inputText)) {
                            presenter.renameFolder(clickedFolderName, inputText);
                            renameDialog.dismiss();
                        } else {
                            showTipMsg("invalid name");
                        }
                    }
                }).create();
        renameDialog.show();
    }

    private void createDeleteDialog(final String clickedFolderName) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete)
                .setMessage(getString(R.string.delete_hint, clickedFolderName))
                .setNegativeButton(R.string.dialog_cancel, null)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteFolder(clickedFolderName);
                    }
                }).create().show();
    }
}
