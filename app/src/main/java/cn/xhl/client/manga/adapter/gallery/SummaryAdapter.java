package cn.xhl.client.manga.adapter.gallery;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.xhl.client.manga.R;
import cn.xhl.client.manga.custom.TextImageSpan;

/**
 * <pre>
 *     author : xiuhaoli
 *     e-mail : xiuhaoli@outlook.com
 *     time   : 2018/01/19
 *     version: 1.0
 * </pre>
 */
public class SummaryAdapter extends BaseQuickAdapter<SummaryAdapter.SummaryItem,
        SummaryAdapter.SummaryViewHolder> {
    public SummaryAdapter(@Nullable List<SummaryItem> data) {
        super(R.layout.item_summary, data);
    }

    @Override
    protected void convert(SummaryViewHolder helper, SummaryItem item) {
        helper.setImageSpanLeft(R.id.imagespan_item_summary,item.getImgRes());
        helper.setText(R.id.imagespan_item_summary, item.getLeft());
        helper.setText(R.id.follow_item_summary, item.getRight());
        helper.addOnClickListener(R.id.follow_item_summary);
    }

    class SummaryViewHolder extends BaseViewHolder {

        private SummaryViewHolder(View view) {
            super(view);
        }

        private void setImageSpanLeft(int resId, int imgId) {
            TextImageSpan imageSpan = getView(resId);
            imageSpan.setLeftImageSpan(imgId);
        }
    }

    public static class SummaryItem {
        private int imgRes;
        private String left;
        private String right;

        public int getImgRes() {
            return imgRes;
        }

        public void setImgRes(int imgRes) {
            this.imgRes = imgRes;
        }

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }
    }
}
