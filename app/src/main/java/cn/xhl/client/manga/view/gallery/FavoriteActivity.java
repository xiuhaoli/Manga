package cn.xhl.client.manga.view.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.view.gallery.fragment.FavoriteFolderFragment;

/**
 * Created by xiuhaoli on 2017/11/17.
 */
public class FavoriteActivity extends BaseActivity implements BaseFragment.BackHandledInterface {
    private BaseFragment baseFragment;
    private String type;
    private int uid;// 其他用户的uid

    @Override
    protected int layoutId() {
        return R.layout.activity_favorite;
    }

    public static void startOthers(Activity activity, String type, int uid) {
        Intent intent = new Intent(activity, FavoriteActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("uid", uid);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void start(Activity activity, String type) {
        Intent intent = new Intent(activity, FavoriteActivity.class);
        intent.putExtra("type", type);
        ActivityCompat.startActivity(activity, intent, null);
    }

    private void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        uid = intent.getIntExtra("uid", 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        FavoriteFolderFragment fragment = new FavoriteFolderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putInt("uid", uid);
        fragment.setArguments(bundle);
        ActivityUtil.switchContentHideCurrent(this, null, fragment,
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
