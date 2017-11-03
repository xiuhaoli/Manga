package cn.xhl.client.manga.view.gallery;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.contract.gallery.ConcreteMangaContract;
import cn.xhl.client.manga.custom.TextImageSpan;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;
import cn.xhl.client.manga.presenter.gallery.ConcreteMangaPresenter;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.DateUtil;
import cn.xhl.client.manga.utils.StringUtil;

/**
 * 这是详情页面
 */
public class ConcreteMangaActivity extends BaseActivity implements ConcreteMangaContract.View, View.OnClickListener {
    private ConcreteMangaContract.Presenter presenter;
    private Res_GalleryList.GalleryEntity galleryEntity;
    private final static int SHOW_TOAST = 1;
    private TextImageSpan star;
    private TextImageSpan popularity;

    private MyHandler handler;

    private static class MyHandler extends Handler {

        private WeakReference<ConcreteMangaActivity> activity;

        private MyHandler(ConcreteMangaActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    activity.get().showToast((String) msg.obj);
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ConcreteMangaPresenter(this);
        handler = new MyHandler(this);

        galleryEntity = (Res_GalleryList.GalleryEntity) getIntent().getBundleExtra("galleryBundle").getSerializable("GalleryEntity");
        SimpleDraweeView titleImg = findViewById(R.id.img_activity_concrete_manga);
        TextView title = findViewById(R.id.title_activity_concrete_manga);
        TextImageSpan author = findViewById(R.id.author_activity_concrete_manga);
        TextImageSpan category = findViewById(R.id.category_activity_concrete_manga);
        TextImageSpan posted = findViewById(R.id.posted_activity_concrete_manga);
        TextImageSpan page = findViewById(R.id.page_activity_concrete_manga);
        TextImageSpan uploader = findViewById(R.id.uploader_activity_concrete_manga);
        TextImageSpan rating = findViewById(R.id.rating_activity_concrete_manga);
        popularity = findViewById(R.id.popularity_activity_concrete_manga);
        star = (TextImageSpan) ControlUtil.initControlOnClick(R.id.star_activity_concrete_manga, this, this);
        ControlUtil.initControlOnClick(R.id.btn_activity_concrete_manga, this, this);

        titleImg.setImageURI(galleryEntity.getThumb());
        title.setText(galleryEntity.getTitle());
        author.setText(getResources().getString(R.string.prompt_author, galleryEntity.getArtist()));
        category.setText(getResources().getString(R.string.prompt_category, galleryEntity.getCategory()));
        posted.setText(getResources().getString(R.string.prompt_posted, DateUtil.stampToDate(galleryEntity.getPosted())));
        page.setText(getResources().getString(R.string.prompt_page, galleryEntity.getFilecount()));
        uploader.setText(getResources().getString(R.string.prompt_uploader, galleryEntity.getUploader()));
        rating.setText(getResources().getString(R.string.prompt_rating, galleryEntity.getRating()));

        star.setText(getResources().getString(R.string.prompt_star, galleryEntity.getSubscribe()));
        popularity.setText(getResources().getString(R.string.prompt_popularity, galleryEntity.getViewed()));

        presenter.viewed(galleryEntity.getId());
        presenter.parsePage(galleryEntity);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_concrete_manga;
    }

    @Override
    public void setPresenter(ConcreteMangaContract.Presenter presenter) {
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
        Message message = new Message();
        message.what = SHOW_TOAST;
        message.obj = msg;
        handler.sendMessage(message);
    }

    @Override
    public void showToastMsg(String msg) {
        super.showToast(msg);
    }

    @Override
    public void changeStarNumber(int delta) {
        star.setText(String.valueOf(Integer.valueOf(star.getText().toString()) + delta));
    }

    @Override
    public void plusViewedNumber() {
        popularity.setText(String.valueOf(galleryEntity.getViewed() + 1));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.unSubscribe();
        hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_concrete_manga:
                if (StringUtil.isEmpty(presenter.getShowKey()) || StringUtil.isEmpty(presenter.getImgKey()) || StringUtil.isEmpty(presenter.getFirstImg())) {
                    super.showToast("lack of some parameters");
                    return;
                }
                // 将解析三级页面获取到的第一张图片的URL，还有showkey和第二张图片的imgkey发送过去
                BrowseImageActivity.start(this, presenter.getShowKey(), presenter.getFirstImg(), galleryEntity.getFilecount(), presenter.getImgKey(), galleryEntity.getGid());
                break;
            case R.id.star_activity_concrete_manga:
                presenter.favorite(galleryEntity.getId());
                break;
            default:
                break;
        }
    }
}
