package cn.xhl.client.manga.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.Toast
import cn.xhl.client.manga.base.BaseObserver
import cn.xhl.client.manga.config.IConstants
import cn.xhl.client.manga.model.api.RetrofitFactory
import cn.xhl.client.manga.model.bean.request.BaseRequest
import cn.xhl.client.manga.model.bean.request.user.Req_CheckUpdate
import cn.xhl.client.manga.model.bean.response.BaseResponse
import cn.xhl.client.manga.model.bean.response.user.Res_CheckUpdate
import cn.xhl.client.manga.utils.*
import com.google.gson.Gson
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
        checkUpdate(DeviceUtil.getVersionCode(this).toInt(),
                DeviceUtil.getVersionName(this))
    }

    private fun checkUpdate(code: Int, name: String) {
        val reqCheckUpdate = Req_CheckUpdate(code, name)
        RetrofitFactory
                .getApiUser()
                .checkNewVersion(StringUtil.getRequestBody(
                        Gson().toJson(
                                BaseRequest.Builder()
                                        .setSign(SignUtil.generateSign(code.toString(), name))
                                        .setData(reqCheckUpdate)
                                        .build()
                        )
                ))
                .compose(RxSchedulesHelper.io_ui<BaseResponse<Res_CheckUpdate>>())
                .subscribeWith(object : BaseObserver<Res_CheckUpdate>() {
                    override fun onHandleSuccess(t: Res_CheckUpdate) {
                        if (!t.isInstall) {
                            mCallback!!.onFinish()
                            LogUtil.e(TAG, "该版本是最新版")
                            return
                        }
                        resCheckUpdate = t
                        if (PrefUtil.getInt(IConstants.IGNORE_APK_INSTALL, 0,
                                this@ApkDownloadService) == t.version_code) {
                            // 如果version_code一样，则说明用户点击过忽略版本，因此不提示安装
                            LogUtil.e(TAG, "该版本被忽略了")
                            mCallback!!.onFinish()
                            return
                        }
                        apkFile = File(FileUtil.getInstance().downloadPath +
                                File.separator + apkName + t.version_name + ".apk")
                        if (apkFile.exists() && checkFileHash()) {
                            // 若文件存在且hash值一样则直接提示用户安装
                            mCallback!!.onSuccess(apkFile, resCheckUpdate)
                            mCallback!!.onFinish()
                        } else {
                            LogUtil.e(TAG, "开始下载apk" + "apkFile.exists() = " +
                                    apkFile.exists() + ", checkFileHash() = " + checkFileHash())
                            startDownloadApk()
                        }
                    }

                    override fun onHandleError(code: Long, msg: String?) {
                        Toast.makeText(this@ApkDownloadService, msg,
                                Toast.LENGTH_SHORT).show()
                        mCallback!!.onFinish()
                    }

                })
    }

    private fun startDownloadApk() {
        RetrofitFactory
                .getApiEh()
                .downloadApk(resCheckUpdate.url)
                .doOnTerminate { mCallback!!.onFinish() }
                .compose(RxSchedulesHelper.io_ui<ResponseBody>())
                .subscribeWith(object : DisposableObserver<ResponseBody>() {
                    override fun onNext(t: ResponseBody) {
                        try {
                            fileUtil.saveInputStream(t.byteStream(), apkFile)
                            if (checkFileHash()) {
                                mCallback!!.onSuccess(apkFile, resCheckUpdate)
                            }
                        } catch (e: IOException) {
                            // discard
                        }
                    }

                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                        mCallback!!.onFailure(e)
                    }

                })
    }

    private fun checkFileHash(): Boolean {
        return MD5Util.getFileMD5(apkFile) == (resCheckUpdate.hash)
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