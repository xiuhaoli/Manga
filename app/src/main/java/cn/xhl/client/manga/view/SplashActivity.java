package cn.xhl.client.manga.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.transition.Transition;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.contract.SplashContract;
import cn.xhl.client.manga.model.bean.response.Res_RefreshToken;
import cn.xhl.client.manga.presenter.SplashPresenter;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.utils.PrefUtil;

/**
 * This is Splash Page
 */
public class SplashActivity extends BaseActivity implements SplashContract.View {
    private SplashContract.Presenter presenter;

    private static class MyHandler extends Handler {
        private final WeakReference<SplashActivity> mActivity;

        private MyHandler(SplashActivity mActivity) {
            this.mActivity = new WeakReference<>(mActivity);
        }
    }

    private MyHandler handler = new MyHandler(this);

    @Override
    protected int layoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        this.overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);// 载入载出动画
        new SplashPresenter(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unSubscribe();
        handler = null;
    }

    @Override
    public void hideTopbar() {
        hideTopBar();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initUserInfo() {
        SharedPreferences sp = PrefUtil.get(this);
        String token = sp.getString("token", "");
        String uid = sp.getString("uid", "");
        String salt = sp.getString("salt", "");
        String expire_time = sp.getString("expire_time", "");
        boolean available = presenter.isUserInfoAvailable(token, uid, salt, expire_time);
        if (available) {
            presenter.refreshToken(token, uid, salt, expire_time);
        } else {
            change2AuthActivity();
        }
    }

    @Override
    public void showTipMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void change2MainActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtil.jump2Activity(this_, MainActivity.class);
                this_.finish();
            }
        }, 2000);
    }

    @Override
    public void change2AuthActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtil.jump2LoginPage(this_, true);
                this_.finish();
            }
        }, 2000);
    }

    @Override
    public void handleUserInfo(Res_RefreshToken res_refreshToken) {
        UserInfo.getInstance().setToken(res_refreshToken.getToken());
        SharedPreferences sp = PrefUtil.get(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", res_refreshToken.getToken());
        editor.putString("expire_time", res_refreshToken.getExpire_time());
        editor.apply();
    }
}
