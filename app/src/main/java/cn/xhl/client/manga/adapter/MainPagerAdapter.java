package cn.xhl.client.manga.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import cn.xhl.client.manga.presenter.main.CategoryPresenter;
import cn.xhl.client.manga.presenter.main.HomePresenter;
import cn.xhl.client.manga.presenter.main.MinePresenter;
import cn.xhl.client.manga.view.main.fragment.CategoryFragment;
import cn.xhl.client.manga.view.main.fragment.HomeFragment;
import cn.xhl.client.manga.view.main.fragment.MineFragment;

/**
 * @author Mike on 2017/10/10 0010.
 * <p>
 * MainActivity最外层的适配器
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                new HomePresenter(homeFragment);
                return homeFragment;
            case 1:
                CategoryFragment categoryFragment = new CategoryFragment();
                new CategoryPresenter(categoryFragment);
                return categoryFragment;
            case 2:
                MineFragment mineFragment = new MineFragment();
                new MinePresenter(mineFragment);
                return mineFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 每次移除item会产生不可控的bug，比如导致一些fragment被回收不再初始化，同时也会造成Fragment反复初始化带来的资源消耗
    }

}
