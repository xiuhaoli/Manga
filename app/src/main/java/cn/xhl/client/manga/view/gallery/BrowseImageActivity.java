package cn.xhl.client.manga.view.gallery;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;

import java.lang.ref.WeakReference;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.contract.gallery.BrowseImageContract;
import cn.xhl.client.manga.custom.VerticalViewPager;
import cn.xhl.client.manga.custom.zoomable.ZoomableDraweeView;
import cn.xhl.client.manga.presenter.gallery.BrowseImagePresenter;
import cn.xhl.client.manga.utils.ActivityUtil;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.PrefUtil;

public class BrowseImageActivity extends BaseActivity
        implements BrowseImageContract.View, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    private static final String BRIGHTNESS = "brightness";
    private static final String LEFT_OR_RIGHT = "left_or_right";
    private static final String VERTICAL_OR_HORIZONTAL = "vertical_or_horizontal";
    private static final String SCROLL_MODE = "scroll_mode";
    // 当前的亮度
    private int currentBrightness;
    // 是否切换为右滑
    private boolean switch2RightDirection;
    // 是否把屏幕置为横向
    private boolean switch2HorizontalScreen;
    // 是否垂直滚动
    private boolean switch2VerticalScroll;
    private BrowseImageContract.Presenter presenter;
    private SparseArray<ZoomableDraweeView> imgArray;
    private VerticalViewPager viewPager;
    private TextView textView;
    private int count;// 当前总页数
    private int currentPage = 1;
    private PopupWindow popupWindow;
    private boolean isPopupWindowShowing;
    private ViewGroup viewPagerGroup;

    public static void start(Activity activity, String showkey, String thumb, int count, String imgkey, int gid) {
        Intent intent = new Intent(activity, BrowseImageActivity.class);
        intent.putExtra("showkey", showkey);
        intent.putExtra("thumb", thumb);
        intent.putExtra("secondImgkey", imgkey);
        intent.putExtra("count", count);
        intent.putExtra("gid", gid);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        Intent intent = getIntent();
        String thumb = intent.getStringExtra("thumb");
        String showkey = intent.getStringExtra("showkey");
        String secondImgkey = intent.getStringExtra("secondImgkey");
        count = intent.getIntExtra("count", 1);
        int gid = intent.getIntExtra("gid", 0);
        imgArray = new SparseArray<>(3);

        new BrowseImagePresenter(this, count, secondImgkey, gid, showkey, thumb);
        textView = findViewById(R.id.tv_activity_browse_image);
        textView.setText(getResources().getString(R.string.prompt_page_number, 1, count));
        ControlUtil.initControlOnClick(R.id.setting_activity_browse_image, this, this);
        viewPager = findViewById(R.id.viewpager_activity_browse_image);
        viewPager.setAdapter(new BrowseImageAdapter(this, count));
        viewPager.addOnPageChangeListener(new BrowseImagePageChangeListener());
        changeBrightness(PrefUtil.getInt(BRIGHTNESS, 50, this));
        changeSwitchDirection(PrefUtil.getBoolean(LEFT_OR_RIGHT, false, this));
        changeScreen(PrefUtil.getBoolean(VERTICAL_OR_HORIZONTAL, false, this));
        changeScrollMode(PrefUtil.getBoolean(SCROLL_MODE, false, this));
        createPopupWindow();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_browse_image;
    }

    @Override
    public void setPresenter(BrowseImageContract.Presenter presenter) {
        this.presenter = presenter;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_popup_browse_image_config:
                presenter.saveImage2Local(currentPage);
                break;
            case R.id.setting_activity_browse_image:
                if (!isPopupWindowShowing) {
                    showPopupWindow();
                } else {
                    hidePopupWindow();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isPopupWindowShowing) {
            hidePopupWindow();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void createPopupWindow() {
        final View rootView = getLayoutInflater().inflate(R.layout.popup_browse_image_config, viewPager, false);
        TextView saveButton = rootView.findViewById(R.id.save_popup_browse_image_config);
        SeekBar brightness = rootView.findViewById(R.id.action_brightness_popup_browse_image_config);
        Switch leftRightSwitch = rootView.findViewById(R.id.left_or_right_popup_browse_image_config);
        Switch verticalHorizontalSwitch = rootView.findViewById(R.id.vertical_or_horizontal_popup_browse_image_config);
        Switch scrollVerticallySwitch = rootView.findViewById(R.id.scroll_vertically_popup_browse_image_config);
        saveButton.setOnClickListener(this);
        brightness.setProgress(currentBrightness);
        brightness.setOnSeekBarChangeListener(this);
        leftRightSwitch.setChecked(switch2RightDirection);
        leftRightSwitch.setOnCheckedChangeListener(this);
        verticalHorizontalSwitch.setChecked(switch2HorizontalScreen);
        verticalHorizontalSwitch.setOnCheckedChangeListener(this);
        scrollVerticallySwitch.setChecked(switch2VerticalScroll);
        scrollVerticallySwitch.setOnCheckedChangeListener(this);
        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(rootView);
        popupWindow.setAnimationStyle(R.style.popupWindow);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
    }

    @Override
    public void showPopupWindow() {
        popupWindow.showAtLocation(viewPager, Gravity.BOTTOM, 0, 0);
        isPopupWindowShowing = true;
    }

    @Override
    public void hidePopupWindow() {
        popupWindow.dismiss();
        isPopupWindowShowing = false;
    }

    @Override
    public void showLoading() {
        super.showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        super.hideLoadingDialog();
    }

    @Override
    public void showTipMsg(String msg) {
        super.showToast(msg);
    }

    @Override
    public void setUrl(int page, Uri uri) {
        ZoomableDraweeView zoomableDraweeView = imgArray.get(page);
        if (zoomableDraweeView != null) {
            zoomableDraweeView.setImageURI(uri);
        }
    }

    @Override
    public void clearUriFromMemoryCache(Uri uri) {
        if (uri != null) {
            Fresco.getImagePipeline().evictFromMemoryCache(uri);
        }
    }

    @Override
    public void clearUriFromDiskCache(Uri uri) {
        if (uri != null) {
            Fresco.getImagePipeline().evictFromDiskCache(uri);
        }
    }

    @Override
    public void changeSwitchDirection(boolean isChecked) {
        switch2RightDirection = isChecked;
        if (viewPagerGroup != null) {
            viewPagerGroup.removeView(imgArray.get(currentPage));
            viewPagerGroup.removeView(imgArray.get(currentPage - 1));
            viewPagerGroup.removeView(imgArray.get(currentPage + 1));
            // 这里选择把原来集合中的当前页面及其左右页面的控件移除
            imgArray.remove(currentPage + 1);
            imgArray.remove(currentPage);
            imgArray.remove(currentPage - 1);
        }
        if (isChecked) {
            switch2Right();
        } else {
            switch2Left();
        }
    }

    @Override
    public void switch2Left() {
        // 设置为左滑,currentPage是从1开始的
        viewPager.setCurrentItem(currentPage - 1);
    }

    @Override
    public void switch2Right() {
        viewPager.setCurrentItem(count - currentPage);
    }

    @Override
    public void changeScreen(boolean isChecked) {
        switch2HorizontalScreen = isChecked;
        if (isChecked) {
            switch2HorizontalScreen();
        } else {
            switch2VerticalScreen();
        }
    }

    @Override
    public void changeScrollMode(boolean isChecked) {
        switch2VerticalScroll = isChecked;
        if (isChecked) {
            switch2VerticalScroll();
        } else {
            switch2HorizontalScroll();
        }
    }

    @Override
    public void switch2VerticalScreen() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void switch2HorizontalScreen() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void switch2VerticalScroll() {
        viewPager.setVerticalScrollMode(true);
    }

    @Override
    public void switch2HorizontalScroll() {
        viewPager.setVerticalScrollMode(false);
    }

    private static class BrowseImageAdapter extends PagerAdapter {
        private WeakReference<BrowseImageActivity> activity;
        private int count;

        private BrowseImageAdapter(BrowseImageActivity activity, int count) {
            this.activity = new WeakReference<>(activity);
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * ViewPager在每次滑动的时候都会调用instantiateItem方法
         * ViewPager在往回滑动的时候，会预加载position - 1位置的item
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            activity.get().viewPagerGroup = container;
            if (activity.get().switch2RightDirection) {
                // 如果是右划，那么要对position进行更改
                position = count - position;
            } else {
                // 如果是左划, 就普通加1，按照起始位置1来算
                position += 1;
            }
            AnimationDrawable animationDrawable = new AnimationDrawable();
            Drawable loading = ActivityCompat.getDrawable(activity.get(), R.drawable.loading);
            animationDrawable.addFrame(loading, 200);
            animationDrawable.setOneShot(false);
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(activity.get().getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .setFailureImage(R.mipmap.failure_img, ScalingUtils.ScaleType.CENTER_INSIDE)
                    .setProgressBarImage(animationDrawable, ScalingUtils.ScaleType.CENTER_INSIDE)
                    .build();
            ZoomableDraweeView zoomableDraweeView = new ZoomableDraweeView(activity.get(), hierarchy);
            activity.get().imgArray.put(position, zoomableDraweeView);
            activity.get().presenter.reqImgUrl(position);
            container.addView(zoomableDraweeView);
            return zoomableDraweeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            // imgArray在这里移除position+1的view出现第十页会加载不出来的情况
        }

    }

    private class BrowseImagePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 该方法比适配器的destroyItem早调用
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            if (switch2RightDirection) {
                currentPage = count - position;
            } else {
                currentPage = position + 1;
            }
            textView.setText(getResources().getString(R.string.prompt_page_number, currentPage, count));
            presenter.startClearUriMemoryTask(currentPage);
            // 将前后隔一页的控件清除
            imgArray.remove(currentPage - 2);
            imgArray.remove(currentPage + 2);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.left_or_right_popup_browse_image_config:
                PrefUtil.putBoolean(LEFT_OR_RIGHT, isChecked, this);
                changeSwitchDirection(isChecked);
                break;
            case R.id.vertical_or_horizontal_popup_browse_image_config:
                PrefUtil.putBoolean(VERTICAL_OR_HORIZONTAL, isChecked, this);
                changeScreen(isChecked);
                break;
            case R.id.scroll_vertically_popup_browse_image_config:
                PrefUtil.putBoolean(SCROLL_MODE, isChecked, this);
                changeScrollMode(isChecked);
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        changeBrightness(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // 在进度条移动结束回调中保存进度
        PrefUtil.putInt(BRIGHTNESS, currentBrightness, this);
    }

    @Override
    public void changeBrightness(int progress) {
        currentBrightness = progress;
        ActivityUtil.setActivityBrightness(progress * 0.01f, this);
    }
}
