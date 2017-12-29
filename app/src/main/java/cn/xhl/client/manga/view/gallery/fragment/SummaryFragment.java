package cn.xhl.client.manga.view.gallery.fragment;

import android.os.Bundle;
import android.view.View;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.contract.gallery.SummaryContract;
import cn.xhl.client.manga.custom.TextImageSpan;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.DateUtil;
import cn.xhl.client.manga.utils.StringUtil;
import cn.xhl.client.manga.view.gallery.BrowseImageActivity;
import cn.xhl.client.manga.view.gallery.ConcreteMangaActivity;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/18
 *     version: 1.0
 * </pre>
 */
public class SummaryFragment extends BaseFragment
        implements View.OnClickListener, SummaryContract.View {
    private Res_GalleryList.GalleryEntity galleryEntity;
    private SummaryContract.Presenter presenter;

    @Override
    protected int layoutId() {
        return R.layout.fragment_manga_summary;
    }

    private void initData() {
        Bundle bundle = getArguments();
        galleryEntity = (Res_GalleryList.GalleryEntity) bundle.getSerializable(ConcreteMangaActivity.GALLERY_ENTITY);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initData();

        TextImageSpan author = view.findViewById(R.id.author_fragment_manga_summary);
        TextImageSpan category = view.findViewById(R.id.category_fragment_manga_summary);
        TextImageSpan posted = view.findViewById(R.id.posted_fragment_manga_summary);
        TextImageSpan page = view.findViewById(R.id.page_fragment_manga_summary);
        TextImageSpan uploader = view.findViewById(R.id.uploader_fragment_manga_summary);
        TextImageSpan rating = view.findViewById(R.id.rating_fragment_manga_summary);

        author.setText(getResources().getString(R.string.prompt_author, galleryEntity.getArtist()));
        category.setText(getResources().getString(R.string.prompt_category, galleryEntity.getCategory()));
        posted.setText(getResources().getString(R.string.prompt_posted, DateUtil.stampToDateWithHMS(galleryEntity.getPosted() + 14400)));
        page.setText(getResources().getString(R.string.prompt_page, galleryEntity.getFilecount()));
        uploader.setText(getResources().getString(R.string.prompt_uploader, galleryEntity.getUploader()));
        rating.setText(getResources().getString(R.string.prompt_rating, galleryEntity.getRating()));

        ControlUtil.initControlOnClick(R.id.btn_fragment_manga_summary, view, this);
        presenter.parsePage(galleryEntity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment_manga_summary:
                if (StringUtil.isEmpty(presenter.getShowKey()) || StringUtil.isEmpty(presenter.getImgKey()) || StringUtil.isEmpty(presenter.getFirstImg())) {
                    showToastMsg("lack of some parameters");
                    return;
                }
                // 将解析三级页面获取到的第一张图片的URL，还有showkey和第二张图片的imgkey发送过去
                BrowseImageActivity.start(getActivity(), presenter.getShowKey(), presenter.getFirstImg(), galleryEntity.getFilecount(), presenter.getImgKey(), galleryEntity.getGid());
                break;
        }
    }

    @Override
    public void setPresenter(SummaryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        mActivity.showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        mActivity.hideLoadingDialog();
    }

    @Override
    public void showToastMsg(String msg) {
        mActivity.showToast(msg);
    }
}
