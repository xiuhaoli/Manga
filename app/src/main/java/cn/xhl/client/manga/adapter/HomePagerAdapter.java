package cn.xhl.client.manga.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.view.main.fragment.RankingFragment;
import cn.xhl.client.manga.view.main.fragment.RecommendFragment;
import cn.xhl.client.manga.view.main.fragment.UpdateFragment;

/**
 * Created by lixiuhao on 2017/10/10 0010.
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
                return new RecommendFragment();
            case 1:
                return new UpdateFragment();
            case 2:
                return new RankingFragment();
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
                return "推荐";
            case 1:
                return "更新";
            case 2:
                return "排行";
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
