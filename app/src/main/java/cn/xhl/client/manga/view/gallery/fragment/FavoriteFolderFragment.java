package cn.xhl.client.manga.view.gallery.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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
import cn.xhl.client.manga.custom.FavoriteItemDecoration;
import cn.xhl.client.manga.custom.MineItemDecoration;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.presenter.gallery.FavoritePresenter;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.utils.ControlUtil;

/**
 * Created by xiuhaoli on 2017/11/17.
 */

public class FavoriteFolderFragment extends BaseFragment
        implements FavoriteContract.View, View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    public static final String TAG = "FavoriteFolderFragment";
    private FavoriteContract.Presenter presenter;
    private LinearLayout retry;
    private TextView noData;// 没有数据的时候显示
    private List<Res_FavoriteFolder.Data> mRecyclerData;
    private FavoriteFolderAdapter mRecyclerAdapter;

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
}
