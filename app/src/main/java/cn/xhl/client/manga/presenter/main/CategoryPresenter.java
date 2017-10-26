package cn.xhl.client.manga.presenter.main;

import cn.xhl.client.manga.contract.main.CategoryContract;

/**
 * @author Mike on 2017/10/9 0009.
 */

public class CategoryPresenter implements CategoryContract.Presenter {
    private CategoryContract.View view;

    public CategoryPresenter(CategoryContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
//        view.initRecyclerData();
    }

    @Override
    public void unSubscribe() {
    }
}
