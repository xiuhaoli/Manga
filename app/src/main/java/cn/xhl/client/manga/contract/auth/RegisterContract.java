package cn.xhl.client.manga.contract.auth;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

/**
 * @author Mike on 2017/9/29 0029.
 */

public interface RegisterContract {
    interface View extends BaseView<Presenter> {
        void createView();

        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void back2Login();

        void countDown();

        void hideKeyboard();

    }

    interface Presenter extends BasePresenter {
        @Deprecated
        void obtainVerify(String email);

        boolean isEmailValid(String email);

        boolean isPasswordValid(String password);

        void submit(String email, String password, String verify);
    }
}
