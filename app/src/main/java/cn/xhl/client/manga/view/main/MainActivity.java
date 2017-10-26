package cn.xhl.client.manga.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import java.lang.ref.WeakReference;

import cn.xhl.client.manga.adapter.MainPagerAdapter;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.R;

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

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = findViewById(R.id.viewpager_activity_main);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        navigation = findViewById(R.id.navigation_activity_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeColor(navigation, R.color.background_main);

        listener = new MyPageChangeListener(this);
        viewPager.addOnPageChangeListener(listener);
    }

    @Override
    protected void onDestroy() {
        viewPager.removeOnPageChangeListener(listener);
        viewPager.removeAllViews();
        listener = null;
        super.onDestroy();
    }
}
