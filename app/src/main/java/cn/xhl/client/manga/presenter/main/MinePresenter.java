package cn.xhl.client.manga.presenter.main;

import java.io.File;
import java.util.concurrent.Callable;

import cn.xhl.client.manga.contract.main.MineContract;
import cn.xhl.client.manga.utils.FileUtil;
import cn.xhl.client.manga.utils.SystemUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Mike on 2017/10/9 0009.
 */
public class MinePresenter implements MineContract.Presenter {
    private MineContract.View view;
    private FileUtil fileUtil = FileUtil.getInstance();
    private File file = new File(fileUtil.getStorageDirectory());

    public MinePresenter(MineContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public String cacheSize() {
        return SystemUtil.formatNumber2(fileUtil.getDirectorySize(file, 0) / 1024 / 1024) + "M";
    }

    @Override
    public void clearCache() {
        view.showLoading();
        Flowable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                fileUtil.deleteFile(file);
                return "success";
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        view.notifyAdapterForCacheItem(cacheSize());
                        view.hideLoading();
                    }
                });
    }
}
