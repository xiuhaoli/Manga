package cn.xhl.client.manga.view.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.main.GalleryListAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.main.LatestContract;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;

/**
 * @author Mike on 2017/10/10 0010.
 */

public class RankingFragment extends BaseFragment implements LatestContract.View ,BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{
    private LatestContract.Presenter presenter;
    private List<Res_GalleryList.GalleryEntity> mRecyclerData;
    private GalleryListAdapter mRecyclerAdapter;

    @Override
    protected int layoutId() {
        return R.layout.fragment_latest;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_latest);
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new GalleryListAdapter(mRecyclerData);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        mRecyclerAdapter.openLoadAnimation();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        presenter.list(IConstants.NON_H, IConstants.RANKING);
    }

    @Override
    public void setPresenter(LatestContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
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
    public void notifyAdapter(Res_GalleryList galleryList) {
        // 将新获取的数据添加到末尾
        mRecyclerData.addAll(galleryList.getData());
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerAdapter.loadMoreComplete();
    }

    @Override
    public void onLoadMoreRequested() {
        presenter.list(IConstants.NON_H, IConstants.RANKING);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        showTipMsg("position: " + position);
    }
}
