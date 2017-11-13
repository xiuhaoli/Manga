package cn.xhl.client.manga.adapter.main;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.model.bean.response.Res_GalleryList;
import cn.xhl.client.manga.utils.DateUtil;
import cn.xhl.client.manga.utils.LogUtil;

/**
 * gallery列表的适配器
 *
 * @author Mike on 2017/10/23 0023.
 */

public class GalleryListAdapter extends BaseQuickAdapter<Res_GalleryList.GalleryEntity, GalleryListAdapter.GalleryListViewHolder> {
    private List<Res_GalleryList.GalleryEntity> data;
    // 列表图片的uri集合
    private SparseArray<Uri> uris;

    public GalleryListAdapter(@Nullable List<Res_GalleryList.GalleryEntity> data) {
        super(R.layout.item_gallery, data);
        this.data = data;
        uris = new SparseArray<>(10);
    }

    @Override
    protected void convert(GalleryListViewHolder helper, Res_GalleryList.GalleryEntity item) {
        // 获取当前item所处的位置
        int position = helper.getAdapterPosition();
        Uri uri = Uri.parse(item.getThumb());
        uris.put(position, uri);
        helper.setText(R.id.title_item_gallery, item.getTitle());
        helper.setText(R.id.author_item_gallery, item.getArtist());
        helper.setText(R.id.category_item_gallery, item.getCategory());
        helper.setText(R.id.posted_item_gallery, DateUtil.stampToDate(item.getPosted()));
        helper.setText(R.id.viewed_item_gallery, String.valueOf(item.getViewed()));
        helper.setText(R.id.subscribe_item_gallery, String.valueOf(item.getSubscribe()));
        helper.setUri(R.id.img_item_gallery, uri);
        // init view for create_time
        if (item.getCreate_time() == 0) {
            return;
        }
        String date = DateUtil.stampToDate(item.getCreate_time());
        if (position == 0) {
            helper.setGone(R.id.text_sticky_item_gallery, true);
            helper.setText(R.id.text_sticky_item_gallery, date);
        } else {
            if (!TextUtils.equals(date, DateUtil.stampToDate(data.get(position - 1).getCreate_time()))) {
                helper.setGone(R.id.text_sticky_item_gallery, true);
                helper.setText(R.id.text_sticky_item_gallery, date);
            } else {
                helper.setGone(R.id.text_sticky_item_gallery, false);
            }
        }
    }

    /**
     * 移除图片
     *
     * @param position
     */
    public void evictImageFromMemoryCache(int position) {
        Uri uri = uris.get(position);
        if (uri != null) {
            Fresco.getImagePipeline().evictFromMemoryCache(uri);
        }
    }

    class GalleryListViewHolder extends BaseViewHolder {

        private GalleryListViewHolder(View view) {
            super(view);
        }

        private void setUri(int resId, Uri uri) {
            SimpleDraweeView simpleDraweeView = getView(resId);
            simpleDraweeView.setImageURI(uri);
        }
    }

}
