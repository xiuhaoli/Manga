package cn.xhl.client.manga.contract;

import cn.xhl.client.manga.BasePresenter;
import cn.xhl.client.manga.BaseView;

/**
 * Created by lixiuhao on 2017/9/22 0022.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void hideTopBar();

        void showLoading();

        void hideLoading();

        void showErrorDialog();

        void hideErrorDialog();

        void hideKeyboard();
    }

    interface Presenter extends BasePresenter {
        boolean checkValid(String account, String username);

        void activateTask();

        void change2Register();

        void change2Reset();

    }

}
