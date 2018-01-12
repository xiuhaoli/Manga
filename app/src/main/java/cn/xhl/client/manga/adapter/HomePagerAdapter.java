package cn.xhl.client.manga.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import cn.xhl.client.manga.presenter.main.LatestPresenter;
import cn.xhl.client.manga.view.main.fragment.RankingFragment;
import cn.xhl.client.manga.view.main.fragment.RecommendFragment;
import cn.xhl.client.manga.view.main.fragment.LatestFragment;

/**
 * @author Mike on 2017/10/10 0010.
 * <p>
 * 首页的适配器
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                // 在getItem的时候会先从FragmentManager中取，不会每次都new
                RecommendFragment recommendFragment = new RecommendFragment();
                new LatestPresenter(recommendFragment);
                return recommendFragment;
            case 1:
                LatestFragment latestFragment = new LatestFragment();
                new LatestPresenter(latestFragment);
                return latestFragment;
            case 2:
                RankingFragment rankingFragment = new RankingFragment();
                new LatestPresenter(rankingFragment);
                return rankingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // tab会从这里取数据作为标题，父类只返回了null，因此要重写
        switch (position) {
            case 0:
                return "Trending";
            case 1:
                return "Latest";
            case 2:
                return "Ranking";
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
