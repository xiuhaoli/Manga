package cn.xhl.client.manga.presenter.main;

import cn.xhl.client.manga.contract.main.HomeContract;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Mike on 2017/10/9 0009.
 * <p>
 * 首页的Presenter
 */
public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private CompositeDisposable compositeDisposable;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void search() {
        view.jump2Search();
    }
}
