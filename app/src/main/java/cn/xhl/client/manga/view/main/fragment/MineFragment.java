package cn.xhl.client.manga.view.main.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.adapter.main.MineAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.main.MineContract;
import cn.xhl.client.manga.custom.MineItemDecoration;
import cn.xhl.client.manga.utils.FileUtil;
import cn.xhl.client.manga.view.gallery.ConcreteCategoryActivity;

public class MineFragment extends BaseFragment implements MineContract.View, BaseQuickAdapter.OnItemClickListener {
    private MineContract.Presenter presenter;
    private MineAdapter mineAdapter;
    private List<MineAdapter.MineItem> mRecyclerData;
    private AlertDialog promptDialog;
    // 缓存一栏的position
    private int cacheItemPosition;

    @Override
    protected int layoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_mine);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        initAdapter();
        recyclerView.setAdapter(mineAdapter);
        recyclerView.addItemDecoration(new MineItemDecoration(mActivity));
        TextView textView = view.findViewById(R.id.email_fragment_mine);
        textView.setText(UserInfo.getInstance().getEmail());
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void initAdapter() {
        mRecyclerData = new ArrayList<>();
        MineAdapter.MineItem item;
        int[] text = {R.string.favorites, R.string.history, R.string.filter, R.string.cache};
        int[] img = {R.mipmap.favorite, R.mipmap.history, R.mipmap.blacklist, R.mipmap.broom};
        cacheItemPosition = text.length - 1;
        for (int i = 0, size = text.length; i < size; i++) {
            item = new MineAdapter.MineItem();
            item.setIcon(img[i]);
            item.setText(text[i]);
            if (i == cacheItemPosition) {
                item.setHaveContent(true);
                item.setContent(presenter.cacheSize());
            }
            mRecyclerData.add(item);
        }
        mineAdapter = new MineAdapter(mRecyclerData);
        mineAdapter.setOnItemClickListener(this);
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
    public void createPromptDialog() {
        promptDialog = new AlertDialog.Builder(mActivity)
                .setMessage(R.string.prompt_clear_cache)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.clearCache();
                    }
                }).create();
    }

    @Override
    public void notifyAdapterForCacheItem(String cacheSize) {
        mRecyclerData.get(cacheItemPosition).setContent(cacheSize);
        mineAdapter.notifyItemChanged(cacheItemPosition);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case 0:
                // jump to favorite
                ConcreteCategoryActivity.start(mActivity, IConstants.ALL, IConstants.FAVORITE);
                break;
            case 1:
                // jump to history
                ConcreteCategoryActivity.start(mActivity, IConstants.ALL, IConstants.HISTORY);
                break;
            case 2:
                // jump to filter

                break;
            case 3:
                if (promptDialog != null) {
                    promptDialog.show();
                } else {
                    createPromptDialog();
                    promptDialog.show();
                }
                break;
            default:
                break;
        }
    }

}
