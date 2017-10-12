package cn.xhl.client.manga.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import cn.xhl.client.manga.view.main.MainActivity;

/**
 * This is Splash Page
 */
public class SplashActivity extends BaseActivity implements SplashContract.View {
    private SplashContract.Presenter presenter;

    private Runnable jump2AuthTask;
    private Runnable jump2MainTask;

    private Handler handler;

    private static class AuthTask implements Runnable {
        private final WeakReference<SplashActivity> weakReference;

        private AuthTask(SplashActivity splashActivity) {
            this.weakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void run() {
            ActivityUtil.jump2LoginPage(weakReference.get());
            weakReference.get().finish();
        }
    }

    private static class MainTask implements Runnable {
        private final WeakReference<SplashActivity> weakReference;

        private MainTask(SplashActivity splashActivity) {
            this.weakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void run() {
            ActivityUtil.jump2Activity(weakReference.get(), MainActivity.class);
            weakReference.get().finish();
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);// 载入载出动画
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        new SplashPresenter(this);

        init();
    }

    private void init() {
        jump2AuthTask = new AuthTask(this);
        jump2MainTask = new MainTask(this);
        handler = new Handler();
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
        handler.removeCallbacksAndMessages(null);// 移除全部任务和消息
        jump2AuthTask = null;
        jump2MainTask = null;
        handler = null;
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
        handler.postDelayed(jump2MainTask, 2000);
    }

    @Override
    public void change2AuthActivity() {
        handler.postDelayed(jump2AuthTask, 2000);
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
