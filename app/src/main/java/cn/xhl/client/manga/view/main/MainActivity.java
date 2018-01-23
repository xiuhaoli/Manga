package cn.xhl.client.manga.view.main;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Locale;

import cn.xhl.client.manga.MyActivityManager;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.adapter.MainPagerAdapter;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.R;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.custom.CustomDialog;
import cn.xhl.client.manga.model.bean.response.user.Res_CheckUpdate;
import cn.xhl.client.manga.service.ApkDownloadService;
import cn.xhl.client.manga.utils.AnalyticsUtil;
import cn.xhl.client.manga.utils.AppUtil;
import cn.xhl.client.manga.utils.FileUtil;
import cn.xhl.client.manga.utils.PrefUtil;

public class MainActivity extends BaseActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private MyPageChangeListener listener;

    private static class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<MainActivity> weakReference;

        private MyPageChangeListener(MainActivity mainActivity) {
            weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            weakReference.get().navigation.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_category:
                    viewPager.setCurrentItem(1, true);
                    return true;
                case R.id.navigation_mine:
                    viewPager.setCurrentItem(2, true);
                    return true;
            }
            return false;
        }

    };

    private ApkDownloadService apkDownloadService;
    private CustomDialog mPromptInstallDialog;
    private boolean mIsBound;// 判断是否绑定了service
    private boolean mIsShowDialog;// 判断是否要展示下载的弹窗提示
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ApkDownloadService.ApkDownloadBinder binder =
                    (ApkDownloadService.ApkDownloadBinder) service;
            apkDownloadService = binder.getService();
            apkDownloadService.setCallback(new ApkDownloadService.Callback() {
                @Override
                public void onSuccess(@NotNull File apkFile, @NotNull Res_CheckUpdate apkInfo) {
                    createInstallDialog(apkFile, apkInfo);
                }

                @Override
                public void onFailure(@NotNull Throwable e) {

                }

                @Override
                public void onFinish() {
                    doUnbindService();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            apkDownloadService = null;
        }
    };

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = findViewById(R.id.viewpager_activity_main);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        navigation = findViewById(R.id.navigation_activity_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeColor(navigation, R.color.background_main);

        listener = new MyPageChangeListener(this);
        viewPager.addOnPageChangeListener(listener);

        googleAnalytics();
        doBindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsShowDialog) {
            mIsShowDialog = false;
            mPromptInstallDialog.show();
        }
    }

    private void doBindService() {
        ApkDownloadService.bind(this, serviceConnection);
        mIsBound = true;
    }

    private void createInstallDialog(final File apkFile, @NotNull final Res_CheckUpdate apkInfo) {
        mPromptInstallDialog = new CustomDialog.DefaultBuilder(this)
                .setTitle("update")
                .setContent("version : " + apkInfo.getVersion_name() +
                        "\n" + "file size : " + apkInfo.getSize() + "\n" +
                        "")
                .setPositiveButtonText(R.string.action_install)
                .setNegativeButtonText(R.string.action_ignore)
                .setNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtil.putInt(IConstants.IGNORE_APK_INSTALL,
                                apkInfo.getVersion_code(), this_);
                    }
                })
                .setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtil.installApp(this_, apkFile);
                    }
                })
                .create();
        if (MyActivityManager.isTop(this)) {
            // 判断当前activity是否在前台
            mPromptInstallDialog.show();
            mIsShowDialog = true;
        }
    }

    @Override
    protected void onDestroy() {
        viewPager.removeOnPageChangeListener(listener);
        viewPager.removeAllViews();
        listener = null;
        doUnbindService();
        super.onDestroy();
    }

    private void doUnbindService() {
        if (mIsBound) {
            this.unbindService(serviceConnection);
            mIsBound = false;
        }
    }

    private void googleAnalytics() {
        new AnalyticsUtil.ScreenBuilder()
                .setClientId(String.valueOf(UserInfo.getInstance().getUid()))
                .setScreenName("MainActivity")
                .setLanguage(Locale.getDefault().getLanguage())
                .build();

    }

}
