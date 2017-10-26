package cn.xhl.client.manga.adapter.main;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.xhl.client.manga.R;

/**
 * 分类页的适配器
 *
 * @author Mike on 2017/10/25 0025.
 */
public class CategoryAdapter extends BaseQuickAdapter<CategoryAdapter.CategoryItem, CategoryAdapter.CategoryViewHolder> {

    public CategoryAdapter(@Nullable List<CategoryItem> data) {
        super(R.layout.item_category, data);
    }

    @Override
    protected void convert(CategoryViewHolder helper, CategoryItem item) {
        helper.setText(R.id.text_item_category, item.getText());
        helper.setImgRes(R.id.img_item_category, item.getImgId());
    }

    class CategoryViewHolder extends BaseViewHolder {

        private CategoryViewHolder(View view) {
            super(view);
        }

        private void setImgRes(int resId, int imgId) {
            SimpleDraweeView simpleDraweeView = getView(resId);
            simpleDraweeView.setActualImageResource(imgId);
        }
    }

    public static class CategoryItem {
        private int imgId;
        private String text;

        public CategoryItem() {
        }

        public CategoryItem(int imgId, String text) {
            this.imgId = imgId;
            this.text = text;
        }

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "CategoryItem{" +
                    "imgId=" + imgId +
                    ", text=" + text +
                    '}';
        }
    }
}
