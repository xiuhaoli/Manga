package cn.xhl.client.manga.view.main

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import cn.xhl.client.manga.R
import cn.xhl.client.manga.UserInfo
import cn.xhl.client.manga.adapter.main.FilterAdapter
import cn.xhl.client.manga.base.BaseActivity
import cn.xhl.client.manga.config.IConstants
import cn.xhl.client.manga.utils.PrefUtil
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/13
 *     version: 1.0
 * </pre>
 */
class FilterActivity : BaseActivity() {
    private val mLanguageData: ArrayList<FilterAdapter.FilterItem> = ArrayList()
    private lateinit var mLanguageAdapter: FilterAdapter
    private val userInfo = UserInfo.getInstance()
    override fun layoutId(): Int {
        return R.layout.activity_filter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSlipClose()
        initData()
        initAdapter()
    }

    private fun initData() {
        var filterItem: FilterAdapter.FilterItem
        for (item in IConstants.ALL_LANGUAGE) {
            filterItem = FilterAdapter.FilterItem()
            filterItem.isChecked = userInfo.filter.contains(item, true)
            filterItem.text = item
            mLanguageData.add(filterItem)
        }
    }

    private fun initAdapter() {
        val language = findViewById<RecyclerView>(R.id.language_recycler_activity_filter)
        mLanguageAdapter = FilterAdapter(mLanguageData)
        mLanguageAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
            var i = 0
            mLanguageData
                    .filter { it.isChecked }
                    .forEach { i++ }
            if (i == mLanguageData.size - 1 && !mLanguageData[position].isChecked) {
                showToast("can not select all")
                return@OnItemClickListener
            }
            mLanguageData[position].isChecked = !mLanguageData[position].isChecked
            mLanguageAdapter.notifyItemChanged(position)
        }
        language.adapter = mLanguageAdapter
        language.layoutManager = GridLayoutManager(this, 3)
    }

    override fun onPause() {
        super.onPause()
        saveFilter()
    }

    /**
     * 保存过滤条件
     * 并以&来拼接
     */
    private fun saveFilter() {
        var filter = ""
        mLanguageData
                .filter { it.isChecked }
                .forEach { filter += ("&" + it.text) }
        userInfo.filter = filter
        PrefUtil.putString(IConstants.FILTER, filter, this)
    }
}