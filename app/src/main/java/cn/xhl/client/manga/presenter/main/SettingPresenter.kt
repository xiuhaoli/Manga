package cn.xhl.client.manga.presenter.main

import cn.xhl.client.manga.contract.main.SettingContract
import cn.xhl.client.manga.utils.FileUtil
import cn.xhl.client.manga.utils.SystemUtil
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/04
 *     version: 1.0
 * </pre>
 */
class SettingPresenter(private var view: SettingContract.View) : SettingContract.Presenter {
    private var fileUtil: FileUtil = FileUtil.getInstance()
    private var file: File = File(fileUtil.storageDirectory)

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cacheSize(): String {
        return SystemUtil.formatNumber2(fileUtil.getDirectorySize(file, 0.0) / 1024.0 / 1024.0) + "M"
    }

    override fun clearCache() {
        view.showLoading()
        Flowable.fromCallable {
            fileUtil.deleteFile(file)
            "success"
        }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.notifyAdapterForCacheItem(cacheSize())
                    view.hideLoading()
                }
    }
}