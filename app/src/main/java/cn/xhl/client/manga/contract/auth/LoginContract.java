package cn.xhl.client.manga.contract.auth;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.Res_Login;

/**
 * @author Mike on 2017/9/22 0022.
 *
 * login 协议
 */
public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void createView();

        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void change2MainActivity();

        void change2Register();

        void change2Reset();

        void hideKeyboard();

        void saveLoginInfo(Res_Login res_login);
    }

    interface Presenter extends BasePresenter {

        boolean isEmailValid(String email);

        boolean isPasswordValid(String password);

        void activateLoginTask(String email, String password);

    }

}
