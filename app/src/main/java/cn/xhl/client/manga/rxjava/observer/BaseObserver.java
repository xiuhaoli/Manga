package cn.xhl.client.manga.rxjava.observer;

import android.util.Log;

import cn.xhl.client.manga.BaseActivity;
import cn.xhl.client.manga.rxjava.BaseEntity;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by lixiuhao on 2017/9/18 0018.
 */

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {
    private static final String TAG = "BaseObserver";
    private BaseActivity mContext;

    protected BaseObserver(BaseActivity context) {
        this.mContext = context;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        Log.e(TAG, "onSubscribe: subscrible");
    }

    @Override
    public void onNext(@NonNull BaseEntity<T> tBaseEntity) {
        if (tBaseEntity.getCode() == 200) {
            T t = tBaseEntity.getData();
            onHandleSuccess(t);
        } else {
            mContext.showToast("");
            onHandleError(tBaseEntity.getMsg());
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        Log.e(TAG, "onError: unknown error", e);
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ok");
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg) {
        Log.e(TAG, "onHandleError: " + msg);
    }
}
