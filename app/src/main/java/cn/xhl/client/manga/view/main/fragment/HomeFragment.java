package cn.xhl.client.manga.view.main.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.HomePagerAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.contract.main.HomeContract;
import cn.xhl.client.manga.utils.ControlUtil;

/**
 * Created by lixiuhao on 2017/10/9 0009.
 * <p>
 * 主页
 */
public class HomeFragment extends BaseFragment implements HomeContract.View, View.OnClickListener {
    private HomeContract.Presenter presenter;
    private SimpleDraweeView searchView;

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
    protected int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.viewpager_fragment_home);
        viewPager.setAdapter(new HomePagerAdapter(mActivity.getSupportFragmentManager()));
        TabLayout tabLayout = view.findViewById(R.id.tab_fragment_home);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_recommend));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_update));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_ranking));
        tabLayout.setupWithViewPager(viewPager, false);

        searchView = (SimpleDraweeView) ControlUtil.initControlOnClick(R.id.search_fragment_home, view, this);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
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
    public void hideKeyboard() {
        mActivity.hideKeyboard();
    }

    @Override
    public void jump2Search() {
        // start animation and show search page
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_fragment_home:
                presenter.search();
                break;
            default:
                break;
        }
    }
}
