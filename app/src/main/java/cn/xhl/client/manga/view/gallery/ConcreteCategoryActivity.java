package cn.xhl.client.manga.view.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.contract.main.LatestContract;
import cn.xhl.client.manga.listener.GalleryListScrollListener;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.presenter.main.LatestPresenter;
import cn.xhl.client.manga.utils.AnalyticsUtil;
import cn.xhl.client.manga.utils.ControlUtil;

/**
 * 这个是具体类型的列表，本来可以和首页的fragment共用
 * 考虑到fragment不能被回收，就采用activity，用完销毁
 */
public class ConcreteCategoryActivity extends BaseActivity implements LatestContract.View, View.OnClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
    private LatestContract.Presenter presenter;
    private List<Res_GalleryList.GalleryEntity> mRecyclerData;
    private GalleryListAdapter mRecyclerAdapter;
    private String category;
    private String type;
    private LinearLayout retry;
    private TextView noData;// 没有数据的时候显示

    /**
     * @param activity
     * @param category 请求的书籍类型，同时又充当搜索的内容
     * @param type     请求的类型
     */
    public static void start(Activity activity, String category, String type) {
        Intent intent = new Intent(activity, ConcreteCategoryActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("type", type);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LatestPresenter(this);
        retry = findViewById(R.id.linear_activity_concrete_category);
        noData = findViewById(R.id.no_data_activity_concrete_category);
        ControlUtil.initControlOnClick(R.id.btn_activity_concrete_category, this, this);
        RecyclerView recyclerView = findViewById(R.id.recycler_activity_concrete_category);
        addScrollListener(recyclerView);
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new GalleryListAdapter(mRecyclerData);
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        mRecyclerAdapter.openLoadAnimation();
        recyclerView.setAdapter(mRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this_));
        category = getIntent().getStringExtra("category");
        type = getIntent().getStringExtra("type");
        presenter.list(category, type, false);

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
        new AnalyticsUtil.ScreenBuilder().setScreenName("ConcreteCategoryActivity:" + category).build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unSubscribe();
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
    public void showNoData() {
        noData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoData() {
        noData.setVisibility(View.GONE);
    }

    @Override
    public void onLoadMoreRequested() {
        presenter.list(category, type, true);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ConcreteMangaActivity.start(this, mRecyclerData.get(position));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_concrete_category:
                presenter.list(category, type, false);
                break;
            default:
                break;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        evictAllImageFromMemoryCache();
    }

    private void evictAllImageFromMemoryCache() {
        for (int i = 0, length = mRecyclerData.size(); i < length; i++) {
            mRecyclerAdapter.evictImageFromMemoryCache(i);
        }
    }
}
