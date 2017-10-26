package cn.xhl.client.manga.view.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.main.GalleryListAdapter;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.main.LatestContract;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;
import cn.xhl.client.manga.presenter.main.LatestPresenter;

public class ConcreteCategoryActivity extends BaseActivity implements LatestContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
    private LatestContract.Presenter presenter;
    private List<Res_GalleryList.GalleryEntity> mRecyclerData;
    private GalleryListAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LatestPresenter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_activity_concrete_category);
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new GalleryListAdapter(mRecyclerData);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        mRecyclerAdapter.openLoadAnimation();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this_));
        presenter.list(getIntent().getStringExtra("category"), IConstants.LATEST);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_concrete_category;
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
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @Override
    public void showTipMsg(String msg) {
        showToast(msg);
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
        presenter.list(IConstants.NON_H, IConstants.LATEST);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        showTipMsg("position: " + position);
    }
}
