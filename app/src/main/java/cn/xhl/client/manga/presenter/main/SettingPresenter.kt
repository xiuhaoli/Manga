package cn.xhl.client.manga.presenter.main

import cn.xhl.client.manga.MyApplication
import cn.xhl.client.manga.contract.main.SettingContract
import cn.xhl.client.manga.model.api.RetrofitFactory
import cn.xhl.client.manga.model.bean.response.user.Res_CheckUpdate
import cn.xhl.client.manga.utils.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException

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
    private var compositeDisposable: CompositeDisposable
    private lateinit var resCheckUpdate: Res_CheckUpdate
    private lateinit var apkFile: File
    private val apkName = "EhConvert_v"

    init {
        view.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
        compositeDisposable.clear()
    }

    override fun cacheSize(): String {
        return SystemUtil.formatNumber2(fileUtil.getDirectorySize(file, 0.0) / 1024.0 / 1024.0) + "M"
    }

    override fun clearCache() {
        view.showLoading()
        Flowable.fromCallable {
            fileUtil.deleteRootDir(file)
            "success"
        }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.notifyAdapterForCacheItem(cacheSize())
                    view.hideLoading()
                }
    }

    override fun checkNewVersion(code: Int, name: String) {
        view.showLoading()
        compositeDisposable.add(RetrofitFactory
                .getApiGithub()
                .checkNewVersion()
                .doOnTerminate { view.hideLoading() }
                .compose(RxSchedulesHelper.io_ui<Res_CheckUpdate>())
                .subscribeWith(object : DisposableObserver<Res_CheckUpdate>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Res_CheckUpdate) {
                        if (DeviceUtil.getVersionName(MyApplication.getAppContext()) < t.tag_name) {
                            resCheckUpdate = t
                            apkFile = File(FileUtil.getInstance().downloadPath +
                                    File.separator + apkName + t.tag_name + ".apk")
                            view.showNewVersionPrompt(t)
                        } else {
                            view.showTipMsg("it is the latest version")
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.showTipMsg("server error")
                    }

                })
        )
    }

    override fun startDownloadApk(url: String) {
        if (apkFile.exists()) {
            view.install(apkFile)
            return
        }
        compositeDisposable.add(RetrofitFactory
                .getApiGithub()
                .downloadApk(url)
                .compose(RxSchedulesHelper.io_ui<ResponseBody>())
                .doOnTerminate { view.hideLoading() }
                .subscribeWith(object : DisposableObserver<ResponseBody>() {
                    override fun onError(e: Throwable) {
                        view.showTipMsg("download failed")
                    }

                    override fun onNext(t: ResponseBody) {
                        try {
                            fileUtil.saveInputStream(t.byteStream(), apkFile)
                            view.install(apkFile)
                        } catch (e: IOException) {
                            // discard
                        }
                    }

                    override fun onComplete() {
                        // discard
                    }

                })
        )
    }

}