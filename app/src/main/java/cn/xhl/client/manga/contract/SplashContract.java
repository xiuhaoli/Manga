package cn.xhl.client.manga.contract;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;
import cn.xhl.client.manga.model.bean.response.auth.Res_RefreshToken;

/**
 * @author Mike on 2017/9/28 0028.
 * <p>
 * Splash 协议
 */
public interface SplashContract {

    interface View extends BaseView<Presenter> {

        /**
         * 通过SharedPreferences获取用户信息，等初始化操作
         */
        void initUserInfo();

        /**
         * expire_time没过期且token等信息不为空，跳转主界面
         */
        void change2MainActivity();

        /**
         * 当本地存储的token、salt、uid为空或者expire_time过期了，就会调用此方法跳转登录
         */
        void change2AuthActivity();

        /**
         * 处理刷新token接口返回的数据
         *
         * @param res_refreshToken 返回信息实体类
         */
        void handleUserInfo(Res_RefreshToken res_refreshToken);
    }

    interface Presenter extends BasePresenter {

        /**
         * 校验登录信息是否完备
         *
         * @param token
         * @param uid
         * @param salt
         * @param expire_time
         * @return
         */
        boolean isUserInfoAvailable(String token, int uid, String salt, int expire_time);

        /**
         * 刷新登录信息
         *
         * @param token
         * @param uid
         * @param salt
         * @param expire_time
         */
        void refreshToken(String token, int uid, String salt, int expire_time);

    }
}
