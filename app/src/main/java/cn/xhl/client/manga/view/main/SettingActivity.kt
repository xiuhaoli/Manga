package cn.xhl.client.manga.view.main

import android.os.Bundle
import cn.xhl.client.manga.R
import cn.xhl.client.manga.base.BaseActivity
import cn.xhl.client.manga.base.BaseFragment
import cn.xhl.client.manga.custom.SlipBackLayout
import cn.xhl.client.manga.presenter.main.SettingPresenter
import cn.xhl.client.manga.utils.ActivityUtil
import cn.xhl.client.manga.view.main.fragment.SettingFragment

class SettingActivity : BaseActivity(), BaseFragment.BackHandledInterface {
    private lateinit var baseFragment: BaseFragment

    override fun layoutId(): Int {
        return R.layout.activity_setting
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingFragment = SettingFragment()
        SettingPresenter(settingFragment)
        ActivityUtil.switchContentHideCurrent(this, null,
                settingFragment,
                SettingFragment.TAG, R.id.framelayout_activity_setting)
        SlipBackLayout.Builder(this)
                .setListener { this_.finish() }
                .build()
    }

    override fun setSelectedFragment(selectedFragment: BaseFragment) {
        baseFragment = selectedFragment
    }

    override fun onBackPressed() {
        baseFragment.onBackPressed()
    }
}
