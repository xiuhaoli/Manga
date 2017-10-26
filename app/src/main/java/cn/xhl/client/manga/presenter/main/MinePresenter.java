package cn.xhl.client.manga.presenter.main;

import cn.xhl.client.manga.contract.main.MineContract;

/**
 * @author Mike on 2017/10/9 0009.
 */

public class MinePresenter implements MineContract.Presenter {
    private MineContract.View view;

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
}
