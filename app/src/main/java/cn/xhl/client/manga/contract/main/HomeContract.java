package cn.xhl.client.manga.contract.main;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

/**
 * @author Mike on 2017/10/9 0009.
 * <p>
 * 主页协议
 */
public interface HomeContract {
    interface View extends BaseView<Presenter> {

        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void hideKeyboard();

        void showSearchDialog();
    }

    interface Presenter extends BasePresenter {
        void search();
    }
}
