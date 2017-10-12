package cn.xhl.client.manga.contract.main;

import cn.xhl.client.manga.base.BasePresenter;
import cn.xhl.client.manga.base.BaseView;

/**
 * Created by lixiuhao on 2017/10/9 0009.
 * <p>
 * 分类页的协议
 */
public interface CategoryContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
