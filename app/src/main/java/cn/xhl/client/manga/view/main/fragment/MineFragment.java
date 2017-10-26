package cn.xhl.client.manga.view.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.adapter.main.MineAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.contract.main.MineContract;
import cn.xhl.client.manga.custom.MineItemDecoration;
import cn.xhl.client.manga.utils.ControlUtil;

public class MineFragment extends BaseFragment implements MineContract.View, BaseQuickAdapter.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private MineContract.Presenter presenter;
    private MineAdapter mineAdapter;
    private List<MineAdapter.MineItem> mRecyclerData;

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
        int[] text = {R.string.theme_type_black, R.string.favorites, R.string.history};
        int[] img = {R.mipmap.moon, R.mipmap.favorite, R.mipmap.history};
        for (int i = 0; i < text.length; i++) {
            item = new MineAdapter.MineItem();
            item.setIcon(img[i]);
            item.setText(text[i]);
            if (i == 0) {
                item.setSwitcher(true);
                item.setListener(this);
                item.setChecked(true);
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case 1:
                // jump to favorite
                break;
            case 2:
                // jump to history
                break;
            case 0:
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_item_mine:
                if (isChecked) {
                    mRecyclerData.get(0).setIcon(R.mipmap.moon);
                    mRecyclerData.get(0).setText(R.string.theme_type_black);
                    mRecyclerData.get(0).setChecked(true);
                } else {
                    mRecyclerData.get(0).setIcon(R.mipmap.sun);
                    mRecyclerData.get(0).setText(R.string.theme_type_white);
                    mRecyclerData.get(0).setChecked(false);
                }
                mineAdapter.notifyItemChanged(0);
                break;
            default:
                break;
        }
    }
}
