package cn.xhl.client.manga.view.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.contract.gallery.BrowseImageContract;
import cn.xhl.client.manga.presenter.gallery.BrowseImagePresenter;
import cn.xhl.client.manga.utils.LogUtil;

/**
 * 还需要增加还没请求到下一张图片的imgkey时，禁止滑动
 */
public class BrowseImageActivity extends BaseActivity implements BrowseImageContract.View {
    private static final String TAG = "BrowseImageActivity";
    private BrowseImageContract.Presenter presenter;
    private SparseArray<SimpleDraweeView> imgArray;

    private final static int SET_URL = 1;
    private MyHandler handler;
    private Message message;
    private TextView textView;
    private int count;

    private static class MyHandler extends Handler {

        private WeakReference<BrowseImageActivity> activity;

        private MyHandler(BrowseImageActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_URL:
                    activity.get().imgArray.get(msg.arg1).setImageURI((Uri) msg.obj);
                    break;
                default:
                    break;
            }
        }

    }

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
        handler = new MyHandler(this);

        Intent intent = getIntent();
        String thumb = intent.getStringExtra("thumb");
        String showkey = intent.getStringExtra("showkey");
        String secondImgkey = intent.getStringExtra("secondImgkey");
        count = intent.getIntExtra("count", 1);
        int gid = intent.getIntExtra("gid", 0);
        imgArray = new SparseArray<>();

        new BrowseImagePresenter(this, count, secondImgkey, gid, showkey, thumb);
        textView = findViewById(R.id.tv_activity_browse_image);
        textView.setText(1 + "/" + count);
        ViewPager viewPager = findViewById(R.id.viewpager_activity_browse_image);
        viewPager.setAdapter(new BrowseImageAdapter(this, count));
        viewPager.addOnPageChangeListener(new BrowseImagePageChangeListener());

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
        message = new Message();
        message.what = SET_URL;
        message.arg1 = page;
        message.obj = uri;
        handler.sendMessage(message);
    }

    @Override
    public void clearUriFromMemoryCache(Uri uri) {
        Fresco.getImagePipeline().evictFromMemoryCache(uri);
    }

    @Override
    public void clearUriFromDiskCache(Uri uri) {
        Fresco.getImagePipeline().evictFromDiskCache(uri);
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
            // 这里按照起始位置1
            position += 1;

            LogUtil.e(TAG, "当前请求的position" + position);

            SimpleDraweeView imgView = activity.get().imgArray.get(position);
            // 如果ImgView不为空说明之前初始化过
            if (imgView != null) {
                if (activity.get().presenter.haveImgkey(position) && !activity.get().presenter.haveImgkey(position + 1)) {
                    // 如果有imgkey且下一个imgkey不存在，说明需要请求下一个imgkey
                    activity.get().presenter.reqImgUrl(position);
                }
                container.addView(imgView);
                return imgView;
            }
            AnimationDrawable animationDrawable = new AnimationDrawable();
            Drawable loading = ActivityCompat.getDrawable(activity.get(), R.drawable.loading);
            animationDrawable.addFrame(loading, 200);
            animationDrawable.setOneShot(false);
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(activity.get().getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
                    .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
                    .setFailureImage(R.mipmap.failure_img, ScalingUtils.ScaleType.CENTER_INSIDE)
                    .setProgressBarImage(animationDrawable, ScalingUtils.ScaleType.CENTER_INSIDE)
                    .build();
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(activity.get(), hierarchy);
            activity.get().imgArray.put(position, simpleDraweeView);
            activity.get().presenter.reqImgUrl(position);
            container.addView(simpleDraweeView);
            return simpleDraweeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private class BrowseImagePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int pageNum = position + 1;
            textView.setText(pageNum + "/" + count);
            presenter.startClearUriMemoryTask(pageNum);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
