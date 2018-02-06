package cn.xhl.client.manga.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.Toast
import cn.xhl.client.manga.config.IConstants
import cn.xhl.client.manga.model.api.RetrofitFactory
import cn.xhl.client.manga.model.bean.response.user.Res_CheckUpdate
import cn.xhl.client.manga.utils.*
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/11
 *     version: 1.0
 * </pre>
 */
class ApkDownloadService : Service() {
    private var mCallback: Callback? = null
    private val fileUtil: FileUtil = FileUtil.getInstance()
    private lateinit var apkFile: File
    private lateinit var resCheckUpdate: Res_CheckUpdate
    private val apkName = "EhConvert_v"
    private val TAG = "ApkDownloadService"

    companion object {
        @JvmStatic
        fun bind(context: Context, connection: ServiceConnection) {
            val intent = Intent(context, ApkDownloadService::class.java)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate() {
        super.onCreate()
        checkUpdate()
    }

    private fun checkUpdate() {
        RetrofitFactory
                .getApiGithub()
                .checkNewVersion()
                .compose(RxSchedulesHelper.io_ui<Res_CheckUpdate>())
                .subscribeWith(object : DisposableObserver<Res_CheckUpdate>() {
                    override fun onError(e: Throwable) {
                        Toast.makeText(this@ApkDownloadService, "server error",
                                Toast.LENGTH_SHORT).show()
                        mCallback?.onFinish()
                    }

                    override fun onNext(t: Res_CheckUpdate) {
                        LogUtil.e(TAG, t.toString())
                        if (DeviceUtil.getVersionName(this@ApkDownloadService) >= t.tag_name) {
                            mCallback?.onFinish()
                            LogUtil.e(TAG, "该版本是最新版")
                            return
                        }
                        resCheckUpdate = t
                        if (PrefUtil.getString(IConstants.IGNORE_APK_INSTALL,
                                        "1.0.0", this@ApkDownloadService) == t.tag_name) {
                            mCallback?.onFinish()
                            return
                        }
                        apkFile = File(FileUtil.getInstance().downloadPath +
                                File.separator + apkName + t.tag_name + ".apk")
                        if (apkFile.exists()) {
                            // 若文件存在且hash值一样则直接提示用户安装
                            mCallback?.onSuccess(apkFile, resCheckUpdate)
                            mCallback?.onFinish()
                        } else {
                            LogUtil.e(TAG, "开始下载apk=>" + "apkFile.exists() = " +
                                    apkFile.exists())
                            startDownloadApk(IConstants.APK_PRE_URL + t.tag_name + "/" +
                                    t.name)
                        }
                    }

                    override fun onComplete() {
                    }

                })
    }

    private fun startDownloadApk(url: String) {
        // 如果不是wifi不更新
        if (!DeviceUtil.isWifi(this@ApkDownloadService)) {
            mCallback?.onFinish()
            return
        }
        RetrofitFactory
                .getApiGithub()
                .downloadApk(url)
                .doOnTerminate { mCallback?.onFinish() }
                .compose(RxSchedulesHelper.io_ui<ResponseBody>())
                .subscribeWith(object : DisposableObserver<ResponseBody>() {
                    override fun onNext(t: ResponseBody) {
                        try {
                            fileUtil.saveInputStream(t.byteStream(), apkFile)
                            mCallback?.onSuccess(apkFile, resCheckUpdate)
                        } catch (e: IOException) {
                            // discard
                        }
                    }

                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                        mCallback?.onFailure(e)
                    }

                })
    }

    override fun onBind(intent: Intent): IBinder? {
        return ApkDownloadBinder()
    }

    inner class ApkDownloadBinder : android.os.Binder() {
        fun getService(): ApkDownloadService {
            return this@ApkDownloadService
        }
    }

    fun setCallback(callback: Callback) {
        this.mCallback = callback
    }

    interface Callback {
        fun onFailure(e: Throwable)

        fun onSuccess(apkFile: File, apkInfo: Res_CheckUpdate)

        fun onFinish()
    }
}