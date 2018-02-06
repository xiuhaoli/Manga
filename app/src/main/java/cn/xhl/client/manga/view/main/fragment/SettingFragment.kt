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
import cn.xhl.client.manga.model.bean.response.user.Res_CheckUpdate
import cn.xhl.client.manga.utils.ActivityUtil
import cn.xhl.client.manga.utils.AppUtil
import cn.xhl.client.manga.utils.DeviceUtil
import cn.xhl.client.manga.utils.PrefUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import java.io.File
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
        initAdapter(recyclerView)
        recyclerView.adapter = mSettingAdapter
    }

    override fun setPresenter(presenter: SettingContract.Presenter) {
        this.presenter = presenter
    }

    private fun initAdapter(recyclerView: RecyclerView) {
        mRecyclerData = ArrayList()
        var item: SettingAdapter.SettingItem
        val itemLevel = intArrayOf(SettingAdapter.ITEM_TEXT_SWITCHER,
                SettingAdapter.ITEM_TEXT_TEXT_ARROW, SettingAdapter.ITEM_TEXT_TEXT,
                SettingAdapter.ITEM_LOGOUT)
        val leftText = intArrayOf(R.string.non_h_mode, R.string.cache, R.string.version, R.string.logout)
        val rightText = arrayOf("", presenter.cacheSize(), DeviceUtil.getVersionName(mActivity), "")
        val switcher = booleanArrayOf(UserInfo.getInstance().isNonhMode, false, false, false)
        var i = 0
        val size = leftText.size
        while (i < size) {
            item = SettingAdapter.SettingItem()
            item.level = itemLevel[i]
            item.text = leftText[i]
            item.content = rightText[i]
            item.isChecked = switcher[i]
            mRecyclerData.add(item)
            i++
        }
        mSettingAdapter = SettingAdapter(mRecyclerData)
        mSettingAdapter.onItemClickListener = this
        recyclerView.addItemDecoration(SettingItemDecoration(mActivity, itemLevel.size))
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unSubscribe()
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

    override fun showNewVersionPrompt(res_checkUpdate: Res_CheckUpdate) {
        CustomDialog.DefaultBuilder(mActivity)
                .setTitle("download?")
                .setContent("version : " + res_checkUpdate.tag_name +
                        "\n" + res_checkUpdate.body)
                .setPositiveButtonText(R.string.action_download)
                .setPositiveListener {
                    presenter.startDownloadApk(IConstants.APK_PRE_URL +
                            res_checkUpdate.tag_name + "/" + res_checkUpdate.name)
                }
                .create().show()
    }

    override fun install(apkPath: File?) {
        AppUtil.installApp(mActivity, apkPath)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
                PrefUtil.putBoolean(IConstants.NON_H_MODE,
                        !UserInfo.getInstance().isNonhMode, mActivity)
                mRecyclerData[0].isChecked = !UserInfo.getInstance().isNonhMode
                mSettingAdapter.notifyItemChanged(0)
                showTipMsg("it will be take effect on the next startup")
            }
            1 -> createClearCachePromptDialog()
            2 -> presenter.checkNewVersion(DeviceUtil.getVersionCode(mActivity).toInt(),
                    DeviceUtil.getVersionName(mActivity))
            3 -> createLogoutPromptDialog()
        }
    }
}