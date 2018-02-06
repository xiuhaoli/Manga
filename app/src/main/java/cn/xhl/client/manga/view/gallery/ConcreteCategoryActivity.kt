package cn.xhl.client.manga.view.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View

import com.chad.library.adapter.base.BaseQuickAdapter

import java.util.ArrayList

import cn.xhl.client.manga.R
import cn.xhl.client.manga.UserInfo
import cn.xhl.client.manga.adapter.main.GalleryListAdapter
import cn.xhl.client.manga.base.BaseActivity
import cn.xhl.client.manga.config.IConstants
import cn.xhl.client.manga.contract.main.LatestContract
import cn.xhl.client.manga.custom.EmptyView
import cn.xhl.client.manga.custom.StickyItemDecoration
import cn.xhl.client.manga.listener.GalleryListScrollListener
import cn.xhl.client.manga.model.bean.response.gallery.Res_GalleryList
import cn.xhl.client.manga.presenter.main.LatestPresenter
import cn.xhl.client.manga.utils.AnalyticsUtil
import cn.xhl.client.manga.utils.DpUtil
import cn.xhl.client.manga.utils.ResourceUtil
import cn.xhl.client.manga.utils.StringUtil

/**
 * 这个是具体类型的列表，本来可以和首页的fragment共用
 * 考虑到fragment不能被回收，就采用activity，用完销毁
 */
class ConcreteCategoryActivity : BaseActivity(), LatestContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
    private var presenter: LatestContract.Presenter? = null
    private var mRecyclerData = ArrayList<Res_GalleryList.GalleryEntity>()
    private var mRecyclerAdapter: GalleryListAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var category: String? = null
    private var type: String? = null
    private var emptyView: EmptyView? = null
    private var typedValue = TypedValue()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSlipClose()
        LatestPresenter(this)
        emptyView = findViewById(R.id.empty_activity_concrete_category)
        getIntentData()
        initRecyclerView()
        initAdapter()
        presenter!!.list(category, type, false)
    }

    private fun getIntentData() {
        category = intent.getStringExtra("category")
        type = intent.getStringExtra("type")
    }

    private fun initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_activity_concrete_category)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this_)
        addScrollListener()
        when (type) {
            IConstants.FAVORITE, IConstants.OTHERS_FAVORITE, IConstants.HISTORY ->
                mRecyclerView!!.addItemDecoration(StickyItemDecoration(this, mRecyclerData)
                        .apply {
                            stickyViewHeight = DpUtil.dp2Px(this_, 30f)
                            stickyTextSize = DpUtil.dp2Px(this_, 14f)
                            stickyTextColor = ResourceUtil.getAttrData(R.attr.item_text, typedValue)
                            stickyViewColor = ResourceUtil.getAttrData(R.attr.main_background, typedValue)
                        }
                )
            else -> {
            }
        }
    }

    private fun initAdapter() {
        mRecyclerAdapter = GalleryListAdapter(mRecyclerData)
        mRecyclerAdapter!!.onItemClickListener = this
        mRecyclerAdapter!!.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerAdapter!!.openLoadAnimation()
        mRecyclerView!!.adapter = mRecyclerAdapter
    }

    override fun layoutId(): Int {
        return R.layout.activity_concrete_category
    }

    override fun setPresenter(presenter: LatestContract.Presenter) {
        this.presenter = presenter
    }

    override fun onResume() {
        super.onResume()
        presenter!!.subscribe()
        AnalyticsUtil.ScreenBuilder()
                .setScreenName("ConcreteCategoryActivity:" + category!!)
                .build()
    }

    override fun onPause() {
        super.onPause()
        presenter!!.unSubscribe()
    }

    override fun showLoading() {
        showLoadingDialog()
    }

    override fun hideLoading() {
        hideLoadingDialog()
    }

    override fun showTipMsg(msg: String) {
        showToast(msg)
    }

    override fun failLoadMore() {
        mRecyclerAdapter!!.loadMoreFail()
    }

    override fun noMoreToLoad() {
        mRecyclerAdapter!!.loadMoreEnd()
    }

    override fun showReTry() {
        emptyView!!.showRetry {presenter!!.list(category, type, false) }
    }

    override fun hideReTry() {
        emptyView!!.hideRetry()
    }

    override fun showEmptyLoading() {
        emptyView!!.showLoading()
    }

    override fun hideEmptyLoading() {
        emptyView!!.hideLoading()
    }

    override fun filterItem(galleryList: Res_GalleryList) {
        when (type) {
            IConstants.CATEGORY_LATEST, IConstants.TITLE,
            IConstants.AUTHOR, IConstants.UPLOADER -> filterLanguage(galleryList)
            IConstants.LANGUAGE, IConstants.FAVORITE -> {
            }
            else -> {
            }
        }
    }

    private fun filterLanguage(galleryList: Res_GalleryList) {
        val filter = UserInfo.getInstance().filter
        if (StringUtil.isEmpty(filter)) return
        val iterator = galleryList.data.iterator()
        while (iterator.hasNext()) {
            val entity = iterator.next()
            var language = entity.language
            if (StringUtil.isEmpty(language)) {
                language = "unknown"
            }
            if (filter.contains(language)) {
                iterator.remove()
            }
        }
    }

    override fun notifyAdapter(galleryList: Res_GalleryList) {
        // 将新获取的数据添加到末尾
        mRecyclerData.addAll(galleryList.data)
        mRecyclerAdapter!!.notifyDataSetChanged()
        mRecyclerAdapter!!.loadMoreComplete()
    }

    override fun showNoData() {
        emptyView!!.showNodata()
    }

    override fun hideNoData() {
        emptyView!!.hideNodata()
    }

    override fun onLoadMoreRequested() {
        presenter!!.list(category, type, true)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        ConcreteMangaActivity.start(this, mRecyclerData[position])
    }

    private fun addScrollListener() {
        mRecyclerView?.addOnScrollListener(object : GalleryListScrollListener() {
            override fun onScrolledUp(firstVisible: Int, lastVisible: Int) {
                for (i in 0 until firstVisible) {
                    mRecyclerAdapter!!.evictImageFromMemoryCache(i)
                }
            }

            override fun onScrolledDown(firstVisible: Int, lastVisible: Int) {
                for (i in mRecyclerData.size - 1 downTo lastVisible + 1) {
                    mRecyclerAdapter!!.evictImageFromMemoryCache(i)
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        evictAllImageFromMemoryCache()
    }

    private fun evictAllImageFromMemoryCache() {
        var i = 0
        val length = mRecyclerData.size
        while (i < length) {
            mRecyclerAdapter!!.evictImageFromMemoryCache(i)
            i++
        }
    }

    companion object {

        /**
         * @param activity
         * @param category 请求的书籍类型（Non-H，doujinshi等），同时又充当搜索的内容
         * @param type     请求的类型
         */
        fun start(activity: Activity, category: String, type: String) {
            val intent = Intent(activity, ConcreteCategoryActivity::class.java)
            intent.putExtra("category", category)
            intent.putExtra("type", type)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}
