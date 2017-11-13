package cn.xhl.client.manga.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


/**
 * 自定义滑动监听,用于清除RecyclerView的不可见图片
 * <p>
 * Created by xiuhaoli on 2017/11/13.
 */
public abstract class GalleryListScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dx == 0 && dy == 0) {
            return;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisible = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisible = linearLayoutManager.findLastVisibleItemPosition();
        if (dy > 0) {
            onScrolledUp(firstVisible, lastVisible);
        } else {
            onScrolledDown(firstVisible, lastVisible);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    public abstract void onScrolledUp(int firstVisible, int lastVisible);

    public abstract void onScrolledDown(int firstVisible, int lastVisible);

}
