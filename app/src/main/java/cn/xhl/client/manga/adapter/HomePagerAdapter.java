package cn.xhl.client.manga.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.presenter.main.LatestPresenter;
import cn.xhl.client.manga.view.main.fragment.LatestFragment;

/**
 * @author Mike on 2017/10/10 0010.
 *         <p>
 *         首页的适配器
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
                LatestFragment latestFragment = new LatestFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", IConstants.LATEST);
                latestFragment.setArguments(bundle);
                new LatestPresenter(latestFragment);
                return latestFragment;
            case 1:
                LatestFragment recommend = new LatestFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", IConstants.RECOMMEND);
                recommend.setArguments(bundle1);
                new LatestPresenter(recommend);
                return recommend;
            case 2:
                LatestFragment rankingFragment = new LatestFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("type", IConstants.RANKING);
                rankingFragment.setArguments(bundle2);
                new LatestPresenter(rankingFragment);
                return rankingFragment;
            case 3:
                LatestFragment attendFragment = new LatestFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putString("type",IConstants.ATTENTION);
                attendFragment.setArguments(bundle3);
                new LatestPresenter(attendFragment);
                return attendFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // tab会从这里取数据作为标题，父类只返回了null，因此要重写
        switch (position) {
            case 0:
                return "Latest";
            case 1:
                return "Trending";
            case 2:
                return "Ranking";
            case 3:
                return "Following";
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
