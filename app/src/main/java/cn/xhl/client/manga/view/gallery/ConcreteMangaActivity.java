package cn.xhl.client.manga.view.gallery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.gallery.ConcreteMangaPagerAdapter;
import cn.xhl.client.manga.adapter.gallery.FavoriteFolderDialogAdapter;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.contract.gallery.ConcreteMangaContract;
import cn.xhl.client.manga.custom.CustomDialog;
import cn.xhl.client.manga.custom.EmptyView;
import cn.xhl.client.manga.custom.SlipBackLayout;
import cn.xhl.client.manga.custom.TextImageSpan;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.presenter.gallery.ConcreteMangaPresenter;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.DpUtil;
import cn.xhl.client.manga.utils.LogUtil;
import cn.xhl.client.manga.utils.QMUIStatusBarHelper;
import cn.xhl.client.manga.utils.StringUtil;

/**
 * 这是详情页面
 */
public class ConcreteMangaActivity extends BaseActivity
        implements ConcreteMangaContract.View, View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    private ConcreteMangaContract.Presenter presenter;
    private Res_GalleryList.GalleryEntity galleryEntity;
    private TextImageSpan star;
    private TextImageSpan popularity;
    private static final String GALLERY_BUNDLE = "GalleryBundle";
    public static final String GALLERY_ENTITY = "GalleryEntity";
    private String selectedFolder = "";// 被选中的文件夹
    private String originalFolder = "";// 原来被选中的folder（没有就为空）该值主要用于判断是否发起请求
    private List<Res_FavoriteFolder.Data> mRecyclerData;
    private FavoriteFolderDialogAdapter mRecyclerAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private Dialog newFolderDialog;
    private EmptyView emptyView;

    public static void start(Activity activity, Res_GalleryList.GalleryEntity galleryEntity) {
        Intent intent = new Intent(activity, ConcreteMangaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GALLERY_ENTITY, galleryEntity);
        intent.putExtra(GALLERY_BUNDLE, bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSlipClose();// 开启滑动关闭

        new ConcreteMangaPresenter(this);

        galleryEntity = (Res_GalleryList.GalleryEntity) getIntent().getBundleExtra(GALLERY_BUNDLE)
                .getSerializable(GALLERY_ENTITY);
        initViewPager();

        SimpleDraweeView titleImg = findViewById(R.id.img_activity_concrete_manga);
        TextView title = findViewById(R.id.title_activity_concrete_manga);

        popularity = findViewById(R.id.popularity_activity_concrete_manga);
        star = (TextImageSpan) ControlUtil.initControlOnClick(R.id.star_activity_concrete_manga,
                this, this);

        titleImg.setImageURI(galleryEntity.getThumb());
        title.setText(galleryEntity.getTitle());

        star.setText(getResources().getString(R.string.prompt_star, galleryEntity.getSubscribe()));
        popularity.setText(getResources().getString(R.string.prompt_popularity, galleryEntity.getViewed()));

        presenter.viewed(galleryEntity.getId());
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_concrete_manga;
    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager_activity_concrete_manga);
        viewPager.setAdapter(new ConcreteMangaPagerAdapter(getSupportFragmentManager(), galleryEntity));
        TabLayout tabLayout = findViewById(R.id.tab_activity_concrete_manga);
        tabLayout.setupWithViewPager(viewPager, false);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star_activity_concrete_manga:
                if (bottomSheetDialog == null) {
                    // 这玩意儿放在onCreate里面但是一次都不show的话会leak
                    createBottomSheet();
                }
                // 点击了收藏弹出底部栏
                showBottomSheet();
                break;
            case R.id.submit_bottomsheet_favorite_folder:
                if (originalFolder.equals(selectedFolder)) {
                    // 如果是一样的不进行请求
                    return;
                }
                presenter.favorite(galleryEntity.getId(), selectedFolder);
                bottomSheetDialog.cancel();
                break;
            case R.id.action_new_folder_bottomsheet_favorite_folder:
                if (newFolderDialog != null) {
                    newFolderDialog.show();
                    return;
                }
                createNewFolderDialog();
                break;
            default:
                break;
        }
    }

    private void createNewFolderDialog() {
        newFolderDialog = new CustomDialog.EditTextBuilder(this)
                .setTitle(R.string.prompt_new_folder_title)
                .setHint(R.string.hint_new_folder_dialog)
                .setPositiveListener(new CustomDialog.OnInputClickListener() {
                    @Override
                    public void onClick(CustomDialog dialog, View v, String inputText) {
                        if (StringUtil.isNotEmpty(inputText) && inputText.length() < 17
                                && StringUtil.isValidName(inputText)) {
                            presenter.favorite(galleryEntity.getId(), inputText);
                            newFolderDialog.cancel();
                            bottomSheetDialog.cancel();
                        } else {
                            showToastMsg("invalid name");
                        }
                    }
                })
                .create();
        newFolderDialog.show();
    }

    @Override
    public void failLoadMore() {
        mRecyclerAdapter.loadMoreFail();
    }

    @Override
    public void noMoreToLoad() {
        mRecyclerAdapter.loadMoreEnd();
    }

    @Override
    public void showReTry() {
        emptyView.showRetry(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.listFolder(false, galleryEntity.getId());
            }
        });
    }

    @Override
    public void hideReTry() {
        emptyView.hideRetry();
    }

    @Override
    public void notifyAdapterFolder(Res_FavoriteFolder favoriteFolder) {
        mRecyclerData.addAll(favoriteFolder.getFolders());
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerAdapter.loadMoreComplete();
        initOriginalFolder(favoriteFolder);
    }

    private void initOriginalFolder(Res_FavoriteFolder favoriteFolder) {
        for (Res_FavoriteFolder.Data data : favoriteFolder.getFolders()) {
            if (data.isChecked()) {
                originalFolder = data.getFolder();
                selectedFolder = originalFolder;
                break;
            }
        }
    }

    @Override
    public void showNoData() {
        emptyView.showNodata();
    }

    @Override
    public void hideNoData() {
        emptyView.hideNodata();
    }

    @Override
    public void showEmptyLoading() {
        emptyView.showLoading();
    }

    @Override
    public void hideEmptyLoading() {
        emptyView.hideLoading();
    }

    @Override
    public void createBottomSheet() {
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new FavoriteFolderDialogAdapter(mRecyclerData);
        final View view = LayoutInflater.from(this).inflate(R.layout.bottomsheet_favorite_folder, null);
        ControlUtil.initControlOnClick(R.id.action_new_folder_bottomsheet_favorite_folder, view, this);
        emptyView = view.findViewById(R.id.empty_bottomsheet_favorite_folder);
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_bottomsheet_favorite_folder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(mRecyclerAdapter);

        // 这玩意儿是防止和bottomsheet的冲突
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                recyclerView.getParent().requestDisallowInterceptTouchEvent(
                        recyclerView.canScrollVertically(-1)
                );
                return false;
            }
        });
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        createBottomSheetCheckButton();

        bottomSheetDialog.create();
    }

    @Override
    public void createBottomSheetCheckButton() {
        RelativeLayout buttonParent = new RelativeLayout(this);
        ViewGroup.LayoutParams rlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParent.setLayoutParams(rlp);
        Button checkButton = new Button(this);
        checkButton.setAllCaps(false);
        checkButton.setId(R.id.submit_bottomsheet_favorite_folder);
        checkButton.setText(R.string.action_submit);
        checkButton.setOnClickListener(this);
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.item_text, typedValue, true);
        checkButton.setTextColor(typedValue.data);
        getTheme().resolveAttribute(R.attr.item_background, typedValue, true);
        checkButton.setBackgroundColor(typedValue.data);
        RelativeLayout.LayoutParams blp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2Px(this, 50));
        blp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        checkButton.setLayoutParams(blp);
        buttonParent.addView(checkButton);
        bottomSheetDialog.addContentView(buttonParent, rlp);
    }

    @Override
    public void showBottomSheet() {
        bottomSheetDialog.show();
        mRecyclerData.clear();
        presenter.initReqListData();
        presenter.listFolder(false, galleryEntity.getId());
    }

    @Override
    public void dismissBottomSheet() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        boolean isCheck = mRecyclerData.get(position).isChecked();
        for (int i = 0, length = mRecyclerData.size(); i < length; i++) {
            mRecyclerData.get(i).setChecked(false);
        }
        if (isCheck) {
            // 如果点中的item是已经选中的则将folder置空
            mRecyclerData.get(position).setChecked(false);
            selectedFolder = "";
        } else {
            mRecyclerData.get(position).setChecked(true);
            selectedFolder = mRecyclerData.get(position).getFolder();
        }
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreRequested() {
        presenter.listFolder(true, galleryEntity.getId());
    }

}
