package cn.xhl.client.manga.adapter.main;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;
import cn.xhl.client.manga.utils.DateUtil;

/**
 * gallery列表的适配器
 *
 * @author Mike on 2017/10/23 0023.
 */

public class GalleryListAdapter extends BaseQuickAdapter<Res_GalleryList.GalleryEntity, GalleryListAdapter.GalleryListViewHolder> {

    public GalleryListAdapter(@Nullable List<Res_GalleryList.GalleryEntity> data) {
        super(R.layout.item_gallery, data);
    }

    @Override
    protected void convert(GalleryListViewHolder helper, Res_GalleryList.GalleryEntity item) {
        helper.setText(R.id.title_item_gallery, item.getTitle());
        helper.setText(R.id.author_item_gallery, item.getArtist());
        helper.setText(R.id.category_item_gallery, item.getCategory());
        helper.setText(R.id.posted_item_gallery, DateUtil.stampToDate(item.getPosted()));
        helper.setText(R.id.viewed_item_gallery, String.valueOf(item.getViewed()));
        helper.setText(R.id.subscribe_item_gallery, String.valueOf(item.getSubscribe()));
        helper.setUri(R.id.img_item_gallery, item.getThumb());
    }

    class GalleryListViewHolder extends BaseViewHolder {

        private GalleryListViewHolder(View view) {
            super(view);
        }

        private void setUri(int resId, String imgPath) {
            SimpleDraweeView simpleDraweeView = getView(resId);
            simpleDraweeView.setImageURI(imgPath);
        }
    }
}
