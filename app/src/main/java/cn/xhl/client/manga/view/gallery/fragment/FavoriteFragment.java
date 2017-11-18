package cn.xhl.client.manga.view.gallery.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.main.GalleryListAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.contract.gallery.FavoriteContract;
import cn.xhl.client.manga.listener.GalleryListScrollListener;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.presenter.gallery.FavoritePresenter;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.view.gallery.ConcreteMangaActivity;

/**
 * Created by xiuhaoli on 2017/11/17.
 */
public class FavoriteFragment extends BaseFragment
        implements FavoriteContract.View, View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    public static final String TAG = "FavoriteFragment";
    public static final String FOLDER = "folder";
    private FavoriteFolderFragment folderFragment;
    private FavoriteContract.Presenter presenter;
    private LinearLayout retry;
    private TextView noData;// 没有数据的时候显示
    private List<Res_GalleryList.GalleryEntity> mRecyclerData;
    private GalleryListAdapter mRecyclerAdapter;
    private String folder;// 当前收藏夹的文件名

    @Override
    protected int layoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        new FavoritePresenter(this);
        Bundle bundle = getArguments();
        folder = bundle.getString(FOLDER);

        retry = view.findViewById(R.id.linear_fragment_favorite);
        noData = view.findViewById(R.id.no_data_fragment_favorite);
        ControlUtil.initControlOnClick(R.id.btn_fragment_favorite, view, this);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_favorite);
        addScrollListener(recyclerView);
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new GalleryListAdapter(mRecyclerData);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        mRecyclerAdapter.openLoadAnimation();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        presenter.listFavorite(false, folder);
    }

    @Override
    public void onResume() {
        super.onResume();
        folderFragment = (FavoriteFolderFragment) mActivity.getSupportFragmentManager().findFragmentByTag(FavoriteFolderFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.switchContentRemoveCurrent(mActivity, this, folderFragment, TAG, R.id.framelayout_activity_favorite);
        backHandledInterface.setSelectedFragment(folderFragment);
        evictAllImageFromMemoryCache();
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
        // discard
    }

    @Override
    public void notifyAdapterFavorite(Res_GalleryList res_galleryList) {
        mRecyclerData.addAll(res_galleryList.getData());
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerAdapter.loadMoreComplete();
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
                presenter.listFavorite(false, folder);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ConcreteMangaActivity.start(mActivity, mRecyclerData.get(position));
    }

    @Override
    public void onLoadMoreRequested() {
        presenter.listFavorite(true, folder);
    }

    private void evictAllImageFromMemoryCache() {
        for (int i = 0, length = mRecyclerData.size(); i < length; i++) {
            mRecyclerAdapter.evictImageFromMemoryCache(i);
        }
    }

    private void addScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new GalleryListScrollListener() {
            @Override
            public void onScrolledUp(int firstVisible, int lastVisible) {
                for (int i = 0; i < firstVisible; i++) {
                    mRecyclerAdapter.evictImageFromMemoryCache(i);
                }
            }

            @Override
            public void onScrolledDown(int firstVisible, int lastVisible) {
                for (int i = mRecyclerData.size() - 1; i > lastVisible; i--) {
                    mRecyclerAdapter.evictImageFromMemoryCache(i);
                }
            }
        });
    }
}
