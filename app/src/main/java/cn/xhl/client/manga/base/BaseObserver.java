package cn.xhl.client.manga.base;


import cn.xhl.client.manga.config.HttpRespCode;
import cn.xhl.client.manga.model.bean.response.BaseResponse;
import cn.xhl.client.manga.utils.LogUtil;
import cn.xhl.client.manga.utils.StringUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author Mike on 2017/9/18 0018.
 */
public abstract class BaseObserver<T> extends DisposableObserver<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";

//    @Override
//    public void onSubscribe(@NonNull Disposable d) {
//        LogUtil.i(TAG, "onSubscribe: subscribe");
//    }

    @Override
    public void onNext(@NonNull BaseResponse<T> tBaseResponse) {
        LogUtil.e(TAG, tBaseResponse.toString());
        long code = tBaseResponse.getCode();
        String msg = tBaseResponse.getMsg();
        if (code == HttpRespCode.SUCCESS_CODE) {
            T t = tBaseResponse.getData();
            onHandleSuccess(t);
        } else {
            if (!StringUtil.isEmpty(msg)) {
                msg = StringUtil.utf_8(msg);
            }
            onHandleError(code, msg);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
//        LogUtil.e(TAG, e.getCause().toString());
        onHandleError(HttpRespCode.SERVER_ERR, "server error");
    }

    @Override
    public void onComplete() {
//        LogUtil.e(TAG, "onComplete: ok");
    }

    protected abstract void onHandleSuccess(T t);

    protected abstract void onHandleError(long code, String msg);
}
