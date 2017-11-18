package cn.xhl.client.manga.adapter.gallery;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.model.bean.response.gallery.Res_FavoriteFolder;

/**
 * Created by xiuhaoli on 2017/11/17.
 */

public class FavoriteFolderAdapter extends BaseQuickAdapter<Res_FavoriteFolder.Data, BaseViewHolder> {
    public FavoriteFolderAdapter(@Nullable List<Res_FavoriteFolder.Data> data) {
        super(R.layout.item_favorite_folder, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Res_FavoriteFolder.Data item) {
        helper.setText(R.id.text_item_favorite_folder, item.getFolder());
        helper.setText(R.id.content_item_favorite_folder, String.valueOf(item.getCount()));
    }
}
