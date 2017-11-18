package cn.xhl.client.manga.view.gallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.adapter.gallery.FavoriteFolderDialogAdapter;
import cn.xhl.client.manga.base.BaseActivity;
import cn.xhl.client.manga.contract.gallery.ConcreteMangaContract;
import cn.xhl.client.manga.custom.TextImageSpan;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList;
import cn.xhl.client.manga.presenter.gallery.ConcreteMangaPresenter;
import cn.xhl.client.manga.utils.ControlUtil;
import cn.xhl.client.manga.utils.DateUtil;
import cn.xhl.client.manga.utils.DpUtil;
import cn.xhl.client.manga.utils.StringUtil;

/**
 * 这是详情页面
 */
public class ConcreteMangaActivity extends BaseActivity
        implements ConcreteMangaContract.View, View.OnClickListener,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {
    private ConcreteMangaContract.Presenter presenter;
    private Res_GalleryList.GalleryEntity galleryEntity;
    private final static int SHOW_TOAST = 1;
    private TextImageSpan star;
    private TextImageSpan popularity;
    public static final String GALLERY_BUNDLE = "GalleryBundle";
    public static final String GALLERY_ENTITY = "GalleryEntity";
    private String selectedFolder = "";// 被选中的文件夹
    private String originalFolder = "";// 原来被选中的folder（没有就为空）该值主要用于判断是否发起请求
    private LinearLayout retry;
    private TextView noData;// 没有数据的时候显示
    private List<Res_FavoriteFolder.Data> mRecyclerData;
    private FavoriteFolderDialogAdapter mRecyclerAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyHandler handler;
    private AlertDialog newFolderDialog;

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
        new ConcreteMangaPresenter(this);
        handler = new MyHandler(this);

        galleryEntity = (Res_GalleryList.GalleryEntity) getIntent().getBundleExtra(GALLERY_BUNDLE).getSerializable(GALLERY_ENTITY);
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
        createBottomSheet();
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
                // 点击了收藏弹出底部栏
                showBottomSheet();
                break;
            case R.id.btn_bottomsheet_favorite_folder:
                // retry
                presenter.listFolder(false, galleryEntity.getId());
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
        int dp = DpUtil.dp2Px(this, 25);
        final EditText editText = new EditText(this);
        editText.setHeight(DpUtil.dp2Px(this, 50));
        editText.setTextSize(DpUtil.dp2Px(this, 10));
        editText.setHint(R.string.hint_new_folder_dialog);
        editText.setMaxEms(16);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setSingleLine();
        editText.setPadding(dp, 0, 0, 0);
        newFolderDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.prompt_new_folder_title)
                .setView(editText)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedFolder = editText.getText().toString();
                        if (originalFolder.equals(selectedFolder)) {
                            return;
                        }
                        if (StringUtil.isNotEmpty(selectedFolder) && selectedFolder.length() < 17
                                && StringUtil.isValidName(selectedFolder)) {
                            presenter.favorite(galleryEntity.getId(), selectedFolder);
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
        retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReTry() {
        retry.setVisibility(View.GONE);
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
        noData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoData() {
        noData.setVisibility(View.GONE);
    }

    @Override
    public void createBottomSheet() {
        mRecyclerData = new ArrayList<>();
        mRecyclerAdapter = new FavoriteFolderDialogAdapter(mRecyclerData);
        View view = LayoutInflater.from(this).inflate(R.layout.bottomsheet_favorite_folder, null);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_bottomsheet_favorite_folder);
        mSwipeRefreshLayout.setOnRefreshListener(this);// 设置刷新监听
        retry = view.findViewById(R.id.linear_bottomsheet_favorite_folder);
        ControlUtil.initControlOnClick(R.id.btn_bottomsheet_favorite_folder, view, this);
        ControlUtil.initControlOnClick(R.id.submit_bottomsheet_favorite_folder, view, this);
        ControlUtil.initControlOnClick(R.id.action_new_folder_bottomsheet_favorite_folder, view, this);
        noData = view.findViewById(R.id.no_data_bottomsheet_favorite_folder);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_bottomsheet_favorite_folder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter.setOnItemClickListener(this);
        mRecyclerAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(mRecyclerAdapter);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.create();
    }

    @Override
    public void startRefreshing() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showBottomSheet() {
        bottomSheetDialog.show();
        mRecyclerData.clear();
        presenter.initReqListData();
        mSwipeRefreshLayout.post(new RefreshTask(this));
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

    @Override
    public void onRefresh() {
        presenter.listFolder(false, galleryEntity.getId());
    }

    private static class RefreshTask implements Runnable {
        private WeakReference<ConcreteMangaActivity> weakReference;

        private RefreshTask(ConcreteMangaActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            weakReference.get().onRefresh();
        }
    }
}
