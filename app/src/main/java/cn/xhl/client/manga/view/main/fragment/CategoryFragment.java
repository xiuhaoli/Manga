package cn.xhl.client.manga.view.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.UserInfo;
import cn.xhl.client.manga.adapter.main.CategoryAdapter;
import cn.xhl.client.manga.base.BaseFragment;
import cn.xhl.client.manga.config.IConstants;
import cn.xhl.client.manga.contract.main.CategoryContract;
import cn.xhl.client.manga.view.gallery.ConcreteCategoryActivity;

/**
 * 分类页的Fragment
 *
 * @author Mike on 2017/10/9 0009.
 */
public class CategoryFragment extends BaseFragment implements CategoryContract.View, BaseQuickAdapter.OnItemClickListener {
    private CategoryContract.Presenter presenter;
    private CategoryAdapter mAdapter;
    private List<CategoryAdapter.CategoryItem> mRecyclerData;
    /**
     * 列表展示的列数
     */
    private static final int COLUMN = 3;
    private String[] category = {IConstants.NON_H, IConstants.DOUJINSHI, IConstants.ARTIST_CG_SETS, IConstants.COSPLAY,
            IConstants.GAME_CG_SETS, IConstants.IMAGE_SETS, IConstants.MANGA, IConstants.MISC, IConstants.WESTERN,
            IConstants.JAPANESE, IConstants.ENGLISH, IConstants.CHINESE
    };

    // Non-H mode展示的item
    private String[] categoryNonH = IConstants.ALL_LANGUAGE;

    @Override
    protected int layoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_fragment_category);
        mRecyclerData = new ArrayList<>();
        mAdapter = new CategoryAdapter(mRecyclerData);
        mAdapter.setOnItemClickListener(this);
        mAdapter.openLoadAnimation();
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, COLUMN));
        recyclerView.setAdapter(mAdapter);
        initRecyclerData();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initRecyclerData() {
        int[] img = {R.mipmap.non_h, R.mipmap.doujinshi, R.mipmap.artist_cg_sets, R.mipmap.cosplay,
                R.mipmap.game_cg, R.mipmap.image_sets, R.mipmap.manga, R.mipmap.misc, R.mipmap.western,
                R.mipmap.japanese, R.mipmap.english, R.mipmap.chinese
        };
        int[] imgNonH = {R.mipmap.japanese, R.mipmap.english, R.mipmap.chinese,
                R.mipmap.korean, R.mipmap.spanish, R.mipmap.russian,
                R.mipmap.vietnamese, R.mipmap.french, R.mipmap.portuguese, R.mipmap.thai,
                R.mipmap.german, R.mipmap.polish, R.mipmap.italian, R.mipmap.greek
        };
        if (UserInfo.getInstance().isNonhMode()) {
            img = imgNonH;
            category = categoryNonH;
        }
        CategoryAdapter.CategoryItem item;
        for (int i = 0; i < img.length; i++) {
            item = new CategoryAdapter.CategoryItem();
            item.setText(category[i]);
            item.setImgId(img[i]);
            mRecyclerData.add(item);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (UserInfo.getInstance().isNonhMode()) {
            ConcreteCategoryActivity.Companion.start(mActivity, IConstants.NON_H + ":" +
                    category[position], IConstants.LANGUAGE);
            return;
        }
        if (position < 9) {
            ConcreteCategoryActivity.Companion.start(mActivity, category[position], IConstants.CATEGORY_LATEST);
        } else {
            ConcreteCategoryActivity.Companion.start(mActivity, IConstants.DEFAULT_CATEGORY + ":" +
                    category[position], IConstants.LANGUAGE);
        }
    }
}
