package cn.xhl.client.manga.view.gallery.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.gallery.SummaryAdapter;
import cn.xhl.client.manga.adapter.gallery.SummaryAdapter.SummaryItem;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.contract.gallery.SummaryContract;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.utils.DateUtil;
import cn.xhl.client.manga.view.gallery.ConcreteMangaActivity;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2017/12/18
 *     version: 1.0
 * </pre>
 */
public class SummaryFragment extends BaseFragment implements
        SummaryContract.View, BaseQuickAdapter.OnItemChildClickListener {
    private Res_GalleryList.GalleryEntity galleryEntity;
    private SummaryContract.Presenter presenter;
    private List<SummaryItem> mRecyclerData;
    private SummaryAdapter mRecyclerAdapter;
    private static final int AUTHOR = 0;
    private static final int UPLOADER = 4;
    private ParamsCallback mParamsCallback;

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
        if (mActivity instanceof ParamsCallback) {
            mParamsCallback = (ParamsCallback) mActivity;
        }
        initData();
        initAdapter(view);
        presenter.parsePage(galleryEntity);
        presenter.isFollowed(galleryEntity.getArtist(), galleryEntity.getUploader());
    }

    private void initAdapter(View view) {
        int[] img = {R.mipmap.span_author, R.mipmap.span_category, R.mipmap.span_posted,
                R.mipmap.span_filecount, R.mipmap.span_uploader, R.mipmap.span_rating};
        String[] leftText = {
                getResources().getString(R.string.prompt_author, galleryEntity.getArtist()),
                getResources().getString(R.string.prompt_category, galleryEntity.getCategory()),
                getResources().getString(R.string.prompt_posted,
                        DateUtil.stampToDateWithHMS(galleryEntity.getPosted() + 14400)),
                getResources().getString(R.string.prompt_page, galleryEntity.getFilecount()),
                getResources().getString(R.string.prompt_uploader, galleryEntity.getUploader()),
                getResources().getString(R.string.prompt_rating, galleryEntity.getRating())
        };
        String[] rightText = {getResources().getString(R.string.follow), "", "", "",
                getResources().getString(R.string.follow), ""};
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_manga_summary);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerData = new ArrayList<>(leftText.length);
        for (int i = 0; i < leftText.length; i++) {
            SummaryItem item = new SummaryItem();
            item.setImgRes(img[i]);
            item.setLeft(leftText[i]);
            item.setRight(rightText[i]);
            mRecyclerData.add(item);
        }
        mRecyclerAdapter = new SummaryAdapter(mRecyclerData);
        mRecyclerAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(mRecyclerAdapter);
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

    @Override
    public void changeArtistFollowButton(boolean isFollowed) {
        mRecyclerData.get(AUTHOR).setRight(isFollowed ?
                getString(R.string.unfollow) : getString(R.string.follow));
        mRecyclerAdapter.notifyItemChanged(AUTHOR);
    }

    @Override
    public void changeUploaderFollowButton(boolean isFollowed) {
        mRecyclerData.get(UPLOADER).setRight(isFollowed ?
                getString(R.string.unfollow) : getString(R.string.follow));
        mRecyclerAdapter.notifyItemChanged(UPLOADER);
    }

    @Override
    public void notifyActivity() {
        if (mParamsCallback != null) {
            mParamsCallback.setParams(presenter.getShowKey(), presenter.getImgKey(),
                    presenter.getFirstImg());
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (!getString(R.string.follow).equals(view.getTag())) return;
        switch (position) {
            case AUTHOR:
                presenter.attendArtist(galleryEntity.getArtist());
                break;
            case UPLOADER:
                presenter.attendUploader(galleryEntity.getUploader());
                break;
            default:
                break;
        }
    }

    /**
     * 用于回调数据到activity的接口
     */
    public interface ParamsCallback {
        void setParams(String showKey, String secondImgKey, String firstImg);
    }
}
