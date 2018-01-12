package cn.xhl.client.manga.view.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.custom.SlipBackLayout;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.view.gallery.fragment.FavoriteFolderFragment;
import cn.xhl.client.manga.view.main.SettingActivity;

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

        setSlipClose();
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
