package cn.xhl.client.manga.adapter.gallery;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.presenter.gallery.CommentPresenter;
import cn.xhl.client.manga.presenter.gallery.SummaryPresenter;
import cn.xhl.client.manga.view.gallery.ConcreteMangaActivity;
import cn.xhl.client.manga.view.gallery.fragment.CommentFragment;
import cn.xhl.client.manga.view.gallery.fragment.SummaryFragment;

/**
 * <p>
 * 首页的适配器
 *
 * @author Mike on 2017/10/10 0010.
 */
public class ConcreteMangaPagerAdapter extends FragmentPagerAdapter {
    private Res_GalleryList.GalleryEntity galleryEntity;

    public ConcreteMangaPagerAdapter(FragmentManager fm, Res_GalleryList.GalleryEntity galleryEntity) {
        super(fm);
        this.galleryEntity = galleryEntity;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                // 在getItem的时候会先从FragmentManager中取，不会每次都new
                SummaryFragment summaryFragment = new SummaryFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConcreteMangaActivity.GALLERY_ENTITY, galleryEntity);
                summaryFragment.setArguments(bundle);
                new SummaryPresenter(summaryFragment);
                return summaryFragment;
            case 1:
                CommentFragment commentFragment = new CommentFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(ConcreteMangaActivity.GALLERY_ENTITY, galleryEntity);
                commentFragment.setArguments(bundle1);
                new CommentPresenter(commentFragment);
                return commentFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // tab会从这里取数据作为标题，父类只返回了null，因此要重写
        switch (position) {
            case 0:
                return "summary";
            case 1:
                return "comment(" + galleryEntity.getComment() + ")";
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
