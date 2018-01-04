package cn.xhl.client.manga.contract.main;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

/**
 * 我的协议
 * <p>
 *
 * @author Mike on 2017/10/9 0009.
 */
public interface MineContract {
    interface View extends BaseView<Presenter> {
        void initAdapter();

        void showLoading();

        void hideLoading();

        void showTipMsg(String msg);

        void changeThemeMode(boolean isNightMode);

        void notifyUI2ChangeUsername(String username);

        void notifyUI2ChangeHeader(String url);
    }

    interface Presenter extends BasePresenter {

        void modifyUsername(String newName);

        void modifyProfileHeader(String url);

    }
}
