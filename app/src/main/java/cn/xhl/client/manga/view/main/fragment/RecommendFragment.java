package cn.xhl.client.manga.view.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.main.GalleryListAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.main.LatestContract;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.view.gallery.ConcreteMangaActivity;

/**
 * @author Mike on 2017/10/10 0010.
 */

public class RecommendFragment extends BaseFragment implements LatestContract.View, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
    private LatestContract.Presenter presenter;
    private List<Res_GalleryList.GalleryEntity> mRecyclerData;
    private GalleryListAdapter mRecyclerAdapter;
    private LinearLayout retry;

    @Override
    protected int layoutId() {
        return R.layout.fragment_latest;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        retry = view.findViewById(R.id.linear_fragment_latest);
        ControlUtil.initControlOnClick(R.id.btn_fragment_latest, view, this);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_latest);
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new GalleryListAdapter(mRecyclerData);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        mRecyclerAdapter.openLoadAnimation();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        presenter.list(IConstants.ALL, IConstants.RECOMMEND, false);
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
    public void showReTry() {
        retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReTry() {
        retry.setVisibility(View.GONE);
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
        presenter.list(IConstants.ALL, IConstants.RECOMMEND, true);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(mActivity, ConcreteMangaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("GalleryEntity", mRecyclerData.get(position));
        intent.putExtra("galleryBundle", bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment_latest:
                presenter.list(IConstants.ALL, IConstants.RECOMMEND, false);
                break;
            default:
                break;
        }
    }
}
