package cn.xhl.client.manga.presenter.main

import cn.xhl.client.manga.base.BaseObserver
import cn.xhl.client.manga.contract.main.SettingContract
import cn.xhl.client.manga.model.api.RetrofitFactory
import cn.xhl.client.manga.model.bean.request.BaseRequest
import cn.xhl.client.manga.model.bean.request.user.Req_CheckUpdate
import cn.xhl.client.manga.model.bean.response.BaseResponse
import cn.xhl.client.manga.model.bean.response.user.Res_CheckUpdate
import cn.xhl.client.manga.utils.*
import com.google.gson.Gson
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
        val reqCheckUpdate = Req_CheckUpdate(code, name)
        compositeDisposable.add(RetrofitFactory
                .getApiUser()
                .checkNewVersion(StringUtil.getRequestBody(
                        Gson().toJson(
                                BaseRequest.Builder()
                                        .setSign(SignUtil.generateSign(code.toString(), name))
                                        .setData(reqCheckUpdate)
                                        .build()
                        )
                ))
                .doOnTerminate { view.hideLoading() }
                .compose(RxSchedulesHelper.io_ui<BaseResponse<Res_CheckUpdate>>())
                .subscribeWith(object : BaseObserver<Res_CheckUpdate>() {
                    override fun onHandleSuccess(t: Res_CheckUpdate?) {
                        if (t!!.isInstall) {
                            resCheckUpdate = t
                            apkFile = File(FileUtil.getInstance().downloadPath +
                                    File.separator + apkName + t.version_name + ".apk")
                            view.showNewVersionPrompt(t)
                        } else {
                            view.showTipMsg("it is the latest version")
                        }
                    }

                    override fun onHandleError(code: Long, msg: String?) {
                        view.showTipMsg(msg)
                    }

                })
        )
    }

    override fun startDownloadApk(url: String) {
        if (apkFile.exists() && checkFileHash()) {
            view.install(apkFile)
            return
        }
        compositeDisposable.add(RetrofitFactory
                .getApiEh()
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
                            if (checkFileHash()) {
                                view.install(apkFile)
                            } else {
                                view.showTipMsg("unknown error")
                            }
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

    private fun checkFileHash(): Boolean {
        return MD5Util.getFileMD5(apkFile) == (resCheckUpdate.hash)
    }

}