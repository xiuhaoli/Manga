package cn.xhl.client.manga.view.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.custom.SlipBackLayout;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.view.gallery.fragment.FavoriteFolderFragment;

/**
 * Created by xiuhaoli on 2017/11/17.
 */
public class FavoriteActivity extends BaseActivity implements BaseFragment.BackHandledInterface {
    private BaseFragment baseFragment;

    @Override
    protected int layoutId() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.switchContentHideCurrent(this, null,
                new FavoriteFolderFragment(),
                FavoriteFolderFragment.TAG, R.id.framelayout_activity_favorite);

        SlipBackLayout.init(this, new SlipBackLayout.OnWindowCloseListener() {
            @Override
            public void onFinish() {
                this_.finish();
            }
        });
    }

    @Override
    protected boolean transparentTheme() {
        return true;
    }

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {
        baseFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBackPressed();
    }
}
