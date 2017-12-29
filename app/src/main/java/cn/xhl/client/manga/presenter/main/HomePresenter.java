package cn.xhl.client.manga.presenter.main;

import cn.xhl.client.manga.contract.main.HomeContract;

/**
 * @author Mike on 2017/10/9 0009.
 *         <p>
 *         首页的Presenter
 */
public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
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
    public void search() {
        view.showSearchDialog();
    }
}
