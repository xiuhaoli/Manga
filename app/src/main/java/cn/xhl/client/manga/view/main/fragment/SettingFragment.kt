package cn.xhl.client.manga.view.main.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.xhl.client.manga.R
import cn.xhl.client.manga.UserInfo
import cn.xhl.client.manga.adapter.main.SettingAdapter
import cn.xhl.client.manga.base.BaseFragment
import cn.xhl.client.manga.config.IConstants
import cn.xhl.client.manga.contract.main.SettingContract
import cn.xhl.client.manga.custom.CustomDialog
import cn.xhl.client.manga.custom.SettingItemDecoration
import cn.xhl.client.manga.utils.ActivityUtil
import cn.xhl.client.manga.utils.PrefUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import java.util.ArrayList

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/04
 *     version: 1.0
 * </pre>
 */
class SettingFragment : BaseFragment(), SettingContract.View,
        BaseQuickAdapter.OnItemClickListener {
    private val cacheItemPosition: Int = 1// 清除缓存那栏的position
    private lateinit var mRecyclerData: ArrayList<SettingAdapter.SettingItem>
    private lateinit var mSettingAdapter: SettingAdapter
    /**
     * var是可变变量的定义
     * val是常量相当于final修饰
     */
    private lateinit var presenter: SettingContract.Presenter

    companion object {
        val TAG: String = "SettingFragment"
    }

    override fun layoutId(): Int {
        return R.layout.recycler_only
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = view!!.findViewById(R.id.recycler_only)
        recyclerView.layoutManager = LinearLayoutManager(mActivity)
        recyclerView.addItemDecoration(SettingItemDecoration(mActivity, 3))
        initAdapter()
        recyclerView.adapter = mSettingAdapter
    }

    override fun setPresenter(presenter: SettingContract.Presenter) {
        this.presenter = presenter
    }

    override fun initAdapter() {
        mRecyclerData = ArrayList()
        var item: SettingAdapter.SettingItem
        val text = intArrayOf(R.string.non_h_mode, R.string.cache, R.string.logout)
        var i = 0
        val size = text.size
        while (i < size) {
            item = SettingAdapter.SettingItem()
            item.text = text[i]
            item.isHaveContent = false
            item.isHaveSwitcher = false
            if (i == 0) {
                item.isHaveSwitcher = true
                item.isChecked = UserInfo.getInstance().isNonhMode
            }
            if (i == cacheItemPosition) {
                item.isHaveContent = true
                item.content = presenter.cacheSize()
            }
            if (i == size - 1) {
                item.isLogout = true
            }
            mRecyclerData.add(item)
            i++
        }
        mSettingAdapter = SettingAdapter(mRecyclerData)
        mSettingAdapter.onItemClickListener = this
    }

    override fun showLoading() {
        mActivity.showLoadingDialog()
    }

    override fun hideLoading() {
        mActivity.hideLoadingDialog()
    }

    override fun showTipMsg(msg: String?) {
        mActivity.showToast(msg)
    }

    override fun createClearCachePromptDialog() {
        CustomDialog.DefaultBuilder(mActivity)
                .setTitle(R.string.prompt_warning)
                .setContent(R.string.prompt_clear_cache)
                .setPositiveListener { presenter.clearCache() }
                .create()
                .show()
    }

    override fun createLogoutPromptDialog() {
        CustomDialog.DefaultBuilder(mActivity)
                .setTitle(R.string.prompt_warning)
                .setContent(R.string.prompt_logout)
                .setPositiveListener { ActivityUtil.jump2LoginPage(mActivity, true) }
                .create()
                .show()
    }

    override fun notifyAdapterForCacheItem(cacheSize: String?) {
        mRecyclerData[cacheItemPosition].content = cacheSize
        mSettingAdapter.notifyItemChanged(cacheItemPosition)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
                PrefUtil.putBoolean(IConstants.NON_H_MODE,
                        !UserInfo.getInstance().isNonhMode, mActivity)
                mRecyclerData[0].isChecked = !UserInfo.getInstance().isNonhMode
                UserInfo.getInstance().isNonhMode = !UserInfo.getInstance().isNonhMode
                mSettingAdapter.notifyItemChanged(0)
                showTipMsg("it will be take effect on the next startup")
            }
            1 -> createClearCachePromptDialog()
            2 -> createLogoutPromptDialog()
            else -> showTipMsg("illegal option")
        }
    }
}