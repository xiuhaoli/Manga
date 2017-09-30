package cn.xhl.client.manga.contract;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

/**
 * Created by lixiuhao on 2017/9/29 0029.
 * <p>
 * 重置密码页面协议
 */
public interface ResetPasswdContract {

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
        void obtainVerify(String email);

        boolean isEmailValid(String email);

        boolean isPasswordValid(String password);

        void submit(String email, String password, String verify);
    }
}
